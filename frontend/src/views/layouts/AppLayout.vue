<template>
  <el-container style="min-height: 100vh">
    <el-aside width="260px" style="border-right: 1px solid #ebeef5">
      <div style="height: 56px; display: flex; align-items: center; padding: 0 16px; font-weight: 600">
        ERP
      </div>
      <el-menu :default-active="active" router style="border-right: none">
        <el-menu-item index="/">Dashboard</el-menu-item>
        <el-sub-menu index="/modules">
          <template #title>Modules</template>
          <el-sub-menu index="/modules/core">
            <template #title>Core Setup</template>
            <el-menu-item index="/modules/core/companies">Companies</el-menu-item>
            <el-menu-item index="/modules/core/orgs">Organizations</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/modules/masterdata">
            <template #title>Master Data</template>
            <el-menu-item index="/modules/masterdata/uoms">UoM</el-menu-item>
            <el-menu-item index="/modules/masterdata/business-partners">Business Partners</el-menu-item>
            <el-menu-item index="/modules/masterdata/currencies">Currencies</el-menu-item>
            <el-menu-item index="/modules/masterdata/products">Products</el-menu-item>
            <el-menu-item index="/modules/masterdata/price-lists">Price Lists</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/modules/purchase">
            <template #title>Purchase</template>
            <el-menu-item index="/modules/purchase/purchase-orders">Purchase Orders</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/modules/sales">
            <template #title>Sales</template>
            <el-menu-item index="/modules/sales/sales-orders">Sales Orders</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/modules/inventory">Inventory</el-menu-item>
          <el-menu-item index="/modules/manufacturing">Manufacturing</el-menu-item>
          <el-menu-item index="/modules/finance">Finance</el-menu-item>
          <el-menu-item index="/modules/hr">HR</el-menu-item>
          <el-menu-item index="/modules/admin">Admin</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="/tools">
          <template #title>Tools</template>
          <el-menu-item index="/tools/api-explorer">API Explorer</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #ebeef5">
        <div />
        <div style="display: flex; gap: 8px; align-items: center">
          <el-tag v-if="auth.role" type="info">{{ auth.role }}</el-tag>
          <el-button size="small" @click="onLogout">Logout</el-button>
        </div>
      </el-header>

      <el-main style="background: #f5f7fa">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const active = computed(() => route.path)

function onLogout() {
  auth.logout()
  router.push('/login')
}
</script>
