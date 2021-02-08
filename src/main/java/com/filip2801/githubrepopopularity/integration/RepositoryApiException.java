package com.filip2801.githubrepopopularity.integration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class RepositoryApiException extends RuntimeException {

}
