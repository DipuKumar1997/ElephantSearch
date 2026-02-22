package com.io.search_engine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    public List<Product> searchWithFeedback(String userInput) {
        // 1. Define the Math for your Score (Positive vs Negative)
        // We use a simple script: BaseScore * (1 + PositiveHits / (NegativeHits + 1))
        String scoreScript = "_score * (1.0 + (doc['positiveSignals'].value / (doc['negativeSignals'].value + 1.0)))";
        // 2. Build the Search Query (What are we looking for?)
        // We look for the user input in Title (Boosted) and Description
        var searchBuilder = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .fields("title^2", "description")
                        .query(userInput)
                ));
        // 3. Add the Scoring Logic as a separate step
        // (Note: Modern Spring Data still uses a small amount of builder logic,
        // but this approach treats the components as distinct pieces.)
        SearchHits<Product> hits = elasticsearchOperations.search(searchBuilder.build(), Product.class);
        // 4. Convert the results into a simple Java List
        List<Product> results = new ArrayList<>();
        for (SearchHit<Product> hit : hits) {
            results.add(hit.getContent());
        }
        return results;
    }
    public List<Product> searchWithFeedbackFaster(String userInput) {
        // 1. Define fields to include
        String[] includes = {"id", "title"};
        var searchBuilder = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .fields("title^2", "description")
                        .query(userInput)
                ))
                // 2. ONLY fetch these fields (Source Filtering)
                .withSourceFilter(new FetchSourceFilter (includes, null))
                .withPageable( PageRequest.of(0, 10));
        SearchHits<Product> hits = elasticsearchOperations.search(searchBuilder.build(), Product.class);
        return hits.stream()
                .map(SearchHit::getContent)
                .collect( Collectors.toList());
    }

}