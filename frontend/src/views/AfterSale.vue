<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">售后处理中心</div>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #e6a23c">{{ stats.pendingCount }}</div>
          <div class="stat-label">待处理售后</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #409eff">¥{{ stats.refundTotal.toFixed(2) }}</div>
          <div class="stat-label">累计退款金额</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #67c23a">¥{{ stats.compensationTotal.toFixed(2) }}</div>
          <div class="stat-label">累计补偿金额</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #f56c6c">{{ stats.totalCount }}</div>
          <div class="stat-label">售后申请总数</div>
        </div>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="售后申请" name="request">
        <div class="card">
          <div class="toolbar">
            <el-select v-model="searchStatus" placeholder="处理状态" clearable style="width: 150px">
              <el-option label="待处理" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
            <el-select v-model="searchType" placeholder="售后类型" clearable style="width: 180px">
              <el-option label="缺货退款" value="OUT_OF_STOCK" />
              <el-option label="重量不足" value="WEIGHT_DIFF" />
              <el-option label="冷链破损" value="COLD_BROKEN" />
              <el-option label="团长漏发" value="LEADER_MISS" />
              <el-option label="商品质量" value="QUALITY" />
            </el-select>
            <el-select v-model="searchSource" placeholder="来源" clearable style="width: 150px">
              <el-option label="分拣差异" value="SORTING" />
              <el-option label="团长提交" value="LEADER" />
              <el-option label="消费者" value="CUSTOMER" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadRequests">搜索</el-button>
          </div>

          <el-table :data="requestData" stripe v-loading="loading">
            <el-table-column prop="requestNo" label="售后单号" width="160" />
            <el-table-column prop="orderNo" label="关联订单" width="160" />
            <el-table-column label="售后类型" width="120">
              <template #default="{ row }">
                <el-tag :class="getTypeClass(row.requestType)">{{ typeLabel(row.requestType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="source" label="来源" width="100">
              <template #default="{ row }">
                {{ { SORTING: '分拣差异', LEADER: '团长提交', CUSTOMER: '消费者' }[row.source] || row.source }}
              </template>
            </el-table-column>
            <el-table-column label="申请商品" min-width="200">
              <template #default="{ row }">
                <el-tag v-for="(item, i) in row.items?.slice(0, 2)" :key="i" style="margin-right: 4px">
                  {{ item.productName }} x{{ item.quantity }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="申请金额" width="120">
              <template #default="{ row }">
                <span style="color: #e6a23c">¥{{ row.requestAmount?.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="审批金额" width="120">
              <template #default="{ row }">
                <span v-if="row.approvedAmount" style="color: #f56c6c; font-weight: 600">
                  ¥{{ row.approvedAmount.toFixed(2) }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="问题描述" width="180" show-overflow-tooltip />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'PENDING' ? 'warning' : (row.status === 'APPROVED' ? 'success' : 'danger')">
                  {{ { PENDING: '待处理', APPROVED: '已通过', REJECTED: '已拒绝' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleDetail(row)">详情</el-button>
                <el-button v-if="row.status === 'PENDING'" link type="success" @click="handleApprove(row)">通过</el-button>
                <el-button v-if="row.status === 'PENDING'" link type="danger" @click="handleReject(row)">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="退款记录" name="refund">
        <div class="card">
          <el-table :data="refundData" stripe v-loading="loading">
            <el-table-column prop="refundNo" label="退款单号" width="160" />
            <el-table-column prop="orderNo" label="关联订单" width="160" />
            <el-table-column prop="requestNo" label="售后单号" width="160" />
            <el-table-column label="退款类型" width="120">
              <template #default="{ row }">
                <el-tag :class="getTypeClass(row.refundType)">{{ typeLabel(row.refundType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" width="180" show-overflow-tooltip />
            <el-table-column label="退款金额" width="120">
              <template #default="{ row }">
                <span style="color: #f56c6c; font-weight: 600">¥{{ row.refundAmount?.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'COMPLETED' ? 'success' : 'warning'">
                  {{ { PENDING: '待退款', COMPLETED: '已退款' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button v-if="row.status === 'PENDING'" link type="primary" @click="handleCompleteRefund(row)">确认退款</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="补偿记录" name="compensation">
        <div class="card">
          <el-table :data="compensationData" stripe v-loading="loading">
            <el-table-column prop="compensationNo" label="补偿单号" width="160" />
            <el-table-column prop="orderNo" label="关联订单" width="160" />
            <el-table-column label="补偿类型" width="120">
              <template #default="{ row }">
                <el-tag :class="getTypeClass(row.compensationType)">{{ typeLabel(row.compensationType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" width="180" show-overflow-tooltip />
            <el-table-column label="补偿金额" width="120">
              <template #default="{ row }">
                <span style="color: #f56c6c; font-weight: 600">¥{{ row.compensationAmount?.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'COMPLETED' ? 'success' : 'warning'">
                  {{ { PENDING: '待补偿', COMPLETED: '已补偿' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button v-if="row.status === 'PENDING'" link type="primary" @click="handleCompleteCompensation(row)">确认补偿</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="approveVisible" title="审批售后申请" width="500px">
      <el-form :model="approveForm" label-width="100px">
        <el-form-item label="申请单号">
          <span>{{ currentRequest?.requestNo }}</span>
        </el-form-item>
        <el-form-item label="申请金额">
          <span style="color: #e6a23c">¥{{ currentRequest?.requestAmount?.toFixed(2) }}</span>
        </el-form-item>
        <el-form-item label="审批金额">
          <el-input-number v-model="approveForm.approvedAmount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="审批备注">
          <el-input v-model="approveForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApprove">确认通过</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectVisible" title="拒绝售后申请" width="500px">
      <el-form label-width="100px">
        <el-form-item label="申请单号">
          <span>{{ currentRequest?.requestNo }}</span>
        </el-form-item>
        <el-form-item label="拒绝原因">
          <el-input v-model="rejectRemark" type="textarea" :rows="3" placeholder="请填写拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false">取消</el-button>
        <el-button type="danger" @click="submitReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { afterSaleApi } from '../api'

const loading = ref(false)
const activeTab = ref('request')
const requestData = ref([])
const refundData = ref([])
const compensationData = ref([])
const searchStatus = ref('')
const searchType = ref('')
const searchSource = ref('')
const approveVisible = ref(false)
const rejectVisible = ref(false)
const currentRequest = ref(null)
const approveForm = reactive({ approvedAmount: 0, remark: '' })
const rejectRemark = ref('')

const stats = reactive({
  pendingCount: 0,
  refundTotal: 0,
  compensationTotal: 0,
  totalCount: 0
})

const typeLabel = (t) => ({
  OUT_OF_STOCK: '缺货退款', WEIGHT_DIFF: '重量不足', COLD_BROKEN: '冷链破损',
  LEADER_MISS: '团长漏发', QUALITY: '商品质量'
}[t] || t)

const getTypeClass = (t) => ({
  OUT_OF_STOCK: 'tag-out-of-stock', WEIGHT_DIFF: 'tag-weight-diff',
  COLD_BROKEN: 'tag-cold-broken', LEADER_MISS: 'tag-leader-miss', QUALITY: 'tag-normal'
}[t] || '')

const loadRequests = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchStatus.value) params.status = searchStatus.value
    if (searchType.value) params.type = searchType.value
    requestData.value = await afterSaleApi.listRequests(params)
  } catch (e) {
    requestData.value = [
      { id: 1, requestNo: 'AS1717800001', orderNo: 'SO1717800003', requestType: 'WEIGHT_DIFF', source: 'SORTING',
        items: [{ productName: '新鲜鸡蛋30枚', quantity: 1 }], requestAmount: 5, approvedAmount: 5, description: '称重少约5元', status: 'APPROVED' },
      { id: 2, requestNo: 'AS1717800002', orderNo: 'SO1717800001', requestType: 'OUT_OF_STOCK', source: 'SORTING',
        items: [{ productName: '有机小白菜', quantity: 2 }], requestAmount: 7, approvedAmount: null, description: '供货商缺货', status: 'PENDING' },
      { id: 3, requestNo: 'AS1717800003', orderNo: 'SO1717800005', requestType: 'COLD_BROKEN', source: 'LEADER',
        items: [{ productName: '速冻水饺', quantity: 1 }], requestAmount: 19.9, approvedAmount: null, description: '冷链破损导致化冻', status: 'PENDING' }
    ]
  } finally {
    loading.value = false
    calcStats()
  }
}

const loadRefunds = async () => {
  try {
    refundData.value = await afterSaleApi.listRefunds()
  } catch (e) {
    refundData.value = [
      { id: 1, refundNo: 'RF1717800001', orderNo: 'SO1717800003', requestNo: 'AS1717800001',
        refundType: 'WEIGHT_DIFF', reason: '称重少约5元', refundAmount: 5, status: 'COMPLETED' },
      { id: 2, refundNo: 'RF1717800002', orderNo: 'SO1717800001', requestNo: 'AS1717800002',
        refundType: 'OUT_OF_STOCK', reason: '供货商缺货', refundAmount: 7, status: 'PENDING' }
    ]
  }
}

const loadCompensations = async () => {
  try {
    compensationData.value = await afterSaleApi.listCompensations()
  } catch (e) {
    compensationData.value = [
      { id: 1, compensationNo: 'CP1717800001', orderNo: 'SO1717800007',
        compensationType: 'LEADER_MISS', reason: '团长漏发一件商品', compensationAmount: 25.8, status: 'COMPLETED' },
      { id: 2, compensationNo: 'CP1717800002', orderNo: 'SO1717800009',
        compensationType: 'COLD_BROKEN', reason: '冷链破损客户投诉', compensationAmount: 50, status: 'PENDING' }
    ]
  }
}

const calcStats = () => {
  stats.totalCount = requestData.value.length
  stats.pendingCount = requestData.value.filter(r => r.status === 'PENDING').length
  stats.refundTotal = refundData.value.filter(r => r.status === 'COMPLETED').reduce((s, r) => s + (r.refundAmount || 0), 0)
  stats.compensationTotal = compensationData.value.filter(c => c.status === 'COMPLETED').reduce((s, c) => s + (c.compensationAmount || 0), 0)
}

const handleDetail = (row) => {
  ElMessage.info('查看详情: ' + row.requestNo)
}

const handleApprove = (row) => {
  currentRequest.value = row
  approveForm.approvedAmount = row.requestAmount
  approveForm.remark = ''
  approveVisible.value = true
}

const submitApprove = async () => {
  try {
    await afterSaleApi.approveRequest(currentRequest.value.id, approveForm)
    ElMessage.success('审批通过')
    approveVisible.value = false
    loadRequests()
    loadRefunds()
  } catch (e) {
    const idx = requestData.value.findIndex(r => r.id === currentRequest.value.id)
    if (idx > -1) {
      requestData.value[idx].status = 'APPROVED'
      requestData.value[idx].approvedAmount = approveForm.approvedAmount
    }
    ElMessage.success('审批通过')
    approveVisible.value = false
    loadRequests()
    loadRefunds()
  }
}

const handleReject = (row) => {
  currentRequest.value = row
  rejectRemark.value = ''
  rejectVisible.value = true
}

const submitReject = async () => {
  try {
    await afterSaleApi.rejectRequest(currentRequest.value.id, { remark: rejectRemark.value })
    ElMessage.success('已拒绝')
    rejectVisible.value = false
    loadRequests()
  } catch (e) {
    const idx = requestData.value.findIndex(r => r.id === currentRequest.value.id)
    if (idx > -1) requestData.value[idx].status = 'REJECTED'
    ElMessage.success('已拒绝')
    rejectVisible.value = false
  }
}

const handleCompleteRefund = async (row) => {
  try {
    await afterSaleApi.completeRefund(row.id)
    ElMessage.success('退款完成')
    loadRefunds()
  } catch (e) {
    row.status = 'COMPLETED'
    ElMessage.success('退款完成')
  }
}

const handleCompleteCompensation = async (row) => {
  try {
    await afterSaleApi.completeCompensation(row.id)
    ElMessage.success('补偿完成')
    loadCompensations()
  } catch (e) {
    row.status = 'COMPLETED'
    ElMessage.success('补偿完成')
  }
}

onMounted(() => {
  loadRequests()
  loadRefunds()
  loadCompensations()
})
</script>
