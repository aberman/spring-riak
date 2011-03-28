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
package org.springframework.data.keyvalue.riak.client.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.keyvalue.riak.client.data.RiakResponse;
import org.springframework.data.keyvalue.riak.client.data.RiakRestResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 * @author andrewberman
 * 
 */
public class MultipartMixedHttpMessageConverter<T> extends
		AbstractHttpMessageConverter<List<RiakResponse<T>>> {

	private Class<T> clazz;

	private static ObjectMapper mapper = new ObjectMapper();

	public MultipartMixedHttpMessageConverter(Class<T> clazz) {
		super(MIMEType.MULTIPART_MIXED);
		this.clazz = clazz;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return List.class.equals(clazz);
	}

	@Override
	protected List<RiakResponse<T>> readInternal(
			Class<? extends List<RiakResponse<T>>> clazz,
			final HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		try {
			List<RiakResponse<T>> responses = new ArrayList<RiakResponse<T>>();
			processMultipart(
					new MimeMultipart(new ByteArrayDataSource(
							inputMessage.getBody(),
							MIMEType.MULTIPART_MIXED.toString())), responses);
			return responses;
		} catch (MessagingException e) {
			throw new HttpMessageNotReadableException(
					"Soemthing wrong with the MIME content", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void processMultipart(Multipart mpart, List<RiakResponse<T>> list)
			throws MessagingException, IOException {
		for (int index = 0; index < mpart.getCount(); index++) {
			BodyPart part = mpart.getBodyPart(index);
			if (part.getContent() instanceof Multipart) {
				processMultipart((Multipart) part.getContent(), list);
			} else {

				HttpHeaders headers = new HttpHeaders();
				for (Enumeration<?> e = part.getAllHeaders(); e
						.hasMoreElements();) {
					Header header = (Header) e.nextElement();
					headers.add(header.getName(), header.getValue());
				}

				list.add(new RiakRestResponse<T>(part.getContent().getClass()
						.equals(this.clazz) ? (T) part.getContent() : mapper
						.convertValue(part.getContent(), this.clazz), headers,
						HttpStatus.OK.toString()));
			}
		}
	}

	@Override
	protected void writeInternal(List<RiakResponse<T>> t,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		throw new UnsupportedOperationException("This is currently unsupported");

	}
}
