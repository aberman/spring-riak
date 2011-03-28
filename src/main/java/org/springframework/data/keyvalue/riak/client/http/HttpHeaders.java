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
package org.springframework.data.keyvalue.riak.client.http;

import java.util.Map;

import org.springframework.data.keyvalue.riak.util.RiakExtraInfo;

/**
 * @author Andrew Berman
 * 
 */
public class HttpHeaders extends org.springframework.http.HttpHeaders implements
		RiakExtraInfo {
	public static final String DATE_PATTERN = "EEE, dd MMM YYYY HH:mm:ss zzz";
	public static final String X_RIAK_META = "X-Riak-Meta";
	public static final String LINK = "Link";
	public static final String ACCEPT = "Accept";
	public static final String IF_NONE_MATCH = "If-None-Match";
	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
	public static final String X_RIAK_VCLOCK = "X-Riak-Vclock";
	public static final String IF_MATCH = "If-Match";
	public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
	public static final String X_RIAK_CLIENT_ID = "X-Riak-ClientId";

	public HttpHeaders(org.springframework.http.HttpHeaders headers) {
		super();
		this.putAll(org.springframework.http.HttpHeaders
				.readOnlyHttpHeaders(headers));
	}
	
	public HttpHeaders() {
		super();
	}

	@Override
	public Map<String, String> getInfo() {
		return this.toSingleValueMap();
	}

}
