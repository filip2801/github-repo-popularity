# Popular GitHub Repositories

Application made as a job interview task. See [exercise requirements](./exercise-requirements.md). 

Allows to check if provided github repository is popular. Info about repository is fetched from GitHub's official [REST API](https://docs.github.com/en/rest).

## Tech stack
Spring boot application written in Java.

Technologies
* jdk11
* gradle 6.7.1
* spring-boot 2.4.2
* resilience4j - for retries
* springdoc-openapi-ui - api docs
* groovy & spock - for testing
* wiremock - for stubbing github api in integration test

## Running unit and integration tests
Requirements: JDK 11

Run `./gradlew clean check`

## Building docker image
Requirements: docker

Run `docker build -t github-repo-popularity .`

## Running app
Requirements: docker and built docker image

Run `docker run -d -p 8080:8080 github-repo-popularity`

## Endpoints
### Github repository popularity
To check repository popularity replace placeholders and run
```
curl -X GET "http://localhost:8080/repositories/{owner}/{repositoryName}/popularity"
```
example:
```
curl -X GET "http://localhost:8080/repositories/spring-projects/spring-boot/popularity"
```

### Health
http://localhost:8080/actuator/health

## Swagger
Go to http://localhost:8080/swagger-ui.html

## What can be added
* CI
* metrics (jvm, resilience4j, number of errors)
* CD
