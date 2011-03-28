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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.keyvalue.riak.client.data.ResultCallbackHandler;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakLinkPhase;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakDeleteParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakPutParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameter;

/**
 * @author Andrew Berman
 * 
 */
public interface RiakManager extends InitializingBean {
	/*
	 * Server operations
	 */
	boolean ping() throws RiakException;

	Map<String, Object> stats() throws RiakException;

	/*
	 * Bucket operations
	 */
	List<String> listBuckets() throws RiakException;

	RiakBucket getBucketInformation(String bucket,
			RiakBucketReadParameter... params) throws RiakException;

	void setBucketProperties(String bucket, RiakBucketProperty<?>... property)
			throws RiakException;

	/*
	 * Key/Value operations
	 */
	<T> RiakResponse<T> getValue(String bucket, String key, Class<T> clazz,
			RiakReadParameter... params) throws RiakException;

	void storeKeyValue(String bucket, String key, Object value,
			RiakPutParameter... params) throws RiakException;

	String storeValue(String bucket, Object value, RiakStoreParameter... params)
			throws RiakException;

	void deleteKey(String bucket, String key, RiakDeleteParameter... params)
			throws RiakException;

	/*
	 * Map/Reduce operations
	 */
	<T> RiakResponse<List<T>> executeMapReduceJob(RiakMapReduceJob job,
			Class<T> clazz) throws RiakException;

	void executeMapReduceJob(RiakMapReduceJob job,
			ResultCallbackHandler callback) throws RiakException;

	/*
	 * Link operations
	 */
	<T> List<RiakResponse<T>> walkLinks(String bucket, String key,
			Class<T> clazz, RiakLinkPhase... phases) throws RiakException;
}
