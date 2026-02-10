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

    @Query("from Products p where p.productName = :productName and p.isActive = true and p.isDelete = false")
    Optional<Products> findByProductName(@Param("productName") String productName);


    @Query("from Products p where p.productId = :productId and p.isActive = true and p.isDelete = false")
    Optional<Products> findByProductId(@Param("productId") Long productId);

    @Query("from Products p where p.productCode = :productCode and p.isActive = true and p.isDelete = false")
    Optional<Products> findByProductCode(@Param("productCode") String productCode);


    @Query(
      "SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Products p WHERE p.productCode = :productCode")
  boolean existsByProductCode(@Param("productCode") String productCode);

    @Query(
            "from Products p " +
                    "where p.isActive = true " +
                    "and p.isDelete = false " +
                    "and (:branchId is null or p.branch.branchId = :branchId) "+
            "order by p.productName asc"
    )
    List<Products> findByIsActiveTrue(
            @Param("branchId") Integer branchId
    );



    @Query(
            "SELECT p " +
                    "FROM Products p " +
                    "WHERE p.isActive = true AND p.isDelete = false " +

                    "AND ( :branchId IS NULL OR p.branch.branchId = :branchId ) " +

                    "AND ( :#{#productName == null || #productName.isEmpty()} = true " +
                    "      OR p.productName IN :productName ) " +

                    "AND ( :#{#brandIds == null || #brandIds.isEmpty()} = true " +
                    "      OR p.brands.brandId IN :brandIds ) " +

                    "AND ( :#{#originIds == null || #originIds.isEmpty()} = true " +
                    "      OR p.origins.originId IN :originIds ) " +

                    "AND ( :#{#categoryIds == null || #categoryIds.isEmpty()} = true " +
                    "      OR p.productCategory.productCategoryId IN :categoryIds ) " +

                    "AND ( :#{#subCategoryIds == null || #subCategoryIds.isEmpty()} = true " +
                    "      OR p.productSubCategory.productSubCategoryId IN :subCategoryIds ) "
    )
    Page<Products> searchProducts(
            @Param("productName") List<String> productName,
            @Param("brandIds") List<Long> brandIds,
            @Param("originIds") List<Long> originIds,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("subCategoryIds") List<Long> subCategoryIds,
            @Param("branchId") Integer branchId,
            Pageable pageable
    );

}
