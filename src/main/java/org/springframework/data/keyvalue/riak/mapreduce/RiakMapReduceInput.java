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
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketKeyValue;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceKeyFilter;

/**
 * @author Andrew Berman
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
abstract class RiakMapReduceInput {

	public static final RiakMapReduceInput getInstance(String bucket) {
		return new RiakBucketInput(bucket);
	}

	public static final RiakBucketInput getInstance(String bucket,
			RiakMapReduceKeyFilter... bucketFilters) {
		return new RiakBucketInput(bucket, bucketFilters);
	}

	public static final RiakBucketKeyPairsInput getInstance(
			RiakBucketKeyValue... bucketKeyPairs) {
		return new RiakBucketKeyPairsInput(bucketKeyPairs);
	}

	public static class RiakBucketInput extends RiakMapReduceInput {

		private String bucket;

		private RiakMapReduceKeyFilter[] bucketKeyFilters;

		public RiakBucketInput(String bucket) {
			this.bucket = bucket;
		}

		public RiakBucketInput(String bucket,
				RiakMapReduceKeyFilter... bucketKeyFilters) {
			this.bucket = bucket;
			this.bucketKeyFilters = bucketKeyFilters;
		}

		@JsonProperty
		public String getBucket() {
			return bucket;
		}

		@JsonProperty("key_filters")
		public RiakMapReduceKeyFilter[] getBucketKeyFilters() {
			return bucketKeyFilters;
		}

	}

	public static class RiakBucketKeyPairsInput extends RiakMapReduceInput {
		private RiakBucketKeyValue[] bucketKeyPairs;

		public RiakBucketKeyPairsInput(RiakBucketKeyValue... bucketKeyPairs) {
			this.bucketKeyPairs = bucketKeyPairs;
		}

		@JsonValue
		public RiakBucketKeyValue[] getBucketKeyPairs() {
			return bucketKeyPairs;
		}
	}
}
