package com.filip2801.githubrepopopularity.domain;

import com.filip2801.githubrepopopularity.utils.Validator;

public class RepositoryId {

	private final String owner;
	private final String repositoryName;

	public RepositoryId(String owner, String repositoryName) {
		this.owner = Validator.notBlank(owner);
		this.repositoryName = Validator.notBlank(repositoryName);
	}

	public String getRepositoryFullName() {
		return owner + "/" + repositoryName;
	}

	@Override
	public String toString() {
		return "RepositoryId{" +
			"owner='" + owner + '\'' +
			", repositoryName='" + repositoryName + '\'' +
			'}';
	}
}
