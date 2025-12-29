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

  @Query("from Products p where p.productName=?1 and p.isActive=true")
  Optional<Products> findByProductName(String productName);

  @Query("from Products p where p.productId=?1 and p.isActive=true")
  Optional<Products> findByProductId(Long productId);

  @Query("from Products p where p.brands.brandId=?1 and p.isActive=true")
  List<Products> findByBrandId(Long brandId);

  @Query("from Products p where p.productSubCategory.productSubCategoryId=?1 and p.isActive=true")
  List<Products> findByProductSubCategory(Long brandId);

  @Query("from Products p where p.unit.unitId=?1 and p.isActive=true")
  List<Products> findByUnitId(Long unitId);

  @Query("from Products p where p.origins.originId=?1 and p.isActive=true")
  List<Products> findByOriginId(Long originId);

  @Query("from Products p where p.productCode=?1 and p.isActive=true")
  Optional<Products> findByProductCode(String productCode);

  @Query("from Products p where p.isActive=true and p.isService=false ORDER BY p.productName ASC")
  List<Products> findAllActive();

  @Query(
      "select p from Products p where (:productId IS NULL OR p.productId = :productId) AND p.isActive=true ORDER BY p.productName ASC")
  Page<Products> findTransactionsByProductNameAndDates(Long productId, Pageable pageable);


  @Query("SELECT COUNT(pu) FROM ProductUnit pu WHERE pu.product.productId = :productId")
  Long countProductUnitsByProductId(@Param("productId") Optional<Long> productId);

  @Query("SELECT COUNT(pu) FROM ProductUnit pu WHERE pu.product.productId = :productId")
  Long countProductUnitsByProductId(@Param("productId") Long productId);

  @Query(
      "SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Products p WHERE p.productCode = :productCode")
  boolean existsByProductCode(@Param("productCode") String productCode);

  Optional<Products> findByProductIdAndIsActiveTrue(Long productId);

  @Query("SELECT p FROM Products p WHERE p.isSync = false")
  List<Products> findByIsSyncFalse();

  @Query("FROM Products p WHERE p.isActive = true")
  List<Products> findByIsActiveTrue();

}
