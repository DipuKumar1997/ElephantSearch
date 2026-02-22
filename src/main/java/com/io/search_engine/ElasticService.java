package com.io.search_engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticService {
    private ProductSearchService productSearchService;//elasticseach service
    private final ProductRepository repo;//its a elasticseach repository

    public ElasticService(ProductRepository repo, ProductSearchService productSearchService) {
        this.repo = repo;
        this.productSearchService = productSearchService;
    }
    public ResultWrapper<Product> fetch(int page,String query) {
        long start = System.nanoTime();
//        Page<Product> result = repo.findAll( PageRequest.of(page, 10));
//        List<Product> result = productSearchService.searchWithFeedback(query);
        List<Product> esResult =productSearchService.searchWithFeedbackFaster (query);
        long end = System.nanoTime();
        // result.getContent()
        return new ResultWrapper<>(esResult, (end - start) / 1_000_000 );
    }
}