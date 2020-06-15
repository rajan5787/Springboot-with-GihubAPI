package com.telstra.codechallenge.controller;

import com.telstra.codechallenge.model.UserResponse;
import com.telstra.codechallenge.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private int port = 8080;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;
    private MockRestServiceServer mockRestServiceServer;

    @Test
    public void getUsersMockTest() {
        final String url = "https://api.github.com/search/users?q=followers:0&sort=joined&order=asc&per_page=";
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(manyTimes(), requestTo(url + "5"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).body("{\n" +
                        "    \"items\": [\n" +
                        "        {\n" +
                        "            \"id\": 44,\n" +
                        "            \"login\": \"errfree\",\n" +
                        "            \"html_url\": \"https://github.com/errfree\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 81,\n" +
                        "            \"login\": \"engineyard\",\n" +
                        "            \"html_url\": \"https://github.com/engineyard\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 119,\n" +
                        "            \"login\": \"ministrycentered\",\n" +
                        "            \"html_url\": \"https://github.com/ministrycentered\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 128,\n" +
                        "            \"login\": \"collectiveidea\",\n" +
                        "            \"html_url\": \"https://github.com/collectiveidea\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"id\": 144,\n" +
                        "            \"login\": \"ogc\",\n" +
                        "            \"html_url\": \"https://github.com/ogc\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}").contentType(MediaType.APPLICATION_JSON));

        UserResponse response = userService.getUserWithZeroFollowers(5);

        assertEquals(5, response.getItems().size());
    }

    @Test
    void getUsersDefault() {
        final String url = "http://localhost:" + 8080 + "/users/zeroFollowers";
        ResponseEntity<UserResponse> responseEntity= restTemplate.getForEntity(url, UserResponse.class);

        assertEquals(200,responseEntity.getStatusCode().value());
        assertEquals(1,responseEntity.getBody().getItems().size());
    }

    @Test
    void getUsers() {
        String url = "http://localhost:" + port + "/users/zeroFollowers?count=2";
        ResponseEntity<UserResponse> responseEntity= restTemplate.getForEntity(url, UserResponse.class);

        assertEquals(200,responseEntity.getStatusCode().value());
        assertEquals(2,responseEntity.getBody().getItems().size());

        url = "http://localhost:" + port + "/users/zeroFollowers?count=-1";
        responseEntity= restTemplate.getForEntity(url, UserResponse.class);

        assertEquals(200,responseEntity.getStatusCode().value());
        assertEquals("Count value is less than 1",responseEntity.getBody().getErrorMessage());
    }

    @Test
    void FallbackGetUsersTest() {
        UserResponse response = new UserResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(response, userService.fallBack(10));
    }

    // This is circuiteBreaker scenario based test case.
    //To test this first set HystrixCommand param("execution.isolation.thread.timeoutInMilliseconds") to less than 20ms
    @Test
    void getUsersCircuitBreaker() {
        final String url = "http://localhost:" + port + "/users/zeroFollowers?count=1";
        ResponseEntity<UserResponse> responseEntity= restTemplate.getForEntity(url, UserResponse.class);

        UserResponse expected = new UserResponse();
        expected.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(expected, responseEntity.getBody());
    }
}