package com.filip2801.yndexercise.integration;

import com.filip2801.yndexercise.domain.RepositoryId;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubRepositoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GithubRepositoryService.class);
	private static final String ACCEPT_HEADER_NAME = "Accept";
	private static final String ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json";

	private final RestTemplate restTemplate;
	private final String githubApiBaseUrl;

	public GithubRepositoryService(
		RestTemplate restTemplate,
		@Value("${githubApi.baseUrl}") String githubApiBaseUrl) {

		this.restTemplate = restTemplate;
		this.githubApiBaseUrl = Objects.requireNonNull(githubApiBaseUrl);
	}

	public GithubRepositoryResource getRepository(RepositoryId repositoryId) {
		LOGGER.info("Get details of {} repository", repositoryId.getRepositoryFullName());

		ResponseEntity<GithubRepositoryResource> result = restTemplate.exchange(
			getUrl(repositoryId),
			HttpMethod.GET,
			entityWithHeaders(),
			GithubRepositoryResource.class);

		LOGGER.info("Repository details {}", result.getBody());
		return result.getBody();
	}

	private String getUrl(RepositoryId repositoryId) {
		return githubApiBaseUrl + "/repos/" + repositoryId.getRepositoryFullName();
	}

	private HttpEntity<String> entityWithHeaders() {
		var headers = new HttpHeaders();
		headers.add(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);

		return new HttpEntity<>(headers);
	}

}
