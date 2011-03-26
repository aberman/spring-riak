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

import java.util.Collections;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonValue;

public class RiakBucketProperty<T> {

	private String key;
	private T value;

	public static final RiakBucketProperty<String> name(String name) {
		return new RiakBucketProperty<String>("name", name);
	}

	public static final RiakBucketProperty<String> nVal(String nVal) {
		return new RiakBucketProperty<String>("n_val", nVal);
	}

	public static final RiakBucketProperty<Boolean> allowMulti(
			boolean allowMulti) {
		return new RiakBucketProperty<Boolean>("allow_mult", new Boolean(
				allowMulti));
	}

	public static final RiakBucketProperty<Boolean> lastWriteWins(
			boolean lastWriteWins) {
		return new RiakBucketProperty<Boolean>("last_write_wins", new Boolean(
				lastWriteWins));
	}

	public static final RiakBucketProperty<String[]> preCommit(
			String[] preCommit) {
		return new RiakBucketProperty<String[]>("pre_commit", preCommit);
	}

	public static final RiakBucketProperty<String[]> postCommit(
			String[] postCommit) {
		return new RiakBucketProperty<String[]>("post_commit", postCommit);
	}

	public static final RiakBucketProperty<Map<String, String>> chashKeyFun(
			Map<String, String> chashKeyFun) {
		return new RiakBucketProperty<Map<String, String>>("chash_keyfun",
				chashKeyFun);
	}

	public static final RiakBucketProperty<Map<String, String>> linkFun(
			Map<String, String> linkFun) {
		return new RiakBucketProperty<Map<String, String>>("linkfun", linkFun);
	}

	public static final RiakBucketProperty<Long> oldVClock(Long oldVClock) {
		return new RiakBucketProperty<Long>("old_vclock", oldVClock);
	}

	public static final RiakBucketProperty<Long> youngVClock(Long youngVClock) {
		return new RiakBucketProperty<Long>("young_vclock", youngVClock);
	}

	public static final RiakBucketProperty<Long> bigVClock(Long bigVClock) {
		return new RiakBucketProperty<Long>("big_vclock", bigVClock);
	}

	public static final RiakBucketProperty<Long> smallVClock(Long smallVClock) {
		return new RiakBucketProperty<Long>("small_vclock", smallVClock);
	}

	public static final RiakBucketProperty<RiakQuorumValue> read(
			RiakQuorumValue read) {
		return new RiakBucketProperty<RiakQuorumValue>("r", read);
	}

	public static final RiakBucketProperty<RiakQuorumValue> write(
			RiakQuorumValue write) {
		return new RiakBucketProperty<RiakQuorumValue>("w", write);
	}

	public static final RiakBucketProperty<RiakQuorumValue> readWrite(
			RiakQuorumValue readWrite) {
		return new RiakBucketProperty<RiakQuorumValue>("rw", readWrite);
	}

	public static final RiakBucketProperty<RiakQuorumValue> durableWrite(
			RiakQuorumValue durableWrite) {
		return new RiakBucketProperty<RiakQuorumValue>("dw", durableWrite);
	}

	/**
	 * @param key
	 * @param value
	 */
	private RiakBucketProperty(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@JsonValue
	public Map<String, T> toMap() {
		return Collections.singletonMap(key, value);
	}

}