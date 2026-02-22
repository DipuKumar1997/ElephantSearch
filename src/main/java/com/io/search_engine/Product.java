package com.io.search_engine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "products") // This maps to your OpenSearch index
public class Product {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;
    @Field(type = FieldType.Double)
    private Double price;
    // SCORING FIELDS: Updated by your feedback service
    @Field(type = FieldType.Integer)
    private Integer positiveSignals = 0;
    @Field(type = FieldType.Integer)
    private Integer negativeSignals = 0;
}