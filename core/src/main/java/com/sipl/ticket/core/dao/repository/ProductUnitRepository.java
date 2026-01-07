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

    @Query("From ProductUnit pu where pu.product.productId = :productId ")
    List<ProductUnit> findAllByProductId(@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query("UPDATE ProductUnit pu SET pu.isActive = false WHERE pu.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
