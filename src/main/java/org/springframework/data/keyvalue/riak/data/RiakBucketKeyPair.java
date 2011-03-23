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
package org.springframework.data.keyvalue.riak.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Andrew Berman
 * 
 */
public class RiakBucketKeyPair implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3228177304912920112L;

	private String bucket;

	private Set<String> keys;

	public RiakBucketKeyPair(String bucket) {
		this.bucket = bucket;
	}

	public RiakBucketKeyPair(String bucket, String... keys) {
		this.bucket = bucket;
		this.keys = new HashSet<String>(Arrays.asList(keys));
	}

	public RiakBucketKeyPair addKey(String key) {
		this.keys.add(key);
		return this;
	}

	public String getBucket() {
		return bucket;
	}

	public Set<String> getKeys() {
		return keys;
	}

	@JsonValue
	public List<String[]> toListArray() {
		List<String[]> list = new ArrayList<String[]>();

		for (String key : keys) {
			list.add(new String[] { bucket, key });
		}

		return list;
	}

}
