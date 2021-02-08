package com.filip2801.githubrepopopularity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext

class GithubRepoPopularityAppIT extends IntegrationTestSpecification {

	@Autowired
	WebApplicationContext context

	def "should start application"() {
		expect: 'application started'
		context != null
	}

}
