package com.telstra.codechallenge.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.telstra.codechallenge.model.RepositoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

@Service
public class RepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryService.class);

    @Value("${github.base.url}")
    private String gitHubBaseUrl;

    private RestTemplate restTemplate;

    public RepositoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000") })
    public RepositoryResponse getHottestRepositories(int count) {
        logger.info("Inside getHottestRepositories method, count value : "+count);
        String postUrl = "/repositories?q=created:>" + getDate() + "&sort=stars&order=desc&per_page="+count;
        RepositoryResponse response = restTemplate.getForObject(gitHubBaseUrl+postUrl, RepositoryResponse.class);
        logger.info("Inside getHottestRepositories method, response : " + response.toString());

        return response;
    }

    public  String getDate(){
        LocalDate date = LocalDate.now();
        date = date.minusDays(7);
        logger.info("Inside getDate method :"+ date.toString());
        return date.toString();
    }

    @SuppressWarnings("unused")
    public RepositoryResponse  fallBack(int count){
        RepositoryResponse response = new RepositoryResponse();
        response.setErrorMessage("SERVER IS DOWN. Please try after sometime");
        logger.error("Server is down");
        return response;
    }
}
