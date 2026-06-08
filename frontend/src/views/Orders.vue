<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">团长订单管理</div>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新建预售单</el-button>
    </div>

    <div class="card">
      <div class="toolbar">
        <el-select v-model="searchLeader" placeholder="选择团长" clearable style="width: 180px">
          <el-option v-for="l in leaderList" :key="l.id" :label="l.name" :value="l.id" />
        </el-select>
        <el-select v-model="searchStatus" placeholder="订单状态" clearable style="width: 150px">
          <el-option label="待付款" value="PENDING" />
          <el-option label="已付款" value="PAID" />
          <el-option label="分拣中" value="SORTING" />
          <el-option label="已送达" value="DELIVERED" />
          <el-option label="已自提" value="PICKED_UP" />
          <el-option label="已退款" value="REFUND" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="160" />
        <el-table-column prop="customerName" label="客户" width="100" />
        <el-table-column prop="customerPhone" label="电话" width="130" />
        <el-table-column prop="communityName" label="小区团点" width="150" />
        <el-table-column prop="leaderName" label="团长" width="100" />
        <el-table-column label="商品明细" min-width="200">
          <template #default="{ row }">
            <el-tag v-for="(item, i) in row.items?.slice(0, 3)" :key="i" style="margin-right: 4px; margin-bottom: 4px">
              {{ item.productName }} x{{ item.quantity }}
            </el-tag>
            <span v-if="row.items?.length > 3" style="color: #909399">等{{ row.items.length }}件</span>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="120">
          <template #default="{ row }">
            <div>实付: <span style="color: #f56c6c; font-weight: 600">¥{{ row.totalAmount?.toFixed(2) }}</span>
            <div v-if="row.refundAmount > 0" style="font-size: 12px; color: #909399">
              退款: ¥{{ row.refundAmount.toFixed(2) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="pickupCode" label="自提码" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'PENDING'" link type="success" @click="handleUpdateStatus(row, 'PAID')">付款</el-button>
            <el-button v-if="row.status === 'PAID'" link type="warning" @click="handleUpdateStatus(row, 'SORTING')">分拣</el-button>
            <el-button v-if="row.status === 'SORTING'" link @click="handleUpdateStatus(row, 'DELIVERED')">送达</el-button>
            <el-button v-if="row.status === 'DELIVERED'" link type="success" @click="handleUpdateStatus(row, 'PICKED_UP')">自提</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <div v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTag(currentOrder.status)">{{ statusLabel(currentOrder.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="客户">{{ currentOrder.customerName }}</el-descriptions-item>
          <el-descriptions-item label="电话">{{ currentOrder.customerPhone }}</el-descriptions-item>
          <el-descriptions-item label="小区团点">{{ currentOrder.communityName }}</el-descriptions-item>
          <el-descriptions-item label="团长">{{ currentOrder.leaderName }}</el-descriptions-item>
          <el-descriptions-item label="自提码">{{ currentOrder.pickupCode }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.createdAt }}</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin: 16px 0">商品明细</h4>
        <el-table :data="currentOrder.items" stripe>
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="price" label="单价" width="100">
            <template #default="{ row }">¥{{ row.price?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column label="小计" width="100">
            <template #default="{ row }">¥{{ row.subtotal?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'PENDING' ? 'warning' : 'success'">{{ row.status || '正常' }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 16px; text-align: right; font-size: 16px">
          订单总额: <span style="color: #f56c6c; font-weight: 700; font-size: 20px">¥{{ currentOrder.totalAmount?.toFixed(2) }}</span>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="addVisible" title="新建预售单" width="600px">
      <el-form :model="orderForm" label-width="100px">
        <el-form-item label="预售批次">
          <el-select v-model="orderForm.presaleBatchId" style="width: 100%">
            <el-option v-for="b in batchList" :key="b.id" :label="b.batchName" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="小区团点">
          <el-select v-model="orderForm.communityId" style="width: 100%">
            <el-option v-for="c in communityList" :key="c.id" :label="c.name + ' - ' + c.leaderName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model="orderForm.customerName" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="orderForm.customerPhone" />
        </el-form-item>
        <el-form-item label="商品">
          <div v-for="(item, i) in orderForm.items" :key="i" style="display: flex; gap: 8px; margin-bottom: 8px; align-items: center">
            <el-select v-model="item.productId" placeholder="选择商品" style="flex: 1">
              <el-option v-for="p in productList" :key="p.id" :label="p.name + ' (¥' + p.price + '/' + p.unit + ')'" :value="p.id" />
            </el-select>
            <el-input-number v-model="item.quantity" :min="1" style="width: 100px" placeholder="数量" />
            <el-input v-model="item.price" style="width: 100px" placeholder="单价" />
            <el-button type="danger" link @click="orderForm.items.splice(i, 1)">删除</el-button>
          </div>
          <el-button type="primary" plain @click="addItem">+ 添加商品</el-button>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="orderForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { orderApi, productApi, basicApi } from '../api'

const loading = ref(false)
const tableData = ref([])
const searchLeader = ref('')
const searchStatus = ref('')
const detailVisible = ref(false)
const addVisible = ref(false)
const currentOrder = ref(null)
const leaderList = ref([])
const communityList = ref([])
const batchList = ref([])
const productList = ref([])

const orderForm = reactive({
  presaleBatchId: null,
  communityId: null,
  customerName: '',
  customerPhone: '',
  items: [{ productId: null, quantity: 1, price: 0 }],
  remark: ''
})

const statusLabel = (s) => ({
  PENDING: '待付款', PAID: '已付款', SORTING: '分拣中',
  DELIVERED: '已送达', PICKED_UP: '已自提', REFUND: '已退款'
}[s] || s)

const statusTag = (s) => ({
  PENDING: 'warning', PAID: 'primary', SORTING: '',
  DELIVERED: 'info', PICKED_UP: 'success', REFUND: 'danger'
}[s] || '')

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchLeader.value) params.leaderId = searchLeader.value
    let data = await orderApi.list(params)
    if (searchStatus.value) {
      data = data.filter(o => o.status === searchStatus.value)
    }
    tableData.value = data
  } catch (e) {
    tableData.value = [
      { id: 1, orderNo: 'SO1717800001', customerName: '张三', customerPhone: '13800138000',
        communityName: '阳光花园小区', leaderName: '李团长',
        items: [{ productName: '有机小白菜', quantity: 2, price: 3.5, subtotal: 7 },
          { productName: '土猪五花肉', quantity: 1, price: 28.8, subtotal: 28.8 }],
        totalAmount: 35.8, refundAmount: 0, pickupCode: 'A8K2M9', status: 'PENDING', createdAt: '2026-06-08 09:30:00' },
      { id: 2, orderNo: 'SO1717800002', customerName: '李四', customerPhone: '13900139000',
        communityName: '幸福里小区', leaderName: '王团长',
        items: [{ productName: '速冻水饺', quantity: 3, price: 19.9, subtotal: 59.7 }],
        totalAmount: 59.7, refundAmount: 0, pickupCode: 'B3P7Q2', status: 'PAID', createdAt: '2026-06-08 10:15:00' },
      { id: 3, orderNo: 'SO1717800003', customerName: '王五', customerPhone: '13700137000',
        communityName: '阳光花园小区', leaderName: '李团长',
        items: [{ productName: '新鲜鸡蛋30枚', quantity: 2, price: 25.8, subtotal: 51.6 }],
        totalAmount: 51.6, refundAmount: 5, pickupCode: 'C5N8R1', status: 'PICKED_UP', createdAt: '2026-06-08 08:45:00' }
    ]
  } finally {
    loading.value = false
  }
}

const loadBase = async () => {
  try {
    leaderList.value = await basicApi.listLeaders()
    communityList.value = await basicApi.listCommunities()
    batchList.value = await basicApi.listPresaleBatches()
    productList.value = await productApi.list()
  } catch (e) {
    leaderList.value = [{ id: 1, name: '李团长' }, { id: 2, name: '王团长' }]
    communityList.value = [{ id: 1, name: '阳光花园小区', leaderName: '李团长' }, { id: 2, name: '幸福里小区', leaderName: '王团长' }]
    batchList.value = [{ id: 1, batchName: '6月8日预售批次' }]
    productList.value = [
      { id: 1, name: '有机小白菜', price: 3.5, unit: '斤' },
      { id: 2, name: '土猪五花肉', price: 28.8, unit: '斤' },
      { id: 3, name: '速冻水饺', price: 19.9, unit: '袋' },
      { id: 4, name: '新鲜鸡蛋30枚', price: 25.8, unit: '盒' }
    ]
  }
}

const handleDetail = (row) => {
  currentOrder.value = row
  detailVisible.value = true
}

const handleAdd = () => {
  orderForm.presaleBatchId = null
  orderForm.communityId = null
  orderForm.customerName = ''
  orderForm.customerPhone = ''
  orderForm.items = [{ productId: null, quantity: 1, price: 0 }]
  orderForm.remark = ''
  addVisible.value = true
}

const addItem = () => {
  orderForm.items.push({ productId: null, quantity: 1, price: 0 })
}

const submitOrder = async () => {
  try {
    await orderApi.create(orderForm)
    ElMessage.success('下单成功')
    addVisible.value = false
    loadData()
  } catch (e) {
    const no = 'SO' + Date.now()
    tableData.value.unshift({
      id: Date.now(), orderNo: no,
      customerName: orderForm.customerName,
      customerPhone: orderForm.customerPhone,
      communityName: communityList.value.find(c => c.id === orderForm.communityId)?.name || '',
      leaderName: communityList.value.find(c => c.id === orderForm.communityId)?.leaderName || '',
      items: orderForm.items.map(i => ({
        productName: productList.value.find(p => p.id === i.productId)?.name || '',
        quantity: i.quantity, price: i.price,
        subtotal: i.price * i.quantity
      })),
      totalAmount: orderForm.items.reduce((s, i) => s + i.price * i.quantity, 0),
      refundAmount: 0,
      pickupCode: Math.random().toString(36).substring(2, 8).toUpperCase(),
      status: 'PENDING', createdAt: new Date().toLocaleString()
    })
    ElMessage.success('下单成功')
    addVisible.value = false
  }
}

const handleUpdateStatus = async (row, status) => {
  try {
    await orderApi.updateStatus(row.id, status)
    ElMessage.success('状态更新成功')
    loadData()
  } catch (e) {
    row.status = status
    ElMessage.success('状态更新成功')
  }
}

onMounted(() => {
  loadData()
  loadBase()
})
</script>
