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

/**
 * @author Andrew Berman
 * 
 */
public abstract class RiakJavascriptMapReduceFunction extends
		RiakMapReduceFunction {

	public RiakJavascriptMapReduceFunction() {
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

	public static final RiakJavascriptMapReduceFunction src(String src) {
		return new RiakJavascriptSrcFunction(src);
	}

	public static final RiakJavascriptMapReduceFunction named(String name) {
		return new RiakJavascriptNamedFunction(name);
	}

	public static final RiakJavascriptMapReduceFunction bucketKey(
			String bucket, String key) {
		return new RiakJavascriptBucketFunction(bucket, key);
	}

	private static class RiakJavascriptSrcFunction extends
			RiakJavascriptMapReduceFunction {
		private String source;

		public RiakJavascriptSrcFunction(String source) {
			this.source = source;
		}

		public String getSource() {
			return source;
		}

	}

	private static class RiakJavascriptBucketFunction extends
			RiakJavascriptMapReduceFunction {
		private String bucket;

		private String key;

		public RiakJavascriptBucketFunction(String bucket, String key) {
			this.bucket = bucket;
			this.key = key;
		}

		public String getBucket() {
			return bucket;
		}

		public String getKey() {
			return key;
		}
	}

	private static class RiakJavascriptNamedFunction extends
			RiakJavascriptMapReduceFunction {

		private String name;

		public RiakJavascriptNamedFunction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}
}
