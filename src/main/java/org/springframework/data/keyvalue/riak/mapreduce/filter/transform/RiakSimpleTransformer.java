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
package org.springframework.data.keyvalue.riak.mapreduce.filter.transform;

import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakKeyFilter;

/**
 * @author Andrew Berman
 * 
 */
public class RiakSimpleTransformer implements RiakKeyFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2583768255800669148L;

	private final String filter;

	static final String QUOTE_COMMA_QUOTE = "\",\"";

	public RiakSimpleTransformer(String filter) {
		this.filter = filter;
	}

	@Override
	public Object[] getValueArray() {
		return new Object[] {filter};
	}

}
