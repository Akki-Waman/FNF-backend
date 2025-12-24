package com.sipl.ticket.core.dao.repository;

import com.sipl.ticket.core.dao.entity.AndroidVersionMaster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AndroidVersionMasterRepository
        extends CrudRepository<AndroidVersionMaster, Integer> {

    @Query("from AndroidVersionMaster am where am.appId=?1 and am.version=?2")
    Optional<AndroidVersionMaster> findByAppIdAndVersion(String appId, String version);

    @Query("from AndroidVersionMaster am where am.appId=?1")
    Optional<AndroidVersionMaster> findByAppId(String appId);
}
