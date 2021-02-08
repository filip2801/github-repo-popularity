package com.filip2801.githubrepopopularity.web

import com.filip2801.githubrepopopularity.IntegrationTestSpecification
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Shared

class RepositoryPopularityControllerIT extends IntegrationTestSpecification {

	private final static int PORT_NUMBER = 18080

	@LocalServerPort
	int port
	@Value('${server.servlet.context-path:/}')
	String contextPath

	@Shared
	RESTClient restClient = new RESTClient()

	@Rule
	WireMockRule wireMockRule = new WireMockRule(PORT_NUMBER)

	def setup() {
		restClient.setUri("http://localhost:${port}${contextPath}/")
		restClient.setContentType(MediaType.APPLICATION_JSON_VALUE)
		wireMockRule.resetAll()
	}

	def setupSpec() {
		// change default rest client behaviour to not fail when response status is 4xx or 5xx
		restClient.handler.failure = { resp, data ->
			resp.setData(data)
			return resp
		}
	}

	def "should get repository popularity"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = "some-repo-name"

		wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(WireMock.aResponse()
												 .withStatus(200)
												 .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
												 .withBody("""{
													"stargazers_count": 100,
													"forks_count": 300
													}""")))


		when:
		def response = restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator

		then:
		response.status == HttpStatus.OK.value()
		response.data.repositoryFullName == "${repositoryOwner}/${repositoryName}"
		response.data.popular == true
	}

	def "should respond with 404 when repository doesn't exist"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = "some-not-existing-repo"

		wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(WireMock.aResponse()
												 .withStatus(404)))

		when:
		def response = restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator

		then:
		response.status == HttpStatus.NOT_FOUND.value()
	}

	def "should respond with 502 when github api responds with error"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = UUID.randomUUID().toString()

		wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(WireMock.aResponse()
												 .withStatus(500)))

		when:
		def response = restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator

		then:
		response.status == HttpStatus.BAD_GATEWAY.value()
	}

	def "should respond with 502 when github api responds too slow"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = UUID.randomUUID().toString()

		wireMockRule.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(WireMock.aResponse()
												 .withStatus(200)
												 .withFixedDelay(20)
												 .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
												 .withBody("""{
													"stargazers_count": 100,
													"forks_count": 300
													}""")))

		when:
		def response = restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator

		then:
		response.status == HttpStatus.BAD_GATEWAY.value()
	}

}
