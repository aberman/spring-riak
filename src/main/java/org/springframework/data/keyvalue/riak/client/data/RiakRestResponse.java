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

import org.apache.commons.lang.StringUtils;
import org.springframework.data.keyvalue.riak.client.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * @author Andrew Berman
 * 
 */
public class RiakRestResponse<T> implements RiakResponse<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4613049362224120711L;
	private T data;
	private HttpHeaders extraInfo;
	private String responseStatus;
	private String id;

	public RiakRestResponse(ResponseEntity<T> re) {
		this(re.getBody(), new HttpHeaders(re.getHeaders()), re.getStatusCode()
				.toString());
	}

	public static final String parseLocationForId(String location) {
		String[] split = StringUtils.split(location, "/");
		return split[split.length - 1];
	}

	public RiakRestResponse(T data, HttpHeaders extraInfo, String responseStatus) {
		this.data = data;
		this.extraInfo = extraInfo;
		this.responseStatus = responseStatus;
		this.id = parseLocationForId(extraInfo.getLocation().getPath());
	}

	@Override
	public T getData() {
		return this.data;
	}

	@Override
	public HttpHeaders getExtraInfo() {
		return this.extraInfo;
	}

	@Override
	public String getResponseStatus() {
		return this.responseStatus;
	}

	@Override
	public String getId() {
		return this.id;
	}

}
