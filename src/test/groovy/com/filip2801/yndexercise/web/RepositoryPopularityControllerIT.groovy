package com.filip2801.yndexercise.web

import com.filip2801.yndexercise.IntegrationTestSpecification
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class RepositoryPopularityControllerIT extends IntegrationTestSpecification {

	@LocalServerPort
	int port
	@Value('${server.servlet.context-path:/}')
	String contextPath

	RESTClient restClient = new RESTClient()

	def setup() {
		restClient.setUri("http://localhost:${port}${contextPath}/")
		restClient.setContentType(MediaType.APPLICATION_JSON_VALUE)
	}

	def "should get repository popularity"() {
		given:
		def repositoryOwner = "some-owner"
		def repositoryName = "some-repo-name"

		when:
		def response = restClient.get(path: "/repositories/${repositoryOwner}/${repositoryName}/popularity") as HttpResponseDecorator

		then:
		response.status == HttpStatus.OK.value()
		response.data.repositoryFullName == "${repositoryOwner}/${repositoryName}"
		response.data.popular == true
	}

}
