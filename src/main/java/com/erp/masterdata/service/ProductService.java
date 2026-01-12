package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.repository.CompanyRepository;
import com.erp.masterdata.entity.Product;
import com.erp.masterdata.entity.Uom;
import com.erp.masterdata.repository.ProductRepository;
import com.erp.masterdata.repository.UomRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final UomRepository uomRepository;

    public ProductService(ProductRepository productRepository, CompanyRepository companyRepository, UomRepository uomRepository) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
        this.uomRepository = uomRepository;
    }

    public List<Product> listByCompany(Long companyId) {
        return productRepository.findByCompanyId(companyId);
    }

    @Transactional
    public Product create(Long companyId, Long uomId, Product product) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        Uom uom = uomRepository.findById(uomId)
                .orElseThrow(() -> new IllegalArgumentException("UOM not found"));

        product.setCompany(company);
        product.setUom(uom);

        return productRepository.save(product);
    }
}
