import request from '../utils/request'

export const productApi = {
  list: (params) => request.get('/products', { params }),
  get: (id) => request.get(`/products/${id}`),
  create: (data) => request.post('/products', data),
  update: (id, data) => request.put(`/products/${id}`, data),
  remove: (id) => request.delete(`/products/${id}`)
}

export const orderApi = {
  list: (params) => request.get('/orders', { params }),
  get: (id) => request.get(`/orders/${id}`),
  create: (data) => request.post('/orders', data),
  updateStatus: (id, status) => request.put(`/orders/${id}/status`, null, { params: { status } })
}

export const purchaseApi = {
  list: (params) => request.get('/purchases', { params }),
  get: (id) => request.get(`/purchases/${id}`),
  create: (data) => request.post('/purchases', data),
  confirm: (id, data) => request.put(`/purchases/${id}/confirm`, data),
  ship: (id) => request.put(`/purchases/${id}/ship`),
  arrive: (id) => request.put(`/purchases/${id}/arrive`)
}

export const sortingApi = {
  listRecords: (params) => request.get('/sorting/records', { params }),
  getRecord: (id) => request.get(`/sorting/records/${id}`),
  createRecord: (data) => request.post('/sorting/records', data),
  completeSorting: (id, data) => request.post(`/sorting/records/${id}/complete`, data),
  listDiscrepancies: (params) => request.get('/sorting/discrepancies', { params }),
  handleDiscrepancy: (id, data) => request.put(`/sorting/discrepancies/${id}`, data)
}

export const afterSaleApi = {
  listRequests: (params) => request.get('/aftersale/requests', { params }),
  getRequest: (id) => request.get(`/aftersale/requests/${id}`),
  createRequest: (data) => request.post('/aftersale/requests', data),
  approveRequest: (id, data) => request.put(`/aftersale/requests/${id}/approve`, data),
  rejectRequest: (id, data) => request.put(`/aftersale/requests/${id}/reject`, data),
  listRefunds: (params) => request.get('/aftersale/refunds', { params }),
  completeRefund: (id) => request.put(`/aftersale/refunds/${id}/complete`),
  listCompensations: () => request.get('/aftersale/compensations'),
  createCompensation: (data) => request.post('/aftersale/compensations', data),
  completeCompensation: (id) => request.put(`/aftersale/compensations/${id}/complete`)
}

export const basicApi = {
  listPresaleBatches: () => request.get('/basic/presale-batches'),
  createPresaleBatch: (data) => request.post('/basic/presale-batches', data),
  listSuppliers: () => request.get('/basic/suppliers'),
  createSupplier: (data) => request.post('/basic/suppliers', data),
  listLeaders: () => request.get('/basic/leaders'),
  createLeader: (data) => request.post('/basic/leaders', data),
  listCommunities: () => request.get('/basic/communities'),
  createCommunity: (data) => request.post('/basic/communities', data),
  listReceipts: (params) => request.get('/basic/receipts', { params }),
  createReceipt: (data) => request.post('/basic/receipts', data),
  confirmReceipt: (id, remark) => request.put(`/basic/receipts/${id}/confirm`, null, { params: { remark } }),
  notifyPickup: (id) => request.put(`/basic/receipts/${id}/notify`),
  listPickups: () => request.get('/basic/pickups'),
  confirmPickup: (data) => request.post('/basic/pickups', data)
}
