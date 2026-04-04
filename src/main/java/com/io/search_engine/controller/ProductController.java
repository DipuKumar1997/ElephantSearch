package com.io.search_engine.controller;


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
