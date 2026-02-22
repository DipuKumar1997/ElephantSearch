//package com.io.search_engine;
//
//import com.opencsv.CSVReader;
//import org.elasticsearch.action.bulk.BulkRequest;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.RequestOptions;
////import org.elasticsearch.action.bulk.*;
//import org.elasticsearch.client.*;
////import org.elasticsearch.action.index.IndexRequest;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.FileReader;
//import java.util.*;
//
//@Component
//public class StreamSeeder implements CommandLineRunner {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RestHighLevelClient esClient;
//
//    public StreamSeeder(JdbcTemplate jdbcTemplate,
//                        RestHighLevelClient esClient) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.esClient = esClient;
//    }
//    @Override
//    public void run(String... args) throws Exception {
//        String csvPath = "/home/dipu/projects/search-engine/archive/train.csv";
//        CSVReader reader = new CSVReader(new FileReader(csvPath));
//        reader.readNext(); // skip header
//        String insertSql =
//                "INSERT INTO products " +
//                        "(id,title,rating,category,platform,price,actual_price) " +
//                        "VALUES (?,?,?,?,?,?,?)";
//
//        int batchSize = 1000;
//        int count = 0;
//        List<Object[]> pgBatch = new ArrayList<>();
//        BulkRequest bulkRequest = new BulkRequest();
//        String[] row;
//        long start = System.currentTimeMillis();
//        while ((row = reader.readNext()) != null) {
//            long id = Long.parseLong(row[0]);
//            String title = row[1];
//            double rating = parseDouble(row[2]);
//            String category = row[3];
//            String platform = row[4];
//            double price = parseDouble(row[5]);
//            double actPrice = parseDouble(row[6]);
//            // ---------- PostgreSQL Batch ----------
//            pgBatch.add(new Object[]{
//                    id, title, rating,
//                    category, platform,
//                    price, actPrice
//            });
//            // ---------- Elasticsearch Bulk ----------
//            Map<String,Object> json = new HashMap<>();
//            json.put("id", id);
//            json.put("title", title);
//            json.put("rating", rating);
//            json.put("category", category);
//            json.put("platform", platform);
//            json.put("price", price);
//            IndexRequest req =
//                    new IndexRequest("products")
//                            .id(String.valueOf(id))
//                            .source(json);
//            bulkRequest.add(req);
//            count++;
//            // ---------- Flush Batch ----------
//            if(count % batchSize == 0){
//                jdbcTemplate.batchUpdate(insertSql, pgBatch);
//                esClient.bulk(bulkRequest,
//                        RequestOptions.DEFAULT);
//                pgBatch.clear();
//                bulkRequest = new BulkRequest();
//                System.out.println("Inserted: " + count);
//            }
//        }
//
//        // flush remainder
//        if(!pgBatch.isEmpty())
//            jdbcTemplate.batchUpdate(insertSql, pgBatch);
//        if(bulkRequest.numberOfActions() > 0)
//            esClient.bulk(bulkRequest,
//                    RequestOptions.DEFAULT);
//        long end = System.currentTimeMillis();
//        System.out.println("DONE — rows=" + count);
//        System.out.println("Time(ms)=" + (end-start));
//    }
//    private double parseDouble(String s){
//        try {
//            return Double.parseDouble(
//                    s.replace("%","").trim());
//        } catch(Exception e){
//            return 0;
//        }
//    }
//}
package com.io.search_engine;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Profile ( "manual-seeding" )
@RequiredArgsConstructor
public class StreamSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    private final ElasticsearchOperations esOps;

//    @Override
//    public void run(String... args) throws Exception {
//        String csvPath = "/home/dipu/projects/search-engine/archive/train.csv";
//        CSVReader reader = new CSVReader(new FileReader(csvPath));
//        reader.readNext(); // skip header
//        String insertSql =
//                "INSERT INTO products " +
//                        "(id,title,rating,category,platform,price,actual_price) " +
//                        "VALUES (?,?,?,?,?,?,?) " +
//                        "ON CONFLICT (id) DO NOTHING";
//        int batchSize = 1000;
//        int count = 0;
//        List<Object[]> pgBatch = new ArrayList<>();
//        List<IndexQuery> esBatch = new ArrayList<>();
//        String[] row;
//        long start = System.currentTimeMillis();
//        while ((row = reader.readNext()) != null) {
//            try {
//                long id = Long.parseLong(clean(row[0]));
//                String title = clean(row[1]);
//                double rating = safeDouble(row[2]);
//                String category = clean(row[3]);
//                String platform = clean(row[4]);
//                double price = safeDouble(row[5]);
//                double actPrice = safeDouble(row[6]);
//                // ---------- PostgreSQL ----------
//                pgBatch.add(new Object[]{id, title, rating,category, platform, price, actPrice});
//
//                // ---------- Elasticsearch ----------
//                Product p = new Product();
//                p.setId(String.valueOf(id));
//                p.setTitle(title);
//                p.setDescription(category + " " + platform);
//                p.setPrice(price);
//
//                IndexQuery iq = new IndexQueryBuilder().withId(p.getId()).withObject(p).build();
//
//                esBatch.add(iq);
//                count++;
//                if (count % batchSize == 0) {
//                    flushBatch(insertSql, pgBatch, esBatch);
//                    System.out.println("Inserted: " + count);
//                }
//            } catch (Exception e) {
//                // Skip bad row but continue processing
//                System.out.println("Skipped bad row at count=" + count);
//            }
//        }
//        // Flush remaining
//        flushBatch(insertSql, pgBatch, esBatch);
//
//        long end = System.currentTimeMillis();
//
//        System.out.println("DONE rows=" + count);
//        System.out.println("Time(ms)=" + (end - start));
//    }

    @Override
    public void run(String... args) throws Exception {
        String csvPath = "/home/dipu/projects/search-engine/archive2_1M/amazon_products.csv";
        CSVReader reader = new CSVReader(new FileReader(csvPath));
        reader.readNext(); // Skip Header: asin,title,imgUrl,productURL,stars,reviews,price...
        String insertSql = "INSERT INTO products (id, title, rating, price, category) VALUES (?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";

        int batchSize = 5000; // Increased for 1M rows
        int count = 0;
        List<Object[]> pgBatch = new ArrayList<>();
        List<IndexQuery> esBatch = new ArrayList<>();
        String[] row;
        while ((row = reader.readNext()) != null) {
            try {
                // Mapping CSV: 0:asin, 1:title, 4:stars, 6:price, 8:category_id
                String asin = clean(row[0]);
                String title = clean(row[1]);
                double stars = safeDouble(row[4]);
                double price = safeDouble(row[6]);
                String category = clean(row[8]);

                // ASIN to Long conversion for Postgres (using hash if not numeric)
                long pgId = (long) count + 1;

                // ---------- PostgreSQL ----------
                pgBatch.add(new Object[]{pgId, title, stars, price, category});

                // ---------- Elasticsearch ----------
                Product p = new Product();
                p.setId(asin); // ES uses String ID, so ASIN is perfect
                p.setTitle(title);
                p.setPrice(price);
                p.setDescription("Category: " + category);

                IndexQuery iq = new IndexQueryBuilder().withId(p.getId()).withObject(p).build();
                esBatch.add(iq);
                count++;
                if (count % batchSize == 0) {
                    flushBatch(insertSql, pgBatch, esBatch);
                    System.out.println("Inserted: " + count);
                }
            } catch (Exception e) {
                log.error ( "some error occurred" );
                e.printStackTrace ();
                // Log error and continue
            }
        }
        flushBatch(insertSql, pgBatch, esBatch);
        System.out.println("Final Count: " + count);
    }


    // ---------------------------------------------------

    private void flushBatch(String insertSql, List<Object[]> pgBatch, List<IndexQuery> esBatch) {

        if (!pgBatch.isEmpty()) {
            try {
                jdbcTemplate.batchUpdate(insertSql, pgBatch);
            } catch (Exception e) {
                System.out.println("PG batch failed — retrying row by row");

                for (Object[] row : pgBatch) {
                    try {
                        jdbcTemplate.update(insertSql, row);
                    } catch (Exception ignored) {
                    }
                }
            }
            pgBatch.clear();
        }

        if (!esBatch.isEmpty()) {
            esOps.bulkIndex(esBatch,
                    esOps.getIndexCoordinatesFor(Product.class));
            esBatch.clear();
        }
    }

    private String clean(String s) {
        if (s == null) return "";
        return s
                .replace("\u00A0", " ")        // remove NBSP
                .replaceAll("[\\x00-\\x1F]", "") // remove control chars
                .trim();
    }

    private double safeDouble(String s) {
        try {
            return Double.parseDouble( clean(s).replace("%", "") );
        } catch (Exception e) {
            return 0;
        }
    }
}