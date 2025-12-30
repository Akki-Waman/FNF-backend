package com.sipl.ticket.core.dao.repository;
import com.sipl.ticket.core.dao.entity.ClientProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientProductsRepository extends JpaRepository<ClientProducts, Long> {
    boolean existsBySerialNumberIgnoreCase(String serialNumber);

    boolean existsByImeiNoIgnoreCase(String imeiNo);

    boolean existsBySerialNumberIgnoreCaseAndClientProductIdNot(String trim, Long clientProductId);

    boolean existsByImeiNoIgnoreCaseAndClientProductIdNot(String trim, Long clientProductId);

    List<ClientProducts> findByIsActiveTrue();
}
