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
package org.springframework.data.keyvalue.riak.parameter;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakQuorumValue;

/**
 * @author Andrew Berman
 * 
 */
public class RiakReadParameters extends RiakParameters {
	private RiakQuorumValue read;

	private String vtag;

	public RiakReadParameters() {
		super();
	}
	
	
	public RiakReadParameters(RiakQuorumValue read, String vtag) {
		super();
		this.read = read;
		this.vtag = vtag;
	}


	@JsonProperty("r")
	public RiakQuorumValue getRead() {
		return read;
	}

	@JsonProperty("r")
	public RiakReadParameters setRead(RiakQuorumValue read) {
		this.read = read;
		return this;
	}

	@JsonProperty("vtag")
	public String getVtag() {
		return vtag;
	}

	@JsonProperty("vtag")
	public RiakReadParameters setVtag(String vtag) {
		this.vtag = vtag;
		return this;
	}
}
