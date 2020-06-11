package com.telstra.codechallenge.service;

import com.telstra.codechallenge.model.UserResponse;
import com.telstra.codechallenge.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void FallbackGetUsersTest() {
        UserResponse response = new UserResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");

        assertEquals(response, userService.fallBack(10));
    }
}