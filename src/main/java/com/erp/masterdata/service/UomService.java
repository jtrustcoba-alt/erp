package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.repository.CompanyRepository;
import com.erp.masterdata.entity.Uom;
import com.erp.masterdata.repository.UomRepository;

@Service
public class UomService {

    private final UomRepository uomRepository;
    private final CompanyRepository companyRepository;

    public UomService(UomRepository uomRepository, CompanyRepository companyRepository) {
        this.uomRepository = uomRepository;
        this.companyRepository = companyRepository;
    }

    public List<Uom> listByCompany(Long companyId) {
        return uomRepository.findByCompanyId(companyId);
    }

    @Transactional
    public Uom create(Long companyId, Uom uom) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        uom.setCompany(company);
        return uomRepository.save(uom);
    }
}
