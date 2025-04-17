import axios from 'axios'

export default {
  /**
   * 获取所有课程
   * @param {Object} params - 过滤参数 (category, level, price, search)
   * @returns {Promise}
   */
  getCourses(params = {}) {
    return axios.get('/courses', { params })
  },
  
  /**
   * 获取课程详情
   * @param {String} courseId - 课程ID
   * @returns {Promise}
   */
  getCourseById(courseId) {
    return axios.get(`/courses/${courseId}`)
  },
  
  /**
   * 获取热门课程
   * @param {Number} limit - 限制数量
   * @returns {Promise}
   */
  getPopularCourses(limit = 6) {
    return axios.get('/courses/popular', { params: { limit } })
  },
  
  /**
   * 获取学生已报名的课程
   * @returns {Promise}
   */
  getEnrolledCourses() {
    return axios.get('/courses/enrolled')
  },
  
  /**
   * 获取教师的教授课程
   * @param {String} teacherId - 教师ID
   * @returns {Promise}
   */
  getTeachingCourses(teacherId) {
    return axios.get(`/courses/teacher/${teacherId}`)
  },
  
  /**
   * 创建新课程
   * @param {Object} courseData - 课程数据
   * @returns {Promise}
   */
  createCourse(courseData) {
    return axios.post('/courses', courseData)
  },
  
  /**
   * 更新课程信息
   * @param {String} courseId - 课程ID
   * @param {Object} courseData - 更新的课程数据
   * @returns {Promise}
   */
  updateCourse(courseId, courseData) {
    return axios.put(`/courses/${courseId}`, courseData)
  },
  
  /**
   * 删除课程
   * @param {String} courseId - 课程ID
   * @returns {Promise}
   */
  deleteCourse(courseId) {
    return axios.delete(`/courses/${courseId}`)
  },
  
  /**
   * 报名课程
   * @param {String} courseId - 课程ID
   * @returns {Promise}
   */
  enrollCourse(courseId) {
    return axios.post(`/courses/${courseId}/enroll`)
  },
  
  /**
   * 添加课程章节
   * @param {String} courseId - 课程ID
   * @param {Object} sectionData - 章节数据
   * @returns {Promise}
   */
  addSection(courseId, sectionData) {
    return axios.post(`/courses/${courseId}/sections`, sectionData)
  },
  
  /**
   * 添加课程课时
   * @param {String} courseId - 课程ID
   * @param {String} sectionId - 章节ID
   * @param {Object} lessonData - 课时数据
   * @returns {Promise}
   */
  addLesson(courseId, sectionId, lessonData) {
    return axios.post(`/courses/${courseId}/sections/${sectionId}/lessons`, lessonData)
  },
  
  /**
   * 上传课程资料
   * @param {String} courseId - 课程ID
   * @param {Object} materialData - 资料数据
   * @param {File} file - 文件
   * @returns {Promise}
   */
  uploadMaterial(courseId, materialData, file) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('title', materialData.title)
    formData.append('description', materialData.description || '')
    formData.append('type', materialData.type || 'OTHER')
    
    return axios.post(`/courses/${courseId}/materials`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  /**
   * 添加课程作业
   * @param {String} courseId - 课程ID
   * @param {Object} assignmentData - 作业数据
   * @returns {Promise}
   */
  addAssignment(courseId, assignmentData) {
    return axios.post(`/courses/${courseId}/assignments`, assignmentData)
  },
  
  /**
   * 提交课程评价
   * @param {String} courseId - 课程ID
   * @param {Object} reviewData - 评价数据
   * @returns {Promise}
   */
  submitReview(courseId, reviewData) {
    return axios.post(`/courses/${courseId}/reviews`, reviewData)
  },
  
  /**
   * 获取课程评价
   * @param {String} courseId - 课程ID
   * @returns {Promise}
   */
  getCourseReviews(courseId) {
    return axios.get(`/courses/${courseId}/reviews`)
  },
  
  /**
   * 获取课程进度
   * @param {String} courseId - 课程ID
   * @returns {Promise}
   */
  getCourseProgress(courseId) {
    return axios.get(`/courses/${courseId}/progress`)
  },
  
  /**
   * 保存课时进度
   * @param {Object} progressData - 进度数据
   * @returns {Promise}
   */
  saveProgress(progressData) {
    return axios.post('/courses/progress', progressData)
  },
  
  /**
   * 获取课时进度
   * @param {String} courseId - 课程ID
   * @param {String} lessonId - 课时ID
   * @returns {Promise}
   */
  getLessonProgress(courseId, lessonId) {
    return axios.get(`/courses/${courseId}/lessons/${lessonId}/progress`)
  }
}