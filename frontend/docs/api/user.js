import axios from 'axios'

export default {
  /**
   * 用户登录
   * @param {Object} credentials - 登录凭证
   * @returns {Promise}
   */
  login(credentials) {
    return axios.post('/users/login', credentials)
  },
  
  /**
   * 用户注册
   * @param {Object} userData - 用户数据
   * @returns {Promise}
   */
  register(userData) {
    const endpoint = userData.type === 'TEACHER' ? '/users/register/teacher' : '/users/register/student'
    return axios.post(endpoint, userData)
  },
  
  /**
   * 获取当前用户信息
   * @returns {Promise}
   */
  getCurrentUser() {
    return axios.get('/users/me')
  },
  
  /**
   * 更新用户信息
   * @param {String} userId - 用户ID
   * @param {Object} userData - 更新的用户数据
   * @returns {Promise}
   */
  updateUser(userId, userData) {
    return axios.put(`/users/${userId}`, userData)
  },
  
  /**
   * 更新用户密码
   * @param {Object} passwordData - 密码数据
   * @returns {Promise}
   */
  updatePassword(passwordData) {
    return axios.put('/users/password', passwordData)
  },
  
  /**
   * 请求重置密码
   * @param {String} email - 用户邮箱
   * @returns {Promise}
   */
  requestPasswordReset(email) {
    return axios.post('/users/password/reset-request', { email })
  },
  
  /**
   * 验证重置密码令牌
   * @param {String} token - 重置令牌
   * @returns {Promise}
   */
  validateResetToken(token) {
    return axios.get(`/users/password/validate-token/${token}`)
  },
  
  /**
   * 重置密码
   * @param {Object} resetData - 重置数据
   * @returns {Promise}
   */
  resetPassword(resetData) {
    return axios.post('/users/password/reset', resetData)
  },
  
  /**
   * 上传用户头像
   * @param {File} file - 头像文件
   * @returns {Promise}
   */
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('avatar', file)
    
    return axios.post('/users/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  /**
   * 获取用户通知
   * @returns {Promise}
   */
  getNotifications() {
    return axios.get('/notifications')
  },
  
  /**
   * 获取未读通知数量
   * @returns {Promise}
   */
  getUnreadNotificationCount() {
    return axios.get('/notifications/unread/count')
  },
  
  /**
   * 将通知标记为已读
   * @param {String} notificationId - 通知ID
   * @returns {Promise}
   */
  markNotificationAsRead(notificationId) {
    return axios.post(`/notifications/${notificationId}/read`)
  },
  
  /**
   * 将所有通知标记为已读
   * @returns {Promise}
   */
  markAllNotificationsAsRead() {
    return axios.post('/notifications/read-all')
  },
  
  /**
   * 获取用户学习统计数据
   * @returns {Promise}
   */
  getLearningStats() {
    return axios.get('/users/learning-stats')
  },
  
  /**
   * 获取用户教学统计数据
   * @returns {Promise}
   */
  getTeachingStats() {
    return axios.get('/users/teaching-stats')
  },
  
  /**
   * 获取用户收到的证书
   * @returns {Promise}
   */
  getUserCertificates() {
    return axios.get('/users/certificates')
  },
  
  /**
   * 验证证书
   * @param {String} certificateId - 证书ID
   * @param {String} serialNumber - 序列号
   * @returns {Promise}
   */
  verifyCertificate(certificateId, serialNumber) {
    return axios.post('/certificates/verify', {
      certificateId,
      serialNumber
    })
  }
}