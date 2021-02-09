package com.filip2801.githubrepopopularity.web

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

import com.filip2801.githubrepopopularity.IntegrationTestSpecification
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
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
		restClient.setContentType(APPLICATION_JSON_VALUE)
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

		wireMockRule.stubFor(get(urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(aResponse()
												 .withStatus(200)
												 .withHeader("Content-Type", APPLICATION_JSON_VALUE)
												 .withBody("""{
													"stargazers_count": 100,
													"forks_count": 300
													}""")))


		when:
		def response = callEndpoint(repositoryOwner, repositoryName)

		then:
		response.status == HttpStatus.OK.value()
		response.data.repositoryFullName == "${repositoryOwner}/${repositoryName}"
		response.data.popular == true
	}

	def "should respond with 404 when repository doesn't exist"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = "some-not-existing-repo"

		wireMockRule.stubFor(get(urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(aResponse().withStatus(404)))

		when:
		def response = callEndpoint(repositoryOwner, repositoryName)

		then:
		response.status == HttpStatus.NOT_FOUND.value()
	}

	def "should respond with 502 when github api responds with error"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = UUID.randomUUID().toString()

		wireMockRule.stubFor(get(urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(aResponse()
												 .withStatus(500)))

		when:
		def response = callEndpoint(repositoryOwner, repositoryName)

		then:
		response.status == HttpStatus.BAD_GATEWAY.value()
	}

	def "should respond with 502 when github api responds too slow"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = UUID.randomUUID().toString()

		wireMockRule.stubFor(get(urlEqualTo("/repos/${repositoryOwner}/${repositoryName}"))
								 .willReturn(aResponse()
												 .withStatus(200)
												 .withFixedDelay(20)
												 .withHeader("Content-Type", APPLICATION_JSON_VALUE)
												 .withBody("""{
													"stargazers_count": 100,
													"forks_count": 300
													}""")))

		when:
		def response = callEndpoint(repositoryOwner, repositoryName)

		then:
		response.status == HttpStatus.BAD_GATEWAY.value()
	}

	private HttpResponseDecorator callEndpoint(repositoryOwner, repositoryName) {
		return restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator
	}

}
