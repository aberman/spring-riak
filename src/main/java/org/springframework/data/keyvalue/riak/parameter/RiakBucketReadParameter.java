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

import org.springframework.data.keyvalue.riak.util.RiakConstants;


/**
 * @author Andrew Berman
 * 
 */
public class RiakBucketReadParameter extends RiakParameter {
	/**
	 * @param key
	 * @param value
	 */
	private RiakBucketReadParameter(String key, String value) {
		super(key, value);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3932057006249425875L;

	public enum KeyRetrievalType {
		TRUE, FALSE, STREAM;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public static final RiakBucketReadParameter showProperties(
			boolean showProperties) {
		return new RiakBucketReadParameter(RiakConstants.PROPS,
				Boolean.toString(showProperties));
	}

	public static final RiakBucketReadParameter keyRetrievalType(
			KeyRetrievalType type) {
		return new RiakBucketReadParameter(RiakConstants.KEYS, type.toString());
	}
}
