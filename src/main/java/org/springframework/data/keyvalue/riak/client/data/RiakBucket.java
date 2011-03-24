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

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Andrew Berman
 * 
 */
public class RiakBucket {
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

	@JsonProperty("props")
	public RiakBucketProperties getBucketProperties() {
		return bucketProperties;
	}

	@JsonProperty("props")
	public void setBucketInfo(RiakBucketProperties bucketProperties) {
		this.bucketProperties = bucketProperties;
	}

	public Set<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return new EqualsBuilder().append(this.getBucketProperties().getName(),
				((RiakBucket) obj).getBucketProperties().getName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(bucketProperties.getName())
				.toHashCode();
	}

}
