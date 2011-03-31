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
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author andrewberman
 * 
 */
public class RiakMappedField {

	private Field field;

	private Annotation annotation;

	private Class<? extends Annotation> annotationClass;

	private Class<?> actualType;

	private boolean linkType;

	private boolean isArray;

	private boolean isMap;

	private boolean isCollection;

	public RiakMappedField(Field field, Annotation annot) {
		this.field = field;
		this.annotation = annot;
		this.annotationClass = annot.annotationType();
		init();
	}

	private void init() {
		Class<?> fieldType = this.field.getType();

		if (Collection.class.isAssignableFrom(fieldType)) {
			// this.actualType = fieldType;
			this.isCollection = true;
		} else if (fieldType.isArray()) {
			// this.actualType = fieldType;
			this.isArray = true;
		} else if (Map.class.isAssignableFrom(fieldType)) {
			// this.actualType = fieldType;
			this.isMap = true;
		}
	}

	public boolean isLinkType() {
		return this.linkType;
	}

	public boolean isPropertyType() {
		return !this.linkType;
	}

	public boolean isArrayType() {
		return this.isArray;
	}

	public boolean isCollectionType() {
		return this.isCollection;
	}

	public boolean isSimpleType() {
		return !isCollection && !isArray && !isMap;
	}
}
