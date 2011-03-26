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

import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter.KeyRetrievalType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author andrewberman
 * 
 */
public class RiakRestClientTests {

	private static RiakManager restClient;

	public static final String TEST_BUCKET = "RiakRestClientTests.bucket4";

	public static String TEST_KEY;

	public static final String TEST_VALUE = "this is a test value";

	@BeforeClass
	public static void setUp() {
		restClient = new RiakRestClient("localhost", 8098, false);
		TEST_KEY = restClient.storeValue(TEST_BUCKET, TEST_VALUE);
	}

	@AfterClass
	public static void tearDown() {
		for (String key : restClient
				.getBucketInformation(
						TEST_BUCKET,
						RiakBucketReadParameter
								.keyRetrievalType(KeyRetrievalType.TRUE),
						RiakBucketReadParameter.showProperties(true)).getKeys())
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
		RiakBucket bucket = restClient
				.getBucketInformation(TEST_BUCKET, RiakBucketReadParameter
						.showProperties(true), RiakBucketReadParameter
						.keyRetrievalType(KeyRetrievalType.TRUE));
		assertNotNull(bucket);
		assertNotNull(bucket.getBucketProperties());
		assertNotNull(bucket.getKeys());
	}

	@Test(groups = { "bucket", "write" })
	public void setBucketInformationTest() {
		restClient.setBucketProperties(TEST_BUCKET,
				RiakBucketProperty.allowMulti(true));
		restClient.setBucketProperties(TEST_BUCKET,
				RiakBucketProperty.allowMulti(false));
		RiakBucket bucket = restClient.getBucketInformation(TEST_BUCKET);
		assertEquals(bucket.getBucketProperties().getAllowMulti(),
				Boolean.FALSE);
	}

	/*
	 * Key Value Tests
	 */
	@Test(groups = { "keyValue", "read" })
	public void getValueTest() {
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET,
				TEST_KEY, String.class);
		assertNotNull(response);
		assertEquals(response.getData(), TEST_VALUE);
	}

	@Test(groups = { "keyValue", "write" })
	public void storeKeyValueTest() {
		restClient.storeKeyValue(TEST_BUCKET, TEST_KEY, "foo test");
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET,
				TEST_KEY, String.class);
		assertNotNull(response);
		assertEquals(response.getData(), "foo test");
	}

	@Test(groups = { "keyValue", "write" })
	public void storeValueTest() {
		String key = restClient.storeValue(TEST_BUCKET, "bar test");
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET, key,
				String.class);
		assertNotNull(response);
		assertEquals(response.getData(), "bar test");
	}

	@Test(groups = { "keyValue", "delete" })
	public void deleteKeyTest() {
		String key = restClient.storeValue(TEST_BUCKET, "foo");
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET, key,
				String.class);
		assertNotNull(response.getData());
		restClient.deleteKey(TEST_BUCKET, key);
		response = restClient.getValue(TEST_BUCKET, key, String.class);
		Assert.assertNull(response);
	}

	/*
	 * Map/Reduce Test
	 */
	public void mapReduceKeyFilterTest() throws Exception {
		// RiakResponse response = restClient
		// .executeMapReduceJob(new RiakMapReduceJob(TEST_BUCKET,
		// RiakMapReduceRestrictions.eq(TEST_KEY)).addMap(
		// RiakJavascriptNamedFunction.MAP_VALUES).addReduce(
		// RiakJavascriptNamedFunction.REDUCE_SORT));
		// assertNotNull(response);
		//
		// restClient.executeMapReduceJob(job, new ResultCallbackHandler<Long>()
		// {
		//
		// @Override
		// public void processResult(RiakResponse<Long> response) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });

	}
}
