package com.filip2801.githubrepopopularity.domain;

import com.filip2801.githubrepopopularity.integration.GithubRepositoryResource;

class PopularityCalculator {

	private static final int MIN_SCORE = 500;

	boolean isPopular(GithubRepositoryResource githubRepository) {
		return (githubRepository.getNumberOfStars() + githubRepository.getNumberOfForks() * 2) >= MIN_SCORE;
	}

}
