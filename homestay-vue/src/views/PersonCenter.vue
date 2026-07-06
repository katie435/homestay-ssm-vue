<template>
  <div id="person-center-page">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="logo">
        <img src="../assets/logo.png" alt="logo" />
        <span>栖宿民宿</span>
      </div>
      <div class="nav-links">
        <a @click="$router.push('/home')">首页</a>
        <a @click="$router.push('/all-rooms')">发现房源</a>
        <a @click="$router.push('/my-orders')">我的订单</a>
        <a @click="$router.push('/person-center')" class="active">个人中心</a>
      </div>
    </div>

    <div class="center-wrap">
      <div class="section-title">个人中心</div>

      <div class="user-card">
        <div class="user-top">
          <div>
            <div class="user-name">{{ userInfo?.username || '暂无姓名' }}</div>
          </div>
        </div>

        <div class="info-item">
          <div class="info-label">用户账号</div>
          <div class="info-value">{{ userInfo?.username || '暂无数据' }}</div>
        </div>
        <div class="info-item">
          <div class="info-label">手机号码</div>
          <div class="info-value">{{ userInfo?.phone || '暂无数据' }}</div>
        </div>

        <button class="edit-btn" @click="openEditForm">修改个人资料</button>
      </div>

      <div class="edit-form" v-if="showEditForm">
        <div class="form-title">修改资料</div>
        <div class="form-group">
          <label>用户名</label>
          <input type="text" v-model="editForm.username" />
        </div>
        <div class="form-group">
          <label>手机号码</label>
          <input type="text" v-model="editForm.phone" />
        </div>
        <button class="save-btn" @click="saveUserInfo">保存修改</button>
      </div>

      <div class="func-card">
        <div class="func-item" @click="go('/my-orders')">
          <div class="func-text">我的订单</div>
          <div class="func-arrow">></div>
        </div>
        <div class="func-item" @click="go('/evaluate')">
          <div class="func-text">我的评价</div>
          <div class="func-arrow">></div>
        </div>
      </div>

      <div class="func-card">
        <div class="func-item" @click="showPwdForm = true">
          <div class="func-text">修改密码</div>
          <div class="func-arrow">></div>
        </div>
        <div class="func-item" @click="logout">
          <div class="func-text logout-text">退出登录</div>
          <div class="func-arrow">></div>
        </div>
      </div>

      <div class="edit-form" v-if="showPwdForm">
        <div class="form-title">修改密码</div>
        <div class="form-group">
          <label>旧密码</label>
          <input type="password" v-model="pwdForm.oldPwd" />
        </div>
        <div class="form-group">
          <label>新密码</label>
          <input type="password" v-model="pwdForm.newPwd" />
        </div>
        <button class="save-btn" @click="updatePassword">确认修改</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PersonCenter',
  data() {
    return {
      userId: '',
      userInfo: {},
      editForm: {
        username: '',
        phone: ''
      },
      pwdForm: { oldPwd: '', newPwd: '' },
      showEditForm: false,
      showPwdForm: false
    }
  },
  mounted() {
    this.userId = localStorage.getItem('userId')
    if (!this.userId) {
      alert('请先登录！')
      this.$router.push('/login')
      return
    }
    this.getUserInfo()
  },
  methods: {
    async getUserInfo() {
      try {
        const res = await axios.get('/api/user/info', {
          params: { userId: this.userId }
        })
        console.log(res.data)
        // 修复：防止返回null导致页面报错
        this.userInfo = res.data.data || {}
      } catch (err) {
        console.error('获取用户信息失败', err)
        this.userInfo = {}
        alert('用户信息异常，请重新登录')
        localStorage.removeItem('userId')
        this.$router.push('/login')
      }
    },
    openEditForm() {
      this.editForm.username = this.userInfo.username
      this.editForm.phone = this.userInfo.phone
      this.showEditForm = true
    },
    async saveUserInfo() {
      try {
        this.editForm.userId = this.userId
        await axios.post('/api/user/update', this.editForm)
        alert('修改成功！')
        this.showEditForm = false
        this.getUserInfo()
      } catch (e) {
        alert('修改失败')
      }
    },
    async updatePassword() {
      try {
        await axios.post('/api/user/updatePwd', {
          userId: this.userId,
          oldPwd: this.pwdForm.oldPwd,
          newPwd: this.pwdForm.newPwd
        })
        alert('密码修改成功！')
        this.showPwdForm = false
      } catch (e) {
        alert('修改失败，旧密码错误')
      }
    },
    logout() {
      localStorage.removeItem('userId')
      alert('已退出登录')
      this.$router.push('/login')
    },
    go(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "微软雅黑", sans-serif;
}

#person-center-page {
  background-color: #fff9e6;
  color: #5a4a3f;
  width: 100%;
  min-height: 100vh;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  background-color: #fff;
  border-bottom: 1px solid #f5e8d0;
  position: relative;
}
.logo {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  color: #8b5a2b;
}
.logo img {
  width: 24px;
  margin-right: 6px;
}
.nav-links {
  display: flex;
  align-items: center;
}
.nav-links a {
  margin: 0 10px;
  text-decoration: none;
  color: #5a4a3f;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  cursor: pointer;
}
.nav-links a:hover {
  font-weight: bold;
  color: #8b5a2b;
}
.nav-links a.active {
  font-weight: bold;
  color: #8b5a2b;
}

.center-wrap {
  margin: 40px 20px;
}

.section-title {
  font-size: 20px;
  color: #8b5a2b;
  font-weight: bold;
  margin-bottom: 20px;
}

.user-card {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

.user-top {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 25px;
  border-bottom: 1px dashed #f0d9b5;
  padding-bottom: 20px;
}

.user-name {
  font-size: 18px;
  font-weight: bold;
  color: #5a4a3f;
}

.info-item {
  display: flex;
  padding: 12px 0;
  font-size: 14px;
  border-bottom: 1px solid #f5e8d0;
}

.info-label {
  width: 100px;
  color: #5a4a3f;
  font-weight: 500;
}

.info-value {
  flex: 1;
  color: #8c786b;
}

.edit-btn {
  display: inline-block;
  margin-top: 20px;
  padding: 8px 25px;
  background-color: #f5b041;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}

.edit-btn:hover {
  background-color: #e69c2e;
}

.edit-form {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.form-title {
  font-size: 16px;
  font-weight: bold;
  color: #8b5a2b;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  font-size: 14px;
  margin-bottom: 6px;
  color: #5a4a3f;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #f0d9b5;
  border-radius: 6px;
  outline: none;
  font-size: 14px;
}

.save-btn {
  background-color: #f5b041;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-top: 10px;
}

.func-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.func-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 10px;
  border-bottom: 1px solid #f5e8d0;
  cursor: pointer;
  transition: background 0.2s;
}

.func-item:last-child {
  border-bottom: none;
}

.func-item:hover {
  background-color: #fef5e3;
}

.func-text {
  font-size: 14px;
  color: #5a4a3f;
}

.func-arrow {
  color: #8c786b;
}

.logout-text {
  color: #e74c3c !important;
  font-weight: bold;
}
</style>