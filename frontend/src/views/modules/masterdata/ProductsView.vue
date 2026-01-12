<template>
  <div style="display: flex; flex-direction: column; gap: 12px">
    <el-card>
      <div style="display: flex; flex-direction: column; gap: 10px">
        <div style="display: flex; align-items: center; justify-content: space-between; gap: 12px">
          <div>
            <div style="font-size: 18px; font-weight: 600">Products</div>
            <div style="color: #606266">Master produk untuk transaksi Purchase/Sales/Manufacturing.</div>
          </div>
          <el-button type="primary" :disabled="!ctx.companyId" @click="openCreate">Create Product</el-button>
        </div>

        <CompanyOrgBar :show-org="false" />
      </div>
    </el-card>

    <el-card>
      <el-alert v-if="!ctx.companyId" title="Pilih company dulu." type="warning" show-icon style="margin-bottom: 12px" />

      <el-table :data="rows" style="width: 100%" v-loading="loading">
        <el-table-column prop="code" label="Code" width="160" />
        <el-table-column prop="name" label="Name" />
        <el-table-column prop="active" label="Active" width="100">
          <template #default="scope">
            <el-tag size="small" :type="scope.row.active ? 'success' : 'info'">{{ scope.row.active ? 'Yes' : 'No' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="createOpen" title="Create Product" width="600px">
      <el-form label-position="top">
        <el-form-item label="Code">
          <el-input v-model="form.code" placeholder="e.g. PRD-001" />
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="form.name" placeholder="e.g. Item A" />
        </el-form-item>
        <el-form-item label="UoM">
          <el-select v-model="form.uomId" filterable placeholder="Select UoM" style="width: 100%" :disabled="uoms.length === 0">
            <el-option v-for="u in uoms" :key="u.id" :label="u.name || u.code || String(u.id)" :value="u.id" />
          </el-select>
          <div v-if="uoms.length === 0" style="color: #909399; font-size: 12px; margin-top: 6px">
            UoM belum ada. Buat UoM dulu di Master Data.
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="createOpen = false">Cancel</el-button>
        <el-button type="primary" :loading="saving" :disabled="!ctx.companyId || !form.uomId" @click="save">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { masterDataApi } from '@/utils/api'
import { useContextStore } from '@/stores/context'
import CompanyOrgBar from '@/views/modules/common/CompanyOrgBar.vue'

const ctx = useContextStore()

const rows = ref([])
const uoms = ref([])
const loading = ref(false)

const createOpen = ref(false)
const saving = ref(false)

const form = reactive({
  code: '',
  name: '',
  uomId: null
})

async function load() {
  if (!ctx.companyId) {
    rows.value = []
    uoms.value = []
    return
  }
  loading.value = true
  try {
    ;[rows.value, uoms.value] = await Promise.all([masterDataApi.listProducts(ctx.companyId), masterDataApi.listUoms(ctx.companyId)])
  } finally {
    loading.value = false
  }
}

function openCreate() {
  form.code = ''
  form.name = ''
  form.uomId = uoms.value[0]?.id || null
  createOpen.value = true
}

async function save() {
  saving.value = true
  try {
    await masterDataApi.createProduct(ctx.companyId, { code: form.code, name: form.name, uomId: form.uomId })
    ElMessage.success('Product created')
    createOpen.value = false
    await load()
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || e?.message || 'Failed')
  } finally {
    saving.value = false
  }
}

watch(
  () => ctx.companyId,
  () => load()
)

onMounted(load)
</script>
