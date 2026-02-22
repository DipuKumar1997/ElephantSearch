package com.io.search_engine;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final ProductRepository productRepository;
    public void crawlProductPage(String url) {
        try {
            // 1. Connect to the website
            Document doc = Jsoup.connect(url).get();
            // 2. Extract data using CSS Selectors (like jQuery)
            String title = doc.title();
            String description = doc.select("meta[name=description]").attr("content");
            // 3. Create a new Product Object
            Product product = new Product();
            product.setTitle(title);
            product.setDescription(description);
            product.setPositiveSignals(0); // Start with neutral feedback
            product.setNegativeSignals(0);
            // 4. Save directly to OpenSearch/Elasticsearch
            productRepository.save(product);
        } catch (IOException e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }
}