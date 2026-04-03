package com.io.search_engine.service;

import com.io.search_engine.model.Product;
import com.io.search_engine.wrapper.ResultWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ElasticService {
    private ProductSearchService productSearchService;//elasticseach service
    // private final ProductRepository repo;//its a elasticseach repository

    public ElasticService( ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }
    public ResultWrapper<Product> fetch(int page, String query) {
        long start = System.nanoTime();
        //  Page<Product> result = repo.findAll( PageRequest.of(page, 10));
        //  List<Product> result = productSearchService.searchWithFeedback(query);
        List<Product> esResult =productSearchService.searchWithFeedbackFaster (query);
        long end = System.nanoTime();
        // result.getContent()
        return new ResultWrapper<>(esResult, (end - start) / 1_000_000 );
    }
}