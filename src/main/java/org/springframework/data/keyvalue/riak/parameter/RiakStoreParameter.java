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

import org.springframework.data.keyvalue.riak.client.data.RiakQuorumValue;

/**
 * @author Andrew Berman
 * 
 */
public class RiakStoreParameter extends RiakParameter {

	/**
	 * @param key
	 * @param value
	 */
	private RiakStoreParameter(String key, String value) {
		super(key, value);
	}

	public static final RiakStoreParameter read(RiakQuorumValue read) {
		return new RiakStoreParameter("r", read.toString());
	}

	public static final RiakStoreParameter write(RiakQuorumValue write) {
		return new RiakStoreParameter("w", write.toString());
	}

	public static final RiakStoreParameter durableWrite(
			RiakQuorumValue durableWrite) {
		return new RiakStoreParameter("dw", durableWrite.toString());
	}

	public static final RiakStoreParameter shouldReturnBody(boolean returnBody) {
		return new RiakStoreParameter("returnbody",
				Boolean.toString(returnBody));
	}

}
