package com.filip2801.githubrepopopularity.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
		return new RestTemplate(clientHttpRequestFactory);
	}

	@Bean
	ClientHttpRequestFactory httpRequestFactory(
		@Value("${timeout.connection:1000}") int connectionTimeout,
		@Value("${timeout.read:1000}") int readTimeout) {

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
		clientHttpRequestFactory.setReadTimeout(readTimeout);
		return clientHttpRequestFactory;
	}
}
