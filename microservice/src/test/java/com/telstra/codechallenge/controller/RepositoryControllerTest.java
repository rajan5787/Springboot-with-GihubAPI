package com.telstra.codechallenge.controller;

import com.telstra.codechallenge.model.RepositoryResponse;
import com.telstra.codechallenge.model.UserResponse;
import com.telstra.codechallenge.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RepositoryService repositoryService;

    @Test
    void getRepositoryDefault() {
        final String url = "http://localhost:" + 8080 + "/repositories/hottest";
        ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity(url, UserResponse.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().getItems().size());
    }

    @Test
    void getRepository() {
        String url = "http://localhost:" + port + "/repositories/hottest?count=2";
        ResponseEntity<RepositoryResponse> responseEntity = restTemplate.getForEntity(url, RepositoryResponse.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(2, responseEntity.getBody().getItems().size());

        url = "http://localhost:" + port + "/repositories/hottest?count=30";
        responseEntity = restTemplate.getForEntity(url, RepositoryResponse.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(30, responseEntity.getBody().getItems().size());

        url = "http://localhost:" + port + "/repositories/hottest?count=-1";
        responseEntity = restTemplate.getForEntity(url, RepositoryResponse.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("Count value is less than 1", responseEntity.getBody().getErrorMessage());
    }

    @Test
    void FallbackGetRepositoryTest() {
        RepositoryResponse response = new RepositoryResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(response, repositoryService.fallBack(10));
    }

    // This is circuiteBreaker scenario based test case.
    //To test this first set HystrixCommand param("execution.isolation.thread.timeoutInMilliseconds") to less than 20ms
    @Test
    void getRepositorysCircuitBreaker() {
        final String url = "http://localhost:" + port + "/repositories/hottest?count=1";
        ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity(url, UserResponse.class);

        UserResponse expected = new UserResponse();
        expected.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(expected, responseEntity.getBody());
    }
}