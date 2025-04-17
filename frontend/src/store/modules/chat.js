import axios from 'axios'

const state = {
  chatRooms: {},  // roomId -> room
  currentRoomId: null,
  messages: {},   // roomId -> messages[]
  loading: false,
  error: null
}

const getters = {
  allChatRooms: state => Object.values(state.chatRooms),
  currentRoomId: state => state.currentRoomId,
  currentRoom: state => state.chatRooms[state.currentRoomId] || null,
  messagesByRoom: state => roomId => state.messages[roomId] || [],
  currentMessages: state => state.messages[state.currentRoomId] || [],
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async fetchChatRoomByCourse({ commit }, courseId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/chat/rooms/course/${courseId}`)
      
      if (response.data) {
        commit('ADD_CHAT_ROOM', response.data)
        return response.data
      } else {
        // 如果课程没有聊天室，尝试创建一个
        const createResponse = await axios.post(`/chat/rooms/course/${courseId}`)
        commit('ADD_CHAT_ROOM', createResponse.data)
        return createResponse.data
      }
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch chat room')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchChatHistory({ commit }, roomId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/chat/rooms/${roomId}/messages`)
      commit('SET_MESSAGES', { roomId, messages: response.data })
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch chat history')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async sendMessage({ commit, rootGetters }, { roomId, content }) {
    try {
      const userId = rootGetters['user/currentUser'].userId
      const response = await axios.post(`/chat/rooms/${roomId}/messages`, {
        userId,
        content
      })
      
      // 实际应用中，消息会通过WebSocket更新，这里为了方便直接添加到本地状态
      commit('ADD_MESSAGE', { roomId, message: response.data })
      return response.data
    } catch (error) {
      commit('SET_ERROR', 'Failed to send message')
      return null
    }
  },
  
  setCurrentRoom({ commit }, roomId) {
    commit('SET_CURRENT_ROOM', roomId)
  },
  
  // WebSocket处理新消息
  receiveMessage({ commit }, message) {
    commit('ADD_MESSAGE', { roomId: message.roomId, message })
  }
}

const mutations = {
  ADD_CHAT_ROOM(state, room) {
    Vue.set(state.chatRooms, room.roomId, room)
  },
  SET_CURRENT_ROOM(state, roomId) {
    state.currentRoomId = roomId
  },
  SET_MESSAGES(state, { roomId, messages }) {
    Vue.set(state.messages, roomId, messages)
  },
  ADD_MESSAGE(state, { roomId, message }) {
    if (!state.messages[roomId]) {
      Vue.set(state.messages, roomId, [])
    }
    state.messages[roomId].push(message)
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