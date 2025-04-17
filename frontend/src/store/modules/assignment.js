import axios from 'axios'

const state = {
  assignments: [],
  submissions: {},  // assignmentId -> submissions[]
  currentAssignment: null,
  currentSubmission: null,
  loading: false,
  error: null
}

const getters = {
  allAssignments: state => state.assignments,
  submissionsByAssignment: state => assignmentId => state.submissions[assignmentId] || [],
  currentAssignment: state => state.currentAssignment,
  currentSubmission: state => state.currentSubmission,
  loading: state => state.loading,
  error: state => state.error
}

const actions = {
  async fetchCourseAssignments({ commit }, courseId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/courses/${courseId}/assignments`)
      commit('SET_ASSIGNMENTS', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch assignments')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchAssignmentById({ commit }, assignmentId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/assignments/${assignmentId}`)
      commit('SET_CURRENT_ASSIGNMENT', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch assignment details')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchSubmissionsByAssignment({ commit }, assignmentId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/assignments/${assignmentId}/submissions`)
      commit('SET_SUBMISSIONS', { assignmentId, submissions: response.data })
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch submissions')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async fetchSubmissionById({ commit }, submissionId) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.get(`/submissions/${submissionId}`)
      commit('SET_CURRENT_SUBMISSION', response.data)
    } catch (error) {
      commit('SET_ERROR', 'Failed to fetch submission details')
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async submitAssignment({ commit }, { assignmentId, submissionData, file }) {
    try {
      commit('SET_LOADING', true)
      
      let response
      
      if (file) {
        const formData = new FormData()
        formData.append('content', submissionData.content || '')
        formData.append('file', file)
        
        response = await axios.post(`/assignments/${assignmentId}/submit`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
      } else {
        response = await axios.post(`/assignments/${assignmentId}/submit`, submissionData)
      }
      
      commit('SET_CURRENT_SUBMISSION', response.data)
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to submit assignment')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  },
  
  async gradeSubmission({ commit }, { submissionId, gradeData }) {
    try {
      commit('SET_LOADING', true)
      const response = await axios.post(`/submissions/${submissionId}/grade`, gradeData)
      
      // Update the submission in the state
      commit('UPDATE_SUBMISSION', response.data)
      commit('SET_CURRENT_SUBMISSION', response.data)
      
      return response.data
    } catch (error) {
      commit('SET_ERROR', error.response ? error.response.data.message : 'Failed to grade submission')
      return null
    } finally {
      commit('SET_LOADING', false)
    }
  }
}

const mutations = {
  SET_ASSIGNMENTS(state, assignments) {
    state.assignments = assignments
  },
  SET_CURRENT_ASSIGNMENT(state, assignment) {
    state.currentAssignment = assignment
  },
  SET_SUBMISSIONS(state, { assignmentId, submissions }) {
    Vue.set(state.submissions, assignmentId, submissions)
  },
  SET_CURRENT_SUBMISSION(state, submission) {
    state.currentSubmission = submission
  },
  UPDATE_SUBMISSION(state, updatedSubmission) {
    if (state.submissions[updatedSubmission.assignmentId]) {
      const index = state.submissions[updatedSubmission.assignmentId].findIndex(
        s => s.submissionId === updatedSubmission.submissionId
      )
      
      if (index !== -1) {
        Vue.set(state.submissions[updatedSubmission.assignmentId], index, updatedSubmission)
      }
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