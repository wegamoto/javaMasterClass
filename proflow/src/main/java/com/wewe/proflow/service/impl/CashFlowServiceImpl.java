package com.wewe.proflow.service.impl;

import com.wewe.proflow.model.CashFlow;
import com.wewe.proflow.repository.CashFlowRepository;
import com.wewe.proflow.service.CashFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashFlowServiceImpl implements CashFlowService {

    private final CashFlowRepository cashFlowRepository;

    @Override
    public List<CashFlow> findAll() {
        return cashFlowRepository.findAll();
    }

    @Override
    public Optional<CashFlow> findById(Long id) {
        return cashFlowRepository.findById(id);
    }

    @Override
    public CashFlow save(CashFlow cashFlow) {
        return cashFlowRepository.save(cashFlow);
    }

    @Override
    public void deleteById(Long id) {
        cashFlowRepository.deleteById(id);
    }
}
