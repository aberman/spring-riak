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

import org.springframework.data.keyvalue.riak.client.data.RiakLink;
import org.springframework.data.keyvalue.riak.client.data.RiakQuorumValue;
import org.springframework.data.keyvalue.riak.client.http.HttpHeaders;
import org.springframework.data.keyvalue.riak.util.RiakConstants;

/**
 * @author Andrew Berman
 * 
 */
public class RiakStoreParameter extends RiakParameter {

	/**
	 * @param key
	 * @param value
	 */
	protected RiakStoreParameter(String key, String value, Type type) {
		super(key, value, type);
	}

	public static final RiakStoreParameter write(RiakQuorumValue write) {
		return new RiakStoreParameter(RiakConstants.WRITE, write.toString(),
				Type.QUERY);
	}

	public static final RiakStoreParameter durableWrite(RiakQuorumValue durableWrite) {
		return new RiakStoreParameter(RiakConstants.DURABLE_WRITE,
				durableWrite.toString(), Type.QUERY);
	}

	public static final RiakStoreParameter shouldReturnBody(boolean returnBody) {
		return new RiakStoreParameter(RiakConstants.RETURNBODY,
				Boolean.toString(returnBody), Type.QUERY);
	}

	public static final RiakStoreParameter metaDataHeader(String metaData) {
		return new RiakStoreParameter(HttpHeaders.X_RIAK_META, metaData,
				Type.HEADER);
	}

	public static final RiakStoreParameter link(RiakLink link) {
		return new RiakStoreParameter(HttpHeaders.LINK, link.toString(),
				Type.HEADER);
	}
	
	public static final RiakStoreParameter clientId(String clientId) {
		return new RiakStoreParameter(HttpHeaders.X_RIAK_CLIENT_ID, clientId,
				Type.HEADER);
	}

	
}
