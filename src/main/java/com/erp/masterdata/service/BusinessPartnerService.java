package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.repository.CompanyRepository;
import com.erp.masterdata.entity.BusinessPartner;
import com.erp.masterdata.repository.BusinessPartnerRepository;

@Service
public class BusinessPartnerService {

    private final BusinessPartnerRepository businessPartnerRepository;
    private final CompanyRepository companyRepository;

    public BusinessPartnerService(BusinessPartnerRepository businessPartnerRepository, CompanyRepository companyRepository) {
        this.businessPartnerRepository = businessPartnerRepository;
        this.companyRepository = companyRepository;
    }

    public List<BusinessPartner> listByCompany(Long companyId) {
        return businessPartnerRepository.findByCompanyId(companyId);
    }

    @Transactional
    public BusinessPartner create(Long companyId, BusinessPartner partner) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        partner.setCompany(company);
        return businessPartnerRepository.save(partner);
    }
}
