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
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class RiakBucketProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7247402092105288471L;

	private String name;

	private String nVal;

	private Boolean allowMult;

	private Boolean lastWriteWins;

	private String[] preCommit;

	private String[] postCommit;

	private Map<String, String> chashKeyFun;

	private Map<String, String> linkFun;

	private Long oldVClock;

	private Long youngVClock;

	private Long bigVClock;

	private Long smallVClock;

	private RiakQuorumValue readOperation;

	private RiakQuorumValue writeOperation;

	private RiakQuorumValue durableWriteOperation;

	private RiakQuorumValue readWriteOperation;

	protected RiakBucketProperties() {

	}

	public static final RiakBucketProperties getInstance() {
		return new RiakBucketProperties();
	}

	public RiakBucketProperties(String name, String nVal, Boolean allowMult,
			Boolean lastWriteWins, String[] preCommit, String[] postCommit,
			Map<String, String> chashKeyFun, Map<String, String> linkFun,
			Long oldVClock, Long youngVClock, Long bigVClock, Long smallVClock,
			RiakQuorumValue readOperation, RiakQuorumValue writeOperation,
			RiakQuorumValue durableWriteOperation,
			RiakQuorumValue readWriteOperation) {
		this.name = name;
		this.nVal = nVal;
		this.allowMult = allowMult;
		this.lastWriteWins = lastWriteWins;
		this.preCommit = preCommit;
		this.postCommit = postCommit;
		this.chashKeyFun = chashKeyFun;
		this.linkFun = linkFun;
		this.oldVClock = oldVClock;
		this.youngVClock = youngVClock;
		this.bigVClock = bigVClock;
		this.smallVClock = smallVClock;
		this.readOperation = readOperation;
		this.writeOperation = writeOperation;
		this.durableWriteOperation = durableWriteOperation;
		this.readWriteOperation = readWriteOperation;
	}

	public String getName() {
		return name;
	}

	public RiakBucketProperties setName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("n_val")
	public String getNVal() {
		return nVal;
	}

	@JsonProperty("n_val")
	public RiakBucketProperties setNVal(String nVal) {
		this.nVal = nVal;
		return this;
	}

	@JsonProperty("allow_mult")
	public Boolean isAllowMult() {
		return allowMult;
	}

	@JsonProperty("allow_mult")
	public RiakBucketProperties setAllowMult(Boolean allowMult) {
		this.allowMult = allowMult;
		return this;
	}

	@JsonProperty("last_write_wins")
	public Boolean isLastWriteWins() {
		return lastWriteWins;
	}

	@JsonProperty("last_write_wins")
	public RiakBucketProperties setLastWriteWins(Boolean lastWriteWins) {
		this.lastWriteWins = lastWriteWins;
		return this;
	}

	@JsonProperty("precommit")
	public String[] getPreCommit() {
		return preCommit;
	}

	@JsonProperty("precommit")
	public RiakBucketProperties setPreCommit(String[] preCommit) {
		this.preCommit = preCommit;
		return this;
	}

	@JsonProperty("postcommit")
	public String[] getPostCommit() {
		return postCommit;
	}

	@JsonProperty("postcommit")
	public RiakBucketProperties setPostCommit(String[] postCommit) {
		this.postCommit = postCommit;
		return this;
	}

	@JsonProperty("chash_keyfun")
	public Map<String, String> getChashKeyFun() {
		return chashKeyFun;
	}

	@JsonProperty("chash_keyfun")
	public RiakBucketProperties setChashKeyFun(Map<String, String> chashKeyFun) {
		this.chashKeyFun = chashKeyFun;
		return this;
	}

	@JsonProperty("linkfun")
	public Map<String, String> getLinkFun() {
		return linkFun;
	}

	@JsonProperty("linkfun")
	public RiakBucketProperties setLinkFun(Map<String, String> linkFun) {
		this.linkFun = linkFun;
		return this;
	}

	@JsonProperty("old_vclock")
	public Long getOldVClock() {
		return oldVClock;
	}

	@JsonProperty("old_vclock")
	public RiakBucketProperties setOldVClock(Long oldVClock) {
		this.oldVClock = oldVClock;
		return this;
	}

	@JsonProperty("young_vclock")
	public Long getYoungVClock() {
		return youngVClock;
	}

	@JsonProperty("young_vclock")
	public RiakBucketProperties setYoungVClock(Long youngVClock) {
		this.youngVClock = youngVClock;
		return this;
	}

	@JsonProperty("big_vclock")
	public Long getBigVClock() {
		return bigVClock;
	}

	@JsonProperty("big_vclock")
	public RiakBucketProperties setBigVClock(Long bigVClock) {
		this.bigVClock = bigVClock;
		return this;
	}

	@JsonProperty("small_vclock")
	public Long getSmallVClock() {
		return smallVClock;
	}

	@JsonProperty("small_vclock")
	public RiakBucketProperties setSmallVClock(Long smallVClock) {
		this.smallVClock = smallVClock;
		return this;
	}

	@JsonProperty("r")
	public RiakQuorumValue getReadOperation() {
		return readOperation;
	}

	@JsonProperty("r")
	public RiakBucketProperties setReadOperation(RiakQuorumValue readOperation) {
		this.readOperation = readOperation;
		return this;
	}

	@JsonProperty("w")
	public RiakQuorumValue getWriteOperation() {
		return writeOperation;
	}

	@JsonProperty("w")
	public RiakBucketProperties setWriteOperation(RiakQuorumValue writeOperation) {
		this.writeOperation = writeOperation;
		return this;
	}

	@JsonProperty("dw")
	public RiakQuorumValue getDurableWriteOperation() {
		return durableWriteOperation;
	}

	@JsonProperty("dw")
	@JsonSetter("dw")
	public RiakBucketProperties setDurableWriteOperation(
			RiakQuorumValue durableWriteOperation) {
		this.durableWriteOperation = durableWriteOperation;
		return this;
	}

	@JsonProperty("rw")
	public RiakQuorumValue getReadWriteOperation() {
		return readWriteOperation;
	}

	@JsonProperty("rw")
	public RiakBucketProperties setReadWriteOperation(
			RiakQuorumValue readWriteOperation) {
		this.readWriteOperation = readWriteOperation;
		return this;
	}
}