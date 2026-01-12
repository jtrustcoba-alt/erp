package com.erp.masterdata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.repository.CompanyRepository;
import com.erp.masterdata.entity.Currency;
import com.erp.masterdata.entity.PriceList;
import com.erp.masterdata.repository.CurrencyRepository;
import com.erp.masterdata.repository.PriceListRepository;

@Service
public class PriceListService {

    private final PriceListRepository priceListRepository;
    private final CompanyRepository companyRepository;
    private final CurrencyRepository currencyRepository;

    public PriceListService(PriceListRepository priceListRepository, CompanyRepository companyRepository, CurrencyRepository currencyRepository) {
        this.priceListRepository = priceListRepository;
        this.companyRepository = companyRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<PriceList> listByCompany(Long companyId) {
        return priceListRepository.findByCompanyId(companyId);
    }

    @Transactional
    public PriceList create(Long companyId, Long currencyId, PriceList priceList) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new IllegalArgumentException("Currency not found"));

        priceList.setCompany(company);
        priceList.setCurrency(currency);

        return priceListRepository.save(priceList);
    }
}
