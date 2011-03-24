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
package org.springframework.data.keyvalue.riak.client;

import static junit.framework.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters.KeyRetrievalType;

/**
 * @author andrewberman
 * 
 */
public class RiakRestClientTests {

	private RiakRestClient restClient;

	@Before
	public void setUp() {
		restClient = new RiakRestClient("192.168.1.6", 8098, false);
		// Need to set up data for testing
	}

	@After
	public void tearDown() {
		// Need to delete any data that was created
	}

	@Test
	public void listBucketsTest() {
		assertNotNull(restClient.listBuckets());
	}

	@Test
	public void getBucketInformationTest() {
		RiakBucket bucket = restClient.getBucketInformation("test",
				new RiakBucketReadParameters(true, KeyRetrievalType.TRUE));
		assertNotNull(bucket);
		assertNotNull(bucket.getBucketProperties());
		assertNotNull(bucket.getKeys());
	}
}
