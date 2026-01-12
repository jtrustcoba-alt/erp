export const modules = [
  {
    id: 'core',
    title: 'Core Setup',
    description: 'Setup perusahaan, organisasi, dan penomoran dokumen.',
    flow: ['Create Company', 'Create Org', 'Setup Document Sequence'],
    pages: [
      { title: 'Companies', description: 'Buat dan pilih company', to: '/modules/core/companies' },
      { title: 'Organizations', description: 'Buat dan pilih org per company', to: '/modules/core/orgs' }
    ],
    match: ['/api/core/']
  },
  {
    id: 'masterdata',
    title: 'Master Data',
    description: 'Data master untuk transaksi: Product, BP, Warehouse, UoM, Currency, Tax, Price List.',
    flow: ['Create UoM', 'Create Warehouse', 'Create Product', 'Create Business Partner', 'Create Tax Rate', 'Create Price List'],
    pages: [
      { title: 'UoM', description: 'Satuan unit (PCS, KG, LTR)', to: '/modules/masterdata/uoms' },
      { title: 'Business Partners', description: 'Customer/Vendor', to: '/modules/masterdata/business-partners' },
      { title: 'Currencies', description: 'Mata uang untuk price list', to: '/modules/masterdata/currencies' },
      { title: 'Products', description: 'Buat produk untuk transaksi', to: '/modules/masterdata/products' },
      { title: 'Price Lists', description: 'Price list dan version', to: '/modules/masterdata/price-lists' }
    ],
    match: ['/api/masterdata/']
  },
  {
    id: 'purchase',
    title: 'Purchase',
    description: 'Alur pembelian dari Purchase Order sampai Goods Receipt.',
    flow: ['Create Purchase Order', 'Receive Goods (Goods Receipt)'],
    pages: [
      { title: 'Purchase Orders', description: 'Buat dan lihat PO', to: '/modules/purchase/purchase-orders' }
    ],
    match: ['/api/purchase/']
  },
  {
    id: 'sales',
    title: 'Sales',
    description: 'Alur penjualan dari Sales Order sampai Goods Shipment.',
    flow: ['Create Sales Order', 'Ship Goods (Goods Shipment)'],
    pages: [
      { title: 'Sales Orders', description: 'Buat dan lihat SO', to: '/modules/sales/sales-orders' }
    ],
    match: ['/api/sales/']
  },
  {
    id: 'inventory',
    title: 'Inventory',
    description: 'Manajemen stok: on-hand, locator, movement, adjustment.',
    flow: ['Setup Locator', 'Check On-hand', 'Inventory Movement', 'Inventory Adjustment (Complete/Void)'],
    pages: [],
    match: ['/api/inventory/']
  },
  {
    id: 'manufacturing',
    title: 'Manufacturing',
    description: 'BOM, Work Order, dan laporan produksi.',
    flow: ['Create BOM', 'Create Work Order', 'Complete/Void Work Order', 'Manufacturing Reports'],
    pages: [],
    match: ['/api/manufacturing/']
  },
  {
    id: 'finance',
    title: 'Finance',
    description: 'Accounting/Finance: GL Account, Journal, Invoice, Payment, Bank, Budget, Period, Reports.',
    flow: ['Setup Fiscal Year/Period', 'Seed GL Accounts', 'Post Invoice', 'Receive/Make Payment', 'Reconcile Bank Statement', 'Run Reports'],
    pages: [],
    match: ['/api/finance/']
  },
  {
    id: 'hr',
    title: 'HR',
    description: 'HR Operations: Employee, Department, Attendance, Leave, Performance, Payroll.',
    flow: ['Create Department', 'Create Employee', 'Mark Attendance', 'Leave Request & Approval', 'Performance Review', 'Payslip/Salary'],
    pages: [],
    match: ['/api/employees', '/api/departments', '/api/attendance', '/api/leaves', '/api/performance-reviews', '/api/payslips', '/api/salaries']
  },
  {
    id: 'admin',
    title: 'Admin',
    description: 'User management (khusus ADMIN).',
    flow: ['Create User', 'Update User', 'Reset Password', 'Change Status', 'List Users'],
    pages: [],
    match: ['/admin/']
  }
]

export function findModule(id) {
  return modules.find((m) => m.id === id) || null
}

export function matchEndpointToModule(endpointPath, moduleDef) {
  return (moduleDef?.match || []).some((prefix) => endpointPath.startsWith(prefix))
}
