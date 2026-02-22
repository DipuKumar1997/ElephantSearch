package com.io.search_engine;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations; // Change this import
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ElasticsearchOperations elasticsearchOperations; // Use Operations instead
    private final ProductRepository productRepository;

    public ProductController(ElasticsearchOperations elasticsearchOperations,
                             ProductRepository productRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.productRepository = productRepository;
    }

    @GetMapping("/all")
    public Iterable<Product> getAllProducts() { // Repositories usually return Iterable or Page
        return productRepository.findAll();
    }

//    @GetMapping("/search")
//    public List<Product> searchProducts(@RequestParam String query) {
//        return productRepository.findByTitleContaining(query);
//    }
}