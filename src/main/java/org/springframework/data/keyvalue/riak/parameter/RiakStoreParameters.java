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
public class RiakStoreParameters extends RiakParameters {
	private RiakQuorumValue read;

	private RiakQuorumValue write;

	private RiakQuorumValue durableWrite;

	private boolean returnBody;

	public RiakStoreParameters() {

	}

	public RiakStoreParameters(RiakQuorumValue read, RiakQuorumValue write,
			RiakQuorumValue durableWrite, boolean returnBody) {
		super();
		this.read = read;
		this.write = write;
		this.durableWrite = durableWrite;
		this.returnBody = returnBody;
	}

	@JsonProperty("r")
	public RiakQuorumValue getRead() {
		return read;
	}

	@JsonProperty("r")
	public RiakStoreParameters setRead(RiakQuorumValue read) {
		this.read = read;
		return this;
	}

	@JsonProperty("w")
	public RiakQuorumValue getWrite() {
		return write;
	}

	@JsonProperty("w")
	public RiakStoreParameters setWrite(RiakQuorumValue write) {
		this.write = write;
		return this;
	}

	@JsonProperty("dw")
	public RiakQuorumValue getDurableWrite() {
		return durableWrite;
	}

	@JsonProperty("dw")
	public RiakStoreParameters setDurableWrite(RiakQuorumValue durableWrite) {
		this.durableWrite = durableWrite;
		return this;
	}

	@JsonProperty("returnbody")
	public boolean isReturnBody() {
		return returnBody;
	}

	@JsonProperty("returnbody")
	public RiakStoreParameters setReturnBody(boolean returnBody) {
		this.returnBody = returnBody;
		return this;
	}

}
