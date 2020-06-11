package com.telstra.codechallenge.controller;


import com.telstra.codechallenge.model.UserResponse;
import com.telstra.codechallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/users/zeroFollowers", method = RequestMethod.GET)
    public UserResponse getUserWithZeroFollowers(@RequestParam(value = "count", defaultValue = "1") int count) {
        if(count<=0){
            UserResponse response = new UserResponse();
            response.setErrorMessage("Count value is less than 1");
            return response;
        }
        return userService.getUserWithZeroFollowers(count);
    }
}
