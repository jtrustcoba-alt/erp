<template>
  <div style="display: flex; flex-direction: column; gap: 12px">
    <el-card>
      <div style="display: flex; flex-direction: column; gap: 10px">
        <div style="display: flex; align-items: center; justify-content: space-between; gap: 12px">
          <div>
            <div style="font-size: 18px; font-weight: 600">Sales Orders</div>
            <div style="color: #606266">Buat SO untuk customer, lalu lanjut Goods Shipment.</div>
          </div>
          <el-button type="primary" :disabled="!ctx.companyId" @click="openCreate">Create SO</el-button>
        </div>

        <CompanyOrgBar />
      </div>
    </el-card>

    <el-card>
      <el-alert v-if="!ctx.companyId" title="Pilih company dulu." type="warning" show-icon style="margin-bottom: 12px" />

      <el-table :data="rows" style="width: 100%" v-loading="loading">
        <el-table-column prop="documentNo" label="Document No" width="180" />
        <el-table-column prop="status" label="Status" width="140" />
        <el-table-column prop="orderDate" label="Order Date" width="140" />
        <el-table-column prop="grandTotal" label="Grand Total" width="140" />
        <el-table-column label="Action" width="200">
          <template #default="scope">
            <div style="display: flex; gap: 8px">
              <el-button size="small" :disabled="!canEditRow(scope.row)" @click="openEdit(scope.row)">Edit</el-button>
              <el-button size="small" type="danger" plain :disabled="!canEditRow(scope.row)" @click="onDelete(scope.row)">Delete</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="createOpen" :title="editMode === 'edit' ? 'Edit Sales Order' : 'Create Sales Order'" width="860px">
      <el-form label-position="top">
        <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px">
          <el-form-item label="Org">
            <el-input v-model="form.orgId" placeholder="Auto from context" :disabled="true" />
          </el-form-item>
          <el-form-item label="Customer">
            <el-select v-model="form.businessPartnerId" filterable placeholder="Select customer" style="width: 100%" :disabled="customers.length === 0">
              <el-option v-for="c in customers" :key="c.id" :label="c.name || c.code || String(c.id)" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="Price List Version">
            <el-select v-model="form.priceListVersionId" filterable placeholder="Select version" style="width: 100%" :disabled="priceListVersions.length === 0">
              <el-option v-for="p in priceListVersions" :key="p.id" :label="p.name || p.code || String(p.id)" :value="p.id" />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="Order Date">
          <el-date-picker v-model="form.orderDate" type="date" value-format="YYYY-MM-DD" style="width: 220px" />
        </el-form-item>

        <div style="display: flex; align-items: center; justify-content: space-between; margin: 12px 0 6px">
          <div style="font-weight: 600">Lines</div>
          <el-button size="small" @click="addLine">Add Line</el-button>
        </div>

        <el-table :data="form.lines" size="small" style="width: 100%">
          <el-table-column label="Product" min-width="320">
            <template #default="scope">
              <el-select v-model="scope.row.productId" filterable placeholder="Select product" style="width: 100%" :disabled="products.length === 0">
                <el-option v-for="p in products" :key="p.id" :label="`${p.code} - ${p.name}`" :value="p.id" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="Qty" width="180">
            <template #default="scope">
              <el-input v-model="scope.row.qty" />
            </template>
          </el-table-column>
          <el-table-column label="" width="70">
            <template #default="scope">
              <el-button type="danger" plain size="small" @click="removeLine(scope.$index)">X</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-alert v-if="customers.length === 0" title="Customer belum ada. Buat Business Partner (customer) dulu di Master Data." type="warning" show-icon style="margin-top: 12px" />
        <el-alert v-if="products.length === 0" title="Product belum ada. Buat Product dulu di Master Data." type="warning" show-icon style="margin-top: 12px" />
      </el-form>

      <template #footer>
        <el-button @click="createOpen = false">Cancel</el-button>
        <el-button type="primary" :loading="saving" :disabled="!canSave" @click="save">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { masterDataApi, salesApi } from '@/utils/api'
import { useContextStore } from '@/stores/context'
import CompanyOrgBar from '@/views/modules/common/CompanyOrgBar.vue'

const ctx = useContextStore()

const rows = ref([])
const loading = ref(false)

const customers = ref([])
const products = ref([])
const priceLists = ref([])
const priceListVersions = ref([])

const createOpen = ref(false)
const saving = ref(false)
const editMode = ref('create')
const editingId = ref(null)

const form = reactive({
  orgId: null,
  businessPartnerId: null,
  priceListVersionId: null,
  orderDate: '',
  lines: []
})

const canSave = computed(() => {
  if (!ctx.companyId) return false
  if (!form.businessPartnerId) return false
  if (!form.priceListVersionId) return false
  if (!form.orderDate) return false
  if (!Array.isArray(form.lines) || form.lines.length === 0) return false
  return form.lines.every((l) => l.productId && String(l.qty).trim())
})

function addLine() {
  form.lines.push({ productId: products.value[0]?.id || null, qty: '1' })
}

function removeLine(idx) {
  form.lines.splice(idx, 1)
}

async function loadLookups() {
  if (!ctx.companyId) {
    customers.value = []
    products.value = []
    priceLists.value = []
    priceListVersions.value = []
    return
  }

  const [bps, prods, pls] = await Promise.all([
    masterDataApi.listBusinessPartners(ctx.companyId),
    masterDataApi.listProducts(ctx.companyId),
    masterDataApi.listPriceLists(ctx.companyId)
  ])

  customers.value = (bps || []).filter((x) => x.type === 'CUSTOMER' || x.type === 'BOTH')
  products.value = prods || []
  priceLists.value = pls || []

  const plId = priceLists.value[0]?.id
  priceListVersions.value = plId ? await masterDataApi.listPriceListVersions(plId) : []
}

async function load() {
  if (!ctx.companyId) {
    rows.value = []
    return
  }
  loading.value = true
  try {
    rows.value = await salesApi.listSalesOrders(ctx.companyId)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editMode.value = 'create'
  editingId.value = null
  form.orgId = ctx.orgId
  form.businessPartnerId = customers.value[0]?.id || null
  form.priceListVersionId = priceListVersions.value[0]?.id || null
  form.orderDate = new Date().toISOString().slice(0, 10)
  form.lines = []
  addLine()
  createOpen.value = true
}

function canEditRow(row) {
  if (!ctx.companyId) return false
  if (!row?.id) return false
  return row.status === 'DRAFTED'
}

function openEdit(row) {
  if (!row?.id) return
  editMode.value = 'edit'
  editingId.value = row.id

  form.orgId = row.orgId || ctx.orgId
  form.businessPartnerId = row.businessPartnerId || customers.value[0]?.id || null
  form.priceListVersionId = row.priceListVersionId || priceListVersions.value[0]?.id || null
  form.orderDate = row.orderDate || new Date().toISOString().slice(0, 10)

  const lines = Array.isArray(row.lines) ? row.lines : []
  form.lines = lines.length
    ? lines.map((l) => ({
        productId: l.productId,
        qty: String(l.qty ?? '')
      }))
    : []

  if (!form.lines.length) addLine()
  createOpen.value = true
}

async function save() {
  saving.value = true
  try {
    const payload = {
      orgId: form.orgId || null,
      businessPartnerId: form.businessPartnerId,
      priceListVersionId: form.priceListVersionId,
      orderDate: form.orderDate,
      lines: form.lines.map((l) => ({
        productId: l.productId,
        qty: l.qty
      }))
    }

    if (editMode.value === 'edit' && editingId.value) {
      await salesApi.updateSalesOrder(ctx.companyId, editingId.value, payload)
      ElMessage.success('Sales Order updated')
    } else {
      await salesApi.createSalesOrder(ctx.companyId, payload)
      ElMessage.success('Sales Order created')
    }
    createOpen.value = false
    await load()
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || e?.message || 'Failed')
  } finally {
    saving.value = false
  }
}

async function onDelete(row) {
  if (!row?.id) return
  try {
    await ElMessageBox.confirm(`Delete Sales Order "${row.documentNo}"?`, 'Confirm', {
      type: 'warning',
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel'
    })
    await salesApi.deleteSalesOrder(ctx.companyId, row.id)
    ElMessage.success('Sales Order deleted')
    await load()
  } catch (e) {
    if (e === 'cancel' || e === 'close') return
    ElMessage.error(e?.response?.data?.message || e?.message || 'Failed')
  }
}

watch(
  () => ctx.companyId,
  async () => {
    await loadLookups()
    await load()
  }
)

watch(
  () => ctx.orgId,
  () => {
    form.orgId = ctx.orgId
  }
)

onMounted(async () => {
  await loadLookups()
  await load()
})
</script>
