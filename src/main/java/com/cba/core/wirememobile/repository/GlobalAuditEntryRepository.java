package com.cba.core.wirememobile.repository;

import com.cba.core.wirememobile.model.GlobalAuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalAuditEntryRepository extends JpaRepository<GlobalAuditEntry, Integer> {
}
