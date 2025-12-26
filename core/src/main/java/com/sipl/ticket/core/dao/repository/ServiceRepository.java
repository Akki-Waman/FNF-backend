package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByServiceNameIgnoreCaseAndIsDeletedFalse(String serviceName);

    boolean existsByServiceNameIgnoreCaseAndServiceIdNotAndIsDeletedFalse(
            String serviceName, Long serviceId
    );

    List<ServiceEntity> findByIsDeletedFalse();
}
