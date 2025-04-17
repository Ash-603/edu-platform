<template>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click="$emit('toggle-drawer')" class="d-md-none"></v-app-bar-nav-icon>
      
      <v-toolbar-title>
        <router-link to="/" class="white--text text-decoration-none">
          <v-icon left>mdi-school</v-icon>
          Online Education Platform
        </router-link>
      </v-toolbar-title>
      
      <v-spacer></v-spacer>
      
      <div class="d-none d-md-flex">
        <v-btn text to="/" exact>Home</v-btn>
        <v-btn text to="/courses">Courses</v-btn>
        
        <template v-if="isAuthenticated">
          <v-btn text :to="dashboardLink">Dashboard</v-btn>
          
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn text v-bind="attrs" v-on="on">
                <v-badge :content="unreadCount" :value="unreadCount" color="error" overlap>
                  <v-icon>mdi-bell</v-icon>
                </v-badge>
              </v-btn>
            </template>
            
            <NotificationList @read="markAsRead" @read-all="markAllAsRead" />
          </v-menu>
          
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn text v-bind="attrs" v-on="on">
                {{ currentUser.name }}
                <v-icon right>mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            
            <v-list>
              <v-list-item to="/profile">
                <v-list-item-icon>
                  <v-icon>mdi-account</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Profile</v-list-item-title>
              </v-list-item>
              
              <v-divider></v-divider>
              
              <v-list-item @click="logout">
                <v-list-item-icon>
                  <v-icon>mdi-logout</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Logout</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </template>
        
        <template v-else>
          <v-btn text to="/login">Login</v-btn>
          <v-btn text to="/register">Register</v-btn>
        </template>
      </div>
    </v-app-bar>
  </template>
  
  <script>
  import { mapGetters, mapActions } from 'vuex'
  import NotificationList from './NotificationList.vue'
  
  export default {
    components: {
      NotificationList
    },
    computed: {
      ...mapGetters('user', ['isAuthenticated', 'currentUser', 'isTeacher']),
      ...mapGetters('notification', ['unreadCount']),
      
      dashboardLink() {
        return this.isTeacher ? '/teacher/dashboard' : '/dashboard'
      }
    },
    methods: {
      ...mapActions('user', ['logout']),
      ...mapActions('notification', ['markAsRead', 'markAllAsRead'])
    },
    created() {
      // 定期获取未读通知数量
      if (this.isAuthenticated) {
        this.$store.dispatch('notification/fetchUnreadCount')
        this.notificationInterval = setInterval(() => {
          this.$store.dispatch('notification/fetchUnreadCount')
        }, 60000) // 每分钟检查一次
      }
    },
    beforeDestroy() {
      if (this.notificationInterval) {
        clearInterval(this.notificationInterval)
      }
    }
  }
  </script>