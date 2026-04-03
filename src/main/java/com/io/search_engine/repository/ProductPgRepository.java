package com.io.search_engine.repository;

import com.io.search_engine.projection.ProductPgProjection;
import com.io.search_engine.model.ProductPg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductPgRepository extends JpaRepository<ProductPg, Long> {

    Page<ProductPg> findAll(Pageable pageable);

@Query(value = "SELECT id, title FROM products WHERE title ILIKE CONCAT('%', :query, '%')",
        countQuery = "SELECT count(*) FROM products WHERE title ILIKE CONCAT('%', :query, '%')",
        nativeQuery = true)
        Page<ProductPgProjection> findProductByManualQuery(Pageable pageable, @Param("query") String query);
}