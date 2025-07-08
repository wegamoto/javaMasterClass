package com.wewe.autogate.repository;

import com.wewe.autogate.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    @Query("SELECT DATE(a.accessTime) as date, " +
            "SUM(CASE WHEN a.status = 'ALLOWED' THEN 1 ELSE 0 END) as allowedCount, " +
            "SUM(CASE WHEN a.status = 'DENIED' THEN 1 ELSE 0 END) as deniedCount " +
            "FROM AccessLog a GROUP BY DATE(a.accessTime) ORDER BY DATE(a.accessTime) DESC")
    List<Object[]> getDailyAccessSummary();

    @Query("SELECT DATE(a.accessTime), " +
            "SUM(CASE WHEN a.status = 'ALLOWED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'DENIED' THEN 1 ELSE 0 END) " +
            "FROM AccessLog a WHERE DATE(a.accessTime) BETWEEN :start AND :end " +
            "GROUP BY DATE(a.accessTime) ORDER BY DATE(a.accessTime)")
    List<Object[]> getDailyAccessSummaryBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);


}

