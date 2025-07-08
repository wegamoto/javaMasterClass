package com.wewe.proflow.service;

import com.wewe.proflow.model.CashFlow;

import java.util.List;
import java.util.Optional;

public interface CashFlowService {
    List<CashFlow> findAll();
    Optional<CashFlow> findById(Long id);
    CashFlow save(CashFlow cashFlow);
    void deleteById(Long id);
}
