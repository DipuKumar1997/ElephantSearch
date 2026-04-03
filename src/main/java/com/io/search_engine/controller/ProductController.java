package com.io.search_engine.controller;
//
//import com.io.search_engine.model.Product;
//import com.io.search_engine.ProductRepository;
//import com.io.search_engine.service.ProductSearchService;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations; // Change this import
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
////    private final ElasticsearchOperations elasticsearchOperations; // Use Operations instead
//    private final ProductRepository productRepository;
//    private final ProductSearchService searchService;
//
//
//    public ProductController(ElasticsearchOperations elasticsearchOperations,
//                             ProductRepository productRepository) {
//        this.elasticsearchOperations = elasticsearchOperations;
//        this.productRepository = productRepository;
//    }
//
//    @GetMapping("/all")
//    public Iterable<Product> getAllProducts() { // Repositories usually return Iterable or Page
//        return productRepository.findAll();
//    }
//     @GetMapping("/search")
//    public List<Product> search(@RequestParam String q) {
//        return searchService.searchWithFeedback(q);
//    }
//
//}

//package com.io.search_engine;

import com.io.search_engine.model.Product;
import com.io.search_engine.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductSearchService searchService;
    //  Normal search
    @GetMapping("/search")
    public List<Product> search(@RequestParam String q) {
        //return productRepository.findByTitleContaining(query);
        return searchService.searchWithFeedback(q);
    }

    // Fast search (pagination)
    @GetMapping("/search-fast")
    public List<Product> searchFast(@RequestParam String q) {
        return searchService.searchWithFeedbackFaster(q);
    }
}