package com.io.search_engine;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final ProductSearchService searchService;
//    private final CrawlerService crawlerService;
    private final ProductRepository productRepository;
    public SearchController(ProductSearchService searchService, ProductRepository productRepository) {
        this.searchService = searchService;
        this.productRepository = productRepository;
    }
    // 1. CRAWL: Visit a website and index it
    // Example: GET /api/search/crawl?url=https://example.com
//    @GetMapping("/crawl")
//    public String crawl(@RequestParam String url) {
//        crawlerService.crawlProductPage(url);
//        return "Crawl request submitted for: " + url;
//    }

    // 2. SEARCH: Get products ranked by BM25 + Feedback
    // Example: GET /api/search?q=iphone
    @GetMapping
    public List<Product> search(@RequestParam String q) {
        return searchService.searchWithFeedback(q);
    }

    // 3. FEEDBACK: Increase positive or negative signals
    // Example: POST /api/search/feedback/123?type=positive
    @PostMapping("/feedback/{id}")
    public String giveFeedback(@PathVariable String id, @RequestParam String type) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (type.equalsIgnoreCase("positive")) {
                product.setPositiveSignals(product.getPositiveSignals() + 1);
            } else {
                product.setNegativeSignals(product.getNegativeSignals() + 1);
            }
            productRepository.save(product);
            return "Feedback updated for " + product.getTitle();
        }
        return "Product not found";
    }
}