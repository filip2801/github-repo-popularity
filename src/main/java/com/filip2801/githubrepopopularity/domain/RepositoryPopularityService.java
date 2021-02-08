package com.filip2801.githubrepopopularity.domain;

import com.filip2801.githubrepopopularity.integration.GithubRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class RepositoryPopularityService {

	private final GithubRepositoryService githubRepositoryService;
	private final PopularityCalculator popularityCalculator;

	public RepositoryPopularityService(GithubRepositoryService githubRepositoryService) {
		this.githubRepositoryService = githubRepositoryService;
		this.popularityCalculator = new PopularityCalculator();
	}

	public boolean isPopular(RepositoryId repositoryId) {
		var githubRepository = githubRepositoryService.getRepository(repositoryId);
		return popularityCalculator.isPopular(githubRepository);
	}

}
