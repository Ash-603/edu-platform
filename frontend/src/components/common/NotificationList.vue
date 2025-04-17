<template>
    <v-card max-width="400" max-height="400" class="overflow-y-auto">
      <v-card-title class="d-flex align-center">
        Notifications
        <v-spacer></v-spacer>
        <v-btn small text color="primary" @click="markAllAsRead" :disabled="!hasUnread">
          Mark all as read
        </v-btn>
      </v-card-title>
      
      <v-divider></v-divider>
      
      <v-list two-line>
        <template v-if="notifications.length > 0">
          <v-list-item 
            v-for="notification in notifications" 
            :key="notification.notificationId"
            :class="{ 'blue lighten-5': !notification.read }"
            @click="markAsRead(notification.notificationId)"
          >
            <v-list-item-avatar>
              <v-icon :color="getNotificationColor(notification.type)">
                {{ getNotificationIcon(notification.type) }}
              </v-icon>
            </v-list-item-avatar>
            
            <v-list-item-content>
              <v-list-item-title>{{ notification.title }}</v-list-item-title>
              <v-list-item-subtitle>{{ notification.content }}</v-list-item-subtitle>
              <v-list-item-subtitle class="text-caption">
                {{ formatDate(notification.createdAt) }}
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </template>
        
        <v-list-item v-else>
          <v-list-item-content class="text-center">
            <v-icon large color="grey lighten-1">mdi-bell-off</v-icon>
            <div class="text-subtitle1 grey--text text--darken-1 mt-3">
              No notifications
            </div>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>
  </template>
  
  <script>
  import { mapGetters, mapActions } from 'vuex'
  import moment from 'moment'
  
  export default {
    computed: {
      ...mapGetters('notification', ['allNotifications']),
      
      notifications() {
        return this.allNotifications.slice(0, 10)  // 只显示最近10条
      },
      
      hasUnread() {
        return this.notifications.some(notification => !notification.read)
      }
    },
    
    methods: {
      ...mapActions('notification', ['markAsRead', 'markAllAsRead']),
      
      formatDate(date) {
        return moment(date).fromNow()
      },
      
      getNotificationIcon(type) {
        switch (type) {
          case 'ASSIGNMENT': return 'mdi-clipboard-text'
          case 'COURSE': return 'mdi-book-open-variant'
          case 'PAYMENT': return 'mdi-credit-card'
          case 'MESSAGE': return 'mdi-message-text'
          default: return 'mdi-bell'
        }
      },
      
      getNotificationColor(type) {
        switch (type) {
          case 'ASSIGNMENT': return 'purple'
          case 'COURSE': return 'blue'
          case 'PAYMENT': return 'green'
          case 'MESSAGE': return 'orange'
          default: return 'grey'
        }
      }
    },
    
    created() {
      this.$store.dispatch('notification/fetchNotifications')
    }
  }
  </script>