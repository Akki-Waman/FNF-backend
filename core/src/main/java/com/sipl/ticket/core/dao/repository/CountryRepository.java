package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {


    boolean existsByCountryNameIgnoreCaseAndCountryIdNot(String name, Long countryId);

    boolean existsByCountryNameIgnoreCase(String name);
}
