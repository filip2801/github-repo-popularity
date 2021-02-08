package com.filip2801.yndexercise.web

import com.filip2801.yndexercise.IntegrationTestSpecification
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class RepositoryPopularityControllerIT extends IntegrationTestSpecification {

	private final static int PORT_NUMBER = 18080

	@LocalServerPort
	int port
	@Value('${server.servlet.context-path:/}')
	String contextPath

	RESTClient restClient = new RESTClient()

	@Rule
	WireMockRule wireMockRule = new WireMockRule(PORT_NUMBER)

	def setup() {
		restClient.setUri("http://localhost:${port}${contextPath}/")
		restClient.setContentType(MediaType.APPLICATION_JSON_VALUE)
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

}
