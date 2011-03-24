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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketKeyValue;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceKeyFilter;

/**
 * @author Andrew Berman
 * 
 */
@JsonPropertyOrder({ "inputs", "query", "timeout" })
@JsonSerialize(include = Inclusion.NON_NULL)
public class RiakMapReduceJob {

	private Integer timeout;

	private RiakMapReduceInput input;

	private List<Map<PhaseType, ? extends RiakAbstractPhaseFunction<?>>> riakPhases = new ArrayList<Map<PhaseType, ? extends RiakAbstractPhaseFunction<?>>>();

	private enum PhaseType {
		MAP, REDUCE, LINK;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

	}

	public RiakMapReduceJob(String bucket) {
		this.input = RiakMapReduceInput.getInstance(bucket);
	}

	public RiakMapReduceJob(RiakBucketKeyValue... bucketKeyPairs) {
		this.input = RiakMapReduceInput.getInstance(bucketKeyPairs);
	}

	public RiakMapReduceJob(String bucket, RiakMapReduceKeyFilter... filter) {
		this.input = RiakMapReduceInput.getInstance(bucket, filter);
	}

	@JsonProperty
	public Integer getTimeout() {
		return timeout;
	}

	@JsonProperty("inputs")
	RiakMapReduceInput getInput() {
		return input;
	}

	@JsonProperty("query")
	List<Map<PhaseType, ? extends RiakAbstractPhaseFunction<?>>> getPhases() {
		return riakPhases;
	}

	public RiakMapReduceJob setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	public RiakMapReduceJob addLink(String bucket, String tag) {
		riakPhases.add(Collections.singletonMap(PhaseType.LINK,
				new RiakLinkPhase(bucket, tag)));
		return this;
	}

	public RiakMapReduceJob addLink(String bucket) {
		return this.addLink(bucket, null);
	}

	public RiakMapReduceJob addEmptyLink() {
		return this.addLink(null, null);
	}

	public RiakMapReduceJob addMap(RiakMapReduceFunction mapFunction) {
		riakPhases.add(Collections.singletonMap(PhaseType.MAP, mapFunction));
		return this;
	}

	public RiakMapReduceJob addReduce(RiakMapReduceFunction mapFunction) {
		riakPhases.add(Collections.singletonMap(PhaseType.REDUCE, mapFunction));
		return this;
	}
}
