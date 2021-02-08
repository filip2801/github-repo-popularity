package com.filip2801.githubrepopopularity.domain

import com.filip2801.githubrepopopularity.integration.GithubRepositoryResource
import spock.lang.Specification
import spock.lang.Unroll

class PopularityCalculatorTest extends Specification {

	def calculator = new PopularityCalculator()

	@Unroll
	def "should be popular for #stars stars and #forks forks"() {
		given:
		def githubRepo = new GithubRepositoryResource(stars, forks)

		expect:
		calculator.isPopular(githubRepo)

		where:
		stars | forks
		500   | 0
		501   | 0
		0     | 250
		0     | 251
		2     | 249
		4     | 248
	}

	@Unroll
	def "should not be popular for #stars stars and #forks forks"() {
		given:
		def githubRepo = new GithubRepositoryResource(stars, forks)

		expect:
		!calculator.isPopular(githubRepo)

		where:
		stars | forks
		0     | 0
		499   | 0
		1     | 249
		249   | 1
	}

}
