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
package org.springframework.data.keyvalue.riak.mapreduce;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.keyvalue.riak.util.RiakConstants;

/**
 * @author Andrew Berman
 * 
 */
public abstract class RiakJavascriptFunction extends RiakMapReduceFunction {

	public RiakJavascriptFunction() {
		super(Language.JAVASCRIPT);
	}

	public static final RiakJavascriptNamedFunction MAP_VALUES = new RiakJavascriptNamedFunction(
			"Riak.mapValues");

	public static final RiakJavascriptNamedFunction MAP_VALUES_JSON = new RiakJavascriptNamedFunction(
			"Riak.mapValuesJson");

	public static final RiakJavascriptNamedFunction REDUCE_SUM = new RiakJavascriptNamedFunction(
			"Riak.reduceSum");

	public static final RiakJavascriptNamedFunction REDUCE_MIN = new RiakJavascriptNamedFunction(
			"Riak.reduceMin");

	public static final RiakJavascriptNamedFunction REDUCE_MAX = new RiakJavascriptNamedFunction(
			"Riak.reduceMax");

	public static final RiakJavascriptNamedFunction REDUCE_SORT = new RiakJavascriptNamedFunction(
			"Riak.reduceSort");

	public static final RiakJavascriptNamedFunction REDUCE_LIMIT = new RiakJavascriptNamedFunction(
			"Riak.reduceLimit");

	public static final RiakJavascriptNamedFunction REDUCE_SLICE = new RiakJavascriptNamedFunction(
			"Riak.reduceSlice");

	public static final RiakJavascriptFunction src(String src) {
		return new RiakJavascriptSrcFunction(src);
	}

	public static final RiakJavascriptFunction named(String name) {
		return new RiakJavascriptNamedFunction(name);
	}

	public abstract String getValue();

	private static class RiakJavascriptSrcFunction extends
			RiakJavascriptFunction {

		private String source;

		public RiakJavascriptSrcFunction(String source) {
			super();
			this.source = source;
		}

		@Override
		@JsonProperty(RiakConstants.SOURCE)
		public String getValue() {
			return this.source;
		}

	}

	private static class RiakJavascriptNamedFunction extends
			RiakJavascriptFunction {

		private String name;

		public RiakJavascriptNamedFunction(String name) {
			this.name = name;
		}

		@Override
		@JsonProperty(RiakConstants.FUNCTION_NAME)
		public String getValue() {
			return this.name;
		}

	}
}
