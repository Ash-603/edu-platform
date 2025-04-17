import axios from 'axios'

export default {
  /**
   * 创建订单
   * @param {Object} orderData - 订单数据
   * @returns {Promise}
   */
  createOrder(orderData) {
    return axios.post('/orders', orderData)
  },
  
  /**
   * 获取订单详情
   * @param {String} orderId - 订单ID
   * @returns {Promise}
   */
  getOrderById(orderId) {
    return axios.get(`/orders/${orderId}`)
  },
  
  /**
   * 获取用户的所有订单
   * @returns {Promise}
   */
  getUserOrders() {
    return axios.get('/orders/user')
  },
  
  /**
   * 取消订单
   * @param {String} orderId - 订单ID
   * @returns {Promise}
   */
  cancelOrder(orderId) {
    return axios.post(`/orders/${orderId}/cancel`)
  },
  
  /**
   * 发起支付
   * @param {String} orderId - 订单ID
   * @param {String} paymentMethod - 支付方式 (ALIPAY, WECHAT, CREDITCARD)
   * @returns {Promise}
   */
  initiatePayment(orderId, paymentMethod) {
    return axios.post(`/payments/initiate`, {
      orderId,
      paymentMethod
    })
  },
  
  /**
   * 验证支付状态
   * @param {String} orderId - 订单ID
   * @returns {Promise}
   */
  verifyPaymentStatus(orderId) {
    return axios.get(`/payments/verify/${orderId}`)
  },
  
  /**
   * 申请退款
   * @param {String} orderId - 订单ID
   * @param {String} reason - 退款原因
   * @returns {Promise}
   */
  requestRefund(orderId, reason) {
    return axios.post(`/payments/refund`, {
      orderId,
      reason
    })
  },
  
  /**
   * 获取退款状态
   * @param {String} refundId - 退款ID
   * @returns {Promise}
   */
  getRefundStatus(refundId) {
    return axios.get(`/payments/refund/${refundId}`)
  },
  
  /**
   * 获取支付方式列表
   * @returns {Promise}
   */
  getPaymentMethods() {
    return axios.get('/payments/methods')
  }
}