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
package org.springframework.data.keyvalue.riak.util;

/**
 * @author andrewberman
 * 
 */
public abstract class RiakConstants {
	public static final String NAME = "name";

	public static final String N_VAL = "n_val";

	public static final String ALLOW_MULT = "allow_mult";

	public static final String LAST_WRITE_WINS = "last_write_wins";

	public static final String PRE_COMMIT = "pre_commit";

	public static final String POST_COMMIT = "post_commit";

	public static final String CHASH_KEYFUN = "chash_keyfun";

	public static final String LINKFUN = "linkfun";

	public static final String OLD_VCLOCK = "old_vclock";

	public static final String YOUNG_VCLOCK = "young_vclock";

	public static final String BIG_VCLOCK = "big_vclock";

	public static final String SMALL_VCLOCK = "small_vclock";

	public static final String READ = "r";

	public static final String WRITE = "w";

	public static final String READ_WRITE = "rw";

	public static final String DURABLE_WRITE = "dw";
	
	public static final String VTAG = "vtag";
	
	public static final String RETURNBODY = "returnbody";
	
	public static final String CHUNKED = "chunked";
	
	public static final String KEYS = "keys";
	
	public static final String PROPS = "props";
}
