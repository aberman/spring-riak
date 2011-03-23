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

import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceFilter;

/**
 * @author Andrew Berman
 * 
 */
public class RiakBetweenPredicate implements RiakMapReduceFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3432533704228979301L;

	private Object min;

	private Object max;

	private Boolean inclusive;
	
	private static final String OP = "between";

	public RiakBetweenPredicate(Object min, Object max, Boolean inclusive) {
		this.min = min;
		this.max = max;
		this.inclusive = inclusive;
	}

	@Override
	public Object[] getValueArray() {
		return new Object[] {OP, min, max, inclusive};
	}

}
