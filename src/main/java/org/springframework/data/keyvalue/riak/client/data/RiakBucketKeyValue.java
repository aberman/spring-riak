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

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Andrew Berman
 * 
 */
public class RiakBucketKeyValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3228177304912920112L;

	private String bucket;

	private String key;

	private String data;

	public RiakBucketKeyValue(String bucket, String key) {
		this.bucket = bucket;
		this.key = key;
	}

	public RiakBucketKeyValue(String bucket, String key, String data) {
		this.bucket = bucket;
		this.key = key;
		this.data = data;
	}

	public String getBucket() {
		return bucket;
	}

	public String getKey() {
		return key;
	}

	public String getData() {
		return data;
	}

	@JsonValue
	public String[] toArray() {
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(data))
			return new String[] { bucket, key, data };
		else if (StringUtils.isNotBlank(key))
			return new String[] { bucket, key };
		else
			return new String[] { bucket };
	}

}
