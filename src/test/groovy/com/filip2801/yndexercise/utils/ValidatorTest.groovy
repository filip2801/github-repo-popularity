package com.filip2801.yndexercise.utils

import spock.lang.Specification
import spock.lang.Unroll

class ValidatorTest extends Specification {

	def "should not throw exception for 'abc'"() {
		given:
		def valueToCheck = "abc"

		when:
		def result = Validator.notBlank(valueToCheck)

		then:
		result == valueToCheck
	}

	@Unroll
	def "should throw exception when value is '#valueToCheck'"() {
		when:
		Validator.notBlank(valueToCheck)

		then:
		thrown(IllegalArgumentException)

		where:
		valueToCheck << [null, "", " ", "\t"]
	}

}
