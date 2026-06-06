package com.ensf.fnf.core.dao.repository;

import com.ensf.fnf.core.dao.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginOtpRepository extends JpaRepository<OtpEntity, Long> {

}