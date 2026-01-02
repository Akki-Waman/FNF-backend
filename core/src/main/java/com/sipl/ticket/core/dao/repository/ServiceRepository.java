package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByServiceNameIgnoreCaseAndIsDeletedFalse(String serviceName);

    boolean existsByServiceNameIgnoreCaseAndServiceIdNotAndIsDeletedFalse(
            String serviceName, Long serviceId
    );

    List<ServiceEntity> findByIsDeletedFalse();


    @Query("SELECT s " +
            "FROM ServiceEntity s " +
            "WHERE s.isActive = true " +
            "AND (:serviceId IS NULL OR s.serviceId = :serviceId)" +
            "ORDER BY s.serviceId DESC" )
    Page<ServiceEntity> searchByServiceId(
            @Param("serviceId") Long serviceId,
            Pageable pageable
    );
}
