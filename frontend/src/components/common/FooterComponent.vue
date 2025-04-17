<template>
    <v-footer app padless>
      <v-card flat tile width="100%" class="primary white--text text-center">
        <v-card-text>
          <v-btn v-for="icon in icons" :key="icon.icon" class="mx-4 white--text" icon>
            <v-icon size="24px">{{ icon.icon }}</v-icon>
          </v-btn>
        </v-card-text>
        
        <v-divider></v-divider>
        
        <v-card-text class="white--text">
          <v-btn text to="/about" class="white--text mx-2">About Us</v-btn>
          <v-btn text to="/contact" class="white--text mx-2">Contact</v-btn>
          <v-btn text to="/privacy" class="white--text mx-2">Privacy Policy</v-btn>
          <v-btn text to="/terms" class="white--text mx-2">Terms of Use</v-btn>
        </v-card-text>
        
        <v-card-text class="white--text pt-0">
          {{ new Date().getFullYear() }} — <strong>Online Education Platform</strong>
        </v-card-text>
      </v-card>
    </v-footer>
  </template>
  
  <script>
  export default {
    data: () => ({
      icons: [
        { icon: 'mdi-facebook' },
        { icon: 'mdi-twitter' },
        { icon: 'mdi-linkedin' },
        { icon: 'mdi-instagram' }
      ]
    })
  }
  </script>