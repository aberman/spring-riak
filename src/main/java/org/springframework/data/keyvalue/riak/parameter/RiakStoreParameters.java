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

/**
 * @author Andrew Berman
 * 
 */
public class RiakStoreParameters extends RiakParameters {
	private Integer read;

	private Integer write;

	private Integer durableWrite;

	private boolean returnBody;

	@JsonProperty("r")
	public Integer getRead() {
		return read;
	}

	@JsonProperty("r")
	public void setRead(Integer read) {
		this.read = read;
	}

	@JsonProperty("w")
	public Integer getWrite() {
		return write;
	}

	@JsonProperty("w")
	public void setWrite(Integer write) {
		this.write = write;
	}

	@JsonProperty("dw")
	public Integer getDurableWrite() {
		return durableWrite;
	}

	@JsonProperty("dw")
	public void setDurableWrite(Integer durableWrite) {
		this.durableWrite = durableWrite;
	}

	@JsonProperty("returnbody")
	public boolean isReturnBody() {
		return returnBody;
	}

	@JsonProperty("returnbody")
	public void setReturnBody(boolean returnBody) {
		this.returnBody = returnBody;
	}

}
