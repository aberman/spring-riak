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
package org.springframework.data.keyvalue.riak.mapreduce;


/**
 * @author Andrew Berman
 * 
 */
public class RiakErlangFunction extends RiakMapReduceFunction {

	private String module;

	private String function;

	private RiakErlangFunction(String module, String function) {
		super(Language.ERLANG);
		this.module = module;
		this.function = function;
	}

	public String getModule() {
		return module;
	}
	
	public String getFunction() {
		return function;
	}

	public static final RiakErlangFunction function(String module,
			String function) {
		return new RiakErlangFunction(module, function);
	}
}
