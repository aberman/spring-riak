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

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;

/**
 * @author Andrew Berman
 * 
 */
public interface RiakOperations {
	<T> T find(String key, Class<T> entityClass) throws DataAccessException;

	<T> T find(String bucket, String key, Class<T> entityClass)
			throws DataAccessException;

	Object find(String bucket, String key) throws DataAccessException;

	<T> List<T> find(Collection<String> keys, Class<T> entityClass)
			throws DataAccessException;

	<T> List<T> find(String bucket, Collection<String> keys, Class<T> entityClass)
			throws DataAccessException;

	List<Object> find(String bucket, Collection<String> keys)
			throws DataAccessException;

	<T> List<T> loadAll(Class<T> entityClass) throws DataAccessException;

	<T> List<T> loadAll(String bucket, Class<T> entityClass)
			throws DataAccessException;

	List<Object> loadAll(String bucket) throws DataAccessException;

	String persist(Object entity) throws DataAccessException;

	String persist(String bucket, byte[] value) throws DataAccessException;

	void persist(Object entity, String key) throws DataAccessException;

	void persist(String bucket, String key, byte[] value)
			throws DataAccessException;

	void remove(Class<?> entityClass, String key) throws DataAccessException;

	void remove(String bucket, String key) throws DataAccessException;

	void removeAll(Class<?> entityClass, Collection<String> keys)
			throws DataAccessException;

	<T> T findByProperty(Class<T> entityClass, String property)
			throws DataAccessException;

	<T> T executeMapReduceJobSingleResult(RiakMapReduceJob job,
			Class<T> entityClass) throws DataAccessException;

	<T> List<T> executeMapReduceJob(RiakMapReduceJob job, Class<T> entityClass)
			throws DataAccessException;

	<T> T execute(RiakCallback<T> action) throws DataAccessException;

	// Add walking for links
}
