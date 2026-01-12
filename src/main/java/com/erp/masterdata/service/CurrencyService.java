package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.repository.CompanyRepository;
import com.erp.masterdata.entity.Currency;
import com.erp.masterdata.repository.CurrencyRepository;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CompanyRepository companyRepository;

    public CurrencyService(CurrencyRepository currencyRepository, CompanyRepository companyRepository) {
        this.currencyRepository = currencyRepository;
        this.companyRepository = companyRepository;
    }

    public List<Currency> listByCompany(Long companyId) {
        return currencyRepository.findByCompanyId(companyId);
    }

    @Transactional
    public Currency create(Long companyId, Currency currency) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        currency.setCompany(company);
        return currencyRepository.save(currency);
    }
}
