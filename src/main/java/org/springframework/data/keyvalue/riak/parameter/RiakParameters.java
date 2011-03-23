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
package org.springframework.data.keyvalue.riak.parameter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * @author Andrew Berman
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public abstract class RiakParameters {
	private final JavaType mapType = TypeFactory.mapType(Map.class,
			String.class, String.class);

	private ObjectMapper mapper;

	public RiakParameters() {
		mapper = new ObjectMapper();
	}

	public Map<String, String> toMap() {
		return mapper.convertValue(this, mapType);
	}
	
	public String toQueryString() {
		return StringUtils.join(toMap().entrySet(), "&");
	}
	
	public static void main(String[] args) {
		RiakReadParameters param = new RiakReadParameters();
		param.setRead(3);
		param.setVtag("rte");
		System.out.println();
	}
}
