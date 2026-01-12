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

import com.erp.masterdata.entity.Uom;
import com.erp.masterdata.service.UomService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/masterdata/companies/{companyId}/uoms")
public class MasterDataUomController {

    private final UomService uomService;

    public MasterDataUomController(UomService uomService) {
        this.uomService = uomService;
    }

    @GetMapping
    public ResponseEntity<List<Uom>> list(@PathVariable Long companyId) {
        return ResponseEntity.ok(uomService.listByCompany(companyId));
    }

    @PostMapping
    public ResponseEntity<Uom> create(@PathVariable Long companyId, @Valid @RequestBody CreateUomRequest request) {
        Uom uom = new Uom();
        uom.setCode(request.getCode());
        uom.setName(request.getName());
        uom.setActive(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(uomService.create(companyId, uom));
    }

    public static class CreateUomRequest {
        @NotBlank
        private String code;

        @NotBlank
        private String name;

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
    }
}
