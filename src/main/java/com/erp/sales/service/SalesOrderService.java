package com.erp.sales.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.core.entity.Company;
import com.erp.core.entity.Org;
import com.erp.core.model.DocumentType;
import com.erp.core.repository.CompanyRepository;
import com.erp.core.repository.OrgRepository;
import com.erp.core.service.DocumentNoService;
import com.erp.masterdata.entity.BusinessPartner;
import com.erp.masterdata.entity.PriceListVersion;
import com.erp.masterdata.entity.Product;
import com.erp.masterdata.entity.ProductPrice;
import com.erp.masterdata.repository.BusinessPartnerRepository;
import com.erp.masterdata.repository.PriceListVersionRepository;
import com.erp.masterdata.repository.ProductPriceRepository;
import com.erp.masterdata.repository.ProductRepository;
import com.erp.sales.entity.SalesOrder;
import com.erp.sales.entity.SalesOrderLine;
import com.erp.sales.repository.SalesOrderRepository;
import com.erp.sales.request.CreateSalesOrderRequest;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CompanyRepository companyRepository;
    private final OrgRepository orgRepository;
    private final BusinessPartnerRepository businessPartnerRepository;
    private final PriceListVersionRepository priceListVersionRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final DocumentNoService documentNoService;

    public SalesOrderService(
            SalesOrderRepository salesOrderRepository,
            CompanyRepository companyRepository,
            OrgRepository orgRepository,
            BusinessPartnerRepository businessPartnerRepository,
            PriceListVersionRepository priceListVersionRepository,
            ProductRepository productRepository,
            ProductPriceRepository productPriceRepository,
            DocumentNoService documentNoService) {
        this.salesOrderRepository = salesOrderRepository;
        this.companyRepository = companyRepository;
        this.orgRepository = orgRepository;
        this.businessPartnerRepository = businessPartnerRepository;
        this.priceListVersionRepository = priceListVersionRepository;
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
        this.documentNoService = documentNoService;
    }

    public List<SalesOrder> listByCompany(Long companyId) {
        return salesOrderRepository.findByCompanyId(companyId);
    }

    @Transactional
    public SalesOrder create(Long companyId, CreateSalesOrderRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Org org = null;
        if (request.getOrgId() != null) {
            org = orgRepository.findById(request.getOrgId())
                    .orElseThrow(() -> new IllegalArgumentException("Org not found"));
        }

        BusinessPartner bp = businessPartnerRepository.findById(request.getBusinessPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("BusinessPartner not found"));

        PriceListVersion plv = priceListVersionRepository.findById(request.getPriceListVersionId())
                .orElseThrow(() -> new IllegalArgumentException("PriceListVersion not found"));

        SalesOrder so = new SalesOrder();
        so.setCompany(company);
        so.setOrg(org);
        so.setBusinessPartner(bp);
        so.setPriceListVersion(plv);
        so.setOrderDate(request.getOrderDate());
        so.setDocumentNo(documentNoService.nextDocumentNo(companyId, DocumentType.SALES_ORDER));

        List<SalesOrderLine> lines = new ArrayList<>();
        BigDecimal totalNet = BigDecimal.ZERO;

        for (CreateSalesOrderRequest.CreateSalesOrderLineRequest lineReq : request.getLines()) {
            Product product = productRepository.findById(lineReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            ProductPrice productPrice = productPriceRepository
                    .findByPriceListVersion_IdAndProduct_Id(plv.getId(), product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product price not found for productId=" + product.getId()));

            BigDecimal qty = lineReq.getQty();
            BigDecimal price = productPrice.getPrice();
            BigDecimal lineNet = price.multiply(qty);

            SalesOrderLine sol = new SalesOrderLine();
            sol.setSalesOrder(so);
            sol.setProduct(product);
            sol.setUom(product.getUom());
            sol.setQty(qty);
            sol.setPrice(price);
            sol.setLineNet(lineNet);

            lines.add(sol);
            totalNet = totalNet.add(lineNet);
        }

        so.setLines(lines);
        so.setTotalNet(totalNet);
        so.setTotalTax(BigDecimal.ZERO);
        so.setGrandTotal(totalNet);

        return salesOrderRepository.save(so);
    }
}
