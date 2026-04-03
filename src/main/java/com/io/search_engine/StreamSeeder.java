package com.io.search_engine;

import com.io.search_engine.model.Product;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.BulkRequest;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile("manual-seeding")
@RequiredArgsConstructor
public class StreamSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    private final OpenSearchClient client;
    @Override
    public void run(String... args) throws Exception {
        String csvPath = "/home/dipu/ElephantSearch/archive/amazon_products.csv";
        CSVReader reader = new CSVReader(new FileReader(csvPath));
        reader.readNext(); // skip header
        String insertSql = "INSERT INTO products (id, title, rating, price, category) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";
        int batchSize = 2000;
        int count = 0;
        List<Object[]> pgBatch = new ArrayList<>();
        List<BulkOperation> esBatch = new ArrayList<>();
        String[] row;
        // Create index once (ignore if exists)
        try {
            client.indices().create(c -> c.index("products"));
        } catch (Exception ignored) {}
        while ((row = reader.readNext()) != null) {
            try {
                String asin = clean(row[0]);
                String title = clean(row[1]);
                double stars = safeDouble(row[4]);
                double price = safeDouble(row[6]);
                String category = clean(row[8]);
                long pgId = count + 1;
                // ---------- PostgreSQL ----------
                pgBatch.add(new Object[]{pgId, title, stars, price, category});
                // ---------- OpenSearch ----------
                Product p = new Product ();
                p.setId(asin);
                p.setTitle(title);
                p.setPrice(price);
                p.setDescription("Category: " + category);
                esBatch.add(
                        BulkOperation.of(b -> b
                                .index(idx -> idx
                                        .index("products")
                                        .id(p.getId())
                                        .document(p)
                                )
                        )
                );
                count++;
                if (count % batchSize == 0) {
                    flushBatch(insertSql, pgBatch, esBatch);
                    log.info("Inserted: {}", count);
                }
            } catch (Exception e) {
                log.error("Row failed", e);
            }
        }
        flushBatch(insertSql, pgBatch, esBatch);

        log.info("Final Count: {}", count);
    }

    // ---------------------------------------------------

    private void flushBatch(String insertSql, List<Object[]> pgBatch, List<BulkOperation> esBatch) {

        // PostgreSQL
        if (!pgBatch.isEmpty()) {
            try {
                jdbcTemplate.batchUpdate(insertSql, pgBatch);
            } catch (Exception e) {
                log.warn("PG batch failed — retrying row by row");
                for (Object[] row : pgBatch) {
                    try {
                        jdbcTemplate.update(insertSql, row);
                    } catch (Exception ignored) {}
                }
            }
            pgBatch.clear();
        }
        // OpenSearch
        if (!esBatch.isEmpty()) {
            try {
                BulkRequest request = new BulkRequest.Builder().operations(esBatch).build();
                client.bulk(request);
            } catch (Exception e) {
                log.error("OpenSearch bulk failed", e);
            }
            esBatch.clear();
        }
    }

    private String clean(String s) {
        if (s == null) return "";
        return s.replace("\u00A0", " ").replaceAll("[\\x00-\\x1F]", "").trim();
    }

    private double safeDouble(String s) {
        try {
            return Double.parseDouble(clean(s).replace("%", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}