import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/dashboard' },
  {
    path: '/dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { title: '数据概览' }
  },
  {
    path: '/products',
    component: () => import('../views/Products.vue'),
    meta: { title: '团购商品' }
  },
  {
    path: '/orders',
    component: () => import('../views/Orders.vue'),
    meta: { title: '团长订单' }
  },
  {
    path: '/purchases',
    component: () => import('../views/Purchases.vue'),
    meta: { title: '采购批次' }
  },
  {
    path: '/sorting',
    component: () => import('../views/Sorting.vue'),
    meta: { title: '分拣差异' }
  },
  {
    path: '/aftersale',
    component: () => import('../views/AfterSale.vue'),
    meta: { title: '售后处理' }
  },
  {
    path: '/basic',
    component: () => import('../views/BasicConfig.vue'),
    meta: { title: '基础配置' }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
