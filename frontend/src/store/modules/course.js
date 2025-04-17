import axios from 'axios'

const state = {
  courses: [],
  currentCourse: null,
  enrolledCourses: [],
  teachingCourses: [],
  loading: false,
  error: null
}

const getters = {
  allCourses: state => state.courses,
  currentCourse: state => state.currentCourse,
  enrolledCourses: state => state.enrolledCourses,
  teachingCourses: state => state.teachingCourses,
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async fetchCourses({ commit }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get('/courses')
      commit('SET_COURSES', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch courses')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchCourseById({ commit }, courseId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/courses/${courseId}`)
      commit('SET_CURRENT_COURSE', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch course details')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchEnrolledCourses({ commit }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get('/courses/enrolled')
      commit('SET_ENROLLED_COURSES', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch enrolled courses')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchTeachingCourses({ commit, rootGetters }) {
    try {
      commit('SET_LOADING', true)
      const teacherId = rootGetters['user/currentUser'].userId
      const response = await axios.get(`/courses/teacher/${teacherId}`)
      commit('SET_TEACHING_COURSES', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch teaching courses')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async createCourse({ commit, rootGetters }, courseData) {
    try {
      commit('SET_LOADING', true)
      const teacherId = rootGetters['user/currentUser'].userId
      const payload = {
        ...courseData,
        teacherId
      }
      const response = await axios.post('/courses', payload)
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to create course')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async enrollCourse({ commit, rootGetters }, courseId) {
    try {
      commit('SET_LOADING', true)
      const studentId = rootGetters['user/currentUser'].userId
      await axios.post(`/courses/${courseId}/enroll`, { studentId })
      return true
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to enroll in course')
      return false
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async addAssignment({ commit }, { courseId, assignmentData }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post(`/courses/${courseId}/assignments`, assignmentData)
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to add assignment')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async uploadCourseMaterial({ commit }, { courseId, materialData, file }) {
    try {
      commit('SET_LOADING', true)
      
      const formData = new FormData()
      formData.append('file', file)
      formData.append('title', materialData.title)
      formData.append('type', materialData.type)
      
      const response = await axios.post(`/courses/${courseId}/materials`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to upload material')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const mutations = {
  SET_COURSES(state, courses) {
    state.courses = courses
  },
  SET_CURRENT_COURSE(state, course) {
    state.currentCourse = course
  },
  SET_ENROLLED_COURSES(state, courses) {
    state.enrolledCourses = courses
  },
  SET_TEACHING_COURSES(state, courses) {
    state.teachingCourses = courses
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