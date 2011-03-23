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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.keyvalue.riak.data.RiakBucket;
import org.springframework.data.keyvalue.riak.data.RiakResponse;
import org.springframework.data.keyvalue.riak.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.data.RiakBucket.RiakBucketProperties;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceFunction;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakDeleteParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakMapReduceParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameters;

/**
 * @author Andrew Berman
 * 
 */
public interface RiakManager extends InitializingBean {
	/*
	 * Bucket operations
	 */
	List<String> listBuckets() throws RiakClientException;

	RiakBucket getBucketInformation(String bucket,
			RiakBucketReadParameters properties) throws RiakClientException;

	void setBucketProperties(String bucket,
			RiakBucketProperties bucketProperties) throws RiakClientException;

	/*
	 * Key/Value operations
	 */
	RiakResponse<?> getValue(String bucket, String key)
			throws RiakClientException;

	RiakResponse<?> getValue(String bucket, String key,
			RiakReadParameters properties) throws RiakClientException;

	void storeKeyValue(String bucket, String key, byte[] value)
			throws RiakClientException;

	String storeValue(String bucket, byte[] value) throws RiakClientException;

	void storeKeyValue(String bucket, String key, byte[] value,
			RiakStoreParameters properties) throws RiakClientException;

	String storeValue(String bucket, byte[] value,
			RiakStoreParameters properties) throws RiakClientException;

	void deleteKey(String bucket, String key) throws RiakClientException;

	void deleteKey(String bucket, String key, RiakDeleteParameters properties)
			throws RiakClientException;

	/*
	 * Map/Reduce operations
	 */
	RiakRestResponse executeMapReduceFunction(RiakMapReduceFunction function)
			throws RiakClientException;

	RiakRestResponse executeMapReduceFunction(RiakMapReduceFunction function,
			RiakMapReduceParameters parameters) throws RiakClientException;
}
