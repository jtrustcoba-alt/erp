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

import com.erp.masterdata.entity.Product;
import com.erp.masterdata.service.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/masterdata/companies/{companyId}/products")
public class MasterDataProductController {

    private final ProductService productService;

    public MasterDataProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list(@PathVariable Long companyId) {
        return ResponseEntity.ok(productService.listByCompany(companyId));
    }

    @PostMapping
    public ResponseEntity<Product> create(@PathVariable Long companyId, @Valid @RequestBody CreateProductRequest request) {
        Product p = new Product();
        p.setCode(request.getCode());
        p.setName(request.getName());
        p.setActive(true);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(companyId, request.getUomId(), p));
    }

    public static class CreateProductRequest {
        @NotBlank
        private String code;

        @NotBlank
        private String name;

        @NotNull
        private Long uomId;

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

        public Long getUomId() {
            return uomId;
        }

        public void setUomId(Long uomId) {
            this.uomId = uomId;
        }
    }
}
