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

import java.util.List;

import org.springframework.data.keyvalue.riak.RiakLink;
import org.springframework.data.keyvalue.riak.RiakTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Andrew Berman
 * 
 */
public class RiakRestTemplateTests {
	private static RiakTemplate template;

	public static final String TEST_BUCKET = "RiakRestClientTests.bucket4";

	public static String TEST_KEY;

	public static final String TEST_VALUE = "this is a test value";

	public static final boolean ENABLED = true;

	public static class TestObject2 {
		private String id;
		
		public TestObject2(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}
	}

	public static class TestObject {
		private String stringProp;

		private Integer intProp;

		private Boolean boolProp;

		@RiakLink(property = "id", value = "test2")
		private TestObject2 test2 = new TestObject2("testingid");

		public TestObject2 getTest2() {
			return test2;
		}

		public void setTest2(TestObject2 test2) {
			this.test2 = test2;
		}

		public String getStringProp() {
			return stringProp;
		}

		public void setStringProp(String stringProp) {
			this.stringProp = stringProp;
		}

		public Integer getIntProp() {
			return intProp;
		}

		public void setIntProp(Integer intProp) {
			this.intProp = intProp;
		}

		public Boolean getBoolProp() {
			return boolProp;
		}

		public void setBoolProp(Boolean boolProp) {
			this.boolProp = boolProp;
		}

	}

	@BeforeClass
	public static void setUp() {
		template = new RiakTemplate(
				new RiakRestClient("localhost", 8098, false));
	}

	@AfterClass
	public static void tearDown() {
		template.removeAllKeys(TestObject.class);
	}

	@Test
	public void findByPropertyTest() {
		String str = "Hello World";
		TestObject obj = new TestObject();
		obj.setBoolProp(Boolean.TRUE);
		obj.setIntProp(1);
		obj.setStringProp(str);

		TestObject obj2 = new TestObject();
		obj2.setBoolProp(Boolean.FALSE);
		obj2.setIntProp(2);
		obj2.setStringProp(str);
		template.persist(obj);
		template.persist(obj2);

		// Find by bool
		List<TestObject> list = template.findByProperty(TestObject.class,
				"boolProp", true);
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 1);
		Assert.assertEquals(list.get(0).getBoolProp().booleanValue(), true);

		// Find by int
		list = template.findByProperty(TestObject.class, "intProp", 1);
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 1);
		Assert.assertEquals(list.get(0).getIntProp().intValue(), 1);

		// Find by string
		list = template.findByProperty(TestObject.class, "stringProp", str);
		Assert.assertNotNull(list);
		Assert.assertEquals(list.size(), 2);

		for (TestObject to : list) {
			Assert.assertEquals(to.getStringProp(), str);
		}
	}

}
