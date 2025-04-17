<template>
    <v-card
      :to="{ name: 'CourseDetail', params: { id: course.courseId } }"
      class="course-card"
      hover
    >
      <v-img
        :src="course.thumbnail || require('@/assets/course-placeholder.jpg')"
        height="180px"
        class="white--text align-end"
      >
        <v-card-title>{{ course.title }}</v-card-title>
      </v-img>
      
      <v-card-subtitle class="pb-0">
        <v-chip x-small color="primary" text-color="white" class="mr-1">
          {{ course.category }}
        </v-chip>
        
        <v-chip x-small :color="getLevelColor(course.level)" text-color="white">
          {{ course.level }}
        </v-chip>
      </v-card-subtitle>
      
      <v-card-text>
        <div class="d-flex align-center">
          <v-avatar size="24" class="mr-2">
            <v-img :src="course.teacherAvatar || 'https://via.placeholder.com/50'"></v-img>
          </v-avatar>
          <span class="caption">{{ course.teacherName }}</span>
        </div>
        
        <div class="text-truncate mt-2">
          {{ course.description }}
        </div>
        
        <div class="d-flex justify-space-between align-center mt-2">
          <div class="d-flex align-center">
            <v-rating
              :value="course.rating || 0"
              color="amber"
              dense
              half-increments
              readonly
              size="14"
            ></v-rating>
            <span class="grey--text ml-1">{{ course.ratingCount || 0 }} ratings</span>
          </div>
          
          <div>
            <span class="subtitle-1 font-weight-bold" v-if="course.price > 0">
              ${{ course.price.toFixed(2) }}
            </span>
            <span class="subtitle-1 font-weight-bold green--text" v-else>Free</span>
          </div>
        </div>
      </v-card-text>
      
      <v-divider></v-divider>
      
      <v-card-actions>
        <v-chip small outlined class="mr-1">
          <v-icon left small>mdi-account-group</v-icon>
          {{ course.enrolledStudents ? course.enrolledStudents.length : 0 }} students
        </v-chip>
        
        <v-chip small outlined>
          <v-icon left small>mdi-clock-outline</v-icon>
          {{ course.duration || '4 weeks' }}
        </v-chip>
        
        <v-spacer></v-spacer>
        
        <v-btn
          text
          color="primary"
          :to="{ name: 'CourseDetail', params: { id: course.courseId } }"
        >
          Details
        </v-btn>
      </v-card-actions>
    </v-card>
  </template>
  
  <script>
  export default {
    props: {
      course: {
        type: Object,
        required: true
      }
    },
    
    methods: {
      getLevelColor(level) {
        switch (level) {
          case 'BEGINNER': return 'green'
          case 'INTERMEDIATE': return 'blue'
          case 'ADVANCED': return 'purple'
          default: return 'grey'
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .course-card {
    transition: transform 0.3s;
  }
  .course-card:hover {
    transform: translateY(-5px);
  }
  </style>