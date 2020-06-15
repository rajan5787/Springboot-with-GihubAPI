package com.telstra.codechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Repository {

    private String name;
    private String url;
    private String watchers_count;
    private String language;
    private String description;
}
