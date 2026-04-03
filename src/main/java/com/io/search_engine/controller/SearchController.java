
package com.io.search_engine.controller;

import com.io.search_engine.model.Product;
import com.io.search_engine.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductSearchService searchService;

    //  Search
    @GetMapping
    public List<Product> search(@RequestParam String q) {
        return searchService.searchWithFeedback(q);
    }

    //  Feedback
    @PostMapping("/feedback/{id}")
    public String giveFeedback(@PathVariable String id, @RequestParam String type) {

        boolean updated = searchService.updateFeedback(id, type);

        if (updated) {
            return "Feedback updated";
        } else {
            return "Product not found";
        }
    }
}