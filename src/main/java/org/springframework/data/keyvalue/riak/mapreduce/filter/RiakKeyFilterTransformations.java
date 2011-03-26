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
package org.springframework.data.keyvalue.riak.mapreduce.filter;

import org.springframework.data.keyvalue.riak.mapreduce.filter.transform.RiakSimpleTransformer;
import org.springframework.data.keyvalue.riak.mapreduce.filter.transform.RiakTokenizeTransformer;

/**
 * @author Andrew Berman
 * 
 */
public abstract class RiakKeyFilterTransformations {
	public static RiakSimpleTransformer intToString() {
		return new RiakSimpleTransformer("int_to_string");
	}

	public static RiakSimpleTransformer stringToInt() {
		return new RiakSimpleTransformer("string_to_int");
	}

	public static RiakSimpleTransformer floatToString() {
		return new RiakSimpleTransformer("float_to_string");
	}

	public static RiakSimpleTransformer stringToFloat() {
		return new RiakSimpleTransformer("string_to_float");
	}

	public static RiakSimpleTransformer toUpper() {
		return new RiakSimpleTransformer("to_upper");
	}

	public static RiakSimpleTransformer toLower() {
		return new RiakSimpleTransformer("to_lower");
	}

	public static RiakTokenizeTransformer tokenize(String token, int index) {
		return new RiakTokenizeTransformer(token, index);
	}

	public static RiakSimpleTransformer urlDecode() {
		return new RiakSimpleTransformer("urldecode");
	}
}
