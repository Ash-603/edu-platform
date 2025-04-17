<template>
    <v-container class="fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-card class="elevation-12">
            <v-toolbar color="primary" dark flat>
              <v-toolbar-title>Login</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn icon to="/">
                <v-icon>mdi-home</v-icon>
              </v-btn>
            </v-toolbar>
            
            <v-card-text>
              <v-alert
                v-if="error"
                type="error"
                dismissible
                @input="error = ''"
              >
                {{ error }}
              </v-alert>
              
              <v-form ref="form" v-model="valid" @submit.prevent="login">
                <v-text-field
                  v-model="email"
                  :rules="emailRules"
                  label="Email"
                  name="email"
                  prepend-icon="mdi-email"
                  type="email"
                  required
                ></v-text-field>
                
                <v-text-field
                  v-model="password"
                  :rules="passwordRules"
                  label="Password"
                  name="password"
                  prepend-icon="mdi-lock"
                  :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPassword ? 'text' : 'password'"
                  @click:append="showPassword = !showPassword"
                  required
                ></v-text-field>
                
                <div class="d-flex justify-space-between align-center mb-4">
                  <v-checkbox
                    v-model="rememberMe"
                    label="Remember me"
                    color="primary"
                  ></v-checkbox>
                  
                  <v-btn text color="primary" small to="/forgot-password">
                    Forgot password?
                  </v-btn>
                </div>
                
                <v-btn
                  :disabled="!valid || loading"
                  :loading="loading"
                  color="primary"
                  block
                  x-large
                  type="submit"
                >
                  Log In
                </v-btn>
              </v-form>
            </v-card-text>
            
            <v-divider></v-divider>
            
            <v-card-actions class="pa-4">
              <span>Don't have an account?</span>
              <v-spacer></v-spacer>
              <v-btn color="secondary" text to="/register">
                Register
              </v-btn>
            </v-card-actions>
            
            <v-divider></v-divider>
            
            <v-card-text class="text-center pa-4">
              <p class="mb-3">Or log in with</p>
              <v-btn
                class="mx-2"
                icon
                color="blue darken-4"
                @click="socialLogin('facebook')"
                :loading="socialLoading === 'facebook'"
              >
                <v-icon>mdi-facebook</v-icon>
              </v-btn>
              <v-btn
                class="mx-2"
                icon
                color="red"
                @click="socialLogin('google')"
                :loading="socialLoading === 'google'"
              >
                <v-icon>mdi-google</v-icon>
              </v-btn>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  import { mapActions } from 'vuex'
  
  export default {
    data() {
      return {
        valid: false,
        email: '',
        password: '',
        showPassword: false,
        rememberMe: false,
        loading: false,
        error: '',
        socialLoading: null,
        
        emailRules: [
          v => !!v || 'Email is required',
          v => /.+@.+\..+/.test(v) || 'Email must be valid'
        ],
        passwordRules: [
          v => !!v || 'Password is required',
          v => v.length >= 6 || 'Password must be at least 6 characters'
        ]
      }
    },
    
    computed: {
      redirect() {
        return this.$route.query.redirect || '/'
      }
    },
    
    methods: {
      ...mapActions('user', ['login']),
      
      async handleLogin() {
        if (!this.$refs.form.validate()) return
        
        this.loading = true
        this.error = ''
        
        try {
          const success = await this.login({
            email: this.email,
            password: this.password,
            rememberMe: this.rememberMe
          })
          
          if (success) {
            // 登录成功，重定向到目标页面
            this.$router.push(this.redirect)
          } else {
            this.error = 'Login failed. Please check your credentials.'
          }
        } catch (err) {
          this.error = err.message || 'An error occurred during login.'
        } finally {
          this.loading = false
        }
      },
      
      async socialLogin(provider) {
        this.socialLoading = provider
        this.error = ''
        
        try {
          // 这里应该实现社交媒体登录，根据后端API调整
          // const success = await this.loginWithSocial(provider)
          
          // 模拟实现
          await new Promise(resolve => setTimeout(resolve, 1000))
          const success = false
          
          if (success) {
            this.$router.push(this.redirect)
          } else {
            this.error = `Login with ${provider} is not available at the moment.`
          }
        } catch (err) {
          this.error = err.message || `Error during ${provider} login.`
        } finally {
          this.socialLoading = null
        }
      }
    }
  }
  </script>