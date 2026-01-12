package com.erp.masterdata.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.masterdata.entity.Currency;
import com.erp.masterdata.service.CurrencyService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/masterdata/companies/{companyId}/currencies")
public class MasterDataCurrencyController {

    private final CurrencyService currencyService;

    public MasterDataCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> list(@PathVariable Long companyId) {
        return ResponseEntity.ok(currencyService.listByCompany(companyId));
    }

    @PostMapping
    public ResponseEntity<Currency> create(@PathVariable Long companyId, @Valid @RequestBody CreateCurrencyRequest request) {
        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setName(request.getName());
        currency.setPrecisionValue(request.getPrecisionValue());
        currency.setActive(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.create(companyId, currency));
    }

    public static class CreateCurrencyRequest {
        @NotBlank
        private String code;

        @NotBlank
        private String name;

        @Min(0)
        private int precisionValue = 2;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrecisionValue() {
            return precisionValue;
        }

        public void setPrecisionValue(int precisionValue) {
            this.precisionValue = precisionValue;
        }
    }
}
