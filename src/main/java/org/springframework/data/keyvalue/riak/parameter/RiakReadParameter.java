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
public class RiakReadParameter extends RiakParameter {

	/**
	 * @param key
	 * @param value
	 */
	private RiakReadParameter(String key, String value) {
		super(key, value);
	}

	public static final RiakReadParameter read(RiakQuorumValue read) {
		return new RiakReadParameter("r", read.toString());
	}

	public static final RiakReadParameter vtag(String vtag) {
		return new RiakReadParameter("vtag", vtag);
	}
}
