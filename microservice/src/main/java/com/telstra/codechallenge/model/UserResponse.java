package com.telstra.codechallenge.model;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private List<User> items;
    private String errorMessage;

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
