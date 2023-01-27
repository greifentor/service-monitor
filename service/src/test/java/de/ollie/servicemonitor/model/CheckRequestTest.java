package de.ollie.servicemonitor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CheckRequestTest {

	private static final String HTTP = CheckRequest.HTTP_PROTOCOL;
	private static final String HTTPS = CheckRequest.HTTPS_PROTOCOL;

	private static final String HOST = "host";
	private static final String PATH = "path";
	private static final Integer PORT = 1701;

	@Nested
	class TestsOfMethod_getUrl {

		@Test
		void checkRequestWithAHostNameOnly_returnsTheHostNameWithHttpProtocolPrefix() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST);
			// Run & Check
			assertEquals(HTTP + HOST, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNameAndAPort_returnsTheHostNameWithHttpProtocolPrefixAndPortSuffix() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setPort(PORT);
			// Run & Check
			assertEquals(HTTP + HOST + ":" + PORT, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNameAndAPathWhichDoesNotStartWithSlash_returnsTheHostNameWithHttpProtocolPrefixASlashAndThePath() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setPath(PATH);
			// Run & Check
			assertEquals(HTTP + HOST + "/" + PATH, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNameAndAPathWhichStartsWithSlash_returnsTheHostNameWithHttpProtocolPrefixASlashAndThePath() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setPath("/" + PATH);
			// Run & Check
			assertEquals(HTTP + HOST + "/" + PATH, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNamePortAndAPathWhichDoesNotStartWithSlash_returnsACorrectUrl() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setPath(PATH).setPort(PORT);
			// Run & Check
			assertEquals(HTTP + HOST + ":" + PORT + "/" + PATH, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNamePortAndAPathWhichStartsWithSlash_returnsACorrectUrl() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setPath("/" + PATH).setPort(PORT);
			// Run & Check
			assertEquals(HTTP + HOST + ":" + PORT + "/" + PATH, checkRequest.getUrl());
		}

		@Test
		void checkRequestWithAHostNamePortPathAndTheHttpsFlagSet_returnsACorrectHttpsUrl() {
			// Prepare
			CheckRequest checkRequest = new CheckRequest().setHost(HOST).setHttps(true).setPath(PATH).setPort(PORT);
			// Run & Check
			assertEquals(HTTPS + HOST + ":" + PORT + "/" + PATH, checkRequest.getUrl());
		}

	}

}
