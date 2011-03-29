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
package org.springframework.data.keyvalue.riak.mapreduce;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Andrew Berman
 * 
 */
public class RiakLinkPhase extends RiakAbstractPhaseFunction<RiakLinkPhase> {

	private String bucket;

	private String tag;

	public RiakLinkPhase(String bucket, String tag, boolean keep) {
		this.bucket = bucket;
		this.tag = tag;
		super.setKeep(keep);
	}

	public RiakLinkPhase(String bucket, String tag) {
		this.bucket = bucket;
		this.tag = tag;
	}

	public RiakLinkPhase(String bucket) {
		this(bucket, null);
	}

	public String getBucket() {
		return bucket;
	}

	public String getTag() {
		return tag;
	}

	public String toUrlFormat() {
		String bucket = StringUtils.defaultIfBlank(getBucket(), "_");
		String tag = StringUtils.defaultIfBlank(getTag(), "_");
		String keep = super.getKeep() == null ? "_" : BooleanUtils
				.toIntegerObject(super.getKeep()).toString();

		return bucket + "," + tag + "," + keep;
	}
}
