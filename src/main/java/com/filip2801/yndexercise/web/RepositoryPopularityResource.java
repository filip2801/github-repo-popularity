package com.filip2801.yndexercise.web;

class RepositoryPopularityResource {

	private final String repositoryFullName;
	private final boolean popular;

	public RepositoryPopularityResource(String repositoryFullName, boolean popular) {
		this.repositoryFullName = repositoryFullName;
		this.popular = popular;
	}

	public String getRepositoryFullName() {
		return repositoryFullName;
	}

	public boolean isPopular() {
		return popular;
	}
}
