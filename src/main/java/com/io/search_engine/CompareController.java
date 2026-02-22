package com.io.search_engine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/compare")
public class CompareController {

    private final PostgresService pg;
    private final ElasticService es;

    public CompareController(PostgresService pg, ElasticService es) {
        this.pg = pg;
        this.es = es;
    }

    @GetMapping("/products")
    public Map<String, Object> compare(@RequestParam(defaultValue = "0") int page,@RequestParam String query) {
        ResultWrapper<ProductPgProjection> pgResult = pg.fetch(page,query);
        ResultWrapper<Product> esResult = es.fetch(page,query);
        Map<String, Object> response = new HashMap<> ();
        response.put("postgres_time_ms", pgResult.getTimeMs());
        response.put("elastic_time_ms", esResult.getTimeMs());
        response.put("postgres_data", pgResult.getData());
        response.put("elastic_data", esResult.getData());
        return response;
    }
    @RestController
    @RequestMapping("/api/search")
    public class SearchController {
        @GetMapping("/postgres")
        public Map<String, Object> searchPostgres(@RequestParam String q) {
            long start = System.nanoTime();
            ResultWrapper<ProductPgProjection> res = pg.fetch(0, q);
            long end = System.nanoTime();
            Map<String, Object> map = new HashMap<>();
            map.put("data", res.getData());
            map.put("time", (end - start) / 1_000_000);
            return map;
        }
        @GetMapping("/elastic")
        public Map<String, Object> searchElastic(@RequestParam String q) {
            long start = System.nanoTime();
            ResultWrapper<Product> res = es.fetch(0, q);
            long end = System.nanoTime();
            Map<String, Object> map = new HashMap<>();
            map.put("data", res.getData());
            map.put("time", (end - start) / 1_000_000);
            return map;
        }
    }

    /*
        @GetMapping
        public CompletableFuture<Map<String, Object>> search(@RequestParam String q) {
            CompletableFuture<Map<String, Object>> pgFuture =
                    CompletableFuture.supplyAsync(() -> {
                        long start = System.currentTimeMillis();
                        List<ProductPgProjection> res = pg.fetch(0,q);
                        long time = System.currentTimeMillis() - start;

                        return Map.of(
                                "timeMs", time,
                                "data", res
                        );
                    });

            CompletableFuture<Map<String, Object>> esFuture =
                    CompletableFuture.supplyAsync(() -> {
                        long start = System.currentTimeMillis();
                        List<?> res = esService.search(q);
                        long time = System.currentTimeMillis() - start;

                        return Map.of(
                                "timeMs", time,
                                "data", res
                        );
                    });

            return pgFuture.thenCombine(esFuture,
                    (pg, es) -> Map.of(
                            "postgres", pg,
                            "elastic", es
                    ));
        }
    */
}