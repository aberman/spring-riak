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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.keyvalue.riak.client.data.ResultCallbackHandler;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperties;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakLink;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.client.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.mapreduce.RiakLinkPhase;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakDeleteParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakMapReduceParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakPutParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakParameter.Type;
import org.springframework.data.keyvalue.riak.parameter.RiakReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameter;
import org.springframework.data.keyvalue.riak.util.RiakConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Andrew Berman
 * 
 */
public class RiakRestClient implements RiakManager {

	private Log logger = LogFactory.getLog(getClass());

	private String host;

	private Integer port = 8098;

	private boolean useSSL = false;

	private RestTemplate restTemplate = new RestTemplate();

	public RiakRestClient() {
	}

	public RiakRestClient(String host, Integer port, boolean useSSL) {
		this(host);
		this.port = port;
		this.useSSL = useSSL;
	}

	public RiakRestClient(String host) {
		this.host = host;
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

	private String buildUrl(List<String> path,
			Collection<RiakParameter> queryParams) {
		try {
			return new URI(isUseSSL() ? "https" : "http", null, this.host,
					this.port, "/" + StringUtils.join(path, "/"),
					queryParams != null && !queryParams.isEmpty() ? StringUtils
							.join(queryParams, "&") : null, null).toString();
		} catch (URISyntaxException e) {
			// should never happen
			logger.fatal("Error formatting the URI");
			throw new RiakClientException("Error formatting the URI", e);
		}
	}

	private String getUrl(String basePath, RiakParameter[] parameters,
			String[] pathParams) {
		List<String> params = new ArrayList<String>();
		params.add(basePath);

		if (pathParams != null && pathParams.length > 0)
			params.addAll(Arrays.asList(pathParams));

		List<RiakParameter> queryParams = null;

		if (parameters != null && parameters.length > 0) {
			queryParams = new ArrayList<RiakParameter>();
			for (RiakParameter parameter : parameters) {
				if (parameter.getType().equals(Type.QUERY))
					queryParams.add(parameter);
			}
		}

		return buildUrl(params, queryParams);
	}

	private String getRiakUrl(RiakParameter[] parameters, String... pathParams) {
		return getUrl("riak", parameters, pathParams);
	}

	private String getMapReduceUrl(RiakParameter[] params, String... pathParams) {
		return getUrl("mapred", params, pathParams);
	}

	@Override
	public List<String> listBuckets() throws RiakClientException {
		try {
			Map<?, ?> map = restTemplate.getForObject(
					getRiakUrl(null).concat("?buckets=true"), Map.class);

			if (map != null && !map.isEmpty()) {
				List<?> buckets = (List<?>) map.get("buckets");
				List<String> bucketNames = new ArrayList<String>();
				for (Iterator<?> it = buckets.iterator(); it.hasNext();)
					bucketNames.add((String) it.next());

				return bucketNames;
			}

			return null;
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public RiakBucket getBucketInformation(String bucket,
			RiakBucketReadParameter... params) throws RiakClientException {
		try {
			return restTemplate.getForObject(getRiakUrl(params, bucket)
					.toString(), RiakBucket.class);
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	private MultiValueMap<String, String> getHeadersMap(
			RiakParameter... parameters) {
		MultiValueMap<String, String> headerMap = null;
		if (parameters != null && parameters.length > 0) {
			headerMap = new LinkedMultiValueMap<String, String>();

			for (RiakParameter param : parameters) {
				if (param.getType().equals(Type.HEADER))
					headerMap.add(param.getKey(), param.getValue());
			}
		}

		return headerMap;
	}

	@Override
	public void setBucketProperties(String bucket,
			RiakBucketProperty<?>... bucketProperties)
			throws RiakClientException {
		Assert.notNull(bucket, "The bucket cannot be null");
		Assert.notNull(bucketProperties,
				"At least one bucket property must be set");

		try {
			restTemplate.put(getRiakUrl((RiakParameter[]) null, bucket),
					Collections.singletonMap(RiakConstants.PROPS,
							new RiakBucketProperties(bucketProperties)));
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public <T> RiakResponse<T> getValue(String bucket, String key,
			Class<T> clazz, RiakReadParameter... params)
			throws RiakClientException {
		try {
			return new RiakRestResponse<T>(restTemplate.exchange(
					getRiakUrl(params, bucket, key), HttpMethod.GET,
					new HttpEntity<T>(getHeadersMap(params)), clazz));
		} catch (RestClientException e) {
			HttpClientErrorException ex = (HttpClientErrorException) e;
			if (!ex.getStatusCode().equals(HttpStatus.NOT_FOUND))
				throw new RiakClientException(e.getMessage(), e);

			return null;
		}
	}

	private void storeKeyValueCommon(String bucket, String key, Object value,
			RiakParameter... params) throws RiakClientException {
		try {
			restTemplate.exchange(getRiakUrl(params, bucket, key),
					HttpMethod.PUT, new HttpEntity<Object>(value,
							getHeadersMap(params)), value.getClass());
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}
	}

	@Override
	public void storeKeyValue(String bucket, String key, Object value,
			RiakPutParameter... params) throws RiakClientException {
		this.storeKeyValueCommon(bucket, key, value, params);
	}

	@Override
	public String storeValue(String bucket, Object value,
			RiakStoreParameter... params) throws RiakClientException {
		try {
			URI location = restTemplate
					.exchange(
							getRiakUrl(params, bucket),
							HttpMethod.POST,
							new HttpEntity<Object>(value, getHeadersMap(params)),
							value.getClass()).getHeaders().getLocation();
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
	public void storeKeyValue(String bucket, String key, Object value,
			List<RiakLink> links, RiakStoreParameter... params)
			throws RiakClientException {
		this.storeKeyValueCommon(bucket, key, value, params);
	}

	@Override
	public String storeValue(String bucket, Object value, List<RiakLink> links,
			RiakStoreParameter... params) throws RiakClientException {
		return this.storeValue(bucket, value, params);
	}

	@Override
	public void deleteKey(String bucket, String key,
			RiakDeleteParameter... params) throws RiakClientException {
		try {
			restTemplate.exchange(getRiakUrl(params, bucket, key),
					HttpMethod.DELETE, new HttpEntity<Object>(
							getHeadersMap(params)), null);
		} catch (RestClientException e) {
			throw new RiakClientException(e.getMessage(), e);
		}

	}

	@SuppressWarnings("unchecked")
	private class RiakResponseCallbackHandler<T> implements
			ResultCallbackHandler {

		private RiakResponse<List<T>> returnResponse;

		@Override
		public void processResult(RiakResponse<Object[]> response) {
			if (response.getData().length > 0) {
				List<T> tList = new ArrayList<T>();
				for (Object o : response.getData())
					tList.add((T) o);

				this.returnResponse = new RiakRestResponse<List<T>>(tList,
						((RiakRestResponse<Object[]>) response).getExtraInfo(),
						response.getResponseStatus());
			}
		}

		public RiakResponse<List<T>> getResponse() {
			return this.returnResponse;
		}
	}

	@Override
	public <T> RiakResponse<List<T>> executeMapReduceJob(RiakMapReduceJob job,
			Class<T> clazz, RiakMapReduceParameter... params)
			throws RiakClientException {
		RiakResponseCallbackHandler<T> callback = new RiakResponseCallbackHandler<T>();
		executeMapReduceJob(job, callback, params);
		return callback.getResponse();
	}

	@Override
	public void executeMapReduceJob(RiakMapReduceJob job,
			ResultCallbackHandler callback, RiakMapReduceParameter... params)
			throws RiakClientException {
		callback.processResult(new RiakRestResponse<Object[]>(restTemplate
				.exchange(getMapReduceUrl(params), HttpMethod.POST,
						new HttpEntity<RiakMapReduceJob>(job,
								getHeadersMap(params)), Object[].class)));
	}

	@Override
	public <T> RiakResponse<T> walkLinks(String bucket, String key,
			Class<T> clazz, RiakLinkPhase... phases) throws RiakClientException {
		List<String> pathParams = new ArrayList<String>(Arrays.asList(bucket,
				key));

		for (RiakLinkPhase phase : phases)
			pathParams.add(phase.toUrlFormat());

		return new RiakRestResponse<T>(restTemplate.getForEntity(
				getRiakUrl(null, (String[]) pathParams
						.toArray(new String[pathParams.size()])), clazz));
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
