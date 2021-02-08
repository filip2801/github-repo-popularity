package com.filip2801.githubrepopopularity.integration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubRepositoryResource {

	private final long numberOfStars;
	private final long numberOfForks;

	@JsonCreator
	public GithubRepositoryResource(
		@JsonProperty("stargazers_count") long numberOfStars,
		@JsonProperty("forks_count") long numberOfForks) {

		this.numberOfStars = numberOfStars;
		this.numberOfForks = numberOfForks;
	}

	public long getNumberOfStars() {
		return numberOfStars;
	}

	public long getNumberOfForks() {
		return numberOfForks;
	}

	@Override
	public String toString() {
		return "GithubRepositoryResource{" +
			", numberOfStars='" + numberOfStars + '\'' +
			", numberOfForks='" + numberOfForks + '\'' +
			'}';
	}
}
