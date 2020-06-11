package com.telstra.codechallenge.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.telstra.codechallenge.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${github.base.url}")
    private String gitHubBaseUrl;
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000") })
    public UserResponse getUserWithZeroFollowers(int count) {

        String post_url = "/users?q=followers:0&sort=joined&order=asc&per_page="+count;
        UserResponse userResponse = restTemplate.getForObject(gitHubBaseUrl + post_url, UserResponse.class);

        return userResponse;
    }

    @SuppressWarnings("unused")
    public UserResponse fallBack(int count){

        UserResponse response = new UserResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");
        return response;
    }
}
