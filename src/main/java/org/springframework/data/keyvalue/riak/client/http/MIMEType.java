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

import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.http.MediaType;

/**
 * @author Andrew Berman
 * 
 */
public class MIMEType extends MediaType {
	public static final MIMEType MULTIPART_MIXED = new MIMEType("multipart",
			"mixed");

	public MIMEType(org.springframework.http.MediaType other,
			Map<String, String> parameters) {
		super(other, parameters);
	}

	public MIMEType(String type, String subtype, Charset charSet) {
		super(type, subtype, charSet);
	}

	public MIMEType(String type, String subtype, double qualityValue) {
		super(type, subtype, qualityValue);
	}

	public MIMEType(String type, String subtype, Map<String, String> parameters) {
		super(type, subtype, parameters);
	}

	public MIMEType(String type, String subtype) {
		super(type, subtype);
	}

	public MIMEType(String type) {
		super(type);
	}

}
