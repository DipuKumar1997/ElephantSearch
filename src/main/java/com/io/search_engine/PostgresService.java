package com.io.search_engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostgresService {

    private final ProductPgRepository repo;
    public PostgresService(ProductPgRepository repo) {
        this.repo = repo;
    }
    public ResultWrapper<ProductPgProjection> fetch(int page,String query) {
        long start = System.nanoTime();
        //Page<ProductPg> result = repo.findAll( PageRequest.of(page, 10));
        Page<ProductPgProjection> result=    repo.findProductByManualQuery(PageRequest.of ( page,10 ),query);
        long end = System.nanoTime();
        return new ResultWrapper<>( result.getContent(), (end - start) / 1_000_000  );// ms
    }
}