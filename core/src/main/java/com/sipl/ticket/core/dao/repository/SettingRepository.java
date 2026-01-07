package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    @Query("SELECT s FROM Setting s WHERE LOWER(s.screen) = LOWER(:screen)")
    Optional<Setting> findByScreenIgnoreCase(@Param("screen") String screen);

    @Query("SELECT s FROM Setting s WHERE s.screen = :screen")
    Optional<Setting> findByScreen(@Param("screen") String screen);
}
