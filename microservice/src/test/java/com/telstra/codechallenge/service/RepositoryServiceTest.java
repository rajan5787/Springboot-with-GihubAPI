package com.telstra.codechallenge.service;

import com.telstra.codechallenge.model.RepositoryResponse;
import com.telstra.codechallenge.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    void FallbackGetRepositoryTest() {
        RepositoryResponse response = new RepositoryResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(response, repositoryService.fallBack(10));
    }
}