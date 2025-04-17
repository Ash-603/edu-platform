import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import VueSocketIO from 'vue-socket.io'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.config.productionTip = false

// 设置API基础URL
axios.defaults.baseURL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api'

// 添加请求拦截器
axios.interceptors.request.use(config => {
  const token = store.getters.token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 添加响应拦截器
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      store.dispatch('logout')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

Vue.use(VueAxios, axios)
Vue.use(Vuetify)

// 配置WebSocket
Vue.use(new VueSocketIO({
  debug: process.env.NODE_ENV === 'development',
  connection: process.env.VUE_APP_SOCKET_URL || 'http://localhost:8080',
  vuex: {
    store,
    actionPrefix: 'SOCKET_',
    mutationPrefix: 'SOCKET_'
  }
}))

new Vue({
  router,
  store,
  vuetify: new Vuetify({
    theme: {
      themes: {
        light: {
          primary: '#3f51b5',
          secondary: '#b0bec5',
          accent: '#8c9eff',
          error: '#b71c1c'
        }
      }
    }
  }),
  render: h => h(App)
}).$mount('#app')