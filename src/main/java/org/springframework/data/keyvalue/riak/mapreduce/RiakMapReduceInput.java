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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketKeyValue;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakKeyFilter;
import org.springframework.data.keyvalue.riak.util.RiakConstants;

/**
 * @author Andrew Berman
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class RiakMapReduceInput {
	private Object data;

	public static final RiakMapReduceInput getInstance(String bucket) {
		return new RiakMapReduceInput(bucket);
	}

	public static final RiakMapReduceInput getInstance(String bucket,
			RiakKeyFilter... bucketFilters) {
		return new RiakMapReduceInput(bucket, bucketFilters);
	}

	public static final RiakMapReduceInput getInstance(
			RiakBucketKeyValue... bucketKeyPairs) {
		return new RiakMapReduceInput(bucketKeyPairs);
	}

	private RiakMapReduceInput(String bucket,
			RiakKeyFilter... bucketFilters) {
		if (bucketFilters != null && bucketFilters.length > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(RiakConstants.BUCKET, bucket);
			map.put(RiakConstants.KEY_FILTERS, bucketFilters);
			this.data = map;
		} else
			this.data = bucket;
	}

	private RiakMapReduceInput(RiakBucketKeyValue... bucketKeyPairs) {
		this.data = bucketKeyPairs;
	}

	@JsonValue
	public Object getData() {
		return this.data;
	}
}
