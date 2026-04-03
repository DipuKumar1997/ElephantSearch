package com.io.search_engine.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductPg {
    @Id
    private Long id;
    private String title;
    private String category;
    private String platform;
    private Double rating;
    private Double price;
    private Double actualPrice;
}