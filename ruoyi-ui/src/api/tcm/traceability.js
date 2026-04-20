import request from '@/utils/request'

export function listBase() {
  return request({ url: '/tcm/base/list', method: 'get' })
}
export function getBase(id) {
  return request({ url: '/tcm/base/' + id, method: 'get' })
}
export function addBase(data) {
  return request({ url: '/tcm/base', method: 'post', data })
}
export function updateBase(data) {
  return request({ url: '/tcm/base', method: 'put', data })
}
export function delBase(id) {
  return request({ url: '/tcm/base/' + id, method: 'delete' })
}

export function listProduct() {
  return request({ url: '/tcm/product/list', method: 'get' })
}
export function addProduct(data) {
  return request({ url: '/tcm/product', method: 'post', data })
}
export function updateProduct(data) {
  return request({ url: '/tcm/product', method: 'put', data })
}
export function delProduct(id) {
  return request({ url: '/tcm/product/' + id, method: 'delete' })
}

export function listBatch() {
  return request({ url: '/tcm/batch/list', method: 'get' })
}
export function addBatch(data) {
  return request({ url: '/tcm/batch', method: 'post', data })
}
export function updateBatch(data) {
  return request({ url: '/tcm/batch', method: 'put', data })
}
export function delBatch(id) {
  return request({ url: '/tcm/batch/' + id, method: 'delete' })
}
export function publishBatch(id) {
  return request({ url: '/tcm/batch/' + id + '/publish', method: 'post' })
}

export function listProcess(query) {
  return request({ url: '/tcm/process/list', method: 'get', params: query })
}
export function addProcess(data) {
  return request({ url: '/tcm/process', method: 'post', data })
}

export function listReview(query) {
  return request({ url: '/tcm/review/list', method: 'get', params: query })
}
export function addReview(data) {
  return request({ url: '/tcm/review', method: 'post', data })
}

export function queryTrace(traceCode) {
  return request({ url: '/tcm/trace/query', method: 'get', params: { traceCode } })
}

export function dashboardSummary() {
  return request({ url: '/tcm/dashboard/summary', method: 'get' })
}
