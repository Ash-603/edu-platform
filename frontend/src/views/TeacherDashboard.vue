<template>
    <div>
      <v-container>
        <v-row>
          <v-col cols="12">
            <h1 class="text-h4 font-weight-bold">Teacher Dashboard</h1>
            <v-breadcrumbs :items="breadcrumbs" class="pa-0 mb-4"></v-breadcrumbs>
          </v-col>
        </v-row>
        
        <v-row>
          <!-- Stats Cards -->
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card primary lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="primary">mdi-book-open-variant</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.courses }}</div>
                <div class="text-subtitle-1">Active Courses</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card indigo lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="indigo">mdi-account-group</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.students }}</div>
                <div class="text-subtitle-1">Total Students</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card red lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="red">mdi-comment-multiple</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.reviews }}</div>
                <div class="text-subtitle-1">Course Reviews</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card green lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="green">mdi-currency-usd</v-icon>
                <div class="text-h4 font-weight-bold mt-2">${{ stats.earnings.toFixed(2) }}</div>
                <div class="text-subtitle-1">Total Earnings</div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        
        <v-row>
          <v-col cols="12" md="8">
            <!-- Course Management -->
            <v-card class="mb-4">
              <v-card-title class="d-flex justify-space-between">
                <div>
                  <v-icon left>mdi-teach</v-icon>
                  My Courses
                </div>
                <v-btn color="primary" to="/teacher/courses/create">
                  <v-icon left>mdi-plus</v-icon>
                  Create Course
                </v-btn>
              </v-card-title>
              
              <v-data-table
                :headers="courseHeaders"
                :items="teachingCourses"
                :loading="loading"
                :items-per-page="5"
                class="elevation-0"
              >
                <template v-slot:item.thumbnail="{ item }">
                  <v-avatar rounded>
                    <v-img :src="item.thumbnail || require('@/assets/course-placeholder.jpg')"></v-img>
                  </v-avatar>
                </template>
                
                <template v-slot:item.status="{ item }">
                  <v-chip
                    small
                    :color="getCourseStatusColor(item.status)"
                    text-color="white"
                  >
                    {{ item.status }}
                  </v-chip>
                </template>
                
                <template v-slot:item.students="{ item }">
                  {{ item.enrolledStudents ? item.enrolledStudents.length : 0 }}
                </template>
                
                <template v-slot:item.rating="{ item }">
                  <div class="d-flex align-center">
                    <v-rating
                      :value="item.rating || 0"
                      color="amber"
                      dense
                      half-increments
                      readonly
                      size="14"
                    ></v-rating>
                    <span class="ml-1">({{ item.ratingCount || 0 }})</span>
                  </div>
                </template>
                
                <template v-slot:item.actions="{ item }">
                  <v-btn
                    icon
                    small
                    :to="`/courses/${item.courseId}`"
                    title="View Course"
                  >
                    <v-icon>mdi-eye</v-icon>
                  </v-btn>
                  
                  <v-btn
                    icon
                    small
                    :to="`/teacher/courses/${item.courseId}/edit`"
                    title="Edit Course"
                  >
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  
                  <v-btn
                    icon
                    small
                    :to="`/chat/course/${item.courseId}`"
                    title="Course Discussion"
                  >
                    <v-icon>mdi-chat</v-icon>
                  </v-btn>
                </template>
              </v-data-table>
            </v-card>
            
            <!-- Student Management -->
            <v-card>
              <v-card-title>
                <v-icon left>mdi-account-group</v-icon>
                My Students
              </v-card-title>
              
              <v-data-table
                :headers="studentHeaders"
                :items="students"
                :loading="loading"
                :search="studentSearch"
                :items-per-page="10"
                class="elevation-0"
              >
                <template v-slot:top>
                  <v-card-text>
                    <v-row>
                      <v-col cols="12" md="6">
                        <v-select
                          v-model="courseFilter"
                          :items="courseFilterOptions"
                          label="Filter by Course"
                          clearable
                        ></v-select>
                      </v-col>
                      <v-col cols="12" md="6">
                        <v-text-field
                          v-model="studentSearch"
                          label="Search Students"
                          prepend-icon="mdi-magnify"
                          clearable
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </template>
                
                <template v-slot:item.avatar="{ item }">
                  <v-avatar>
                    <v-img :src="item.avatar || 'https://via.placeholder.com/40'"></v-img>
                  </v-avatar>
                </template>
                
                <template v-slot:item.progress="{ item }">
                  <v-progress-linear
                    :value="item.progress"
                    height="12"
                    color="primary"
                  >
                    <template v-slot:default="{ value }">
                      <span class="white--text">{{ Math.ceil(value) }}%</span>
                    </template>
                  </v-progress-linear>
                </template>
                
                <template v-slot:item.actions="{ item }">
                  <v-btn
                    icon
                    small
                    @click="showStudentDetails(item)"
                    title="View Student Details"
                  >
                    <v-icon>mdi-account-details</v-icon>
                  </v-btn>
                  
                  <v-btn
                    icon
                    small
                    :to="`/chat/user/${item.userId}`"
                    title="Message Student"
                  >
                    <v-icon>mdi-message-text</v-icon>
                  </v-btn>
                </template>
              </v-data-table>
            </v-card>
          </v-col>
          
          <v-col cols="12" md="4">
            <!-- Recent Activities -->
            <v-card class="mb-4">
              <v-card-title>
                <v-icon left>mdi-clock-time-four</v-icon>
                Recent Activities
              </v-card-title>
              
              <v-card-text>
                <v-timeline dense>
                  <v-timeline-item
                    v-for="(activity, index) in recentActivity"
                    :key="index"
                    :color="activity.color"
                    small
                  >
                    <div class="d-flex justify-space-between">
                      <div>
                        <div class="font-weight-medium">{{ activity.title }}</div>
                        <div class="text-caption">{{ activity.description }}</div>
                      </div>
                      <div class="text-caption">{{ formatDate(activity.timestamp) }}</div>
                    </div>
                  </v-timeline-item>
                </v-timeline>
              </v-card-text>
            </v-card>
            
            <!-- Pending Assignments -->
            <v-card class="mb-4">
              <v-card-title>
                <v-icon left>mdi-clipboard-text</v-icon>
                Pending Assignments
                <v-spacer></v-spacer>
                <v-chip color="error" v-if="pendingAssignments.length > 0">
                  {{ pendingAssignments.length }}
                </v-chip>
              </v-card-title>
              
              <v-card-text>
                <div v-if="pendingAssignments.length > 0">
                  <v-list dense>
                    <v-list-item
                      v-for="assignment in pendingAssignments"
                      :key="assignment.submissionId"
                      :to="`/teacher/assignments/${assignment.assignmentId}/submissions/${assignment.submissionId}`"
                    >
                      <v-list-item-content>
                        <v-list-item-title>{{ assignment.title }}</v-list-item-title>
                        <v-list-item-subtitle>
                          {{ assignment.studentName }} - {{ assignment.courseName }}
                        </v-list-item-subtitle>
                        <v-list-item-subtitle class="text-caption">
                          Submitted {{ formatDate(assignment.submittedAt) }}
                        </v-list-item-subtitle>
                      </v-list-item-content>
                      
                      <v-list-item-action>
                        <v-btn
                          x-small
                          color="primary"
                          :to="`/teacher/assignments/${assignment.assignmentId}/submissions/${assignment.submissionId}`"
                        >
                          Grade
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                  </v-list>
                </div>
                <div v-else class="text-center py-4">
                  <v-icon large color="green">mdi-check-circle</v-icon>
                  <p class="mt-2 grey--text text--darken-1">
                    No pending assignments to grade.
                  </p>
                </div>
              </v-card-text>
            </v-card>
            
            <!-- Earnings Chart -->
            <v-card>
              <v-card-title>
                <v-icon left>mdi-chart-line</v-icon>
                Monthly Earnings
                <v-spacer></v-spacer>
                <v-btn text color="primary" to="/teacher/earnings">
                  View Details
                </v-btn>
              </v-card-title>
              
              <v-card-text>
                <canvas ref="earningsChart" width="400" height="200"></canvas>
                
                <div class="d-flex justify-space-between mt-4">
                  <div>
                    <div class="text-overline mb-1">This Month</div>
                    <div class="text-h6">${{ monthlyEarnings.thisMonth.toFixed(2) }}</div>
                  </div>
                  
                  <div>
                    <div class="text-overline mb-1">Last Month</div>
                    <div class="text-h6">${{ monthlyEarnings.lastMonth.toFixed(2) }}</div>
                  </div>
                  
                  <div>
                    <div class="text-overline mb-1">Growth</div>
                    <div class="text-h6" :class="earningsGrowth >= 0 ? 'green--text' : 'red--text'">
                      {{ earningsGrowth >= 0 ? '+' : '' }}{{ earningsGrowth.toFixed(1) }}%
                    </div>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
      
      <!-- Student Details Dialog -->
      <v-dialog v-model="studentDialog" max-width="800">
        <v-card v-if="selectedStudent">
          <v-card-title class="headline">
            Student Details
            <v-spacer></v-spacer>
            <v-btn icon @click="studentDialog = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-card-title>
          
          <v-card-text>
            <v-row>
              <v-col cols="12" md="4" class="text-center">
                <v-avatar size="120">
                  <v-img :src="selectedStudent.avatar || 'https://via.placeholder.com/120'"></v-img>
                </v-avatar>
                <h3 class="text-h6 mt-3">{{ selectedStudent.name }}</h3>
                <p class="text-subtitle-2">{{ selectedStudent.email }}</p>
                
                <v-btn
                  color="primary"
                  class="mt-3"
                  :to="`/chat/user/${selectedStudent.userId}`"
                >
                  <v-icon left>mdi-message-text</v-icon>
                  Message
                </v-btn>
              </v-col>
              
              <v-col cols="12" md="8">
                <h3 class="text-h6 mb-3">Enrolled Courses</h3>
                
                <v-list dense>
                  <v-list-item
                    v-for="course in selectedStudent.courses"
                    :key="course.courseId"
                  >
                    <v-list-item-avatar>
                      <v-img :src="course.thumbnail || require('@/assets/course-placeholder.jpg')"></v-img>
                    </v-list-item-avatar>
                    
                    <v-list-item-content>
                      <v-list-item-title>{{ course.title }}</v-list-item-title>
                      <v-list-item-subtitle>
                        Enrolled: {{ formatDate(course.enrolledAt) }}
                      </v-list-item-subtitle>
                      <v-progress-linear
                        :value="course.progress"
                        height="8"
                        class="mt-1"
                      >
                        <template v-slot:default="{ value }">
                          <span class="white--text text-caption">{{ Math.ceil(value) }}%</span>
                        </template>
                      </v-progress-linear>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                
                <h3 class="text-h6 mb-3 mt-4">Assignment Performance</h3>
                
                <v-simple-table>
                  <template v-slot:default>
                    <thead>
                      <tr>
                        <th>Assignment</th>
                        <th>Grade</th>
                        <th>Submitted</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="assignment in selectedStudent.assignments" :key="assignment.submissionId">
                        <td>{{ assignment.title }}</td>
                        <td>
                          <v-chip
                            x-small
                            :color="getGradeColor(assignment.grade)"
                            text-color="white"
                          >
                            {{ assignment.grade }}/100
                          </v-chip>
                        </td>
                        <td>{{ formatDate(assignment.submittedAt) }}</td>
                      </tr>
                    </tbody>
                  </template>
                </v-simple-table>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-dialog>
    </div>
  </template>
  
  <script>
  import { mapGetters } from 'vuex'
  import moment from 'moment'
  import Chart from 'chart.js'
  
  export default {
    data() {
      return {
        loading: false,
        studentSearch: '',
        courseFilter: null,
        studentDialog: false,
        selectedStudent: null,
        
        breadcrumbs: [
          {
            text: 'Home',
            disabled: false,
            href: '/'
          },
          {
            text: 'Teacher Dashboard',
            disabled: true
          }
        ],
        
        stats: {
        courses: 0,
        students: 0,
        reviews: 0,
        earnings: 0
      },
      
      courseHeaders: [
        { text: '', value: 'thumbnail', sortable: false, width: '50px' },
        { text: 'Course Title', value: 'title' },
        { text: 'Status', value: 'status' },
        { text: 'Students', value: 'students', align: 'center' },
        { text: 'Price', value: 'price', align: 'center' },
        { text: 'Rating', value: 'rating', align: 'center' },
        { text: 'Actions', value: 'actions', sortable: false, align: 'center' }
      ],
      
      studentHeaders: [
        { text: '', value: 'avatar', sortable: false, width: '50px' },
        { text: 'Name', value: 'name' },
        { text: 'Email', value: 'email' },
        { text: 'Course', value: 'courseName' },
        { text: 'Enrolled', value: 'enrolledAt' },
        { text: 'Progress', value: 'progress', align: 'center' },
        { text: 'Actions', value: 'actions', sortable: false, align: 'center' }
      ],
      
      teachingCourses: [],
      students: [],
      recentActivity: [],
      pendingAssignments: [],
      
      monthlyEarnings: {
        thisMonth: 0,
        lastMonth: 0
      },
      earningsData: {
        labels: [],
        amounts: []
      }
    }
  },
  
  computed: {
    ...mapGetters('user', ['currentUser']),
    
    courseFilterOptions() {
      return this.teachingCourses.map(course => ({
        text: course.title,
        value: course.courseId
      }))
    },
    
    earningsGrowth() {
      if (this.monthlyEarnings.lastMonth === 0) return 0
      return ((this.monthlyEarnings.thisMonth - this.monthlyEarnings.lastMonth) / 
              this.monthlyEarnings.lastMonth) * 100
    }
  },
  
  created() {
    this.fetchDashboardData()
  },
  
  mounted() {
    this.initEarningsChart()
  },
  
  methods: {
    async fetchDashboardData() {
      this.loading = true
      
      try {
        // 获取教师统计数据
        const statsResponse = await this.$axios.get('/users/teaching-stats')
        this.stats = statsResponse.data
        
        // 获取教授的课程
        const coursesResponse = await this.$axios.get('/courses/teaching')
        this.teachingCourses = coursesResponse.data
        
        // 获取学生数据
        const studentsResponse = await this.$axios.get('/users/teacher/students')
        this.students = studentsResponse.data
        
        // 获取近期活动
        const activityResponse = await this.$axios.get('/users/teacher/activity')
        this.recentActivity = activityResponse.data
        
        // 获取待评分的作业
        const assignmentsResponse = await this.$axios.get('/assignments/pending-grading')
        this.pendingAssignments = assignmentsResponse.data
        
        // 获取收益数据
        const earningsResponse = await this.$axios.get('/teacher/earnings/monthly')
        this.monthlyEarnings = {
          thisMonth: earningsResponse.data.thisMonth,
          lastMonth: earningsResponse.data.lastMonth
        }
        this.earningsData = {
          labels: earningsResponse.data.chart.labels,
          amounts: earningsResponse.data.chart.amounts
        }
        
        // 更新收益图表
        this.updateEarningsChart()
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error)
      } finally {
        this.loading = false
      }
    },
    
    initEarningsChart() {
      const ctx = this.$refs.earningsChart.getContext('2d')
      this.earningsChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: [],
          datasets: [{
            label: 'Monthly Earnings',
            data: [],
            backgroundColor: 'rgba(33, 150, 243, 0.2)',
            borderColor: 'rgba(33, 150, 243, 1)',
            borderWidth: 2,
            pointBackgroundColor: 'rgba(33, 150, 243, 1)',
            tension: 0.4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true,
                callback: function(value) {
                  return '$' + value
                }
              }
            }]
          },
          tooltips: {
            callbacks: {
              label: function(tooltipItem, data) {
                return '$' + tooltipItem.yLabel
              }
            }
          }
        }
      })
    },
    
    updateEarningsChart() {
      if (!this.earningsChart) return
      
      this.earningsChart.data.labels = this.earningsData.labels
      this.earningsChart.data.datasets[0].data = this.earningsData.amounts
      this.earningsChart.update()
    },
    
    showStudentDetails(student) {
      this.selectedStudent = student
      this.studentDialog = true
    },
    
    formatDate(date) {
      return moment(date).format('MMM DD, YYYY')
    },
    
    getCourseStatusColor(status) {
      switch (status) {
        case 'PUBLISHED': return 'success'
        case 'DRAFT': return 'grey'
        case 'UNDER_REVIEW': return 'warning'
        case 'ARCHIVED': return 'error'
        default: return 'primary'
      }
    },
    
    getGradeColor(grade) {
      if (grade >= 90) return 'success'
      if (grade >= 75) return 'primary'
      if (grade >= 60) return 'warning'
      return 'error'
    }
  }
}
</script>

<style scoped>
.stat-card {
  transition: transform 0.3s;
}
.stat-card:hover {
  transform: translateY(-5px);
}
</style>