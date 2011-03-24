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

import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;

/**
 * @author Andrew Berman
 * 
 */
public interface RiakOperations {
	<T> T find(String key, Class<T> entityClass) throws RiakDataException;

	<T> T find(String bucket, String key, Class<T> entityClass)
			throws RiakDataException;

	Object find(String bucket, String key) throws RiakDataException;

	<T> List<T> find(Collection<String> keys, Class<T> entityClass)
			throws RiakDataException;

	<T> List<T> find(String bucket, Collection<String> keys, Class<T> entityClass)
			throws RiakDataException;

	List<Object> find(String bucket, Collection<String> keys)
			throws RiakDataException;

	<T> List<T> loadAll(Class<T> entityClass) throws RiakDataException;

	<T> List<T> loadAll(String bucket, Class<T> entityClass)
			throws RiakDataException;

	List<Object> loadAll(String bucket) throws RiakDataException;

	String persist(Object entity) throws RiakDataException;

	String persist(String bucket, byte[] value) throws RiakDataException;

	void persist(Object entity, String key) throws RiakDataException;

	void persist(String bucket, String key, byte[] value)
			throws RiakDataException;

	void remove(Class<?> entityClass, String key) throws RiakDataException;

	void remove(String bucket, String key) throws RiakDataException;

	void removeAll(Class<?> entityClass, Collection<String> keys)
			throws RiakDataException;

	<T> T findByProperty(Class<T> entityClass, String property)
			throws RiakDataException;

	<T> T executeMapReduceJobSingleResult(RiakMapReduceJob job,
			Class<T> entityClass) throws RiakDataException;

	<T> List<T> executeMapReduceJob(RiakMapReduceJob job, Class<T> entityClass)
			throws RiakDataException;

	<T> T execute(RiakCallback<T> action) throws RiakDataException;

	// Add walking for links
}
