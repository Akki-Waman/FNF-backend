package com.sipl.ticket.core.dao.repository;


import com.sipl.ticket.core.dao.entity.ProductUnit;
import com.sipl.ticket.core.dao.entity.Products;
import com.sipl.ticket.core.dao.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long> {

    @Query("SELECT pu from ProductUnit pu where pu.product.productId = :productId")
    List<ProductUnit> findAllByProductId(@Param("productId") Long productId);

    @Query("SELECT pu from ProductUnit pu where pu.productUnitId = :productUnitId")
    Optional<ProductUnit> findByProductUnitId(@Param("productUnitId") Long productUnitId);

    @Query(
            "from ProductUnit pu where pu.product.productId=?1 and pu.unit.unitId=?2 and pu.isActive = true")
    ProductUnit getProductUnitByProductAndUnitId(Long productId, Long unitId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductUnit pu SET pu.isActive = false WHERE pu.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Query("From ProductUnit p where p.product.productId = :productId")
    List<ProductUnit> findByProductAndIsActiveTrue(Long productId);

    @Query(
            ""
                    + " SELECT pu FROM ProductUnit pu "
                    + " WHERE pu.product = :product "
                    + " AND pu.unit = :unit "
                    + " AND pu.isSellingUnit = true "
                    + " AND pu.isActive = true ")
    Optional<ProductUnit> findByProductAndUnitAndIsSellingUnitTrue(
            @Param("product") Products product, @Param("unit") Unit unit);
}
