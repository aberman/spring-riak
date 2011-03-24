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
package org.springframework.data.keyvalue.riak.client.data;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonValue;

public class RiakQuorumValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7122009472874090333L;

	public static final RiakQuorumValue QUORUM = new RiakQuorumValue("quorum");

	public static final RiakQuorumValue ALL = new RiakQuorumValue("all");

	public static final RiakQuorumValue ONE = new RiakQuorumValue("one");

	private Object quorumValue;

	public RiakQuorumValue(int quorumValue) {
		this.quorumValue = new Integer(quorumValue);
	}

	public RiakQuorumValue(String quorumValue) {
		this.quorumValue = quorumValue;
	}

	public RiakQuorumValue() {

	}

	@JsonValue
	public Object getQuorumValue() {
		return quorumValue;
	}

	void setQuorumValue(Object quorumValue) {
		this.quorumValue = quorumValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((quorumValue == null) ? 0 : quorumValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RiakQuorumValue other = (RiakQuorumValue) obj;
		if (quorumValue == null) {
			if (other.quorumValue != null)
				return false;
		} else if (!quorumValue.equals(other.quorumValue))
			return false;
		return true;
	}

}