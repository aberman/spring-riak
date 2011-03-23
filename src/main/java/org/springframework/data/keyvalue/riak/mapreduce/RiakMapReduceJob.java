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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.keyvalue.riak.data.RiakBucketKeyPair;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceFilter;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceRestrictions;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceTransformations;

/**
 * @author Andrew Berman
 * 
 */
@JsonPropertyOrder({ "bucket", "key_filters", "map" })
public class RiakMapReduceJob {

	private Integer timeout;

	@JsonProperty
	private Inputs inputs;

	@JsonProperty("map")
	private List<Phase> phases = new ArrayList<Phase>();

	private enum PhaseType {
		MAP, REDUCE, LINK
	}

	public RiakMapReduceJob(String bucket) {
		this.inputs = new Inputs(bucket);
	}

	public RiakMapReduceJob(RiakBucketKeyPair bucketKeyPairs) {
		this.inputs = new Inputs(bucketKeyPairs);
	}

//	public RiakMapReduceJob addBucketKeyPairs(
//			RiakBucketKeyPair... bucketKeyPairs) {
//		this.inputs.getBucketKeyPairs().addAll(Arrays.asList(bucketKeyPairs));
//		return this;
//	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public RiakMapReduceJob addFilter(RiakMapReduceFilter... filters) {
		this.inputs.getFilters().addAll(Arrays.asList(filters));
		return this;
	}

	public RiakMapReduceJob addLink(String bucket, boolean keepResults) {
		return addLink(bucket, null, keepResults);
	}

	public RiakMapReduceJob addLink(String bucket, String tag,
			boolean keepResults) {
		phases.add(new Phase(PhaseType.LINK, new RiakLinkFunction(bucket, tag),
				null, keepResults));
		return this;
	}

	public RiakMapReduceJob addMap(RiakMapReduceFunction mapFunction,
			boolean keepResults) {
		return addMap(mapFunction, null, keepResults);
	}

	public RiakMapReduceJob addMap(RiakMapReduceFunction mapFunction,
			Object functionArg, boolean keepResults) {
		phases.add(new Phase(PhaseType.REDUCE, mapFunction, functionArg,
				keepResults));
		return this;
	}

	public RiakMapReduceJob addReduce(RiakMapReduceFunction mapFunction,
			boolean keepResults) {
		return addMap(mapFunction, null, keepResults);
	}

	public RiakMapReduceJob addReduce(RiakMapReduceFunction mapFunction,
			Object functionArg, boolean keepResults) {
		phases.add(new Phase(PhaseType.REDUCE, mapFunction, functionArg,
				keepResults));
		return this;
	}

	private class Phase {
		private PhaseType type;
		private RiakMapReduceFunction function;
		private Object arg;
		private boolean keepResults;

		public Phase(PhaseType type, RiakMapReduceFunction function,
				Object arg, boolean keepResults) {
			this.type = type;
			this.function = function;
			this.arg = arg;
			this.keepResults = keepResults;
		}

	}

	@JsonPropertyOrder({ "bucket", "bucketKeyPairs", "filters" })
	@JsonSerialize(include = Inclusion.NON_NULL)
	private class Inputs {
		private String bucket;

		private List<RiakMapReduceFilter> filters = new ArrayList<RiakMapReduceFilter>();

		private RiakBucketKeyPair bucketKeyPair;

		public Inputs(String bucket) {
			this.bucket = bucket;
		}

		public Inputs(RiakBucketKeyPair keyPair) {
			this.bucketKeyPair = keyPair;
		}
		
		public Inputs(RiakMapReduceFilter... filters) {
			this.filters.addAll(Arrays.asList(filters));
		}

		@JsonProperty("bucket")
		public String getBucket() {
			return bucket;
		}

		@JsonProperty("key_filters")
		public List<RiakMapReduceFilter> getFilters() {
			return filters;
		}

		@JsonProperty
		public RiakBucketKeyPair getBucketKeyPair() {
			return bucketKeyPair;
		}

	}

	public static void main(String[] args) throws Exception {
		RiakMapReduceJob job = new RiakMapReduceJob(new RiakBucketKeyPair(
				"test", "test1", "test2")).addFilter(
				RiakMapReduceRestrictions.between(1, 2, true)).addFilter(
				RiakMapReduceTransformations.floatToString());
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(job));

	}

}
