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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.data.keyvalue.riak.client.data.ResultCallbackHandler;
import org.springframework.data.keyvalue.riak.client.data.RiakBucket;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperties;
import org.springframework.data.keyvalue.riak.client.data.RiakBucketProperty;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.client.data.RiakRestResponse;
import org.springframework.data.keyvalue.riak.client.http.HttpHeaders;
import org.springframework.data.keyvalue.riak.client.http.MultipartMixedHttpMessageConverter;
import org.springframework.data.keyvalue.riak.mapreduce.RiakLinkPhase;
import org.springframework.data.keyvalue.riak.mapreduce.RiakMapReduceJob;
import org.springframework.data.keyvalue.riak.parameter.RiakBucketReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakDeleteParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakParameter.Type;
import org.springframework.data.keyvalue.riak.parameter.RiakPutParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakReadParameter;
import org.springframework.data.keyvalue.riak.parameter.RiakStoreParameter;
import org.springframework.data.keyvalue.riak.util.RiakConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

	private String clientId = RandomStringUtils.randomAlphanumeric(10);

	private RestTemplate restTemplate = new RestTemplate();

	private ObjectMapper mapper = new ObjectMapper();

	private static final String RIAK_PATH = "riak";

	private static final String RIAK_MAP_RED_PATH = "mapred";

	private static final String RIAK_PING = "ping";

	private static final String RIAK_STATS = "stats";

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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	private MultiValueMap<String, String> getHeadersMap(
			RiakParameter... parameters) {
		MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		headerMap.add(HttpHeaders.X_RIAK_CLIENT_ID, getClientId());
		if (parameters != null && parameters.length > 0) {
			for (RiakParameter param : parameters) {
				if (param.getType().equals(Type.HEADER))
					headerMap.add(param.getKey(), param.getValue());
			}
		}

		return headerMap;
	}

	private <T> ResponseEntity<T> execute(String path, HttpMethod method,
			Object value, RiakParameter[] parameters, Class<T> clazz,
			String... pathParams) throws RiakException {
		try {
			if (value == null)
				return restTemplate.exchange(
						getUrl(path, parameters, pathParams), method,
						new HttpEntity<Object>(getHeadersMap(parameters)),
						clazz);
			else
				return restTemplate
						.exchange(getUrl(path, parameters, pathParams), method,
								new HttpEntity<Object>(value,
										getHeadersMap(parameters)), clazz);
		} catch (RestClientException e) {
			if (e instanceof HttpClientErrorException) {
				// 400 error
				HttpClientErrorException ex = (HttpClientErrorException) e;
				if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND))
					throw new RiakObjectNotFoundException(
							HttpStatus.NOT_FOUND.toString());

				throw new RiakUncategorizedClientErrorException(
						ex.getStatusText());
			} else if (e instanceof HttpServerErrorException) {
				// 500 error
				HttpServerErrorException ex = (HttpServerErrorException) e;
				throw new RiakServerErrorException(ex.getStatusText());
			}

			throw new RiakUncategorizedException(
					"Uncategorized exception thrown", e);
		}
	}

	private <T> ResponseEntity<T> executeRiak(HttpMethod method, Object value,
			RiakParameter[] parameters, Class<T> clazz, String... pathParams)
			throws RiakException {
		return execute(RIAK_PATH, method, value, parameters, clazz, pathParams);
	}

	private <T> ResponseEntity<T> executeMapReduce(HttpMethod method,
			Object value, RiakParameter[] parameters, Class<T> clazz,
			String... pathParams) throws RiakException {
		return execute(RIAK_MAP_RED_PATH, method, value, parameters, clazz,
				pathParams);
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

		try {
			return new URI(isUseSSL() ? "https" : "http", null, this.host,
					this.port, "/" + StringUtils.join(params, "/"),
					queryParams != null && !queryParams.isEmpty() ? StringUtils
							.join(queryParams, "&") : null, null).toString();
		} catch (URISyntaxException e) {
			// should never happen
			logger.fatal("Error formatting the URI");
			throw new RiakUncategorizedException("Error formatting the URI", e);
		}
	}

	@Override
	public boolean ping() throws RiakException {
		ResponseEntity<String> re = execute(RIAK_PING, HttpMethod.GET, null,
				null, String.class);
		return re.getStatusCode().equals(HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> stats() throws RiakException {
		return execute(RIAK_STATS, HttpMethod.GET, null, null, Map.class)
				.getBody();
	}

	@Override
	public List<String> listBuckets() throws RiakException {
		Map<?, ?> map = executeRiak(
				HttpMethod.GET,
				null,
				new RiakParameter[] { new RiakParameter("buckets", "true",
						Type.QUERY) }, Map.class).getBody();

		if (map != null && !map.isEmpty()) {
			List<?> buckets = (List<?>) map.get("buckets");
			List<String> bucketNames = new ArrayList<String>();
			for (Iterator<?> it = buckets.iterator(); it.hasNext();)
				bucketNames.add((String) it.next());

			return bucketNames;
		}

		return null;
	}

	@Override
	public RiakBucket getBucketInformation(String bucket,
			RiakBucketReadParameter... params) throws RiakException {
		return executeRiak(HttpMethod.GET, null, params, RiakBucket.class,
				bucket).getBody();
	}

	@Override
	public void setBucketProperties(String bucket,
			RiakBucketProperty<?>... bucketProperties) throws RiakException {
		Assert.notNull(bucket, "The bucket cannot be null");
		Assert.notNull(bucketProperties,
				"At least one bucket property must be set");
		executeRiak(HttpMethod.PUT,
				Collections.singletonMap(RiakConstants.PROPS,
						new RiakBucketProperties(bucketProperties)), null,
				byte[].class, bucket);

	}

	@Override
	public <T> RiakResponse<T> getValue(String bucket, String key,
			Class<T> clazz, RiakReadParameter... params) throws RiakException {
		return new RiakRestResponse<T>(executeRiak(HttpMethod.GET, null,
				params, clazz, bucket, key), key);
	}

	@Override
	public void storeKeyValue(String bucket, String key, Object value,
			RiakPutParameter... params) throws RiakException {
		executeRiak(HttpMethod.PUT, value, params, byte[].class, bucket, key);
	}

	@Override
	public String storeValue(String bucket, Object value,
			RiakStoreParameter... params) throws RiakException {
		return new RiakRestResponse<byte[]>(executeRiak(HttpMethod.POST, value,
				params, byte[].class, bucket)).getKey();
	}

	@Override
	public void deleteKey(String bucket, String key,
			RiakDeleteParameter... params) throws RiakException {
		executeRiak(HttpMethod.DELETE, null, params, byte[].class, bucket, key);
	}

	@SuppressWarnings("unchecked")
	private class RiakResponseCallbackHandler<T> implements
			ResultCallbackHandler {

		private RiakResponse<List<T>> returnResponse;

		private Class<T> clazz;

		public RiakResponseCallbackHandler(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public void processResult(RiakResponse<Object[]> response) {
			this.returnResponse = new RiakRestResponse<List<T>>(
					(List<T>) mapper.convertValue(response.getBody(),
							TypeFactory.collectionType(List.class, this.clazz)),
					(HttpHeaders) response.getExtraInfo(), response
							.getResponseStatus());

		}

		public RiakResponse<List<T>> getResponse() {
			return this.returnResponse;
		}
	}

	@Override
	public <T> RiakResponse<List<T>> executeMapReduceJob(RiakMapReduceJob job,
			Class<T> clazz) throws RiakException {
		RiakResponseCallbackHandler<T> callback = new RiakResponseCallbackHandler<T>(
				clazz);
		executeMapReduceJob(job, callback);
		return callback.getResponse();
	}

	@Override
	public void executeMapReduceJob(RiakMapReduceJob job,
			ResultCallbackHandler callback) throws RiakException {
		callback.processResult(new RiakRestResponse<Object[]>(executeMapReduce(
				HttpMethod.POST, job, null, Object[].class)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<RiakResponse<T>> walkLinks(String bucket, String key,
			Class<T> clazz, RiakLinkPhase... phases) throws RiakException {
		List<String> pathParams = new ArrayList<String>(Arrays.asList(bucket,
				key));

		for (RiakLinkPhase phase : phases)
			pathParams.add(phase.toUrlFormat());
		RestTemplate privTemp = new RestTemplate();
		privTemp.getMessageConverters().add(
				new MultipartMixedHttpMessageConverter<T>(clazz));
		return (List<RiakResponse<T>>) privTemp.getForObject(
				getUrl("riak", null, (String[]) pathParams
						.toArray(new String[pathParams.size()])), List.class);
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
