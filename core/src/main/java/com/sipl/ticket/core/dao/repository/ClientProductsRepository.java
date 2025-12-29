package com.sipl.ticket.core.dao.repository;
import com.sipl.ticket.core.dao.entity.ClientProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProductsRepository extends JpaRepository<ClientProducts, Long> {
}
