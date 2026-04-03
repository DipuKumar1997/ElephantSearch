

package com.io.search_engine.service;

import com.io.search_engine.model.Product;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final OpenSearchClient client;
    public boolean updateFeedback(String id, String type) {

    try {
        // 1. Get existing product
        var response = client.get(g -> g
                        .index("products")
                        .id(id),
                Product.class
        );

        if (!response.found()) {
            return false;
        }

        Product product = response.source();

        // 2. Update signals
        if ("positive".equalsIgnoreCase(type)) {
            product.setPositiveSignals(product.getPositiveSignals() + 1);
        } else {
            product.setNegativeSignals(product.getNegativeSignals() + 1);
        }

        // 3. Save back
        client.index(i -> i
                .index("products")
                .id(id)
                .document(product)
        );
        return true;

    } catch (Exception e) {
        throw new RuntimeException("Feedback update failed", e);
    }
}
    // 🔍 Basic search (with relevance)
    public List<Product> searchWithFeedback(String userInput) {

        try {
            SearchResponse<Product> response = client.search( s -> s
                            .index("products")
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .query(userInput)
                                            .fields("title^2", "description")
                                    )
                            ),
                    Product.class
            );

            List<Product> results = new ArrayList<>();

            response.hits().hits().forEach(hit -> {
                if (hit.source() != null) {
                    results.add(hit.source());
                }
            });

            return results;

        } catch (Exception e) {
            throw new RuntimeException("Search failed", e);
        }
    }

    // ⚡ Optimized search (pagination + limited fields)
    public List<Product> searchWithFeedbackFaster(String userInput) {

        try {
            SearchResponse<Product> response = client.search(s -> s
                            .index("products")
                            .from(0)
                            .size(10)
                            .source(src -> src
                                    .filter(f -> f
                                            .includes("id", "title")
                                    )
                            )
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .query(userInput)
                                            .fields("title^2", "description")
                                    )
                            ),
                    Product.class
            );

            List<Product> results = new ArrayList<>();

            response.hits().hits().forEach(hit -> {
                if (hit.source() != null) {
                    results.add(hit.source());
                }
            });

            return results;

        } catch (Exception e) {
            throw new RuntimeException("Search failed", e);
        }
    }
}