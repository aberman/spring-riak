/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.keyvalue.riak;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.keyvalue.riak.util.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * @author andrewberman
 * 
 */
public class RiakMappedClass {
	private Field id;

	private String name;

	private Boolean isEntity;

	private List<RiakMappedField> propertyFields;

	private List<RiakMappedField> linkFields;

	private static List<Class<? extends Annotation>> entityAnnotations;

	private static List<Class<? extends Annotation>> lifecycleAnnotations;

	private Class<? extends Object> clazz;

	static {
		entityAnnotations = new ArrayList<Class<? extends Annotation>>();
		entityAnnotations.add(Embedded.class);
		entityAnnotations.add(Embeddable.class);
		entityAnnotations.add(Entity.class);
		entityAnnotations.add(Column.class);
		entityAnnotations.add(OneToMany.class);
		entityAnnotations.add(ManyToOne.class);
		entityAnnotations.add(JoinColumn.class);
		entityAnnotations.add(GeneratedValue.class);
		entityAnnotations.add(EntityListeners.class);
		entityAnnotations.add(Id.class);
		entityAnnotations.add(Version.class);
		entityAnnotations.add(Transient.class);

		lifecycleAnnotations = new ArrayList<Class<? extends Annotation>>();
		lifecycleAnnotations.add(PrePersist.class);
		lifecycleAnnotations.add(PreUpdate.class);
		lifecycleAnnotations.add(PreRemove.class);
		lifecycleAnnotations.add(PostLoad.class);
		lifecycleAnnotations.add(PostUpdate.class);
		lifecycleAnnotations.add(PostPersist.class);
		lifecycleAnnotations.add(PostRemove.class);
	}

	public RiakMappedClass(Class<? extends Object> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("The class cannot be null");

		this.clazz = clazz;
		init();
	}

	// Go through class and find all the annotations and build the RiakObject
	// which will be sent to the database
	private void init() {
		// Class cannot be mapped without either of these annotations
		Entity entity = null;
		Embedded embedded = null;
		this.isEntity = ((entity = (Entity) AnnotationUtils.findAnnotation(
				this.clazz, Entity.class)) != null);
		boolean isEmbedded = ((embedded = AnnotationUtils.findAnnotation(
				this.clazz, Embedded.class)) != null);

		if (!this.isEntity && !isEmbedded)
			return;
		else if (this.isEntity && isEmbedded)
			throw new IllegalArgumentException(
					"The class cannot be mapped as an Entity and Embedded");

		this.name = this.isEntity.booleanValue() ? entity.name() : this.clazz
				.getSimpleName();

		if (StringUtils.isEmpty(this.name))
			throw new IllegalArgumentException(
					"The name of the class cannot be empty in the Entity tag or your class is anonymous");

		// Process the fields
		initFields();
	}

	private void initFields() {
		ReflectionUtils.doWithFields(this.clazz, new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				if (!field.isAccessible())
					ReflectionUtils.makeAccessible(field);

				if (field.isAnnotationPresent(Transient.class)
						|| field.isSynthetic()
						|| field.getModifiers() == Modifier.FINAL
						|| field.getModifiers() == Modifier.TRANSIENT) {
					return;
				}

				// Field can only have one of these, if more than one throw an
				// error
				List<Annotation> annots = org.springframework.data.keyvalue.riak.util.AnnotationUtils
						.getFoundAnnotation(Id.class, Column.class,
								Embedded.class, Version.class, ManyToOne.class,
								OneToMany.class);

				// Probably allow auto generation at some point, but for now
				// have to add one of the annotations
				if (annots.size() > 1)
					throw new IllegalArgumentException(
							String.format(
									"The field %s must have only one of the following annotations: "
											+ "@Id, @Column, @Embedded, @Version, @ManyToOne, @OneToMany",
									field.getName()));

				Annotation annot = annots.get(0);

				if (annot.annotationType().equals(Id.class))
					RiakMappedClass.this.id = field;

				// Create a new mapped field here and then add to a list of
				// property MappedFields
				propertyFields.add(new RiakMappedField(field, annot));
			}
		});
		Map<Class<? extends Annotation>, Annotation> fieldAnnotMap = new HashMap<Class<? extends Annotation>, Annotation>();
		for (Class<? extends Annotation> a : entityAnnotations) {
			Target target = a.getAnnotation(Target.class);
			if (target != null
					&& (ArrayUtils.contains(target.value(), ElementType.FIELD) || ArrayUtils
							.contains(target.value(), ElementType.METHOD))) {
				Annotation fieldAnnot;
				if ((fieldAnnot = this.clazz.getAnnotation(a)) != null) {
					fieldAnnotMap.put(a, fieldAnnot);
				}
			}
		}
	}
}
