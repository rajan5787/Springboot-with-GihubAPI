package com.telstra.codechallenge.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.telstra.codechallenge.model.RepositoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class RepositoryService {

    @Value("${github.base.url}")
    private String gitHubBaseUrl;

    private RestTemplate restTemplate;

    public RepositoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000") })
    public RepositoryResponse getHottestRepositories(int count) {

        String postUrl = "/repositories?q=created:>" + getDate() + "&sort=stars&order=desc&per_page="+count;
        RepositoryResponse response = restTemplate.getForObject(gitHubBaseUrl + postUrl, RepositoryResponse.class);

        return response;
    }

    private String getDate(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7);

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return timeStamp;
    }

    @SuppressWarnings("unused")
    public RepositoryResponse  fallBack(int count){
        RepositoryResponse response = new RepositoryResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");
        return response;
    }
}
