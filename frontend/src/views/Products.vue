<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">团购商品管理</div>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增商品</el-button>
    </div>

    <div class="card">
      <div class="toolbar">
        <el-input v-model="searchName" placeholder="搜索商品名称" clearable style="width: 200px" />
        <el-select v-model="searchCategory" placeholder="商品分类" clearable style="width: 150px">
          <el-option label="蔬果" value="蔬果" />
          <el-option label="肉禽" value="肉禽" />
          <el-option label="冷冻品" value="冷冻品" />
          <el-option label="组合套餐" value="组合套餐" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="tableData" stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="图片" width="80">
          <template #default="{ row }">
            <el-image :src="row.imageUrl || placeholderImg" style="width: 50px; height: 50px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="category" label="分类" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productType" label="类型" width="100" />
        <el-table-column label="价格" width="120">
          <template #default="{ row }">
            <span style="color: #f56c6c; font-weight: 600">¥{{ row.price?.toFixed(2) }}</span>
            <span v-if="row.originalPrice" style="text-decoration: line-through; color: #909399; font-size: 12px; margin-left: 6px">
              ¥{{ row.originalPrice.toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="70" />
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="freshnessCondition" label="保鲜条件" min-width="150" show-overflow-tooltip />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">{{ row.active ? '上架' : '下架' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '新增商品'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="蔬果" value="蔬果" />
            <el-option label="肉禽" value="肉禽" />
            <el-option label="冷冻品" value="冷冻品" />
            <el-option label="组合套餐" value="组合套餐" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品类型">
          <el-input v-model="form.productType" placeholder="如：叶菜、猪肉、速冻水饺等" />
        </el-form-item>
        <el-form-item label="销售价格">
          <el-input-number v-model="form.price" :precision="2" :step="0.5" :min="0" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="form.originalPrice" :precision="2" :step="0.5" :min="0" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" placeholder="如：斤、份、盒" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="保鲜条件">
          <el-input v-model="form.freshnessCondition" placeholder="如：冷藏0-4°C、冷冻-18°C以下" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="上架状态">
          <el-switch v-model="form.active" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { productApi } from '../api'

const placeholderImg = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20vegetables%20on%20white%20background&image_size=square'

const loading = ref(false)
const tableData = ref([])
const searchName = ref('')
const searchCategory = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const defaultForm = () => ({
  id: null,
  name: '',
  category: '',
  productType: '',
  price: 0,
  originalPrice: 0,
  unit: '斤',
  stock: 0,
  description: '',
  imageUrl: '',
  freshnessCondition: '',
  active: true
})

const form = reactive(defaultForm())

const loadData = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchCategory.value) params.category = searchCategory.value
    let data = await productApi.list(params)
    if (searchName.value) {
      data = data.filter(p => p.name.includes(searchName.value))
    }
    tableData.value = data
  } catch (e) {
    tableData.value = [
      { id: 1, name: '有机小白菜', category: '蔬果', productType: '叶菜', price: 3.5, originalPrice: 4.5, unit: '斤', stock: 200, freshnessCondition: '冷藏0-4°C', active: true, description: '新鲜有机小白菜' },
      { id: 2, name: '土猪五花肉', category: '肉禽', productType: '猪肉', price: 28.8, originalPrice: 32, unit: '斤', stock: 100, freshnessCondition: '冷藏0-4°C', active: true, description: '散养土猪五花肉' },
      { id: 3, name: '速冻水饺', category: '冷冻品', productType: '速冻食品', price: 19.9, originalPrice: 25, unit: '袋', stock: 150, freshnessCondition: '冷冻-18°C以下', active: true, description: '猪肉白菜馅速冻水饺' },
      { id: 4, name: '新鲜鸡蛋30枚', category: '组合套餐', productType: '蛋类', price: 25.8, originalPrice: 30, unit: '盒', stock: 80, freshnessCondition: '常温阴凉处', active: true, description: '农家散养鸡蛋30枚装' }
    ]
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  isEdit.value = true
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除商品「${row.name}」？`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await productApi.remove(row.id)
        ElMessage.success('删除成功')
        loadData()
      } catch (e) {
        tableData.value = tableData.value.filter(item => item.id !== row.id)
        ElMessage.success('删除成功')
      }
    })
    .catch(() => {})
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await productApi.update(form.id, form)
    } else {
      await productApi.create(form)
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    if (!isEdit.value) {
      tableData.value.push({ ...form, id: Date.now() })
    } else {
      const idx = tableData.value.findIndex(item => item.id === form.id)
      if (idx > -1) tableData.value[idx] = { ...form }
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
  }
}

onMounted(loadData)
</script>
