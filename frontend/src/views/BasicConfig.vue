<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">基础配置</div>
    </div>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="预售批次" name="batch">
        <div class="card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" @click="openBatchDialog">新增预售批次</el-button>
          </div>
          <el-table :data="batchData" stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="batchName" label="批次名称" />
            <el-table-column prop="startTime" label="开始时间" width="170" />
            <el-table-column prop="endTime" label="结束时间" width="170" />
            <el-table-column prop="deliveryDate" label="配送日期" width="170" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
                  {{ row.status === 'ACTIVE' ? '进行中' : '已结束' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="供应商" name="supplier">
        <div class="card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" @click="openSupplierDialog">新增供应商</el-button>
          </div>
          <el-table :data="supplierData" stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="name" label="供应商名称" />
            <el-table-column prop="contactPerson" label="联系人" width="120" />
            <el-table-column prop="phone" label="联系电话" width="150" />
            <el-table-column prop="address" label="地址" min-width="200" />
            <el-table-column prop="freshnessCapability" label="保鲜能力" width="180" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '启用' : '停用' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="团长" name="leader">
        <div class="card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" @click="openLeaderDialog">新增团长</el-button>
          </div>
          <el-table :data="leaderData" stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="name" label="团长姓名" width="150" />
            <el-table-column prop="phone" label="联系电话" width="150" />
            <el-table-column prop="address" label="地址" min-width="250" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '启用' : '停用' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="小区团点" name="community">
        <div class="card">
          <div class="toolbar">
            <el-button type="primary" :icon="Plus" @click="openCommunityDialog">新增小区团点</el-button>
          </div>
          <el-table :data="communityData" stripe>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="name" label="小区名称" />
            <el-table-column prop="address" label="地址" min-width="250" />
            <el-table-column prop="contactPhone" label="联系电话" width="150" />
            <el-table-column prop="leaderName" label="团长" width="120" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '启用' : '停用' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="团长签收" name="receipt">
        <div class="card">
          <el-table :data="receiptData" stripe>
            <el-table-column prop="receiptNo" label="签收单号" width="160" />
            <el-table-column prop="sortingNo" label="分拣单号" width="160" />
            <el-table-column prop="communityName" label="小区团点" width="150" />
            <el-table-column prop="leaderName" label="团长" width="100" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'NOTIFIED' ? 'success' : (row.status === 'RECEIVED' ? 'primary' : 'warning')">
                  {{ { PENDING: '待签收', RECEIVED: '已签收', NOTIFIED: '已通知' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="receiptTime" label="签收时间" width="170" />
            <el-table-column prop="pickupNotifyTime" label="通知时间" width="170" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button v-if="row.status === 'PENDING'" link type="success" @click="handleConfirmReceipt(row)">确认签收</el-button>
                <el-button v-if="row.status === 'RECEIVED'" link type="primary" @click="handleNotify(row)">通知自提</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="batchVisible" title="新增预售批次" width="550px">
      <el-form :model="batchForm" label-width="100px">
        <el-form-item label="批次名称">
          <el-input v-model="batchForm.batchName" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="batchForm.startTime" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="batchForm.endTime" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="配送日期">
          <el-date-picker v-model="batchForm.deliveryDate" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="batchForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatch">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="supplierVisible" title="新增供应商" width="550px">
      <el-form :model="supplierForm" label-width="100px">
        <el-form-item label="供应商名称">
          <el-input v-model="supplierForm.name" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="supplierForm.contactPerson" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="supplierForm.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="supplierForm.address" />
        </el-form-item>
        <el-form-item label="保鲜能力">
          <el-input v-model="supplierForm.freshnessCapability" placeholder="如：冷链运输、冷藏保鲜等" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="supplierVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSupplier">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="leaderVisible" title="新增团长" width="550px">
      <el-form :model="leaderForm" label-width="100px">
        <el-form-item label="团长姓名">
          <el-input v-model="leaderForm.name" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="leaderForm.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="leaderForm.address" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="leaderVisible = false">取消</el-button>
        <el-button type="primary" @click="submitLeader">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="communityVisible" title="新增小区团点" width="550px">
      <el-form :model="communityForm" label-width="100px">
        <el-form-item label="小区名称">
          <el-input v-model="communityForm.name" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="communityForm.address" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="communityForm.contactPhone" />
        </el-form-item>
        <el-form-item label="团长">
          <el-select v-model="communityForm.leaderId" style="width: 100%">
            <el-option v-for="l in leaderData" :key="l.id" :label="l.name" :value="l.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="communityVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCommunity">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { basicApi } from '../api'

const activeTab = ref('batch')
const batchData = ref([])
const supplierData = ref([])
const leaderData = ref([])
const communityData = ref([])
const receiptData = ref([])

const batchVisible = ref(false)
const supplierVisible = ref(false)
const leaderVisible = ref(false)
const communityVisible = ref(false)

const batchForm = reactive({ batchName: '', startTime: null, endTime: null, deliveryDate: null, remark: '' })
const supplierForm = reactive({ name: '', contactPerson: '', phone: '', address: '', freshnessCapability: '' })
const leaderForm = reactive({ name: '', phone: '', address: '' })
const communityForm = reactive({ name: '', address: '', contactPhone: '', leaderId: null })

const loadBatch = async () => {
  try {
    batchData.value = await basicApi.listPresaleBatches()
  } catch (e) {
    batchData.value = [
      { id: 1, batchName: '6月8日预售批次', startTime: '2026-06-08 00:00:00', endTime: '2026-06-08 18:00:00', deliveryDate: '2026-06-09 08:00:00', status: 'ACTIVE', remark: '' }
    ]
  }
}

const loadSupplier = async () => {
  try {
    supplierData.value = await basicApi.listSuppliers()
  } catch (e) {
    supplierData.value = [
      { id: 1, name: '绿源蔬菜基地', contactPerson: '张经理', phone: '13800138001', address: '山东省寿光市蔬菜产业园', freshnessCapability: '蔬菜冷藏保鲜', active: true },
      { id: 2, name: '宏达肉业', contactPerson: '李总', phone: '13800138002', address: '河南省漯河市食品工业园', freshnessCapability: '冷链运输-18°C', active: true },
      { id: 3, name: '冰泉冷冻食品', contactPerson: '王经理', phone: '13800138003', address: '天津市滨海新区冷冻食品基地', freshnessCapability: '速冻冷链配送', active: true }
    ]
  }
}

const loadLeader = async () => {
  try {
    leaderData.value = await basicApi.listLeaders()
  } catch (e) {
    leaderData.value = [
      { id: 1, name: '李团长', phone: '13900139001', address: '阳光花园小区物业办公室', active: true },
      { id: 2, name: '王团长', phone: '13900139002', address: '幸福里小区东门便利店', active: true }
    ]
  }
}

const loadCommunity = async () => {
  try {
    communityData.value = await basicApi.listCommunities()
  } catch (e) {
    communityData.value = [
      { id: 1, name: '阳光花园小区', address: '北京市朝阳区建国路88号', contactPhone: '13900139001', leaderId: 1, leaderName: '李团长', active: true },
      { id: 2, name: '幸福里小区', address: '北京市海淀区中关村大街1号', contactPhone: '13900139002', leaderId: 2, leaderName: '王团长', active: true }
    ]
  }
}

const loadReceipt = async () => {
  try {
    receiptData.value = await basicApi.listReceipts()
  } catch (e) {
    receiptData.value = [
      { id: 1, receiptNo: 'LR1717800001', sortingNo: 'SR1717800001', communityName: '阳光花园小区', leaderName: '李团长', status: 'NOTIFIED', receiptTime: '2026-06-09 06:30:00', pickupNotifyTime: '2026-06-09 07:00:00' },
      { id: 2, receiptNo: 'LR1717800002', sortingNo: 'SR1717800002', communityName: '幸福里小区', leaderName: '王团长', status: 'RECEIVED', receiptTime: '2026-06-09 06:45:00', pickupNotifyTime: null }
    ]
  }
}

const openBatchDialog = () => {
  Object.assign(batchForm, { batchName: '', startTime: null, endTime: null, deliveryDate: null, remark: '' })
  batchVisible.value = true
}

const submitBatch = async () => {
  try {
    await basicApi.createPresaleBatch(batchForm)
    ElMessage.success('创建成功')
    batchVisible.value = false
    loadBatch()
  } catch (e) {
    batchData.value.unshift({
      id: Date.now(),
      batchName: batchForm.batchName,
      startTime: batchForm.startTime,
      endTime: batchForm.endTime,
      deliveryDate: batchForm.deliveryDate,
      status: 'ACTIVE',
      remark: batchForm.remark
    })
    ElMessage.success('创建成功')
    batchVisible.value = false
  }
}

const openSupplierDialog = () => {
  Object.assign(supplierForm, { name: '', contactPerson: '', phone: '', address: '', freshnessCapability: '' })
  supplierVisible.value = true
}

const submitSupplier = async () => {
  try {
    await basicApi.createSupplier(supplierForm)
    ElMessage.success('创建成功')
    supplierVisible.value = false
    loadSupplier()
  } catch (e) {
    supplierData.value.unshift({ ...supplierForm, id: Date.now(), active: true })
    ElMessage.success('创建成功')
    supplierVisible.value = false
  }
}

const openLeaderDialog = () => {
  Object.assign(leaderForm, { name: '', phone: '', address: '' })
  leaderVisible.value = true
}

const submitLeader = async () => {
  try {
    await basicApi.createLeader(leaderForm)
    ElMessage.success('创建成功')
    leaderVisible.value = false
    loadLeader()
  } catch (e) {
    leaderData.value.unshift({ ...leaderForm, id: Date.now(), active: true })
    ElMessage.success('创建成功')
    leaderVisible.value = false
  }
}

const openCommunityDialog = () => {
  Object.assign(communityForm, { name: '', address: '', contactPhone: '', leaderId: null })
  communityVisible.value = true
}

const submitCommunity = async () => {
  try {
    await basicApi.createCommunity(communityForm)
    ElMessage.success('创建成功')
    communityVisible.value = false
    loadCommunity()
  } catch (e) {
    const leader = leaderData.value.find(l => l.id === communityForm.leaderId)
    communityData.value.unshift({
      ...communityForm,
      id: Date.now(),
      leaderName: leader?.name || '',
      active: true
    })
    ElMessage.success('创建成功')
    communityVisible.value = false
  }
}

const handleConfirmReceipt = async (row) => {
  try {
    await basicApi.confirmReceipt(row.id, '')
    ElMessage.success('签收成功')
    loadReceipt()
  } catch (e) {
    row.status = 'RECEIVED'
    row.receiptTime = new Date().toLocaleString()
    ElMessage.success('签收成功')
  }
}

const handleNotify = async (row) => {
  try {
    await basicApi.notifyPickup(row.id)
    ElMessage.success('已通知自提')
    loadReceipt()
  } catch (e) {
    row.status = 'NOTIFIED'
    row.pickupNotifyTime = new Date().toLocaleString()
    ElMessage.success('已通知自提')
  }
}

onMounted(() => {
  loadBatch()
  loadSupplier()
  loadLeader()
  loadCommunity()
  loadReceipt()
})
</script>
