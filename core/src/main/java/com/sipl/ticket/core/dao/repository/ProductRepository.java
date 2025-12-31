package com.sipl.ticket.core.dao.repository;

import java.util.List;
import java.util.Optional;

import com.sipl.ticket.core.dao.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

  @Query("from Products p where p.productName= :productName and p.isActive=true")
  Optional<Products> findByProductName(@Param("productName") String productName);

  @Query("from Products p where p.productId= :productId and p.isActive=true")
  Optional<Products> findByProductId(@Param("productId") Long productId);

  @Query("from Products p where p.productCode= :productCode and p.isActive=true")
  Optional<Products> findByProductCode(@Param("productCode") String productCode);

  @Query(
      "SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Products p WHERE p.productCode = :productCode")
  boolean existsByProductCode(@Param("productCode") String productCode);

    @Query("from Products p where p.isActive=true")
    List<Products> findByIsActiveTrue();
}
