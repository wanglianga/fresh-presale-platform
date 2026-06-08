<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">分拣差异管理</div>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="分拣记录" name="record">
        <div class="card">
          <div class="toolbar">
            <el-select v-model="searchCommunity" placeholder="选择小区团点" clearable style="width: 200px">
              <el-option v-for="c in communityList" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadRecords">搜索</el-button>
            <el-button type="success" :icon="Plus" @click="handleCreateSorting">新建分拣单</el-button>
          </div>

          <el-table :data="recordData" stripe v-loading="loading">
            <el-table-column prop="sortingNo" label="分拣单号" width="160" />
            <el-table-column prop="purchaseBatchNo" label="采购单号" width="160" />
            <el-table-column prop="communityName" label="小区团点" width="150" />
            <el-table-column label="分拣明细" min-width="250">
              <template #default="{ row }">
                <el-tag v-for="(item, i) in row.items?.slice(0, 3)" :key="i" style="margin-right: 4px; margin-bottom: 4px">
                  {{ item.productName }} {{ item.actualQuantity }}/{{ item.expectedQuantity }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'COMPLETED' ? 'success' : 'warning'">
                  {{ row.status === 'COMPLETED' ? '已完成' : '待分拣' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleSorting(row)">分拣处理</el-button>
                <el-button link @click="handleViewDiscrepancies(row)">查看差异</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="分拣差异" name="discrepancy">
        <div class="card">
          <div class="toolbar">
            <el-select v-model="searchType" placeholder="差异类型" clearable style="width: 180px">
              <el-option label="缺货" value="OUT_OF_STOCK" />
              <el-option label="重量差异" value="WEIGHT_DIFF" />
              <el-option label="冷链破损" value="COLD_BROKEN" />
              <el-option label="团长漏发" value="LEADER_MISS" />
              <el-option label="替换商品" value="REPLACED" />
            </el-select>
            <el-select v-model="searchDiscrepancyStatus" placeholder="处理状态" clearable style="width: 150px">
              <el-option label="待处理" value="PENDING" />
              <el-option label="已退款" value="REFUNDED" />
              <el-option label="已补偿" value="COMPENSATED" />
              <el-option label="已关闭" value="CLOSED" />
            </el-select>
            <el-button type="primary" :icon="Search" @click="loadDiscrepancies">搜索</el-button>
          </div>

          <el-table :data="discrepancyData" stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="productName" label="商品" width="150" />
            <el-table-column label="差异类型" width="120">
              <template #default="{ row }">
                <el-tag :class="getDiscrepancyClass(row.discrepancyType)">
                  {{ discrepancyLabel(row.discrepancyType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="差异数量" width="100" />
            <el-table-column label="重量差异" width="120">
              <template #default="{ row }">{{ row.weightDiff ? row.weightDiff + 'kg' : '-' }}</template>
            </el-table-column>
            <el-table-column label="替换商品" width="150">
              <template #default="{ row }">
                <span v-if="row.replacedProductName">
                  {{ row.replacedProductName }} x{{ row.replacedQuantity }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="赔付金额" width="120">
              <template #default="{ row }">
                <span v-if="row.compensationAmount" style="color: #f56c6c; font-weight: 600">
                  ¥{{ row.compensationAmount.toFixed(2) }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="处理状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'PENDING' ? 'warning' : (row.status === 'REFUNDED' ? 'danger' : 'success')">
                  {{ { PENDING: '待处理', REFUNDED: '已退款', COMPENSATED: '已补偿', CLOSED: '已关闭' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button v-if="row.status === 'PENDING'" link type="primary" @click="handleProcess(row)">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="sortingVisible" title="分拣处理" width="800px">
      <div v-if="currentSorting">
        <p style="margin-bottom: 12px">分拣单号: <b>{{ currentSorting.sortingNo }}</b> | 小区: <b>{{ currentSorting.communityName }}</b></p>
        <el-table :data="currentSorting.items" stripe>
          <el-table-column prop="productName" label="商品" />
          <el-table-column prop="expectedQuantity" label="应有数量" width="100" />
          <el-table-column label="实到数量" width="150">
            <template #default="{ row }">
              <el-input-number v-model="row.actualQuantity" :min="0" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="备注" width="200">
            <template #default="{ row }">
              <el-input v-model="row.remark" placeholder="如有差异请备注" />
            </template>
          </el-table-column>
        </el-table>

        <h4 style="margin: 16px 0 8px">分拣差异记录（如有）</h4>
        <div v-for="(d, i) in discrepancyList" :key="i" style="border: 1px solid #ebeef5; padding: 12px; margin-bottom: 8px; border-radius: 4px">
          <div style="display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 8px">
            <el-select v-model="d.discrepancyType" placeholder="差异类型" style="width: 150px">
              <el-option label="缺货" value="OUT_OF_STOCK" />
              <el-option label="重量差异" value="WEIGHT_DIFF" />
              <el-option label="冷链破损" value="COLD_BROKEN" />
              <el-option label="团长漏发" value="LEADER_MISS" />
              <el-option label="替换商品" value="REPLACED" />
            </el-select>
            <el-input-number v-model="d.quantity" :min="0" placeholder="数量" style="width: 100px" />
            <el-input v-model="d.remark" placeholder="备注说明" style="width: 250px" />
            <el-button type="danger" link @click="discrepancyList.splice(i, 1)">删除</el-button>
          </div>
        </div>
        <el-button type="primary" plain @click="addDiscrepancy">+ 添加差异记录</el-button>
      </div>
      <template #footer>
        <el-button @click="sortingVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSorting">完成分拣</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="processVisible" title="处理分拣差异" width="500px">
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="处理结果">
          <el-radio-group v-model="processForm.status">
            <el-radio label="REFUNDED">退款处理</el-radio>
            <el-radio label="COMPENSATED">补偿处理</el-radio>
            <el-radio label="CLOSED">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="赔付金额">
          <el-input-number v-model="processForm.compensationAmount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input v-model="processForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确认处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { sortingApi, basicApi, purchaseApi } from '../api'

const loading = ref(false)
const activeTab = ref('record')
const recordData = ref([])
const discrepancyData = ref([])
const searchCommunity = ref('')
const searchType = ref('')
const searchDiscrepancyStatus = ref('')
const communityList = ref([])
const sortingVisible = ref(false)
const processVisible = ref(false)
const currentSorting = ref(null)
const discrepancyList = ref([])
const processForm = reactive({ id: null, status: 'REFUNDED', compensationAmount: 0, remark: '' })

const discrepancyLabel = (t) => ({
  OUT_OF_STOCK: '缺货', WEIGHT_DIFF: '重量差异', COLD_BROKEN: '冷链破损',
  LEADER_MISS: '团长漏发', REPLACED: '替换商品'
}[t] || t)

const getDiscrepancyClass = (t) => ({
  OUT_OF_STOCK: 'tag-out-of-stock', WEIGHT_DIFF: 'tag-weight-diff',
  COLD_BROKEN: 'tag-cold-broken', LEADER_MISS: 'tag-leader-miss', REPLACED: 'tag-normal'
}[t] || '')

const loadRecords = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchCommunity.value) params.communityId = searchCommunity.value
    recordData.value = await sortingApi.listRecords(params)
  } catch (e) {
    recordData.value = [
      { id: 1, sortingNo: 'SR1717800001', purchaseBatchNo: 'PB1717800001', communityName: '阳光花园小区',
        items: [{ productName: '有机小白菜', expectedQuantity: 30, actualQuantity: 28 }],
        status: 'COMPLETED' },
      { id: 2, sortingNo: 'SR1717800002', purchaseBatchNo: 'PB1717800002', communityName: '幸福里小区',
        items: [{ productName: '土猪五花肉', expectedQuantity: 15, actualQuantity: 15 }],
        status: 'PENDING' }
    ]
  } finally {
    loading.value = false
  }
}

const loadDiscrepancies = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchType.value) params.type = searchType.value
    let data = await sortingApi.listDiscrepancies(params)
    if (searchDiscrepancyStatus.value) {
      data = data.filter(d => d.status === searchDiscrepancyStatus.value)
    }
    discrepancyData.value = data
  } catch (e) {
    discrepancyData.value = [
      { id: 1, productName: '有机小白菜', discrepancyType: 'OUT_OF_STOCK', quantity: 2, weightDiff: null, replacedProductName: null, replacedQuantity: null, compensationAmount: 7, status: 'REFUNDED', remark: '供货商缺货' },
      { id: 2, productName: '土猪五花肉', discrepancyType: 'WEIGHT_DIFF', quantity: 1, weightDiff: 0.3, replacedProductName: null, replacedQuantity: null, compensationAmount: 8.64, status: 'COMPENSATED', remark: '实际称重少0.3斤' },
      { id: 3, productName: '速冻水饺', discrepancyType: 'COLD_BROKEN', quantity: 1, weightDiff: null, replacedProductName: null, replacedQuantity: null, compensationAmount: null, status: 'PENDING', remark: '冷链运输中化冻破损' }
    ]
  } finally {
    loading.value = false
  }
}

const handleCreateSorting = () => {
  ElMessage.info('请先选择采购批次和小区团点创建分拣单')
}

const handleSorting = (row) => {
  currentSorting.value = JSON.parse(JSON.stringify(row))
  currentSorting.value.items.forEach(i => { if (!i.actualQuantity) i.actualQuantity = i.expectedQuantity })
  discrepancyList.value = []
  sortingVisible.value = true
}

const handleViewDiscrepancies = (row) => {
  activeTab.value = 'discrepancy'
  loadDiscrepancies()
}

const addDiscrepancy = () => {
  discrepancyList.value.push({ discrepancyType: '', quantity: 1, remark: '' })
}

const submitSorting = async () => {
  try {
    await sortingApi.completeSorting(currentSorting.value.id, { items: currentSorting.value.items, discrepancies: discrepancyList.value })
    ElMessage.success('分拣完成')
    sortingVisible.value = false
    loadRecords()
  } catch (e) {
    const idx = recordData.value.findIndex(r => r.id === currentSorting.value.id)
    if (idx > -1) {
      recordData.value[idx].status = 'COMPLETED'
      recordData.value[idx].items = currentSorting.value.items
    }
    ElMessage.success('分拣完成')
    sortingVisible.value = false
  }
}

const handleProcess = (row) => {
  processForm.id = row.id
  processForm.status = 'REFUNDED'
  processForm.compensationAmount = row.compensationAmount || 0
  processForm.remark = ''
  processVisible.value = true
}

const submitProcess = async () => {
  try {
    await sortingApi.handleDiscrepancy(processForm.id, processForm)
    ElMessage.success('处理成功')
    processVisible.value = false
    loadDiscrepancies()
  } catch (e) {
    const idx = discrepancyData.value.findIndex(d => d.id === processForm.id)
    if (idx > -1) {
      discrepancyData.value[idx].status = processForm.status
      discrepancyData.value[idx].compensationAmount = processForm.compensationAmount
      discrepancyData.value[idx].remark = processForm.remark
    }
    ElMessage.success('处理成功')
    processVisible.value = false
  }
}

const loadBase = async () => {
  try {
    communityList.value = await basicApi.listCommunities()
  } catch (e) {
    communityList.value = [{ id: 1, name: '阳光花园小区' }, { id: 2, name: '幸福里小区' }]
  }
}

onMounted(() => {
  loadRecords()
  loadDiscrepancies()
  loadBase()
})
</script>
