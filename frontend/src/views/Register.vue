<template>
    <div>
      <v-container>
        <v-row>
          <v-col cols="12">
            <h1 class="text-h4 font-weight-bold">Dashboard</h1>
            <v-breadcrumbs :items="breadcrumbs" class="pa-0 mb-4"></v-breadcrumbs>
          </v-col>
        </v-row>
        
        <v-row>
          <!-- Stats Cards -->
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card primary lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="primary">mdi-book-open-variant</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.enrolledCourses }}</div>
                <div class="text-subtitle-1">Enrolled Courses</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card purple lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="purple">mdi-clipboard-check</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.completedAssignments }}</div>
                <div class="text-subtitle-1">Completed Assignments</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card orange lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="orange">mdi-certificate</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.certificates }}</div>
                <div class="text-subtitle-1">Certificates Earned</div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <v-col cols="12" sm="6" md="3">
            <v-card class="stat-card green lighten-4" hover>
              <v-card-text class="text-center">
                <v-icon size="48" color="green">mdi-clock-outline</v-icon>
                <div class="text-h4 font-weight-bold mt-2">{{ stats.hoursLearned }}</div>
                <div class="text-subtitle-1">Hours Learned</div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
        
        <v-row>
          <!-- Learning Progress -->
          <v-col cols="12" md="8">
            <v-card class="mb-4">
              <v-card-title>
                <v-icon left>mdi-chart-timeline-variant</v-icon>
                Learning Progress
              </v-card-title>
              
              <v-card-text>
                <v-tabs v-model="progressTab">
                  <v-tab>Recent Courses</v-tab>
                  <v-tab>Upcoming Deadlines</v-tab>
                  <v-tab>Activity</v-tab>
                </v-tabs>
                
                <v-tabs-items v-model="progressTab">
                  <!-- Recent Courses Tab -->
                  <v-tab-item>
                    <div v-if="enrolledCourses.length > 0">
                      <v-list two-line>
                        <v-list-item
                          v-for="course in enrolledCourses.slice(0, 5)"
                          :key="course.courseId"
                          :to="`/courses/${course.courseId}`"
                        >
                          <v-list-item-avatar>
                            <v-img :src="course.thumbnail || require('@/assets/course-placeholder.jpg')"></v-img>
                          </v-list-item-avatar>
                          
                          <v-list-item-content>
                            <v-list-item-title>{{ course.title }}</v-list-item-title>
                            <v-list-item-subtitle>{{ course.teacherName }}</v-list-item-subtitle>
                            
                            <v-progress-linear
                              :value="course.progress || 0"
                              height="15"
                              class="mt-2"
                              color="primary"
                            >
                              <template v-slot:default="{ value }">
                                <span class="white--text">{{ Math.ceil(value) }}%</span>
                              </template>
                            </v-progress-linear>
                          </v-list-item-content>
                          
                          <v-list-item-action>
                            <v-btn
                              color="primary"
                              text
                              :to="`/courses/${course.courseId}`"
                            >
                              Continue
                            </v-btn>
                          </v-list-item-action>
                        </v-list-item>
                      </v-list>
                      
                      <div class="text-center mt-4">
                        <v-btn text color="primary" to="/courses/enrolled">
                          View All Courses
                        </v-btn>
                      </div>
                    </div>
                    <div v-else class="text-center py-4">
                      <v-icon large color="grey lighten-1">mdi-book-outline</v-icon>
                      <p class="mt-2 grey--text text--darken-1">You haven't enrolled in any courses yet.</p>
                      <v-btn color="primary" class="mt-2" to="/courses">Explore Courses</v-btn>
                    </div>
                  </v-tab-item>
                  
                  <!-- Upcoming Deadlines Tab -->
                  <v-tab-item>
                    <div v-if="upcomingDeadlines.length > 0">
                      <v-timeline dense>
                        <v-timeline-item
                          v-for="(deadline, index) in upcomingDeadlines"
                          :key="index"
                          :color="getDeadlineColor(deadline.daysLeft)"
                          small
                        >
                          <div class="d-flex justify-space-between">
                            <div>
                              <div class="font-weight-medium">{{ deadline.title }}</div>
                              <div class="text-caption">{{ deadline.courseName }}</div>
                            </div>
                            
                            <div :class="getDeadlineTextClass(deadline.daysLeft)">
                              <v-icon small :color="getDeadlineColor(deadline.daysLeft)" class="mr-1">
                                mdi-clock-outline
                              </v-icon>
                              {{ formatDeadline(deadline.daysLeft) }}
                            </div>
                          </div>
                        </v-timeline-item>
                      </v-timeline>
                    </div>
                    <div v-else class="text-center py-4">
                      <v-icon large color="green">mdi-check-circle</v-icon>
                      <p class="mt-2 grey--text text--darken-1">No upcoming deadlines. You're all caught up!</p>
                    </div>
                  </v-tab-item>
                  
                  <!-- Activity Tab -->
                  <v-tab-item>
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
                  </v-tab-item>
                </v-tabs-items>
              </v-card-text>
            </v-card>
            
            <!-- Certificates -->
            <v-card>
              <v-card-title>
                <v-icon left>mdi-certificate</v-icon>
                My Certificates
              </v-card-title>
              
              <v-card-text>
                <div v-if="certificates.length > 0">
                  <v-row>
                    <v-col
                      v-for="certificate in certificates"
                      :key="certificate.certificateId"
                      cols="12"
                      sm="6"
                      md="4"
                    >
                      <v-card outlined hover>
                        <v-img
                          src="@/assets/certificate-bg.jpg"
                          height="120"
                          class="white--text align-end"
                        >
                          <v-card-title class="pb-0">{{ certificate.courseName }}</v-card-title>
                        </v-img>
                        
                        <v-card-text class="text-center">
                          <p class="mb-0">Issued on {{ formatDate(certificate.issueDate) }}</p>
                        </v-card-text>
                        
                        <v-card-actions>
                          <v-btn text color="primary" @click="viewCertificate(certificate)">
                            <v-icon left>mdi-eye</v-icon>
                            View
                          </v-btn>
                          <v-spacer></v-spacer>
                          <v-btn text color="primary" @click="downloadCertificate(certificate)">
                            <v-icon left>mdi-download</v-icon>
                            Download
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-col>
                  </v-row>
                </div>
                <div v-else class="text-center py-4">
                  <v-icon large color="grey lighten-1">mdi-certificate-outline</v-icon>
                  <p class="mt-2 grey--text text--darken-1">
                    Complete courses to earn certificates.
                  </p>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
          
          <!-- Sidebar -->
          <v-col cols="12" md="4">
            <!-- Upcoming Classes -->
            <v-card class="mb-4">
              <v-card-title>
                <v-icon left>mdi-calendar</v-icon>
                Upcoming Live Classes
              </v-card-title>
              
              <v-card-text>
                <div v-if="upcomingClasses.length > 0">
                  <v-list dense>
                    <v-list-item
                      v-for="(liveClass, index) in upcomingClasses"
                      :key="index"
                    >
                      <v-list-item-content>
                        <v-list-item-title>{{ liveClass.title }}</v-list-item-title>
                        <v-list-item-subtitle>
                          {{ liveClass.courseName }}
                        </v-list-item-subtitle>
                        <v-list-item-subtitle>
                          <v-icon small>mdi-clock-outline</v-icon>
                          {{ formatDatetime(liveClass.startTime) }}
                        </v-list-item-subtitle>
                      </v-list-item-content>
                      
                      <v-list-item-action>
                        <v-btn small outlined color="primary" :disabled="!liveClass.isActive">
                          {{ liveClass.isActive ? 'Join' : 'Remind' }}
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                  </v-list>
                </div>
                <div v-else class="text-center py-4">
                  <v-icon large color="grey lighten-1">mdi-calendar-blank</v-icon>
                  <p class="mt-2 grey--text text--darken-1">No upcoming live classes.</p>
                </div>
              </v-card-text>
            </v-card>
            
            <!-- Recent Messages -->
            <v-card class="mb-4">
              <v-card-title>
                <v-icon left>mdi-message-text</v-icon>
                Recent Messages
              </v-card-title>
              <v-card-text>
              <div v-if="recentMessages.length > 0">
                <v-list two-line>
                  <v-list-item
                    v-for="message in recentMessages"
                    :key="message.messageId"
                    :to="`/chat/${message.roomId}`"
                  >
                    <v-list-item-avatar>
                      <v-img :src="message.senderAvatar || 'https://via.placeholder.com/40'"></v-img>
                    </v-list-item-avatar>
                    
                    <v-list-item-content>
                      <v-list-item-title>{{ message.senderName }}</v-list-item-title>
                      <v-list-item-subtitle class="text-truncate">
                        {{ message.content }}
                      </v-list-item-subtitle>
                      <v-list-item-subtitle class="text-caption">
                        {{ formatDatetime(message.timestamp) }}
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                
                <div class="text-center mt-2">
                  <v-btn text color="primary" to="/chat">
                    View All Messages
                  </v-btn>
                </div>
              </div>
              <div v-else class="text-center py-4">
                <v-icon large color="grey lighten-1">mdi-chat-outline</v-icon>
                <p class="mt-2 grey--text text--darken-1">No recent messages.</p>
                <v-btn text color="primary" to="/chat">
                  Go to Chat
                </v-btn>
              </div>
            </v-card-text>
          </v-card>
          
          <!-- Recommended Courses -->
          <v-card>
            <v-card-title>
              <v-icon left>mdi-star</v-icon>
              Recommended for You
            </v-card-title>
            
            <v-card-text>
              <div v-if="recommendedCourses.length > 0">
                <v-list>
                  <v-list-item
                    v-for="course in recommendedCourses"
                    :key="course.courseId"
                    :to="`/courses/${course.courseId}`"
                  >
                    <v-list-item-avatar tile>
                      <v-img :src="course.thumbnail || require('@/assets/course-placeholder.jpg')"></v-img>
                    </v-list-item-avatar>
                    
                    <v-list-item-content>
                      <v-list-item-title>{{ course.title }}</v-list-item-title>
                      <v-list-item-subtitle>{{ course.teacherName }}</v-list-item-subtitle>
                      <div class="d-flex align-center">
                        <v-rating
                          :value="course.rating"
                          color="amber"
                          dense
                          half-increments
                          readonly
                          size="14"
                        ></v-rating>
                        <span class="ml-1 text-caption">
                          ({{ course.ratingCount }})
                        </span>
                      </div>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                
                <div class="text-center mt-2">
                  <v-btn text color="primary" to="/courses">
                    Explore More
                  </v-btn>
                </div>
              </div>
              <div v-else class="text-center py-4">
                <v-icon large color="grey lighten-1">mdi-book-search</v-icon>
                <p class="mt-2 grey--text text--darken-1">
                  Complete more courses to get personalized recommendations.
                </p>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
    
    <!-- Certificate Dialog -->
    <v-dialog v-model="certificateDialog" max-width="800">
      <v-card>
        <v-card-title class="headline">
          Certificate of Completion
          <v-spacer></v-spacer>
          <v-btn icon @click="certificateDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        
        <v-card-text v-if="selectedCertificate">
          <div class="certificate-container">
            <img src="@/assets/certificate-template.jpg" alt="Certificate" class="certificate-bg">
            <div class="certificate-content">
              <h2 class="certificate-title">Certificate of Completion</h2>
              <p class="certificate-text">This is to certify that</p>
              <h3 class="certificate-name">{{ userName }}</h3>
              <p class="certificate-text">has successfully completed the course</p>
              <h3 class="certificate-course">{{ selectedCertificate.courseName }}</h3>
              <p class="certificate-date">
                Issued on {{ formatDate(selectedCertificate.issueDate) }}
              </p>
              <div class="certificate-footer">
                <div class="certificate-signature">
                  <img src="@/assets/signature.png" alt="Signature" class="signature-img">
                  <p>{{ selectedCertificate.instructorName }}</p>
                  <p>Instructor</p>
                </div>
                <div class="certificate-seal">
                  <img src="@/assets/seal.png" alt="Seal" class="seal-img">
                </div>
              </div>
              <p class="certificate-id">Certificate ID: {{ selectedCertificate.certificateId }}</p>
            </div>
          </div>
        </v-card-text>
        
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="downloadCertificate(selectedCertificate)">
            <v-icon left>mdi-download</v-icon>
            Download
          </v-btn>
          <v-btn color="primary" text @click="certificateDialog = false">
            Close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import moment from 'moment'

export default {
  data() {
    return {
      loading: false,
      progressTab: 0,
      certificateDialog: false,
      selectedCertificate: null,
      
      breadcrumbs: [
        {
          text: 'Home',
          disabled: false,
          href: '/'
        },
        {
          text: 'Dashboard',
          disabled: true
        }
      ],
      
      stats: {
        enrolledCourses: 0,
        completedAssignments: 0,
        certificates: 0,
        hoursLearned: 0
      },
      
      enrolledCourses: [],
      upcomingDeadlines: [],
      recentActivity: [],
      certificates: [],
      upcomingClasses: [],
      recentMessages: [],
      recommendedCourses: []
    }
  },
  
  computed: {
    ...mapGetters('user', ['currentUser']),
    
    userName() {
      if (!this.currentUser) return ''
      return `${this.currentUser.firstName} ${this.currentUser.lastName}`
    }
  },
  
  created() {
    this.fetchDashboardData()
  },
  
  methods: {
    async fetchDashboardData() {
      this.loading = true
      
      try {
        // 获取学习统计数据
        const statsResponse = await this.$axios.get('/users/learning-stats')
        this.stats = statsResponse.data
        
        // 获取已报名课程
        const coursesResponse = await this.$axios.get('/courses/enrolled')
        this.enrolledCourses = coursesResponse.data
        
        // 获取即将到期的任务
        const deadlinesResponse = await this.$axios.get('/assignments/upcoming')
        this.upcomingDeadlines = deadlinesResponse.data
        
        // 获取近期活动
        const activityResponse = await this.$axios.get('/users/activity')
        this.recentActivity = activityResponse.data
        
        // 获取证书
        const certificatesResponse = await this.$axios.get('/users/certificates')
        this.certificates = certificatesResponse.data
        
        // 获取即将开始的直播课
        const classesResponse = await this.$axios.get('/live-classes/upcoming')
        this.upcomingClasses = classesResponse.data
        
        // 获取最近消息
        const messagesResponse = await this.$axios.get('/chat/recent-messages')
        this.recentMessages = messagesResponse.data
        
        // 获取推荐课程
        const recommendedResponse = await this.$axios.get('/courses/recommended')
        this.recommendedCourses = recommendedResponse.data
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error)
      } finally {
        this.loading = false
      }
    },
    
    viewCertificate(certificate) {
      this.selectedCertificate = certificate
      this.certificateDialog = true
    },
    
    downloadCertificate(certificate) {
      // 实际实现中应该调用下载API
      console.log('Downloading certificate:', certificate.certificateId)
      
      // 模拟下载行为
      this.$notify('Certificate download started', { color: 'success' })
    },
    
    formatDate(date) {
      return moment(date).format('MMM DD, YYYY')
    },
    
    formatDatetime(datetime) {
      return moment(datetime).format('MMM DD, YYYY • hh:mm A')
    },
    
    formatDeadline(daysLeft) {
      if (daysLeft === 0) return 'Due today'
      if (daysLeft < 0) return `Overdue by ${Math.abs(daysLeft)} days`
      if (daysLeft === 1) return 'Due tomorrow'
      return `Due in ${daysLeft} days`
    },
    
    getDeadlineColor(daysLeft) {
      if (daysLeft < 0) return 'error'
      if (daysLeft <= 1) return 'warning'
      if (daysLeft <= 3) return 'orange'
      return 'success'
    },
    
    getDeadlineTextClass(daysLeft) {
      if (daysLeft < 0) return 'red--text'
      if (daysLeft <= 1) return 'warning--text'
      return ''
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

.certificate-container {
  position: relative;
  width: 100%;
  height: 560px;
  overflow: hidden;
}

.certificate-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.certificate-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  text-align: center;
}

.certificate-title {
  font-size: 32px;
  margin-bottom: 20px;
  color: #333;
}

.certificate-text {
  font-size: 18px;
  margin: 8px 0;
}

.certificate-name {
  font-size: 28px;
  margin: 15px 0;
  color: #1976d2;
}

.certificate-course {
  font-size: 24px;
  margin: 15px 0;
  color: #333;
}

.certificate-date {
  font-size: 16px;
  margin: 15px 0;
}

.certificate-footer {
  display: flex;
  justify-content: space-between;
  width: 80%;
  margin-top: 30px;
}

.certificate-signature,
.certificate-seal {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.signature-img {
  width: 120px;
  margin-bottom: 8px;
}

.seal-img {
  width: 80px;
}

.certificate-id {
  position: absolute;
  bottom: 10px;
  right: 20px;
  font-size: 12px;
  color: #666;
}
</style>