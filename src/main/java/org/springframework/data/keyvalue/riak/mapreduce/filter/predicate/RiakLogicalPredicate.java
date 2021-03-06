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
public class RiakLogicalPredicate implements RiakKeyFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5596033843413257919L;

	private RiakSimplePredicate left;

	private RiakSimplePredicate right;

	private String operation;

	public RiakLogicalPredicate(String operation, RiakSimplePredicate left,
			RiakSimplePredicate right) {
		this.left = left;
		this.right = right;
		this.operation = operation;
	}

	public RiakLogicalPredicate(String operation, RiakSimplePredicate filter) {
		this.left = filter;
		this.operation = operation;
	}

	@Override
	public Object[] getValueArray() {
		return new Object[] {operation, left, right};
	}

}
