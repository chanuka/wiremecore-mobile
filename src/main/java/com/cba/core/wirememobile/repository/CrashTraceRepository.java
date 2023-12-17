package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.CrashTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CrashTraceRepository extends JpaRepository<CrashTrace, Integer>, JpaSpecificationExecutor<CrashTrace> {
}
