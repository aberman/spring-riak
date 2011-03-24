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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket.RiakBucketProperties;
import org.springframework.data.keyvalue.riak.client.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketListParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakDeleteParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakMapReduceParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakReadParameters;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameters;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Andrew Berman
 * 
 */
public class RiakRestClient implements RiakManager<RiakRestResponse> {

	private Log logger = LogFactory.getLog(getClass());

	private String host;

	private Integer port;

	private boolean useSSL;

	private RestTemplate restTemplate = new RestTemplate();

	public RiakRestClient() {
	}

	public RiakRestClient(String host, Integer port, boolean useSSL) {
		this.host = host;
		this.port = port;
		this.useSSL = useSSL;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	private String buildUrl(List<String> path, RiakParameters queryParams) {
		try {
			return new URI(isUseSSL() ? "https" : "http", null, this.host,
					this.port, "/" + StringUtils.join(path, "/"),
					queryParams != null ? queryParams.toQueryString() : null,
					null).toString();
		} catch (URISyntaxException e) {
			// should never happen
			logger.fatal("Error formatting the URI");
			throw new RiakClientException("Error formatting the URI", e);
		}
	}

	private String getUrl(String basePath, RiakParameters parameters,
			String... pathParams) {
		List<String> params = new ArrayList<String>();
		params.add(basePath);
		params.addAll(Arrays.asList(pathParams));
		return buildUrl(params, parameters);
	}

	private String getRiakUrl(RiakParameters parameters, String... pathParams) {
		return getUrl("riak", parameters, pathParams);
	}

	private String getRiakUrl(String... pathParams) {
		return getRiakUrl(null, pathParams);
	}

	private String getMapReduceUrl(RiakParameters parameters,
			String... pathParams) {
		return getUrl("mapred", parameters, pathParams);
	}

	@Override
	public List<String> listBuckets() throws RiakClientException {
		try {
			Map<?, ?> list = restTemplate.getForObject(
					getRiakUrl(new RiakBucketListParameters()), Map.class);

			List<?> buckets = (List<?>) list.get("buckets");
			List<String> bucketNames = new ArrayList<String>();
			for (Iterator<?> it = buckets.iterator(); it.hasNext();)
				bucketNames.add((String) it.next());

			return bucketNames;
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public RiakBucket getBucketInformation(String bucket,
			RiakBucketReadParameters properties) throws RiakClientException {
		try {
			return restTemplate.getForObject(getRiakUrl(properties, bucket)
					.toString(), RiakBucket.class);
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public void setBucketProperties(String bucket,
			RiakBucketProperties bucketProperties) throws RiakClientException {
		try {
			restTemplate.put(getRiakUrl((RiakParameters) null, bucket),
					bucketProperties);
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public RiakRestResponse getValue(String bucket, String key) {
		return getValue(bucket, key, null);
	}

	@Override
	public RiakRestResponse getValue(String bucket, String key,
			RiakReadParameters properties) throws RiakClientException {
		try {
			return new RiakRestResponse(restTemplate.getForEntity(
					getRiakUrl(properties, bucket, key), byte[].class));
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public void storeKeyValue(String bucket, String key, byte[] value) {
		storeKeyValue(bucket, key, value, null);
	}

	@Override
	public String storeValue(String bucket, byte[] value) {
		return storeValue(bucket, value, null);
	}

	@Override
	public void storeKeyValue(String bucket, String key, byte[] value,
			RiakStoreParameters properties) throws RiakClientException {
		try {
			restTemplate.put(getRiakUrl((RiakParameters) null, bucket, key),
					value);
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public String storeValue(String bucket, byte[] value,
			RiakStoreParameters properties) throws RiakClientException {
		try {
			URI location = restTemplate.postForLocation(getRiakUrl(properties),
					value, String.class, bucket);
			if (location != null) {
				String[] split = StringUtils.split(location.getPath(), "/");
				return split[split.length - 1];
			}

			return null;
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public void deleteKey(String bucket, String key) {
		deleteKey(bucket, key, null);
	}

	@Override
	public void deleteKey(String bucket, String key,
			RiakDeleteParameters properties) throws RiakClientException {
		try {
			restTemplate.delete(getRiakUrl(bucket, key));
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public RiakRestResponse executeMapReduceJob(RiakMapReduceJob job)
			throws RiakClientException {
		return executeMapReduceJob(job, null);
	}

	@Override
	public RiakRestResponse executeMapReduceJob(RiakMapReduceJob job,
			RiakMapReduceParameters parameters) throws RiakClientException {
		return new RiakRestResponse(
				restTemplate.postForEntity(
						getMapReduceUrl(parameters, (String[]) null), job,
						byte[].class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (getHost() == null)
			throw new IllegalArgumentException("Property 'host' is required");

		if (getPort() == null)
			throw new IllegalArgumentException("Property 'port' is required");
	}

}
