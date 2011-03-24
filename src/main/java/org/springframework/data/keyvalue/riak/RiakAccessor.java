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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.keyvalue.riak.client.RiakManager;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.util.RiakExtraInfo;

/**
 * Base class for {@link RiakTemplate} and other Riak accessing gateway helpers,
 * defining common properties such as the {@link RiakManager} to operate on.
 * 
 * <p>
 * Not intended to be used directly. See {@link RiakTemplate}.
 * 
 * @author Andrew Berman
 * @since 1.0
 * @see RiakTemplate
 */
public abstract class RiakAccessor implements InitializingBean {

	private RiakManager<? extends RiakResponse<? extends RiakExtraInfo>> riakManager;

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * @return the riakManager
	 */
	public RiakManager<? extends RiakResponse<? extends RiakExtraInfo>> getRiakManager() {
		return riakManager;
	}

	/**
	 * @param riakManager
	 *            the riakManager to set
	 */
	public void setRiakManager(
			RiakManager<? extends RiakResponse<? extends RiakExtraInfo>> riakManager) {
		this.riakManager = riakManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		if (getRiakManager() == null)
			throw new IllegalArgumentException(
					"Property 'riakManager' is required");
	}
}
