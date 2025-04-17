<template>
    <v-navigation-drawer
      v-model="drawer"
      app
      :temporary="isMobile"
      :permanent="!isMobile"
    >
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-img :src="userAvatar"></v-img>
        </v-list-item-avatar>
        
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            {{ currentUser.name }}
          </v-list-item-title>
          <v-list-item-subtitle>
            {{ isTeacher ? 'Teacher' : 'Student' }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      
      <v-divider></v-divider>
      
      <v-list dense nav>
        <v-list-item to="/" exact>
          <v-list-item-icon>
            <v-icon>mdi-home</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Home</v-list-item-title>
        </v-list-item>
        
        <v-list-item to="/courses">
          <v-list-item-icon>
            <v-icon>mdi-book-open-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Courses</v-list-item-title>
        </v-list-item>
        
        <template v-if="isTeacher">
          <v-list-item to="/teacher/dashboard">
            <v-list-item-icon>
              <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item>
          
          <v-list-item to="/teacher/courses/create">
            <v-list-item-icon>
              <v-icon>mdi-plus-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Create Course</v-list-item-title>
          </v-list-item>
          
          <v-list-group prepend-icon="mdi-teach" value="false">
            <template v-slot:activator>
              <v-list-item-title>Teaching</v-list-item-title>
            </template>
            
            <v-list-item
              v-for="course in teachingCourses"
              :key="course.courseId"
              :to="`/courses/${course.courseId}`"
            >
              <v-list-item-icon>
                <v-icon>mdi-book</v-icon>
              </v-list-item-icon>
              <v-list-item-title>{{ course.title }}</v-list-item-title>
            </v-list-item>
          </v-list-group>
        </template>
        
        <template v-else>
          <v-list-item to="/dashboard">
            <v-list-item-icon>
              <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item>
          
          <v-list-group prepend-icon="mdi-book-multiple" value="false">
            <template v-slot:activator>
              <v-list-item-title>My Courses</v-list-item-title>
            </template>
            
            <v-list-item
              v-for="course in enrolledCourses"
              :key="course.courseId"
              :to="`/courses/${course.courseId}`"
            >
              <v-list-item-icon>
                <v-icon>mdi-book</v-icon>
              </v-list-item-icon>
              <v-list-item-title>{{ course.title }}</v-list-item-title>
            </v-list-item>
          </v-list-group>
        </template>
        
        <v-list-item to="/chat">
          <v-list-item-icon>
            <v-icon>mdi-chat</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Chat</v-list-item-title>
        </v-list-item>
        
        <v-list-item to="/profile">
          <v-list-item-icon>
            <v-icon>mdi-account</v-icon>
          </v-list-item-icon>
          <v-list-item-title>Profile</v-list-item-title>
        </v-list-item>
      </v-list>
      
      <template v-slot:append>
        <div class="pa-2">
          <v-btn block color="error" @click="logout">
            <v-icon left>mdi-logout</v-icon>
            Logout
          </v-btn>
        </div>
      </template>
    </v-navigation-drawer>
  </template>
  
  <script>
  import { mapGetters, mapActions } from 'vuex'
  
  export default {
    props: {
      value: Boolean
    },
    
    data() {
      return {
        isMobile: false
      }
    },
    
    computed: {
      ...mapGetters('user', ['currentUser', 'isTeacher']),
      ...mapGetters('course', ['enrolledCourses', 'teachingCourses']),
      
      drawer: {
        get() {
          return this.value
        },
        set(value) {
          this.$emit('input', value)
        }
      },
      
      userAvatar() {
        return this.currentUser.avatar || 'https://via.placeholder.com/150'
      }
    },
    
    methods: {
      ...mapActions('user', ['logout']),
      
      checkScreenSize() {
        this.isMobile = window.innerWidth < 960
      }
    },
    
    created() {
      this.checkScreenSize()
      window.addEventListener('resize', this.checkScreenSize)
      
      // 获取课程信息
      if (this.isTeacher) {
        this.$store.dispatch('course/fetchTeachingCourses')
      } else {
        this.$store.dispatch('course/fetchEnrolledCourses') 
      }
    },
    
    beforeDestroy() {
      window.removeEventListener('resize', this.checkScreenSize)
    }
  }
  </script>