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
package org.springframework.data.keyvalue.riak.mapreduce.filter.predicate;

import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakKeyFilter;

/**
 * @author Andrew Berman
 * 
 */
public class RiakSimplePredicate implements RiakKeyFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7326012047070592973L;

	private final String filter;

	private final Object value;

	public RiakSimplePredicate(String filter, Object value) {
		this.filter = filter;
		this.value = value;
	}

	@Override
	public Object[] getValueArray() {
		return new Object[] { filter, value };
	}

}
