package com.io.search_engine;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

//public interface ProductRepository extends ElasticsearchRepository<Product, String> {
//    // Spring generates the query for you!
//    List<Product> findByTitleContaining(String keyword);
//}

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    // Standard CRUD is handled here automatically
}