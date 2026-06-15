package com.sitetour.sitetourapplication.repository;

import com.sitetour.sitetourapplication.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByTimestampDesc();

    List<AuditLog> findByTeamNameOrderByTimestampDesc(
            String teamName
    );
}