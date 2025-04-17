import axios from 'axios'

const state = {
  token: null,
  currentUser: null,
  loading: false,
  error: null
}

const getters = {
  isAuthenticated: state => !!state.token,
  currentUser: state => state.currentUser,
  isTeacher: state => state.currentUser && state.currentUser.type === 'TEACHER',
  isStudent: state => state.currentUser && state.currentUser.type === 'STUDENT',
  token: state => state.token,
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async login({ commit }, credentials) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post('/users/login', credentials)
      
      if (response.data.token) {
        commit('SET_TOKEN', response.data.token)
        commit('SET_CURRENT_USER', response.data.user)
        return true
      } else {
        commit('SET_ERROR', 'Invalid credentials')
        return false
      }
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Login failed')
      return false
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async register({ commit }, userData) {
    try {
      commit('SET_LOADING', true)
      let endpoint = '/users/register/'
      
      if (userData.type === 'STUDENT') {
        endpoint += 'student'
      } else if (userData.type === 'TEACHER') {
        endpoint += 'teacher'
      } else {
        throw new Error('Invalid user type')
      }
      
      const response = await axios.post(endpoint, userData)
      
      if (response.data.success) {
        return true
      } else {
        commit('SET_ERROR', 'Registration failed')
        return false
      }
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Registration failed')
      return false
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchCurrentUser({ commit, state }) {
    if (!state.token) return
    
    try {
      commit('SET_LOADING', true)
      const response = await axios.get('/users/me')
      commit('SET_CURRENT_USER', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch user data')
      commit('LOGOUT')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async updateProfile({ commit }, profileData) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.put(`/users/${profileData.userId}`, profileData)
      commit('SET_CURRENT_USER', response.data)
      return true
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to update profile')
      return false
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  logout({ commit }) {
    commit('LOGOUT')
  }
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
  },
  SET_CURRENT_USER(state, user) {
    state.currentUser = user
  },
  SET_LOADING(state, loading) {
    state.loading = loading
  },
  SET_ERROR(state, error) {
    state.error = error
  },
  LOGOUT(state) {
    state.token = null
    state.currentUser = null
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}