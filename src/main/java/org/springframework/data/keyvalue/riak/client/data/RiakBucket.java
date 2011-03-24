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

import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Andrew Berman
 * 
 */
public class RiakBucket {
	@JsonProperty("props")
	private RiakBucketProperties bucketProperties;

	private Set<String> keys;

	public RiakBucket() {

	}

	/**
	 * @param bucketInfo
	 * @param keys
	 */
	public RiakBucket(RiakBucketProperties bucketProperties, Set<String> keys) {
		this.bucketProperties = bucketProperties;
		this.keys = keys;
	}

	public RiakBucketProperties getBucketProperties() {
		return bucketProperties;
	}

	public void setBucketInfo(RiakBucketProperties bucketProperties) {
		this.bucketProperties = bucketProperties;
	}

	@JsonIgnore
	public Set<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}

	public static class RiakBucketProperties {
		private String name;

		private String nVal;

		private boolean allowMult;

		private boolean lastWriteWins;

		private String[] preCommit;

		private String[] postCommit;

		private Map<String, String> chashKeyFun;

		private Map<String, String> linkFun;

		private long oldVClock;

		private long youngVClock;

		private long bigVClock;

		private long smallVClock;

		private String readOperation;

		private String writeOperation;

		private String durableWriteOperation;

		private String readWriteOperation;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@JsonProperty("n_val")
		public String getNVal() {
			return nVal;
		}

		@JsonProperty("n_val")
		public void setNVal(String nVal) {
			this.nVal = nVal;
		}

		@JsonProperty("allow_mult")
		public boolean isAllowMult() {
			return allowMult;
		}

		@JsonProperty("allow_mult")
		public void setAllowMult(boolean allowMult) {
			this.allowMult = allowMult;
		}

		@JsonProperty("last_write_wins")
		public boolean isLastWriteWins() {
			return lastWriteWins;
		}

		@JsonProperty("last_write_wins")
		public void setLastWriteWins(boolean lastWriteWins) {
			this.lastWriteWins = lastWriteWins;
		}

		@JsonProperty("precommit")
		public String[] getPreCommit() {
			return preCommit;
		}

		@JsonProperty("precommit")
		public void setPreCommit(String[] preCommit) {
			this.preCommit = preCommit;
		}

		@JsonProperty("postcommit")
		public String[] getPostCommit() {
			return postCommit;
		}

		@JsonProperty("postcommit")
		public void setPostCommit(String[] postCommit) {
			this.postCommit = postCommit;
		}

		@JsonProperty("chash_keyfun")
		public Map<String, String> getChashKeyFun() {
			return chashKeyFun;
		}

		@JsonProperty("chash_keyfun")
		public void setChashKeyFun(Map<String, String> chashKeyFun) {
			this.chashKeyFun = chashKeyFun;
		}

		@JsonProperty("linkfun")
		public Map<String, String> getLinkFun() {
			return linkFun;
		}

		@JsonProperty("linkfun")
		public void setLinkFun(Map<String, String> linkFun) {
			this.linkFun = linkFun;
		}

		@JsonProperty("old_vclock")
		public long getOldVClock() {
			return oldVClock;
		}

		@JsonProperty("old_vclock")
		public void setOldVClock(long oldVClock) {
			this.oldVClock = oldVClock;
		}

		@JsonProperty("young_vclock")
		public long getYoungVClock() {
			return youngVClock;
		}

		@JsonProperty("young_vclock")
		public void setYoungVClock(long youngVClock) {
			this.youngVClock = youngVClock;
		}

		@JsonProperty("big_vclock")
		public long getBigVClock() {
			return bigVClock;
		}

		@JsonProperty("big_vclock")
		public void setBigVClock(long bigVClock) {
			this.bigVClock = bigVClock;
		}

		@JsonProperty("small_vclock")
		public long getSmallVClock() {
			return smallVClock;
		}

		@JsonProperty("small_vclock")
		public void setSmallVClock(long smallVClock) {
			this.smallVClock = smallVClock;
		}

		@JsonProperty("r")
		public String getReadOperation() {
			return readOperation;
		}

		@JsonProperty("r")
		public void setReadOperation(String readOperation) {
			this.readOperation = readOperation;
		}

		@JsonProperty("w")
		public String getWriteOperation() {
			return writeOperation;
		}

		@JsonProperty("w")
		public void setWriteOperation(String writeOperation) {
			this.writeOperation = writeOperation;
		}

		@JsonProperty("dw")
		public String getDurableWriteOperation() {
			return durableWriteOperation;
		}

		@JsonProperty("dw")
		public void setDurableWriteOperation(String durableWriteOperation) {
			this.durableWriteOperation = durableWriteOperation;
		}

		@JsonProperty("rw")
		public String getReadWriteOperation() {
			return readWriteOperation;
		}

		@JsonProperty("rw")
		public void setReadWriteOperation(String readWriteOperation) {
			this.readWriteOperation = readWriteOperation;
		}

	}
}
