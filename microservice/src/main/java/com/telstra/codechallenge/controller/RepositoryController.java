package com.telstra.codechallenge.controller;

import com.telstra.codechallenge.model.RepositoryResponse;
import com.telstra.codechallenge.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;


@RestController
public class RepositoryController {
    private RepositoryService repositoryService;
    private static final Logger logger = LoggerFactory.getLogger(RepositoryService.class);

    public RepositoryController(
            RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // Get Hottest Repositories
    @RequestMapping(path = "/repositories/hottest", method = RequestMethod.GET)
    public RepositoryResponse getHottestRepository(@RequestParam(value = "count", defaultValue = "1") int count) {

        logger.info("Inside getHottestRepository method count value : "+count);
        if(count<=0){
            RepositoryResponse response = new RepositoryResponse();
            response.setErrorMessage("Count value is less than 1");
            logger.info("Count value is less than 1");

            return response;
        }
        return repositoryService.getHottestRepositories(count);
    }
}
