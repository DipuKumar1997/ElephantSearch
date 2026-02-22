package com.io.search_engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductPgRepository extends JpaRepository<ProductPg, Long> {

    Page<ProductPg> findAll(Pageable pageable);

//    @Query("""
//select id , title from com.io.search_engine.ProductPg ILike %:query% limit 10
//""")
//@Query(value = "SELECT id, title FROM product_table  WHERE title ILIKE CONCAT('%', :query, '%') " + "LIMIT 10", nativeQuery = true)
//    Page<ProductPg> findProductByManualQuery(Pageable pageable,  @Param ( "query" ) String query);
@Query(value = "SELECT id, title FROM products WHERE title ILIKE CONCAT('%', :query, '%')",
        countQuery = "SELECT count(*) FROM products WHERE title ILIKE CONCAT('%', :query, '%')",
        nativeQuery = true)
//Page<Object[]> findProductByManualQuery(Pageable pageable, @Param("query") String query);
Page<ProductPgProjection> findProductByManualQuery(Pageable pageable, @Param("query") String query);
}