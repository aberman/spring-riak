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
package org.springframework.data.keyvalue.riak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.keyvalue.riak.client.RiakException;
import org.springframework.data.keyvalue.riak.client.RiakManager;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter.KeyRetrievalType;
import org.springframework.util.Assert;

/**
 * @author Andrew Berman
 * 
 */
public class RiakTemplate extends RiakAccessor implements RiakOperations {

	private ObjectMapper mapper;

	public RiakTemplate() {

	}

	public RiakTemplate(RiakManager riakManager) {
		setRiakManager(riakManager);
		mapper = new ObjectMapper();
		afterPropertiesSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.
	 * Class, java.lang.String)
	 */
	@Override
	public <T> T find(String key, Class<T> entityClass)
			throws RiakDataException {
		return find(entityClass.getName(), key, entityClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.
	 * String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T find(String bucket, String key, Class<T> entityClass)
			throws RiakDataException {
		try {
			RiakResponse<T> response = getRiakManager().getValue(bucket, key,
					entityClass);
			return response.getBody();
		} catch (RiakException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(
					"There was a problem deserializing the data", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public Object find(String bucket, String key) throws RiakDataException {
		return find(bucket, key, Object.class);
	}

	@Override
	public <T> List<T> find(Collection<String> keys, Class<T> entityClass)
			throws RiakDataException {
		return find(entityClass.getName(), keys, entityClass);
	}

	@Override
	public <T> List<T> find(String bucket, Collection<String> keys,
			Class<T> entityClass) throws RiakDataException {
		List<T> list = new ArrayList<T>();

		for (String key : keys)
			list.add(find(bucket, key, entityClass));

		return list;
	}

	@Override
	public List<Object> find(String bucket, Collection<String> keys)
			throws RiakDataException {
		return find(bucket, keys, Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang
	 * .Class)
	 */
	@Override
	public <T> List<T> loadAll(Class<T> entityClass) throws RiakDataException {
		return loadAll(entityClass.getName(), entityClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang
	 * .String, java.lang.Class)
	 */
	@Override
	public <T> List<T> loadAll(String bucket, Class<T> entityClass)
			throws RiakDataException {
		RiakBucket rBucket = getRiakManager().getBucketInformation(
				bucket,
				RiakBucketReadParameter
						.keyRetrievalType(KeyRetrievalType.FALSE),
				RiakBucketReadParameter.showProperties(false));

		List<T> list = new ArrayList<T>();
		for (String key : rBucket.getKeys())
			list.add(find(key, entityClass));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang
	 * .String)
	 */
	@Override
	public List<Object> loadAll(String bucket) throws RiakDataException {
		return loadAll(bucket, Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang
	 * .Object)
	 */
	@Override
	public String persist(Object entity) throws RiakDataException {
		return persist(entity.getClass().getName(),
				mapper.convertValue(entity, byte[].class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang
	 * .String, byte[])
	 */
	@Override
	public String persist(String bucket, byte[] value) throws RiakDataException {
		return getRiakManager().storeValue(bucket, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang
	 * .Object, java.lang.String)
	 */
	@Override
	public void persist(Object entity, String key) throws RiakDataException {
		try {
			persist(entity.getClass().getName(), key,
					mapper.writeValueAsBytes(entity));
		} catch (Exception e) {
			logger.error("Error serializing object");
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang
	 * .String, java.lang.String, byte[])
	 */
	@Override
	public void persist(final String bucket, final String key,
			final byte[] value) throws RiakDataException {
		getRiakManager().storeKeyValue(bucket, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#remove(java.lang
	 * .Object)
	 */
	@Override
	public void remove(Class<?> entityClass, String key)
			throws RiakDataException {
		remove(entityClass.getName(), key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#remove(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void remove(String bucket, String key) throws RiakDataException {
		getRiakManager().deleteKey(bucket, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#removeAll(java.
	 * util.Collection )
	 */
	@Override
	public void removeAll(Class<?> entityClass, Collection<String> keys)
			throws RiakDataException {
		for (String key : keys)
			remove(entityClass, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#findByProperty(
	 * java.lang .Class, java.lang.String)
	 */
	@Override
	public <T> T findByProperty(Class<T> entityClass, String property)
			throws RiakDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.keyvalue.riak.RiakOperations#
	 * executeMapReduceJobSingleResult
	 * (org.springframework.data.keyvalue.riak.RiakMapReduceJob,
	 * java.lang.Class)
	 */
	@Override
	public <T> T executeMapReduceJobSingleResult(RiakMapReduceJob job,
			Class<T> entityClass) throws RiakDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#executeMapReduceJob
	 * (org. springframework.data.riak.RiakMapReduceJob, java.lang.Class)
	 */
	@Override
	public <T> List<T> executeMapReduceJob(RiakMapReduceJob job,
			Class<T> entityClass) throws RiakDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.keyvalue.riak.RiakOperations#execute(org.
	 * springframework .data.riak.RiakCallback)
	 */
	@Override
	public <T> T execute(RiakCallback<T> action) throws RiakDataException {
		Assert.notNull(action, "Callback object must not be null");

		RiakManager rm = getRiakManager();

		if (rm == null) {
			throw new RiakDataException("The RiakManager cannot be null");
		}

		try {
			return action.doInRiak(rm);
		} catch (RiakException ex) {
			throw new RiakDataException(ex.getMessage(), ex);
		}

	}

}
