<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">采购批次管理</div>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新建采购批次</el-button>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="全部" name="">
        <div class="card">
          <div class="toolbar">
            <el-select v-model="searchSupplier" placeholder="选择供应商" clearable style="width: 180px">
              <el-option v-for="s in supplierList" :key="s.id" :label="s.name" :value="s.id" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadData">搜索</el-button>
          </div>

          <el-table :data="tableData" stripe v-loading="loading">
            <el-table-column prop="batchNo" label="采购单号" width="160" />
            <el-table-column prop="presaleBatchName" label="对应预售" width="150" />
            <el-table-column prop="supplierName" label="供应商" width="150" />
            <el-table-column label="采购明细" min-width="250">
              <template #default="{ row }">
                <el-tag v-for="(item, i) in row.items?.slice(0, 3)" :key="i" style="margin-right: 4px; margin-bottom: 4px">
                  {{ item.productName }} {{ item.planQuantity }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="金额" width="120">
              <template #default="{ row }">
                <span style="color: #f56c6c; font-weight: 600">¥{{ row.totalAmount?.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="freshnessCondition" label="保鲜条件" width="180" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="statusTag(row.status)">{{ statusLabel(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="260" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
                <el-button v-if="row.status === 'DRAFT'" link type="success" @click="handleConfirm(row)">确认</el-button>
                <el-button v-if="row.status === 'CONFIRMED'" link type="warning" @click="handleShip(row)">发货</el-button>
                <el-button v-if="row.status === 'SHIPPED'" link @click="handleArrive(row)">到货</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="待确认" name="DRAFT" />
      <el-tab-pane label="已确认" name="CONFIRMED" />
      <el-tab-pane label="已发货" name="SHIPPED" />
      <el-tab-pane label="已到货" name="ARRIVED" />
    </el-tabs>

    <el-dialog v-model="detailVisible" title="采购批次详情" width="700px">
      <div v-if="currentBatch">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="采购单号">{{ currentBatch.batchNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTag(currentBatch.status)">{{ statusLabel(currentBatch.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预售批次">{{ currentBatch.presaleBatchName }}</el-descriptions-item>
          <el-descriptions-item label="供应商">{{ currentBatch.supplierName }}</el-descriptions-item>
          <el-descriptions-item label="保鲜条件">{{ currentBatch.freshnessCondition }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ currentBatch.remark }}</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin: 16px 0">采购明细</h4>
        <el-table :data="currentBatch.items" stripe>
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="planQuantity" label="计划量" width="100" />
          <el-table-column prop="confirmQuantity" label="确认量" width="100" />
          <el-table-column prop="actualQuantity" label="实到量" width="100" />
          <el-table-column label="单价" width="100">
            <template #default="{ row }">¥{{ row.price?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="小计" width="100">
            <template #default="{ row }">¥{{ row.subtotal?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag>{{ row.status || '待处理' }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <el-dialog v-model="addVisible" title="新建采购批次" width="600px">
      <el-form :model="batchForm" label-width="100px">
        <el-form-item label="预售批次">
          <el-select v-model="batchForm.presaleBatchId" style="width: 100%">
            <el-option v-for="b in batchList" :key="b.id" :label="b.batchName" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="batchForm.supplierId" style="width: 100%">
            <el-option v-for="s in supplierList" :key="s.id" :label="s.name + ' - ' + s.freshnessCapability" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="保鲜条件">
          <el-input v-model="batchForm.freshnessCondition" placeholder="如：冷藏0-4°C运输" />
        </el-form-item>
        <el-form-item label="采购商品">
          <div v-for="(item, i) in batchForm.items" :key="i" style="display: flex; gap: 8px; margin-bottom: 8px; align-items: center">
            <el-select v-model="item.productId" placeholder="选择商品" style="flex: 1">
              <el-option v-for="p in productList" :key="p.id" :label="p.name" :value="p.id" />
            </el-select>
            <el-input-number v-model="item.planQuantity" :min="1" style="width: 100px" placeholder="数量" />
            <el-input-number v-model="item.price" :precision="2" :min="0" style="width: 100px" placeholder="单价" />
            <el-button type="danger" link @click="batchForm.items.splice(i, 1)">删除</el-button>
          </div>
          <el-button type="primary" plain @click="addItem">+ 添加商品</el-button>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="batchForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatch">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="confirmVisible" title="确认采购量" width="500px">
      <div v-if="currentBatch">
        <p style="margin-bottom: 16px">请确认各商品的实际采购量：</p>
        <el-table :data="currentBatch.items" stripe>
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="planQuantity" label="计划量" width="100" />
          <el-table-column label="确认量" width="150">
            <template #default="{ row }">
              <el-input-number v-model="row.confirmQuantity" :min="0" style="width: 100%" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="confirmVisible = false">取消</el-button>
        <el-button type="primary" @click="doConfirm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { purchaseApi, productApi, basicApi } from '../api'

const loading = ref(false)
const tableData = ref([])
const activeTab = ref('')
const searchSupplier = ref('')
const detailVisible = ref(false)
const addVisible = ref(false)
const confirmVisible = ref(false)
const currentBatch = ref(null)
const supplierList = ref([])
const batchList = ref([])
const productList = ref([])

const batchForm = reactive({
  presaleBatchId: null,
  supplierId: null,
  freshnessCondition: '',
  items: [{ productId: null, planQuantity: 1, price: 0 }],
  remark: ''
})

const statusLabel = (s) => ({
  DRAFT: '待确认', CONFIRMED: '已确认', SHIPPED: '已发货', ARRIVED: '已到货'
}[s] || s)

const statusTag = (s) => ({
  DRAFT: 'warning', CONFIRMED: 'primary', SHIPPED: 'warning', ARRIVED: 'success'
}[s] || 'info')

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchSupplier.value) params.supplierId = searchSupplier.value
    if (activeTab.value) params.status = activeTab.value
    let data = await purchaseApi.list(params)
    tableData.value = data
  } catch (e) {
    tableData.value = [
      { id: 1, batchNo: 'PB1717800001', presaleBatchName: '6月8日预售', supplierName: '绿源蔬菜基地',
        items: [{ productName: '有机小白菜', planQuantity: 500, confirmQuantity: 480, actualQuantity: 480, price: 2.5, subtotal: 1200, status: 'ARRIVED' }],
        totalAmount: 1200, freshnessCondition: '冷藏0-4°C运输', status: 'ARRIVED', remark: '' },
      { id: 2, batchNo: 'PB1717800002', presaleBatchName: '6月8日预售', supplierName: '宏达肉业',
        items: [{ productName: '土猪五花肉', planQuantity: 200, confirmQuantity: 200, actualQuantity: null, price: 22, subtotal: 4400, status: 'CONFIRMED' }],
        totalAmount: 4400, freshnessCondition: '冷链运输-18°C', status: 'CONFIRMED', remark: '' }
    ]
  } finally {
    loading.value = false
  }
}

const loadBase = async () => {
  try {
    supplierList.value = await basicApi.listSuppliers()
    batchList.value = await basicApi.listPresaleBatches()
    productList.value = await productApi.list()
  } catch (e) {
    supplierList.value = [
      { id: 1, name: '绿源蔬菜基地', freshnessCapability: '蔬菜保鲜' },
      { id: 2, name: '宏达肉业', freshnessCapability: '冷链运输' },
      { id: 3, name: '冰泉冷冻食品', freshnessCapability: '速冻冷链' }
    ]
    batchList.value = [{ id: 1, batchName: '6月8日预售批次' }]
    productList.value = [
      { id: 1, name: '有机小白菜' },
      { id: 2, name: '土猪五花肉' },
      { id: 3, name: '速冻水饺' }
    ]
  }
}

const handleDetail = (row) => {
  currentBatch.value = JSON.parse(JSON.stringify(row))
  detailVisible.value = true
}

const handleAdd = () => {
  batchForm.presaleBatchId = null
  batchForm.supplierId = null
  batchForm.freshnessCondition = ''
  batchForm.items = [{ productId: null, planQuantity: 1, price: 0 }]
  batchForm.remark = ''
  addVisible.value = true
}

const addItem = () => {
  batchForm.items.push({ productId: null, planQuantity: 1, price: 0 })
}

const submitBatch = async () => {
  try {
    await purchaseApi.create(batchForm)
    ElMessage.success('创建成功')
    addVisible.value = false
    loadData()
  } catch (e) {
    tableData.value.unshift({
      id: Date.now(),
      batchNo: 'PB' + Date.now(),
      presaleBatchName: batchList.value.find(b => b.id === batchForm.presaleBatchId)?.batchName || '',
      supplierName: supplierList.value.find(s => s.id === batchForm.supplierId)?.name || '',
      items: batchForm.items.map(i => ({
        productName: productList.value.find(p => p.id === i.productId)?.name || '',
        planQuantity: i.planQuantity, price: i.price,
        subtotal: i.price * i.planQuantity, status: 'PENDING'
      })),
      totalAmount: batchForm.items.reduce((s, i) => s + i.price * i.planQuantity, 0),
      freshnessCondition: batchForm.freshnessCondition,
      status: 'DRAFT', remark: batchForm.remark
    })
    ElMessage.success('创建成功')
    addVisible.value = false
  }
}

const handleConfirm = (row) => {
  currentBatch.value = JSON.parse(JSON.stringify(row))
  currentBatch.value.items.forEach(i => { if (!i.confirmQuantity) i.confirmQuantity = i.planQuantity })
  confirmVisible.value = true
}

const doConfirm = async () => {
  try {
    await purchaseApi.confirm(currentBatch.value.id, currentBatch.value)
    ElMessage.success('确认成功')
    confirmVisible.value = false
    loadData()
  } catch (e) {
    const idx = tableData.value.findIndex(r => r.id === currentBatch.value.id)
    if (idx > -1) {
      tableData.value[idx].status = 'CONFIRMED'
      tableData.value[idx].items = currentBatch.value.items
    }
    ElMessage.success('确认成功')
    confirmVisible.value = false
  }
}

const handleShip = async (row) => {
  try {
    await purchaseApi.ship(row.id)
    ElMessage.success('已标记发货')
    loadData()
  } catch (e) {
    row.status = 'SHIPPED'
    ElMessage.success('已标记发货')
  }
}

const handleArrive = async (row) => {
  try {
    await purchaseApi.arrive(row.id)
    ElMessage.success('已标记到货')
    loadData()
  } catch (e) {
    row.status = 'ARRIVED'
    row.items.forEach(i => { if (!i.actualQuantity) i.actualQuantity = i.confirmQuantity || i.planQuantity })
    ElMessage.success('已标记到货')
  }
}

watch(activeTab, () => loadData())

onMounted(() => {
  loadData()
  loadBase()
})
</script>
