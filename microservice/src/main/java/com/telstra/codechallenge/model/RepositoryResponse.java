package com.telstra.codechallenge.model;

import lombok.Data;

import java.util.List;

@Data
public class RepositoryResponse {

    private List<Repository> items;

    private String errorMessage;

    public List<Repository> getItems() {
        return items;
    }

    public void setItems(List<Repository> items) {
        this.items = items;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
