package com.telstra.codechallenge.controller;

import com.telstra.codechallenge.model.RepositoryResponse;
import com.telstra.codechallenge.service.RepositoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {
    private RepositoryService repositoryService;

    public RepositoryController(
            RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // Get Hottest Repositories
    @RequestMapping(path = "/repositories/hottest", method = RequestMethod.GET)
    public RepositoryResponse quotes(@RequestParam(value = "count", defaultValue = "1") int count) {
        if(count<=0){
            RepositoryResponse response = new RepositoryResponse();
            response.setErrorMessage("Count value is less than 1");
            return response;
        }
        return repositoryService.getHottestRepositories(count);
    }
}
