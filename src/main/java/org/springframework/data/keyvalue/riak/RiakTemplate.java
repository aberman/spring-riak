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
import org.springframework.dao.DataAccessException;
import org.springframework.data.keyvalue.riak.client.RiakClientException;
import org.springframework.data.keyvalue.riak.client.RiakManager;
import org.springframework.data.keyvalue.riak.client.RiakRestClient;
import org.springframework.data.keyvalue.riak.data.RiakBucket;
import org.springframework.data.keyvalue.riak.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters.KeyRetrievalType;
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
	 * @see org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public <T> T find(String key, Class<T> entityClass)
			throws DataAccessException {
		return find(entityClass.getName(), key, entityClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T find(String bucket, String key, Class<T> entityClass)
			throws DataAccessException {
		RiakRestResponse response;
		try {
			response = (RiakRestResponse) getRiakManager()
					.getValue(bucket, key);
			byte[] bytes = response.getBytes();
			return mapper.readValue(bytes, 0, bytes.length, entityClass);
		} catch (RiakClientException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(
					"There was a problem deserializing the data", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.keyvalue.riak.RiakOperations#find(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object find(String bucket, String key) throws DataAccessException {
		return find(bucket, key, Object.class);
	}

	@Override
	public <T> List<T> find(Collection<String> keys, Class<T> entityClass)
			throws DataAccessException {
		return find(entityClass.getName(), keys, entityClass);
	}

	@Override
	public <T> List<T> find(String bucket, Collection<String> keys,
			Class<T> entityClass) throws DataAccessException {
		List<T> list = new ArrayList<T>();

		for (String key : keys)
			list.add(find(bucket, key, entityClass));

		return list;
	}

	@Override
	public List<Object> find(String bucket, Collection<String> keys)
			throws DataAccessException {
		return find(bucket, keys, Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang.Class)
	 */
	@Override
	public <T> List<T> loadAll(Class<T> entityClass) throws DataAccessException {
		return loadAll(entityClass.getName(), entityClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public <T> List<T> loadAll(String bucket, Class<T> entityClass)
			throws DataAccessException {
		RiakBucket rBucket = getRiakManager().getBucketInformation(
				bucket,
				new RiakBucketReadParameters().setKeyRetrievalType(
						KeyRetrievalType.FALSE).setShowProperties(false));

		List<T> list = new ArrayList<T>();
		for (String key : rBucket.getKeys())
			list.add(find(key, entityClass));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#loadAll(java.lang.String)
	 */
	@Override
	public List<Object> loadAll(String bucket) throws DataAccessException {
		return loadAll(bucket, Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang.Object)
	 */
	@Override
	public String persist(Object entity) throws DataAccessException {
		return persist(entity.getClass().getName(),
				mapper.convertValue(entity, byte[].class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang.String,
	 * byte[])
	 */
	@Override
	public String persist(String bucket, byte[] value)
			throws DataAccessException {
		return getRiakManager().storeValue(bucket, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public void persist(Object entity, String key) throws DataAccessException {
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
	 * org.springframework.data.keyvalue.riak.RiakOperations#persist(java.lang.String,
	 * java.lang.String, byte[])
	 */
	@Override
	public void persist(final String bucket, final String key,
			final byte[] value) throws DataAccessException {
		getRiakManager().storeKeyValue(bucket, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#remove(java.lang.Object)
	 */
	@Override
	public void remove(Class<?> entityClass, String key)
			throws DataAccessException {
		remove(entityClass.getName(), key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#remove(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void remove(String bucket, String key) throws DataAccessException {
		getRiakManager().deleteKey(bucket, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#removeAll(java.util.Collection
	 * )
	 */
	@Override
	public void removeAll(Class<?> entityClass, Collection<String> keys)
			throws DataAccessException {
		for (String key : keys)
			remove(entityClass, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#findByProperty(java.lang
	 * .Class, java.lang.String)
	 */
	@Override
	public <T> T findByProperty(Class<T> entityClass, String property)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#executeMapReduceJobSingleResult
	 * (org.springframework.data.keyvalue.riak.RiakMapReduceJob, java.lang.Class)
	 */
	@Override
	public <T> T executeMapReduceJobSingleResult(RiakMapReduceJob job,
			Class<T> entityClass) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#executeMapReduceJob(org.
	 * springframework.data.riak.RiakMapReduceJob, java.lang.Class)
	 */
	@Override
	public <T> List<T> executeMapReduceJob(RiakMapReduceJob job,
			Class<T> entityClass) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.keyvalue.riak.RiakOperations#execute(org.springframework
	 * .data.riak.RiakCallback)
	 */
	@Override
	public <T> T execute(RiakCallback<T> action) throws DataAccessException {
		Assert.notNull(action, "Callback object must not be null");

		RiakRestClient rm = (RiakRestClient) getRiakManager();

		if (rm == null) {
			logger.debug("Creating new RiakManager for execute");
			// create new riakManager
		}

		try {
			return action.doInRiak(rm);
		} catch (RuntimeException ex) {
			throw new RiakClientException(ex.getMessage(), ex);
		}

	}

}
