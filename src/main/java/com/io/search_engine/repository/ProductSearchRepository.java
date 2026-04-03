package com.io.search_engine.repository;

import com.io.search_engine.model.Product;

import java.util.List;

public interface ProductSearchRepository {
    void save(Product product);
    List<Product> search(String query);
}