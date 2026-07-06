<template>
  <div id="app">
    <div class="container">
      <!-- 左侧导航 -->
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
        <div class="menu-item" @click="$router.push('/user-manage')">👤 客户列表</div>

        <div class="menu-group-title">库房监控</div>
        <div class="menu-item" @click="$router.push('/storage-manage')">📦 库房管理</div>

        <div class="menu-group-title">系统操作</div>
        <div class="menu-item active">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧主体内容 -->
      <div class="main-content">
        <div class="content-header">
          <h3>👨‍💼 员工管理</h3>
          <div class="header-buttons">
            <button class="btn btn-primary" @click="openAdd">➕ 新增员工</button>
            <button class="btn btn-default" @click="loadList">刷新</button>
          </div>
        </div>

        <!-- 搜索栏 -->
        <div class="filter-bar">
          <div class="filter-item">
            <label>员工姓名 / 电话 / 岗位：</label>
            <input v-model="searchKey" placeholder="输入关键词搜索" @keyup.enter="loadList" />
          </div>
          <button class="btn btn-primary" @click="loadList">搜索</button>
          <button class="btn btn-default" @click="reset">重置</button>
        </div>

        <!-- 员工列表 -->
        <div class="card-grid">
          <div class="employee-card" v-for="item in employeeList" :key="item.staffId">
            <div class="card-avatar">{{ item.staffName.substring(0,1) }}</div>
            <div class="card-body">
              <div class="card-name">{{ item.staffName }}</div>
              <div class="card-phone">{{ item.phone }}</div>
              <div class="card-post">{{ item.position || '普通员工' }}</div>
              <div class="card-status">
                <span class="tag-normal">正常</span>
              </div>
            </div>
            <div class="card-actions">
              <button class="btn btn-edit" @click="openEdit(item)">编辑</button>
              <button class="btn btn-del" @click="deleteItem(item.staffId)">删除</button>
            </div>
          </div>
        </div>

        <div class="empty-tip" v-if="employeeList.length === 0 && !loading">暂无员工数据</div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-show="showDialog" class="dialog-mask">
      <div class="dialog-box">
        <div class="dialog-header">
          <h3>{{ title }}</h3>
          <button class="close" @click="closeDialog">×</button>
        </div>
        <div class="dialog-body">
          <div class="form-item">
            <label>姓名</label>
            <input v-model="form.staffName" placeholder="请输入姓名" />
          </div>
          <div class="form-item">
            <label>电话</label>
            <input v-model="form.phone" placeholder="请输入电话" />
          </div>
          <div class="form-item">
            <label>密码</label>
            <input v-model="form.password" placeholder="请输入密码" type="password" />
          </div>
          <div class="form-item">
            <label>岗位/职位</label>
            <input v-model="form.position" placeholder="例如：前台、保洁、店长、管理员" />
          </div>
        </div>
        <div class="dialog-footer">
          <button class="btn btn-default" @click="closeDialog">取消</button>
          <button class="btn btn-primary" @click="submit">确认保存</button>
        </div>
      </div>
    </div>

    <div v-show="toast" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'EmployeeManage',
  data() {
    return {
      loading: false,
      employeeList: [],
      searchKey: '',
      showDialog: false,
      title: '新增员工',
      form: {
        staffId: null,
        staffName: '',
        phone: '',
        password: '',
        role: 'staff',
        position: ''
      },
      toast: false,
      toastMsg: ''
    }
  },
  mounted() {
    this.loadList()
  },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const { data } = await axios.get('/ssm_springmvc/api/employee/list')
        let list = data.data || []
        if (this.searchKey) {
          const k = this.searchKey
          list = list.filter(e =>
              (e.staffName && e.staffName.includes(k)) ||
              (e.phone && e.phone.includes(k)) ||
              (e.position && e.position.includes(k))
          )
        }
        this.employeeList = list
      } catch (err) {
        console.error(err)
        this.showToast('加载失败')
      } finally {
        this.loading = false
      }
    },

    openAdd() {
      this.title = '新增员工'
      this.form = {
        staffId: null,
        staffName: '',
        phone: '',
        password: '',
        role: 'staff',
        position: ''
      }
      this.showDialog = true
    },

    openEdit(row) {
      this.title = '编辑员工'
      this.form = { ...row }
      this.showDialog = true
    },

    async submit() {
      if (!this.form.staffName) return this.showToast('请输入姓名')
      if (!this.form.phone) return this.showToast('请输入电话')
      if (!this.form.password && !this.form.staffId) return this.showToast('请输入密码')

      try {
        if (this.form.staffId) {
          await axios.post('/ssm_springmvc/api/employee/update', this.form)
          this.showToast('修改成功')
        } else {
          await axios.post('/ssm_springmvc/api/employee/add', this.form)
          this.showToast('新增成功')
        }
        this.closeDialog()
        this.loadList()
      } catch (e) {
        console.error(e)
        this.showToast('操作失败')
      }
    },

    async deleteItem(id) {
      if (!confirm('确定删除该员工吗？删除后无法恢复！')) return
      try {
        await axios.get(`/ssm_springmvc/api/employee/delete?staffId=${id}`)
        this.showToast('删除成功')
        this.loadList()
      } catch (e) {
        console.error(e)
        this.showToast('删除失败')
      }
    },

    closeDialog() {
      this.showDialog = false
    },

    reset() {
      this.searchKey = ''
      this.loadList()
    },

    showToast(msg) {
      this.toastMsg = msg
      this.toast = true
      setTimeout(() => this.toast = false, 1800)
    },

    logout() {
      localStorage.clear()
      this.$router.push('/login')
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
html, body { height: 100%; overflow: hidden; }
#app { background: #fff5eb; color: #5a4a3f; height: 100vh; }
.container { display: flex; height: 100vh; }

.sidebar {
  width: 220px;
  background: #fff;
  padding: 20px 0;
  position: fixed;
  left: 0; top: 0;
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
  transition: 0.2s;
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
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-buttons { display: flex; gap: 10px; }

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: 0.2s;
}
.btn-primary { background: #f5b041; color: #fff; }
.btn-default { background: #f8f9fa; color: #333; border: 1px solid #eee; }
.btn-edit { background: #2a9d8f; color: #fff; }
.btn-del { background: #e74c3c; color: #fff; }

.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 12px;
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
  width: 280px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 18px;
}
.employee-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px;
  box-shadow: 0 3px 12px rgba(0,0,0,0.06);
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
  transition: 0.25s;
}
.employee-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 18px rgba(0,0,0,0.09);
}
.card-avatar {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: #f5b041;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-body { flex: 1; }
.card-name { font-size: 16px; font-weight: bold; color: #333; }
.card-phone { font-size: 13px; color: #666; margin-top: 4px; }
.card-post { font-size: 12px; color: #999; margin-top: 4px; }
.card-status { margin-top: 6px; }
.tag-normal { background: #e8f5e9; color: #2e7d32; padding: 3px 8px; border-radius: 10px; font-size: 12px; }
.tag-stop { background: #ffeef0; color: #d32f2f; padding: 3px 8px; border-radius: 10px; font-size: 12px; }
.card-actions { display: flex; flex-direction: column; gap: 6px; }

.empty-tip { text-align: center; padding: 60px 0; color: #999; }

.dialog-mask {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.dialog-box {
  width: 460px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
}
.dialog-header {
  padding: 16px 20px;
  background: #f8f9fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-header h3 { color: #8b5a2b; }
.close { background: none; border: none; font-size: 20px; cursor: pointer; color: #666; }
.dialog-body { padding: 24px; }
.form-item { margin-bottom: 16px; }
.form-item label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  color: #333;
}
.form-item input, .form-item select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  font-size: 14px;
}
.dialog-footer {
  padding: 0 24px 20px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
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