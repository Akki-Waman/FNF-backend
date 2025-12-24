package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.PlantMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantMasterRepository extends JpaRepository<PlantMaster,Long> {

    @Query("SELECT p FROM PlantMaster p WHERE p.plantCode = :plantCode")
    Optional<PlantMaster> findByPlantCode(@Param("plantCode") String plantCode);


    @Query("SELECT p FROM PlantMaster p WHERE p.plantCode = :plantCode AND p.plantId <> :plantId")
    Optional<PlantMaster> findByPlantCodeAndPlantIdNot(@Param("plantCode") String plantCode,
                                                       @Param("plantId") Long plantId);
    @Query("SELECT p FROM PlantMaster p WHERE p.active = true")
    List<PlantMaster> findByActiveTrue();

    @Query("SELECT p FROM PlantMaster p WHERE p.active = true")
    Page<PlantMaster> findByActiveTrue(Pageable pageable);

    @Query("SELECT p FROM PlantMaster p " +
            "WHERE (:isActive IS NULL OR p.active = :isActive) " +
            "AND (:search IS NULL OR LOWER(p.plantCode) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.plantDescription) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PlantMaster> findByQuery(@Param("search") String search,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);
}
