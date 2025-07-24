package com.wewe.taxcertify.service;

import com.wewe.taxcertify.model.TaxCertificate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaxCertificateService {

    private final List<TaxCertificate> certificates = new ArrayList<>();
    private Long nextId = 1L;

    public void save(TaxCertificate cert) {
        if (cert.getId() == null) {
            cert.setId(nextId++);
            certificates.add(cert);
        } else {
            certificates.replaceAll(c -> c.getId().equals(cert.getId()) ? cert : c);
        }
    }

    public List<TaxCertificate> findAll() {
        return certificates;
    }

    public TaxCertificate findById(Long id) {
        Optional<TaxCertificate> cert = certificates.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        return cert.orElse(null);
    }

    public void deleteById(Long id) {
        certificates.removeIf(c -> c.getId().equals(id));
    }
}
