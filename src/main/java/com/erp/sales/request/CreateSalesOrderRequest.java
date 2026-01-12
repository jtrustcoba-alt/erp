package com.erp.sales.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateSalesOrderRequest {

    private Long orgId;

    @NotNull
    private Long businessPartnerId;

    @NotNull
    private Long priceListVersionId;

    @NotNull
    private LocalDate orderDate;

    @Valid
    @NotNull
    private List<CreateSalesOrderLineRequest> lines;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getBusinessPartnerId() {
        return businessPartnerId;
    }

    public void setBusinessPartnerId(Long businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    public Long getPriceListVersionId() {
        return priceListVersionId;
    }

    public void setPriceListVersionId(Long priceListVersionId) {
        this.priceListVersionId = priceListVersionId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<CreateSalesOrderLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<CreateSalesOrderLineRequest> lines) {
        this.lines = lines;
    }

    public static class CreateSalesOrderLineRequest {
        @NotNull
        private Long productId;

        @NotNull
        private java.math.BigDecimal qty;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public java.math.BigDecimal getQty() {
            return qty;
        }

        public void setQty(java.math.BigDecimal qty) {
            this.qty = qty;
        }
    }
}
