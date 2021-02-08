package com.filip2801.githubrepopopularity.integration;

import com.filip2801.githubrepopopularity.domain.RepositoryId;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

	@Retry(name = "githubApi", fallbackMethod = "fallback")
	public GithubRepositoryResource getRepository(RepositoryId repositoryId) {
		LOGGER.info("Get details of {} repository", repositoryId.getRepositoryFullName());

		return restTemplate.exchange(
			getUrl(repositoryId),
			HttpMethod.GET,
			entityWithHeaders(),
			GithubRepositoryResource.class).getBody();
	}

	private String getUrl(RepositoryId repositoryId) {
		return githubApiBaseUrl + "/repos/" + repositoryId.getRepositoryFullName();
	}

	private HttpEntity<String> entityWithHeaders() {
		var headers = new HttpHeaders();
		headers.add(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE);

		return new HttpEntity<>(headers);
	}

	private GithubRepositoryResource fallback(RepositoryId repositoryId, HttpClientErrorException.NotFound exception) {
		LOGGER.debug("Repository {} does not exist", repositoryId.getRepositoryFullName(), exception);
		throw new RepositoryNotFound();
	}

	private GithubRepositoryResource fallback(RepositoryId repositoryId, Exception exception) {
		LOGGER.info("Cannot fetch repository {}", repositoryId.getRepositoryFullName(), exception);
		throw new RepositoryApiException();
	}

}
