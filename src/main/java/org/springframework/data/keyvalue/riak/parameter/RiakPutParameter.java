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

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.keyvalue.riak.client.data.RiakQuorumValue;
import org.springframework.data.keyvalue.riak.client.http.HttpHeaders;
import org.springframework.data.keyvalue.riak.util.RiakConstants;

/**
 * @author Andrew Berman
 * 
 */
public class RiakPutParameter extends RiakStoreParameter {

	private RiakPutParameter(String key, String value, Type type) {
		super(key, value, Type.HEADER);
	}

	public static final RiakPutParameter read(RiakQuorumValue read) {
		return new RiakPutParameter(RiakConstants.READ, read.toString(),
				Type.QUERY);
	}

	public static final RiakPutParameter vclock(long vclock) {
		return new RiakPutParameter(HttpHeaders.X_RIAK_VCLOCK,
				String.valueOf(vclock), Type.HEADER);
	}

	public static final RiakPutParameter ifNoneMatch(String etag) {
		return new RiakPutParameter(HttpHeaders.IF_NONE_MATCH, etag,
				Type.HEADER);
	}

	public static final RiakPutParameter ifMatch(String etag) {
		return new RiakPutParameter(HttpHeaders.IF_MATCH, etag, Type.HEADER);
	}

	public static final RiakPutParameter ifModifiedSince(Date date) {
		return new RiakPutParameter(HttpHeaders.IF_MODIFIED_SINCE,
				DateTimeFormat.forPattern(HttpHeaders.DATE_PATTERN).print(
						new DateTime(date).toDateTime(DateTimeZone.UTC)),
				Type.HEADER);
	}

	public static final RiakPutParameter ifUnmodifiedSince(Date date) {
		return new RiakPutParameter(HttpHeaders.IF_UNMODIFIED_SINCE,
				DateTimeFormat.forPattern(HttpHeaders.DATE_PATTERN).print(
						new DateTime(date).toDateTime(DateTimeZone.UTC)),
				Type.HEADER);
	}

}
