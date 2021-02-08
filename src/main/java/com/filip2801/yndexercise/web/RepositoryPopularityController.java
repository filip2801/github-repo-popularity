package com.filip2801.yndexercise.web;

import com.filip2801.yndexercise.domain.RepositoryId;
import com.filip2801.yndexercise.domain.RepositoryPopularityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryPopularityController {

	private final RepositoryPopularityService repositoryPopularityService;

	public RepositoryPopularityController(RepositoryPopularityService repositoryPopularityService) {
		this.repositoryPopularityService = repositoryPopularityService;
	}

	@GetMapping("/repositories/{owner}/{repositoryName}/popularity")
	public RepositoryPopularityResource getRepositoryPopularity(@PathVariable String owner, @PathVariable String repositoryName) {

		RepositoryId repositoryId = new RepositoryId(owner, repositoryName);
		boolean popular = repositoryPopularityService.isPopular(repositoryId);

		return new RepositoryPopularityResource(repositoryId, popular);
	}

}
