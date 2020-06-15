package com.telstra.codechallenge.controller;

import com.telstra.codechallenge.model.RepositoryResponse;
import com.telstra.codechallenge.model.UserResponse;
import com.telstra.codechallenge.service.RepositoryService;
import com.telstra.codechallenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryControllerTest {

    private int port = 8080;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RepositoryService repositoryService;
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private UserService userService;

    @Test
    public void getRepositoryMockTest() {
        final String url = "https://api.github.com/search/repositories?q=created:%3E"+repositoryService.getDate()+"&sort=stars&order=desc&per_page=";
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(manyTimes(), requestTo(url + "2"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body("{" +
                        "\"items\":[" +
                        "{" +
                        "\"name\":\"xgenecloud\"," +
                        "\"url\":\"https://api.github.com/repos/xgenecloud/xgenecloud\"," +
                        "\"watchers_count\":\"835\"," +
                        "\"language\":\"JavaScript\"," +
                        "\"description\":\":fire: :fire: Instantly generate REST & GraphQL APIs on any Database (Supports : MySQL, PostgreSQL, MsSQL, SQLite, MariaDB)\"" +
                        "}," +
                        "{\"name\":\"wwdc\"," +
                        "\"url\":\"https://api.github.com/repos/twostraws/wwdc\"," +
                        "\"watchers_count\":\"294\"," +
                        "\"language\":null," +
                        "\"description\":\"WWDC Community: Learning and sharing together\"" +
                        "}]," +
                        "\"errorMessage\":null}").contentType(MediaType.APPLICATION_JSON));

        RepositoryResponse response = repositoryService.getHottestRepositories(2);

        assertEquals(2, response.getItems().size());
    }

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

    }

    @Test
    void getRepositoryNegative(){
        String url = "http://localhost:" + port + "/repositories/hottest?count=-1";
        ResponseEntity<RepositoryResponse> responseEntity = restTemplate.getForEntity(url, RepositoryResponse.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("Count value is less than 1", responseEntity.getBody().getErrorMessage());

    }

    @Test
    void FallbackGetRepositoryTest() {
        RepositoryResponse response = new RepositoryResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(response, repositoryService.fallBack(10));
    }

    @Test
    void dateTest() {
        LocalDate date = LocalDate.now();
        String str = date.minusDays(7).toString();
        assertEquals(str, repositoryService.getDate());
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