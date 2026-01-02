package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByServiceNameIgnoreCase(String serviceName);

    boolean existsByServiceNameIgnoreCaseAndServiceIdNot(
            String serviceName, Long serviceId
    );

    @Query(" SELECT s " +
            " FROM ServiceEntity s " +
            " WHERE (:isActive IS NULL OR s.isActive = :isActive) " +
            " AND (:serviceId IS NULL OR s.serviceId = :serviceId) ")
    Page<ServiceEntity> searchServices(
            @Param("serviceId") Long serviceId,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}
