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

    @Transactional
    public Uom update(Long companyId, Long uomId, Uom patch) {
        Uom existing = uomRepository.findById(uomId)
                .orElseThrow(() -> new IllegalArgumentException("UoM not found"));
        if (existing.getCompany() == null || existing.getCompany().getId() == null || !existing.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("UoM company mismatch");
        }

        existing.setCode(patch.getCode());
        existing.setName(patch.getName());
        existing.setActive(patch.isActive());
        return uomRepository.save(existing);
    }

    @Transactional
    public void delete(Long companyId, Long uomId) {
        Uom existing = uomRepository.findById(uomId)
                .orElseThrow(() -> new IllegalArgumentException("UoM not found"));
        if (existing.getCompany() == null || existing.getCompany().getId() == null || !existing.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("UoM company mismatch");
        }
        uomRepository.delete(existing);
    }
}
