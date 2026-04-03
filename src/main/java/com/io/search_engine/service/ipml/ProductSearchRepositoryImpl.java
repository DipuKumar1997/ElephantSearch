package com.io.search_engine.service.ipml;


import com.io.search_engine.model.Product;
import com.io.search_engine.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepositoryImpl implements ProductSearchRepository {

    private final OpenSearchClient client;

    // SAVE (like repository.save())
    @Override
    public void save(Product product) {
        try {
            client.index(IndexRequest.of(i -> i
                    .index("products")
                    .id(product.getId())
                    .document(product)
            ));
        } catch (Exception e) {
            throw new RuntimeException("Indexing failed", e);
        }
    }

    // SEARCH (like repository.findBy...)
    @Override
    public List<Product> search(String query) {

        try {
            SearchResponse<Product> response = client.search(s -> s
                            .index("products")
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .query(query)
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