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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.keyvalue.riak.util.RiakConstants;

@JsonSerialize(include = Inclusion.NON_NULL, as = Map.class)
public class RiakBucketProperties extends HashMap<String, Object> implements
		Map<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2505648174204678446L;

	public RiakBucketProperties() {

	}

	public RiakBucketProperties(RiakBucketProperty<?>[] props) {
		for (RiakBucketProperty<?> prop : props)
			put(prop.getKey(), prop.getValue());
	}

	public String getName() {
		return (String) get(RiakConstants.NAME);
	}

	public String getnVal() {
		return (String) get(RiakConstants.N_VAL);
	}

	public Boolean getAllowMulti() {
		return (Boolean) get(RiakConstants.ALLOW_MULT);
	}

	public Boolean getLastWriteWins() {
		return (Boolean) get(RiakConstants.LAST_WRITE_WINS);
	}

	public String[] getPostCommit() {
		return (String[]) get(RiakConstants.POST_COMMIT);
	}

	public String[] getPreCommit() {
		return (String[]) get(RiakConstants.PRE_COMMIT);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getChashKeyFun() {
		return (Map<String, String>) get(RiakConstants.CHASH_KEYFUN);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getLinkFun() {
		return (Map<String, String>) get(RiakConstants.LINKFUN);
	}

	public Long getOldVClock() {
		return (Long) get(RiakConstants.OLD_VCLOCK);
	}

	public Long getYoungVClock() {
		return (Long) get(RiakConstants.YOUNG_VCLOCK);
	}

	public Long getBigVClock() {
		return (Long) get(RiakConstants.BIG_VCLOCK);
	}

	public Long getSmallVClock() {
		return (Long) get(RiakConstants.SMALL_VCLOCK);
	}

	public RiakQuorumValue getRead() {
		return (RiakQuorumValue) get(RiakConstants.READ);
	}

	public RiakQuorumValue getWrite() {
		return (RiakQuorumValue) get(RiakConstants.WRITE);
	}

	public RiakQuorumValue getReadWrite() {
		return (RiakQuorumValue) get(RiakConstants.READ_WRITE);
	}

	public RiakQuorumValue getDurableWrite() {
		return (RiakQuorumValue) get(RiakConstants.DURABLE_WRITE);
	}

}