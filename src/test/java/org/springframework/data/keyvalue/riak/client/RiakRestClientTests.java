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

import java.util.List;
import java.util.Map;

import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakLink;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakJavascriptFunction;
import org.springframework.data.keyvalue.riak.mapreduce.RiakLinkPhase;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.mapreduce.filter.RiakKeyFilterRestrictions;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter.KeyRetrievalType;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameter;
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

	public static final boolean ENABLED = true;

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
	@Test(groups = { "bucket", "read" }, enabled = ENABLED)
	public void listBucketsTest() {
		assertNotNull(restClient.listBuckets());
	}

	@Test(groups = { "bucket", "read" }, enabled = ENABLED)
	public void getBucketInformationTest() {
		RiakBucket bucket = restClient
				.getBucketInformation(TEST_BUCKET, RiakBucketReadParameter
						.showProperties(true), RiakBucketReadParameter
						.keyRetrievalType(KeyRetrievalType.TRUE));
		assertNotNull(bucket);
		assertNotNull(bucket.getBucketProperties());
		assertNotNull(bucket.getKeys());
	}

	@Test(groups = { "bucket", "write" }, enabled = ENABLED)
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
	@Test(groups = { "keyValue", "read" }, enabled = ENABLED)
	public void getValueTest() {
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET,
				TEST_KEY, String.class);
		assertNotNull(response);
		assertEquals(response.getBody(), TEST_VALUE);
	}

	@Test(groups = { "keyValue", "write" }, enabled = ENABLED)
	public void storeKeyValueTest() {
		restClient.storeKeyValue(TEST_BUCKET, TEST_KEY, "foo test");
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET,
				TEST_KEY, String.class);
		assertNotNull(response);
		assertEquals(response.getBody(), "foo test");
	}

	@Test(groups = { "keyValue", "write" }, enabled = ENABLED)
	public void storeValueTest() {
		String key = restClient.storeValue(TEST_BUCKET, "bar test",
				RiakStoreParameter.metaDataHeader("Some meta data"));
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET, key,
				String.class);
		assertNotNull(response);
		assertEquals(response.getBody(), "bar test");
	}

	@Test(groups = { "keyValue", "delete" }, enabled = ENABLED)
	public void deleteKeyTest() {
		String key = restClient.storeValue(TEST_BUCKET, "foo");
		RiakResponse<String> response = restClient.getValue(TEST_BUCKET, key,
				String.class);
		assertNotNull(response.getBody());
		restClient.deleteKey(TEST_BUCKET, key);
		try {
			response = restClient.getValue(TEST_BUCKET, key, String.class);
			Assert.assertNull(response);
		} catch (RiakObjectNotFoundException e) {
			//Should happen
		}
	}

	/*
	 * Map/Reduce Test
	 */
	@Test(enabled = ENABLED)
	public void mapReduceKeyFilterTest() throws Exception {
		RiakResponse<List<String>> response = restClient.executeMapReduceJob(
				new RiakMapReduceJob(TEST_BUCKET, RiakKeyFilterRestrictions
						.eq(TEST_KEY))
						.addMap(RiakJavascriptFunction.MAP_VALUES).addReduce(
								RiakJavascriptFunction.REDUCE_SORT),
				String.class);
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().get(0), TEST_VALUE);
	}

	/*
	 * Link test
	 */
	@Test
	public void linkTest() {
		String id = restClient.storeValue(TEST_BUCKET, "Value 1");
		String id2 = restClient.storeValue(TEST_BUCKET, "Value 2");
		String id3 = restClient
				.storeValue(TEST_BUCKET, "Value 3", RiakStoreParameter
						.link(new RiakLink(TEST_BUCKET, id, "val1")),
						RiakStoreParameter.link(new RiakLink(TEST_BUCKET, id2,
								"val2")));
		List<RiakResponse<String>> responses = restClient.walkLinks(
				TEST_BUCKET, id3, String.class, new RiakLinkPhase(TEST_BUCKET,
						null, true));
		assertNotNull(responses);

		for (RiakResponse<String> response : responses) {
			if (response.getKey().equals(id))
				assertEquals(response.getBody(), "Value 1");
			else
				assertEquals(response.getBody(), "Value 2");
		}
	}

	@Test
	public void pingTest() {
		assertEquals(restClient.ping(), true);
	}

	@Test
	public void statsTest() {
		Map<String, Object> map = restClient.stats();
		assertNotNull(map);
		Assert.assertTrue(map.size() > 1);
	}
}
