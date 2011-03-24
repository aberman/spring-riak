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

import org.springframework.data.keyvalue.riak.util.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

/**
 * @author Andrew Berman
 * 
 */
public class RiakRestResponse implements RiakResponse<HttpHeaders> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4613049362224120711L;
	private ResponseEntity<byte[]> re;

	public RiakRestResponse(ResponseEntity<byte[]> re) {
		Assert.notNull(re, "ResponseEntity cannot be null");
		this.re = re;
	}

	@Override
	public byte[] getBytes() {
		return re.getBody();
	}

	@Override
	public HttpHeaders getExtraInfo() {
		return (HttpHeaders) re.getHeaders();
	}

	public HttpStatus getHttpStatus() {
		return re.getStatusCode();
	}
}
