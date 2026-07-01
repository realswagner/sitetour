package com.sitetour.sitetourapplication.service;

import com.sitetour.sitetourapplication.entity.AuditLog;
import com.sitetour.sitetourapplication.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(
            AuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(
            String username,
            String teamName,
            String action,
            String details
    ) {

        AuditLog log = new AuditLog();

        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setTeamName(teamName);
        log.setAction(action);
        log.setDetails(details);

        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {

        return auditLogRepository
                .findAllByOrderByTimestampDesc();
    }

    public List<AuditLog> getLogsByTeam(
            String teamName
    ) {

        return auditLogRepository
                .findByTeamNameOrderByTimestampDesc(
                        teamName
                );
    }


}