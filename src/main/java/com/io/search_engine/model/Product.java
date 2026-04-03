package com.io.search_engine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private String id;
    private String title;
    private String description;
    private Double price;

    // Scoring fields
    private Integer positiveSignals = 0;
    private Integer negativeSignals = 0;
}