<template>
  <div id="app">
    <div class="container">
      <!-- 左侧导航（和你系统完全一样） -->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
        <div class="menu-item" @click="$router.push('/house-add')">➕ 房源录入</div>
        <div class="menu-item" @click="$router.push('/room-status')">🏷️ 房态管理</div>

        <div class="menu-group-title">订单管理</div>
        <div class="menu-item" @click="$router.push('/order-manage')">📋 预订管理</div>
        <div class="menu-item" @click="$router.push('/add-order')">➕ 新增预定</div>
        <div class="menu-item" @click="$router.push('/review-manage')">💬 评价管理</div>

        <div class="menu-group-title">客户管理</div>
        <div class="menu-item active">👤 客户列表</div>

        <div class="menu-group-title">库房监控</div>
        <div class="menu-item" @click="$router.push('/storage-manage')">📦 库房管理</div>

        <div class="menu-group-title">系统操作</div>
        <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧主体内容 -->
      <div class="main-content">
        <div class="content-header">
          <h3>👤 客户管理</h3>
          <div class="header-buttons">
            <button class="btn" @click="showAddUser">➕ 新增客户</button>
            <button class="btn btn-default" @click="loadUsers">刷新</button>
          </div>
        </div>

        <!-- 搜索栏 -->
        <div class="filter-bar">
          <div class="filter-item">
            <label>客户姓名 / 电话：</label>
            <input v-model="searchKey" placeholder="输入搜索关键词" />
          </div>
          <button class="btn" @click="loadUsers">搜索</button>
          <button class="btn btn-default" @click="reset">重置</button>
        </div>

        <!-- 高级卡片式用户列表 -->
        <div class="user-card-list">
          <div class="user-card" v-for="user in userList" :key="user.userId">
            <div class="user-avatar">
              {{ user.username ? user.username.substring(0,1) : '客' }}
            </div>
            <div class="user-info">
              <div class="user-name">{{ user.username || '未设置姓名' }}</div>
              <div class="user-phone">{{ user.phone || '无联系方式' }}</div>
              <div class="user-id">ID：{{ user.userId }}</div>
            </div>
            <div class="user-status">
              <span class="tag-normal">正常客户</span>
            </div>
            <div class="user-actions">
              <button class="btn btn-detail" @click="showDetail(user)">查看详情</button>
              <!-- 新增删除按钮 -->
              <button class="btn btn-delete" @click="deleteUser(user)">删除</button>
            </div>
          </div>
        </div>

        <!-- 空数据 -->
        <div class="empty" v-if="userList.length === 0 && !loading">
          暂无客户数据
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="showDetailFlag" class="detail-box">
      <div class="box-content">
        <h3 class="box-title">客户详情</h3>
        <div class="box-body">
          <p>客户ID：{{ detail.userId }}</p>
          <p>姓名：{{ detail.username || '未设置' }}</p>
          <p>电话：{{ detail.phone || '未设置' }}</p>
          <p>状态：正常客户</p>
        </div>
        <button class="close-btn" @click="showDetailFlag=false">关闭</button>
      </div>
    </div>

    <!-- 新增用户弹窗 -->
    <div v-if="showAddFlag" class="detail-box">
      <div class="box-content">
        <h3 class="box-title">新增客户</h3>
        <div class="box-body">
          <div class="form-item">
            <label>客户姓名：</label>
            <input v-model="addForm.username" placeholder="请输入姓名" />
          </div>
          <div class="form-item" style="margin-top:12px;">
            <label>联系电话：</label>
            <input v-model="addForm.phone" placeholder="请输入电话" />
          </div>
          <div class="form-item" style="margin-top:12px;">
            <label>登录密码：</label>
            <input v-model="addForm.password" placeholder="请设置密码" type="password" />
          </div>
        </div>
        <div style="text-align:right;margin-top:20px;">
          <button class="close-btn" style="background:#ccc;margin-right:10px;" @click="showAddFlag=false">取消</button>
          <button class="close-btn" @click="addUser">确认添加</button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="showDeleteFlag" class="detail-box">
      <div class="box-content">
        <h3 class="box-title">确认删除</h3>
        <div class="box-body">
          <p>确定要删除客户 <b>{{ deleteUserInfo.username }}</b> 吗？</p>
          <p style="color:red">删除后将无法恢复</p>
        </div>
        <div style="text-align:right;margin-top:20px;">
          <button class="close-btn" style="background:#ccc;margin-right:10px;" @click="showDeleteFlag=false">取消</button>
          <button class="close-btn btn-confirm-delete" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>

    <div v-show="toast" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "UserManage",
  data() {
    return {
      loading: false,
      userList: [],
      searchKey: "",
      showDetailFlag: false,
      detail: {},
      // 新增用户相关
      showAddFlag: false,
      addForm: {
        username: '',
        phone: '',
        password: ''
      },
      // 删除用户相关
      showDeleteFlag: false,
      deleteUserInfo: {},
      toast: false,
      toastMsg: ""
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      this.loading = true
      try {
        const res = await axios.get("/api/user/list")
        let list = res.data.data || []

        if (this.searchKey.trim()) {
          const k = this.searchKey.trim()
          list = list.filter(u =>
              (u.username && u.username.includes(k)) ||
              (u.phone && u.phone.includes(k))
          )
        }

        this.userList = list
      } catch (e) {
        this.toastMsg = "加载失败"
        this.toast = true
      } finally {
        this.loading = false
      }
    },

    reset() {
      this.searchKey = ""
      this.loadUsers()
    },

    showDetail(user) {
      this.detail = user
      this.showDetailFlag = true
    },

    showAddUser() {
      this.addForm = { username: '', phone: '', password: '' }
      this.showAddFlag = true
    },

    async addUser() {
      if (!this.addForm.username) {
        this.toastMsg = "请输入客户姓名"
        this.toast = true
        return
      }
      if (!this.addForm.phone) {
        this.toastMsg = "请输入联系电话"
        this.toast = true
        return
      }
      if (!this.addForm.password) {
        this.toastMsg = "请设置登录密码"
        this.toast = true
        return
      }

      try {
        await axios.post("/api/user/add", this.addForm)
        this.toastMsg = "添加成功！"
        this.showAddFlag = false
        this.loadUsers()
      } catch (e) {
        this.toastMsg = "添加失败"
      } finally {
        this.toast = true
        setTimeout(() => this.toast = false, 2000)
      }
    },

    // ================== 删除功能 ==================
    deleteUser(user) {
      this.deleteUserInfo = user
      this.showDeleteFlag = true
    },

    async confirmDelete() {
      try {
        // 调用删除接口 → 数据库同步删除
        await axios.get("/api/user/delete", {
          params: {
            userId: this.deleteUserInfo.userId
          }
        })
        this.toastMsg = "删除成功！"
        this.showDeleteFlag = false
        this.loadUsers() // 刷新列表
      } catch (e) {
        this.toastMsg = "删除失败"
      } finally {
        this.toast = true
        setTimeout(() => this.toast = false, 2000)
      }
    },

    logout() {
      localStorage.clear()
      this.$router.push("/login")
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

html, body {
  height: 100%;
  overflow: hidden;
}

#app {
  background: #fff5eb;
  color: #5a4a3f;
  height: 100vh;
}

.container {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #f0d9b5;
  padding: 20px 0;
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  overflow-y: auto;
  scrollbar-width: none;
}
.sidebar::-webkit-scrollbar { display: none; }
.sidebar h2 {
  text-align: center;
  color: #8b5a2b;
  margin-bottom: 30px;
  font-size: 18px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0d9b5;
}
.menu-group-title {
  padding: 10px 20px;
  font-size: 14px;
  color: #8b5a2b;
  font-weight: bold;
  margin-top: 15px;
}
.menu-item {
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
}
.menu-item.active, .menu-item:hover {
  background: #f5b041;
  color: #fff;
}

.main-content {
  flex: 1;
  margin-left: 220px;
  padding: 20px;
  height: 100vh;
  overflow-y: auto;
}

.content-header {
  background: #fff;
  padding: 18px 24px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-buttons {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}
.btn-default {
  background: #f8f9fa;
  color: #5a4a3f;
  border: 1px solid #eee;
}
.btn-detail {
  background: #2a9d8f;
  color: #fff;
}
/* 删除按钮样式 */
.btn-delete {
  background: #f44336;
  color: #fff;
  margin-left: 8px;
}
.btn-confirm-delete {
  background: #f44336 !important;
}

.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  gap: 15px;
  align-items: center;
}
.filter-item { display: flex; align-items: center; gap: 8px; }
.filter-item input {
  padding: 8px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  width: 240px;
}

.form-item input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
}

.user-card-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 16px;
}
.user-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  gap: 14px;
  position: relative;
}
.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: #f5b041;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
}
.user-info {
  flex: 1;
}
.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}
.user-phone {
  font-size: 13px;
  color: #666;
  margin-top: 4px;
}
.user-id {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
.user-status {
  margin-right: 10px;
}
.tag-normal {
  background: #e8f5e9;
  color: #2e7d32;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.detail-box {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 999;
}
.box-content {
  width: 420px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px #ccc;
  position: relative;
}
.box-title {
  margin-bottom: 16px;
  color: #8b5a2b;
}
.box-body p {
  line-height: 28px;
}
.close-btn {
  padding: 8px 20px;
  font-size: 14px;
  border: none;
  background: #f5b041;
  color: #fff;
  border-radius: 6px;
  cursor: pointer;
}

.toast {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: #2a9d8f;
  color: #fff;
  padding: 12px 24px;
  border-radius: 6px;
  z-index: 9999;
}
</style>