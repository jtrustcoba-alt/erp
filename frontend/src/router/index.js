import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

import AuthLayout from '@/views/layouts/AuthLayout.vue'
import AppLayout from '@/views/layouts/AppLayout.vue'

import LoginView from '@/views/auth/LoginView.vue'
import DashboardView from '@/views/DashboardView.vue'
import ModuleLandingView from '@/views/modules/ModuleLandingView.vue'
import CoreCompaniesView from '@/views/modules/core/CoreCompaniesView.vue'
import CoreOrgsView from '@/views/modules/core/CoreOrgsView.vue'
import UomsView from '@/views/modules/masterdata/UomsView.vue'
import BusinessPartnersView from '@/views/modules/masterdata/BusinessPartnersView.vue'
import CurrenciesView from '@/views/modules/masterdata/CurrenciesView.vue'
import ProductsView from '@/views/modules/masterdata/ProductsView.vue'
import PriceListsView from '@/views/modules/masterdata/PriceListsView.vue'
import PurchaseOrdersView from '@/views/modules/purchase/PurchaseOrdersView.vue'
import SalesOrdersView from '@/views/modules/sales/SalesOrdersView.vue'
import ApiExplorerView from '@/views/tools/ApiExplorerView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      component: AuthLayout,
      children: [{ path: '', name: 'login', component: LoginView }]
    },
    {
      path: '/',
      component: AppLayout,
      children: [
        { path: '', name: 'dashboard', component: DashboardView },
        { path: 'modules/:module', name: 'module-landing', component: ModuleLandingView },
        { path: 'modules/core/companies', name: 'core-companies', component: CoreCompaniesView },
        { path: 'modules/core/orgs', name: 'core-orgs', component: CoreOrgsView },
        { path: 'modules/masterdata/uoms', name: 'md-uoms', component: UomsView },
        { path: 'modules/masterdata/business-partners', name: 'md-business-partners', component: BusinessPartnersView },
        { path: 'modules/masterdata/currencies', name: 'md-currencies', component: CurrenciesView },
        { path: 'modules/masterdata/products', name: 'md-products', component: ProductsView },
        { path: 'modules/masterdata/price-lists', name: 'md-price-lists', component: PriceListsView },
        { path: 'modules/purchase/purchase-orders', name: 'purchase-orders', component: PurchaseOrdersView },
        { path: 'modules/sales/sales-orders', name: 'sales-orders', component: SalesOrdersView },
        { path: 'tools/api-explorer', name: 'api-explorer', component: ApiExplorerView }
      ]
    }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  const isPublic = to.path === '/login'
  if (!isPublic && !auth.isAuthenticated) return '/login'
  if (isPublic && auth.isAuthenticated) return '/'
  return true
})

export default router
