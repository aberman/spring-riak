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


/**
 * @author Andrew Berman
 * 
 */
public abstract class RiakParameter {
	private String key;

	private String value;

	private Type type;

	public enum Type {
		QUERY, HEADER
	}

	protected RiakParameter(String key, String value, Type type) {
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return key + "=" + value;
	}
}
