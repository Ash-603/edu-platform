import axios from 'axios'

const state = {
  notifications: [],
  unreadCount: 0,
  loading: false,
  error: null
}

const getters = {
  allNotifications: state => state.notifications,
  unreadNotifications: state => state.notifications.filter(n => !n.read),
  unreadCount: state => state.unreadCount,
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async fetchNotifications({ commit, rootGetters }) {
    try {
      commit('SET_LOADING', true)
      const userId = rootGetters['user/currentUser'].userId
      const response = await axios.get(`/notifications/${userId}`)
      commit('SET_NOTIFICATIONS', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch notifications')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchUnreadCount({ commit, rootGetters }) {
    try {
      const userId = rootGetters['user/currentUser'].userId
      const response = await axios.get(`/notifications/${userId}/unread`)
      commit('SET_UNREAD_COUNT', response.data.length)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch unread count')
    }
  },
  
  async markAsRead({ commit }, notificationId) {
    try {
      await axios.post(`/notifications/${notificationId}/read`)
      commit('MARK_NOTIFICATION_READ', notificationId)
    } catch (error) {
      commit('SET_ERROR', 'Failed to mark notification as read')
    }
  },
  
  async markAllAsRead({ commit, rootGetters }) {
    try {
      const userId = rootGetters['user/currentUser'].userId
      await axios.post(`/notifications/${userId}/read-all`)
      commit('MARK_ALL_READ')
    } catch (error) {
      commit('SET_ERROR', 'Failed to mark all notifications as read')
    }
  },
  
  // WebSocket处理新通知
  receiveNotification({ commit }, notification) {
    commit('ADD_NOTIFICATION', notification)
  }
}

const mutations = {
  SET_NOTIFICATIONS(state, notifications) {
    state.notifications = notifications
    state.unreadCount = notifications.filter(n => !n.read).length
  },
  ADD_NOTIFICATION(state, notification) {
    state.notifications.unshift(notification)
    if (!notification.read) {
      state.unreadCount++
    }
  },
  MARK_NOTIFICATION_READ(state, notificationId) {
    const notification = state.notifications.find(n => n.notificationId === notificationId)
    if (notification && !notification.read) {
      notification.read = true
      state.unreadCount = Math.max(0, state.unreadCount - 1)
    }
  },
  MARK_ALL_READ(state) {
    state.notifications.forEach(notification => {
      notification.read = true
    })
    state.unreadCount = 0
  },
  SET_UNREAD_COUNT(state, count) {
    state.unreadCount = count
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