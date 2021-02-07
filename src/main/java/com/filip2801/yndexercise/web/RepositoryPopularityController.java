package com.filip2801.yndexercise.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryPopularityController {

	@GetMapping("/repositories/{owner}/{repositoryName}/popularity")
	public RepositoryPopularityResource getRepositoryPopularity(@PathVariable String owner, @PathVariable String repositoryName) {

		return new RepositoryPopularityResource(owner + "/" + repositoryName, true);
	}

}
