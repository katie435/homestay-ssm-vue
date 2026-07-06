<template>
  <div id="app">
    <div class="container">
      <!-- 侧边栏 -->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
        <div class="menu-item" @click="$router.push('/house-add')">➕ 房源录入</div>
        <div class="menu-item active">🏷️ 房态管理</div>

        <div class="menu-group-title">订单管理</div>
        <div class="menu-item" @click="$router.push('/order-manage')">📋 预订管理</div>
        <div class="menu-item" @click="$router.push('/add-order')">➕ 新增预定</div>
        <div class="menu-item" @click="$router.push('/review-manage')">💬 评价管理</div>

        <div class="menu-group-title">客户管理</div>
        <div class="menu-item" @click="$router.push('/user-manage')">👤 客户列表</div>

        <div class="menu-group-title">库房监控</div>
        <div class="menu-item" @click="$router.push('/storage-manage')">📦 库房管理</div>

        <div class="menu-group-title">系统操作</div>
        <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧主内容 -->
      <div class="main-content">
        <div class="content-header">
          <h3>🏷️ 房态管理</h3>
          <button class="btn btn-default" @click="loadRoomList">刷新数据</button>
        </div>

        <!-- 房态统计 -->
        <div class="status-board">
          <div class="board-card">
            <div class="board-icon" style="background: #e8f5e9; color: #2a9d8f;">✅</div>
            <div class="board-text">
              <div class="num">{{ stats.canSale }}</div>
              <div class="label">可售房源</div>
            </div>
          </div>
          <div class="board-card">
            <div class="board-icon" style="background: #fff4e5; color: #ff9800;">📅</div>
            <div class="board-text">
              <div class="num">{{ stats.booked }}</div>
              <div class="label">已预订</div>
            </div>
          </div>
          <div class="board-card">
            <div class="board-icon" style="background: #e3f2fd; color: #2196f3;">🏠</div>
            <div class="board-text">
              <div class="num">{{ stats.living }}</div>
              <div class="label">已入住</div>
            </div>
          </div>
          <div class="board-card">
            <div class="board-icon" style="background: #f5f5f5; color: #999;">🔧</div>
            <div class="board-text">
              <div class="num">{{ stats.repair }}</div>
              <div class="label">维护中</div>
            </div>
          </div>
        </div>

        <!-- 筛选 -->
        <div class="filter-bar">
          <div class="filter-item">
            <label>房源名称：</label>
            <input v-model="filter.roomName" placeholder="搜索房源" type="text">
          </div>
          <div class="filter-item">
            <label>房态：</label>
            <select v-model="filter.roomStatus">
              <option value="">全部</option>
              <option value="empty">可售</option>
              <option value="booked">已预订</option>
              <option value="occupied">已入住</option>
              <option value="repair">维护中</option>
            </select>
          </div>
          <button class="btn" @click="searchData">查询</button>
          <button class="btn btn-default" @click="resetFilter">重置</button>
        </div>

        <!-- 表格 -->
        <div class="table-container">
          <div v-if="loading" class="loading">加载中...</div>
          <table v-else>
            <thead>
            <tr>
              <th>序号</th>
              <th>房源ID</th>
              <th>房源名称</th>
              <th>当前房态</th>
              <th>价格</th>
              <th>上下架</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item, index) in tableData" :key="item.roomId">
              <td>{{ index + 1 }}</td>
              <td>{{ item.roomId }}</td>
              <td>{{ item.roomName }}</td>
              <td>
                <span class="status-tag" :class="getStatusClass(item.roomStatus)">
                  {{ getStatusName(item.roomStatus) }}
                </span>
              </td>
              <td>¥{{ item.price }}</td>
              <td>
                <label class="switch">
                  <input type="checkbox" :checked="item.status === 1" @change="toggleStatus(item)">
                  <span class="slider"></span>
                </label>
              </td>
              <td>
                <div class="operate-btns">
                  <button class="btn" @click="editRoom(item)">编辑房态</button>
                  <button
                      class="btn"
                      :class="item.roomStatus==='repair' ? 'btn-success' : 'btn-danger'"
                      @click="setRepair(item)"
                  >
                    {{ item.roomStatus==='repair' ? '恢复可售' : '维护' }}
                  </button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 自定义居中模态框 -->
    <div v-show="showModal" class="modal-mask">
      <div class="modal-box">
        <div class="modal-header">{{ modalTitle }}</div>
        <div class="modal-body">
          <p>{{ modalText }}</p>
          <select v-if="modalType === 'select'" v-model="selectRoomStatus" class="modal-select">
            <option value="empty">可售</option>
            <option value="booked">已预订</option>
            <option value="occupied">已入住</option>
            <option value="repair">维护中</option>
          </select>
        </div>
        <div class="modal-footer">
          <button class="modal-btn modal-cancel" @click="closeModal">取消</button>
          <button class="modal-btn modal-confirm" @click="confirmAction">确定</button>
        </div>
      </div>
    </div>

    <!-- 轻提示 -->
    <div v-show="showToast" class="toast">{{ toastText }}</div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "RoomStatus",
  data() {
    return {
      loading: false,
      stats: { canSale: 0, booked: 0, living: 0, repair: 0 },
      filter: { roomName: '', roomStatus: '' },
      tableData: [],
      originalData: [],

      showModal: false,
      modalTitle: '',
      modalText: '',
      modalType: '',
      currentItem: null,
      selectRoomStatus: '',
      operateType: '',

      showToast: false,
      toastText: ''
    }
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    const role = localStorage.getItem('userRole')
    if (!userId || !role) {
      this.$router.replace('/login')
      return
    }
    this.loadRoomList()
  },
  methods: {
    async loadRoomList() {
      this.loading = true
      try {
        // 已修复：和员工管理接口格式完全一致
        const res = await axios.get('/api/api/room/staff/list')
        if (res.data.code === 200 || res.data.code === 0) {
          this.tableData = res.data.data
          this.originalData = res.data.data
          this.calculateStats()
        }
      } catch (err) {
        console.error(err)
        this.showToastMsg('加载失败')
      } finally {
        this.loading = false
      }
    },

    calculateStats() {
      this.tableData.forEach(item => {
        if (!item.roomStatus || item.roomStatus === '') item.roomStatus = 'empty'
      })
      const data = this.tableData
      this.stats.canSale = data.filter(i => i.status === 1 && i.roomStatus === 'empty').length
      this.stats.booked = data.filter(i => i.status === 1 && i.roomStatus === 'booked').length
      this.stats.living = data.filter(i => i.status === 1 && i.roomStatus === 'occupied').length
      this.stats.repair = data.filter(i => i.roomStatus === 'repair').length
    },

    getStatusClass(s) {
      const obj = {empty: 'tag-success', booked: 'tag-wait', occupied: 'tag-live', repair: 'tag-disable'}
      return obj[s] || 'tag-disable'
    },
    getStatusName(s) {
      const obj = {empty: '可售', booked: '已预订', occupied: '已入住', repair: '维护中'}
      return obj[s] || '未知'
    },

    openConfirmModal(title, text, item, type) {
      this.modalTitle = title
      this.modalText = text
      this.modalType = 'confirm'
      this.currentItem = item
      this.operateType = type
      this.showModal = true
    },
    openSelectModal(item) {
      this.modalTitle = '编辑房态'
      this.modalText = `请选择【${item.roomName}】的房态：`
      this.modalType = 'select'
      this.currentItem = item
      this.selectRoomStatus = item.roomStatus
      this.operateType = 'edit'
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
    },
    showToastMsg(text) {
      this.toastText = text
      this.showToast = true
      setTimeout(() => {
        this.showToast = false
      }, 1500)
    },

    toggleStatus(item) {
      const text = item.status === 1 ? '下架' : '上架'
      this.openConfirmModal('确认操作', `确定将【${item.roomName}】${text}？`, item, 'toggle')
    },
    setRepair(item) {
      if (item.roomStatus === 'repair') {
        this.openConfirmModal('恢复可售', `确定将【${item.roomName}】恢复为可售并上架？`, item, 'restore')
      } else {
        this.openConfirmModal('设置维护', `确定将【${item.roomName}】设为维护中？`, item, 'repair')
      }
    },
    editRoom(item) {
      this.openSelectModal(item)
    },

    async confirmAction() {
      const item = this.currentItem
      if (!item) return this.closeModal()

      try {
        if (this.operateType === 'toggle') {
          const newStatus = item.status === 1 ? 0 : 1
          // 已修复
          await axios.get('/api/api/room/staff/status', {
            params: {roomId: item.roomId, status: newStatus}
          })
          item.status = newStatus
          this.calculateStats()
          this.showToastMsg('操作成功')
        }

        if (this.operateType === 'repair') {
          // 已修复
          await axios.get('/api/api/room/staff/roomStatus', {
            params: {roomId: item.roomId, roomStatus: 'repair'}
          })
          item.roomStatus = 'repair'
          this.calculateStats()
          this.showToastMsg('设置成功')
        }

        if (this.operateType === 'restore') {
          // 已修复
          await axios.get('/api/api/room/staff/roomStatus', {
            params: {roomId: item.roomId, roomStatus: 'empty'}
          })
          await axios.get('/api/api/room/staff/status', {
            params: {roomId: item.roomId, status: 1}
          })
          item.roomStatus = 'empty'
          item.status = 1
          this.calculateStats()
          this.showToastMsg('已恢复可售并上架')
        }

        if (this.operateType === 'edit') {
          // 已修复
          await axios.get('/api/api/room/staff/roomStatus', {
            params: {roomId: item.roomId, roomStatus: this.selectRoomStatus}
          })
          item.roomStatus = this.selectRoomStatus
          this.calculateStats()
          this.showToastMsg('修改成功')
        }
      } catch (err) {
        console.error(err)
        this.showToastMsg('操作失败')
      }

      this.closeModal()
    },

    searchData() {
      let data = [...this.originalData]
      if (this.filter.roomName) data = data.filter(i => i.roomName.includes(this.filter.roomName))
      if (this.filter.roomStatus) data = data.filter(i => i.roomStatus === this.filter.roomStatus)
      this.tableData = data
      this.calculateStats()
    },
    resetFilter() {
      this.filter = {roomName: '', roomStatus: ''}
      this.loadRoomList()
    },

    logout() {
      localStorage.clear()
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
/* 全局统一 */
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

/* ========== 左侧导航栏（和你其他页面完全一样） ========== */
.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #f0d9b5;
  padding: 20px 0;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  overflow-y: auto;

  /* 隐藏滚动条 */
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.sidebar::-webkit-scrollbar {
  display: none;
}

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
  transition: all 0.2s ease;
}

.menu-item.active, .menu-item:hover {
  background: #f5b041;
  color: #fff;
}

/* ========== 右侧内容：删除多余滚动条 ========== */
.main-content {
  flex: 1;
  margin-left: 220px;
  padding: 20px;
  height: 100vh;
  overflow-y: auto;

  /* 隐藏右侧滚动条 */
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.main-content::-webkit-scrollbar {
  display: none;
}

.content-header {
  background: #fff;
  padding: 18px 24px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-header h3 {
  color: #8b5a2b;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn {
  padding: 8px 16px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.btn:hover {
  background: #e69c2e;
}

.btn-default {
  background: #f8f9fa;
  color: #5a4a3f;
  border: 1px solid #eee;
}

.btn-danger {
  background: #e76f51;
}

.btn-success {
  background: #2a9d8f;
}

.btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.status-board {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.board-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.2s ease;
}

.board-card:hover {
  transform: translateY(-3px);
}

.board-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.board-text {
  flex: 1;
}

.board-text .num {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.board-text .label {
  font-size: 14px;
  color: #999;
}

.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-item label {
  font-size: 14px;
  color: #5a4a3f;
}

.filter-item input, .filter-item select {
  padding: 8px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  outline: none;
  font-size: 14px;
}

.table-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  width: 100%;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: #f9f0e1;
}

th, td {
  padding: 16px 20px;
  text-align: left;
  border-bottom: 1px solid #f5f5f5;
  font-size: 14px;
}

th {
  color: #8b5a2b;
  font-weight: bold;
}

tbody tr:hover {
  background: #faf7f2;
}

.status-tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.tag-success {
  background: #e8f5e9;
  color: #2a9d8f;
}

.tag-wait {
  background: #fff4e5;
  color: #ff9800;
}

.tag-live {
  background: #e3f2fd;
  color: #2196f3;
}

.tag-disable {
  background: #f5f5f5;
  color: #999;
}

.switch {
  position: relative;
  display: inline-block;
  width: 44px;
  height: 22px;
}

.switch input {
  opacity: 0;
}

.slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .3s;
  border-radius: 22px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 2px;
  bottom: 2px;
  background-color: white;
  transition: .3s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #f5b041;
}

input:checked + .slider:before {
  transform: translateX(22px);
}

.operate-btns {
  display: flex;
  gap: 8px;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #8b5a2b;
  font-size: 16px;
}

.modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-box {
  width: 420px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.modal-header {
  padding: 16px 20px;
  background: #f5b041;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
}

.modal-body {
  padding: 24px;
  text-align: center;
}

.modal-body p {
  font-size: 15px;
  color: #5a4a3f;
  margin-bottom: 16px;
}

.modal-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  margin: 10px 0;
  font-size: 14px;
}

.modal-footer {
  padding: 16px;
  display: flex;
  justify-content: center;
  gap: 12px;
  border-top: 1px solid #f5f5f5;
}

.modal-btn {
  padding: 8px 24px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  border: none;
}

.modal-cancel {
  background: #f8f9fa;
  color: #666;
}

.modal-confirm {
  background: #f5b041;
  color: #fff;
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

@media (max-width: 1200px) {
  .status-board {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .status-board {
    grid-template-columns: 1fr;
  }
}
</style>