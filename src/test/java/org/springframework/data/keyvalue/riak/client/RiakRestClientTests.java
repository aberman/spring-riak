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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperties;
import org.springframework.data.keyvalue.riak.client.data.RiakQuorumValue;
import org.springframework.data.keyvalue.riak.client.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakJavascriptMapReduceFunction.RiakJavascriptNamedFunction;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakMapReduceRestrictions;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters.KeyRetrievalType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author andrewberman
 * 
 */
public class RiakRestClientTests {

	private static RiakManager<RiakRestResponse> restClient;

	public static final String TEST_BUCKET = "RiakRestClientTests.bucket4";

	public static String TEST_KEY;

	public static final String TEST_VALUE = "this is a test value";

	@BeforeClass
	public static void setUp() {
		restClient = new RiakRestClient("localhost", 8099, false);
		TEST_KEY = restClient.storeValue(TEST_BUCKET, TEST_VALUE.getBytes());
	}

	@AfterClass
	public static void tearDown() {
		for (String key : restClient.getBucketInformation(TEST_BUCKET,
				new RiakBucketReadParameters(false, KeyRetrievalType.TRUE))
				.getKeys())
			restClient.deleteKey(TEST_BUCKET, key);
	}

	/*
	 * Bucket tests
	 */
	@Test(groups = { "bucket", "read" })
	public void listBucketsTest() {
		assertNotNull(restClient.listBuckets());
	}

	@Test(groups = { "bucket", "read" })
	public void getBucketInformationTest() {
		RiakBucket bucket = restClient.getBucketInformation(TEST_BUCKET,
				new RiakBucketReadParameters(true, KeyRetrievalType.TRUE));
		assertNotNull(bucket);
		assertNotNull(bucket.getBucketProperties());
		assertNotNull(bucket.getKeys());
	}

	@Test(groups = { "bucket", "write" })
	public void setBucketInformationTest() {
		restClient.setBucketProperties(TEST_BUCKET, new RiakBucketProperties()
				.setWriteOperation(RiakQuorumValue.ALL));
		RiakBucket bucket = restClient.getBucketInformation(TEST_BUCKET);
		assertEquals(bucket.getBucketProperties().getWriteOperation(),
				RiakQuorumValue.ALL);
	}

	/*
	 * Key Value Tests
	 */
	@Test(groups = { "keyValue", "read" })
	public void getValueTest() {
		RiakRestResponse response = restClient.getValue(TEST_BUCKET, TEST_KEY);
		assertNotNull(response);
		assertEquals(new String(response.getBytes()), TEST_VALUE);
	}

	@Test(groups = { "keyValue", "write" })
	public void storeKeyValueTest() {
		restClient.storeKeyValue(TEST_BUCKET, TEST_KEY, "foo test".getBytes());
		RiakRestResponse response = restClient.getValue(TEST_BUCKET, TEST_KEY);
		assertNotNull(response);
		assertEquals(new String(response.getBytes()), "foo test");
	}

	@Test(groups = { "keyValue", "write" })
	public void storeValueTest() {
		String key = restClient.storeValue(TEST_BUCKET, "bar test".getBytes());
		RiakRestResponse response = restClient.getValue(TEST_BUCKET, key);
		assertNotNull(response);
		assertEquals(new String(response.getBytes()), "bar test");
	}

	@Test(groups = { "keyValue", "delete" })
	public void deleteKeyTest() {
		String key = restClient.storeValue(TEST_BUCKET, "foo".getBytes());
		RiakRestResponse response = restClient.getValue(TEST_BUCKET, key);
		assertNotNull(response.getBytes());
		restClient.deleteKey(TEST_BUCKET, key);
		response = restClient.getValue(TEST_BUCKET, key);
		Assert.assertNull(response);
	}

	/*
	 * Map/Reduce Test
	 */
	@Test
	public void mapReduceKeyFilterTest() throws Exception {
		RiakRestResponse response = restClient
				.executeMapReduceJob(new RiakMapReduceJob(TEST_BUCKET,
						RiakMapReduceRestrictions.eq(TEST_KEY))
						.addMap(RiakJavascriptNamedFunction.MAP_VALUES));
		assertNotNull(response);
		ObjectMapper mapper = new ObjectMapper();
		assertEquals(mapper.readValue(response.getBytes(), 0,
				response.getBytes().length, String[].class)[0], TEST_VALUE);

	}
}
