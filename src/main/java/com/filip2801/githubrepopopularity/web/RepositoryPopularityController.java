package com.filip2801.githubrepopopularity.web;

import com.filip2801.githubrepopopularity.domain.RepositoryId;
import com.filip2801.githubrepopopularity.domain.RepositoryPopularityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryPopularityController {

	private final RepositoryPopularityService repositoryPopularityService;

	public RepositoryPopularityController(RepositoryPopularityService repositoryPopularityService) {
		this.repositoryPopularityService = repositoryPopularityService;
	}

	@Operation(summary = "Check if github repository is popular")
	@ApiResponses({
		@ApiResponse(responseCode = "404", description = "Repository does not exist", content = @Content),
		@ApiResponse(responseCode = "502", description = "Github api does not respond", content = @Content)
	})
	@GetMapping("/repositories/{owner}/{repositoryName}/popularity")
	public RepositoryPopularityResource getRepositoryPopularity(@PathVariable String owner, @PathVariable String repositoryName) {

		RepositoryId repositoryId = new RepositoryId(owner, repositoryName);
		boolean popular = repositoryPopularityService.isPopular(repositoryId);

		return new RepositoryPopularityResource(repositoryId, popular);
	}

}
