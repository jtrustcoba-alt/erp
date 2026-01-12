package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.entity.Org;
import com.erp.core.repository.CompanyRepository;
import com.erp.core.repository.OrgRepository;
import com.erp.masterdata.entity.Warehouse;
import com.erp.masterdata.repository.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final CompanyRepository companyRepository;
    private final OrgRepository orgRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, CompanyRepository companyRepository, OrgRepository orgRepository) {
        this.warehouseRepository = warehouseRepository;
        this.companyRepository = companyRepository;
        this.orgRepository = orgRepository;
    }

    public List<Warehouse> listByCompany(Long companyId) {
        return warehouseRepository.findByCompanyId(companyId);
    }

    @Transactional
    public Warehouse create(Long companyId, Long orgId, Warehouse warehouse) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        warehouse.setCompany(company);

        if (orgId != null) {
            Org org = orgRepository.findById(orgId)
                    .orElseThrow(() -> new IllegalArgumentException("Org not found"));
            warehouse.setOrg(org);
        }

        return warehouseRepository.save(warehouse);
    }
}
