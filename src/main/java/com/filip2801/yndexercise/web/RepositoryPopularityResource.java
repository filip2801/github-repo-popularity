package com.filip2801.yndexercise.web;

import com.filip2801.yndexercise.domain.RepositoryId;

class RepositoryPopularityResource {

	private final String repositoryFullName;
	private final boolean popular;

	public RepositoryPopularityResource(RepositoryId repositoryId, boolean popular) {
		this.repositoryFullName = repositoryId.getRepositoryFullName();
		this.popular = popular;
	}

	public String getRepositoryFullName() {
		return repositoryFullName;
	}

	public boolean isPopular() {
		return popular;
	}
}
