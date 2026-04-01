package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import org.hibernate.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByServiceNameIgnoreCase(String serviceName);

    boolean existsByServiceNameIgnoreCaseAndServiceIdNot(
            String serviceName, Long serviceId
    );
    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM ServiceEntity s " +
                    "WHERE LOWER(s.serviceName) = LOWER(:serviceName) " +
                    "AND s.company.companyId = :companyId " +
                    "AND s.isDelete = false"
    )
    boolean existsActiveServiceForCompany(
            @Param("serviceName") String serviceName,
            @Param("companyId") Long companyId
    );


    @Query(
            "SELECT s FROM ServiceEntity s " +
                    "WHERE s.isDelete = false " +
                    "AND ( :companyId IS NULL OR s.company.companyId = :companyId ) " +
                    "AND ( :isActive IS NULL OR s.isActive = :isActive ) " +
                    "AND ( :query IS NULL OR :query = '' " +
                    "   OR LOWER(s.serviceName) LIKE CONCAT('%', LOWER(:query), '%') " +
                    "   OR CAST(s.serviceId AS string) LIKE CONCAT('%', :query, '%') " +
                    ")"
    )
    Page<ServiceEntity> searchServices(
            @Param("query") String query,
            @Param("isActive") Boolean isActive,
            @Param("companyId") Long companyId,
            Pageable pageable
    );



    @Query(
            "SELECT s FROM ServiceEntity s " +
                    "WHERE s.isDelete = false " +
                    "AND s.isActive = true " +
                    "AND (:companyId IS NULL OR s.company.companyId = :companyId) " +
                    "ORDER BY s.serviceId DESC"
    )
    List<ServiceEntity> findServices(@Param("companyId") Long companyId);



}
