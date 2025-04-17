import axios from 'axios'

const state = {
  orders: [],
  currentOrder: null,
  paymentMethods: [],
  loading: false,
  error: null
}

const getters = {
  allOrders: state => state.orders,
  currentOrder: state => state.currentOrder,
  paymentMethods: state => state.paymentMethods,
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async fetchUserOrders({ commit }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get('/orders/user')
      commit('SET_ORDERS', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch orders')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchOrderById({ commit }, orderId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/orders/${orderId}`)
      commit('SET_CURRENT_ORDER', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch order details')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async createOrder({ commit }, orderData) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post('/orders', orderData)
      commit('SET_CURRENT_ORDER', response.data)
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to create order')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async cancelOrder({ commit }, orderId) {
    try {
      commit('SET_LOADING', true)
      await axios.post(`/orders/${orderId}/cancel`)
      
      // Update order status in state
      commit('UPDATE_ORDER_STATUS', { orderId, status: 'CANCELLED' })
      
      return true
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to cancel order')
      return false
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchPaymentMethods({ commit }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get('/payments/methods')
      commit('SET_PAYMENT_METHODS', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch payment methods')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async initiatePayment({ commit }, { orderId, paymentMethod }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post('/payments/initiate', {
        orderId,
        paymentMethod
      })
      
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to initiate payment')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async verifyPaymentStatus({ commit }, orderId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/payments/verify/${orderId}`)
      
      // If payment is successful, update order status
      if (response.data.success) {
        commit('UPDATE_ORDER_STATUS', { orderId, status: 'PAID' })
      }
      
      return response.data
    } catch (error) {
      commit('SET_ERROR', 'Failed to verify payment status')
      return { success: false }
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async requestRefund({ commit }, { orderId, reason }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post('/payments/refund', {
        orderId,
        reason
      })
      
      // Update order status in state
      commit('UPDATE_ORDER_STATUS', { orderId, status: 'REFUND_REQUESTED' })
      
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to request refund')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const mutations = {
  SET_ORDERS(state, orders) {
    state.orders = orders
  },
  SET_CURRENT_ORDER(state, order) {
    state.currentOrder = order
  },
  SET_PAYMENT_METHODS(state, methods) {
    state.paymentMethods = methods
  },
  UPDATE_ORDER_STATUS(state, { orderId, status }) {
    // Update in orders array
    const orderIndex = state.orders.findIndex(order => order.orderId === orderId)
    if (orderIndex !== -1) {
      Vue.set(state.orders[orderIndex], 'status', status)
    }
    
    // Update current order if it matches
    if (state.currentOrder && state.currentOrder.orderId === orderId) {
      Vue.set(state.currentOrder, 'status', status)
    }
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_ERROR(state, error) {
    state.error = error
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}