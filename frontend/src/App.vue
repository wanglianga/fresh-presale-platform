<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon><ShoppingCart /></el-icon>
        <span>生鲜预售平台</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#ffffffa6"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <el-icon><Goods /></el-icon>
          <span>团购商品</span>
        </el-menu-item>
        <el-menu-item index="/orders">
          <el-icon><List /></el-icon>
          <span>团长订单</span>
        </el-menu-item>
        <el-menu-item index="/purchases">
          <el-icon><Box /></el-icon>
          <span>采购批次</span>
        </el-menu-item>
        <el-menu-item index="/sorting">
          <el-icon><Sort /></el-icon>
          <span>分拣差异</span>
        </el-menu-item>
        <el-menu-item index="/aftersale">
          <el-icon><Service /></el-icon>
          <span>售后处理</span>
        </el-menu-item>
        <el-menu-item index="/basic">
          <el-icon><Setting /></el-icon>
          <span>基础配置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-title">{{ currentTitle }}</div>
        <div class="header-user">
          <el-icon><UserFilled /></el-icon>
          <span>管理员</span>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '生鲜预售平台')
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #001529;
  overflow: auto;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  gap: 8px;
  border-bottom: 1px solid #1f3a5c;
}

.logo .el-icon {
  font-size: 24px;
  color: #67c23a;
}

:deep(.el-menu) {
  border-right: none;
}

.header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  font-size: 14px;
}

.el-main {
  background-color: #f5f7fa;
  padding: 0;
  overflow: auto;
}
</style>
