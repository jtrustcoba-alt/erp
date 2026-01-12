package com.erp.sales.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.sales.dto.SalesOrderDto;
import com.erp.sales.dto.SalesOrderLineDto;
import com.erp.sales.entity.SalesOrder;
import com.erp.sales.entity.SalesOrderLine;
import com.erp.sales.request.CreateSalesOrderRequest;
import com.erp.sales.service.SalesOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales/companies/{companyId}/sales-orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderDto>> list(@PathVariable Long companyId) {
        List<SalesOrderDto> result = salesOrderService.listByCompany(companyId).stream().map(this::toDto).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<SalesOrderDto> create(@PathVariable Long companyId, @Valid @RequestBody CreateSalesOrderRequest request) {
        SalesOrder saved = salesOrderService.create(companyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    private SalesOrderDto toDto(SalesOrder so) {
        SalesOrderDto dto = new SalesOrderDto();
        dto.setId(so.getId());
        dto.setCompanyId(so.getCompany() != null ? so.getCompany().getId() : null);
        dto.setOrgId(so.getOrg() != null ? so.getOrg().getId() : null);
        dto.setBusinessPartnerId(so.getBusinessPartner() != null ? so.getBusinessPartner().getId() : null);
        dto.setPriceListVersionId(so.getPriceListVersion() != null ? so.getPriceListVersion().getId() : null);
        dto.setDocumentNo(so.getDocumentNo());
        dto.setStatus(so.getStatus());
        dto.setOrderDate(so.getOrderDate());
        dto.setTotalNet(so.getTotalNet());
        dto.setTotalTax(so.getTotalTax());
        dto.setGrandTotal(so.getGrandTotal());
        dto.setLines(so.getLines() != null ? so.getLines().stream().map(this::toLineDto).toList() : List.of());
        return dto;
    }

    private SalesOrderLineDto toLineDto(SalesOrderLine line) {
        SalesOrderLineDto dto = new SalesOrderLineDto();
        dto.setId(line.getId());
        dto.setProductId(line.getProduct() != null ? line.getProduct().getId() : null);
        dto.setUomId(line.getUom() != null ? line.getUom().getId() : null);
        dto.setQty(line.getQty());
        dto.setPrice(line.getPrice());
        dto.setLineNet(line.getLineNet());
        dto.setShippedQty(line.getShippedQty());
        return dto;
    }
}
