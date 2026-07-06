import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

Vue.config.productionTip = false

// 配置 axios 基础路径（必须在挂载之前写）
// axios.defaults.baseURL = 'http://localhost:8080/homestay-ssm'
// // 允许跨域带cookie
// axios.defaults.withCredentials = true

// 全局挂载到 Vue
Vue.prototype.$axios = axios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')