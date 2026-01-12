import { http } from './http'

export const authApi = {
  signup: (payload) => http.post('/auth/signup', payload).then((r) => r.data),
  signin: (payload) => http.post('/auth/signin', payload).then((r) => r.data)
}

export const adminApi = {
  createUser: (payload) => http.post('/admin/create-user-management', payload).then((r) => r.data),
  updateUser: (id, payload) => http.put(`/admin/update-user/${id}`, payload).then((r) => r.data),
  resetPassword: (id, payload) => http.put(`/admin/reset-password/${id}`, payload).then((r) => r.data),
  changeStatus: (id) => http.put(`/admin/change-status/${id}`).then((r) => r.data),
  deleteUser: (id) => http.delete(`/admin/delete-user/${id}`).then((r) => r.data),
  listUsers: (params) => http.get('/admin/all-users', { params }).then((r) => r.data)
}

export const genericApi = {
  request: ({ method, url, params, data, headers }) =>
    http
      .request({ method, url, params, data, headers })
      .then((r) => ({ status: r.status, data: r.data, headers: r.headers }))
}

export const coreApi = {
  listCompanies: () => http.get('/api/core/companies').then((r) => r.data),
  createCompany: (payload) => http.post('/api/core/companies', payload).then((r) => r.data),
  listOrgs: (companyId) => http.get(`/api/core/companies/${companyId}/orgs`).then((r) => r.data),
  createOrg: (companyId, payload) => http.post(`/api/core/companies/${companyId}/orgs`, payload).then((r) => r.data)
}

export const masterDataApi = {
  listUoms: (companyId) => http.get(`/api/masterdata/companies/${companyId}/uoms`).then((r) => r.data),
  createUom: (companyId, payload) => http.post(`/api/masterdata/companies/${companyId}/uoms`, payload).then((r) => r.data),

  listCurrencies: (companyId) => http.get(`/api/masterdata/companies/${companyId}/currencies`).then((r) => r.data),
  createCurrency: (companyId, payload) => http.post(`/api/masterdata/companies/${companyId}/currencies`, payload).then((r) => r.data),

  listProducts: (companyId) => http.get(`/api/masterdata/companies/${companyId}/products`).then((r) => r.data),
  createProduct: (companyId, payload) => http.post(`/api/masterdata/companies/${companyId}/products`, payload).then((r) => r.data),

  listBusinessPartners: (companyId) => http.get(`/api/masterdata/companies/${companyId}/business-partners`).then((r) => r.data),
  createBusinessPartner: (companyId, payload) => http.post(`/api/masterdata/companies/${companyId}/business-partners`, payload).then((r) => r.data),

  listPriceLists: (companyId) => http.get(`/api/masterdata/companies/${companyId}/price-lists`).then((r) => r.data),
  createPriceList: (companyId, payload) => http.post(`/api/masterdata/companies/${companyId}/price-lists`, payload).then((r) => r.data),

  listPriceListVersions: (priceListId) => http.get(`/api/masterdata/price-lists/${priceListId}/versions`).then((r) => r.data),
  createPriceListVersion: (priceListId, payload) => http.post(`/api/masterdata/price-lists/${priceListId}/versions`, payload).then((r) => r.data),

  listProductPrices: (priceListVersionId) =>
    http.get(`/api/masterdata/price-list-versions/${priceListVersionId}/product-prices`).then((r) => r.data),
  upsertProductPrice: (priceListVersionId, payload) =>
    http.post(`/api/masterdata/price-list-versions/${priceListVersionId}/product-prices`, payload).then((r) => r.data)
}

export const purchaseApi = {
  listPurchaseOrders: (companyId) => http.get(`/api/purchase/companies/${companyId}/purchase-orders`).then((r) => r.data),
  createPurchaseOrder: (companyId, payload) => http.post(`/api/purchase/companies/${companyId}/purchase-orders`, payload).then((r) => r.data)
}

export const salesApi = {
  listSalesOrders: (companyId) => http.get(`/api/sales/companies/${companyId}/sales-orders`).then((r) => r.data),
  createSalesOrder: (companyId, payload) => http.post(`/api/sales/companies/${companyId}/sales-orders`, payload).then((r) => r.data)
}
