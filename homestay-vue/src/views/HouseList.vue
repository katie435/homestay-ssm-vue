<template>
  <div id="app">
    <div class="container">
      <!-- 左侧导航栏（样式与录入页完全一致） -->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item active">🏘️ 房源列表</div>
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
        <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧：房源列表 -->
      <div class="main-content">
        <div class="content-header">
          <h3>房源列表</h3>
          <button class="btn" @click="goPage('/house-add')">新增房源</button>
        </div>

        <div class="house-table-container">
          <div v-if="loading" class="loading">数据加载中...</div>
          <div v-else-if="roomList.length === 0" class="empty-tip">暂无房源数据</div>
          <table class="house-table" v-else>
            <thead>
            <tr>
              <th>房源图片</th>
              <th>房源ID</th>
              <th>房源名称</th>
              <th>价格（元/晚）</th>
              <th>描述</th>
              <th>房型类型</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="room in roomList" :key="room.roomId">
              <td>
                <img
                    :src="room.img.startsWith('/upload/') ? `http://localhost:8080/ssm_springmvc${room.img}` : `http://localhost:8080/ssm_springmvc/upload/${room.img}`"
                    class="house-img"
                    alt="房源图片"
                >
              </td>
              <td>{{ room.roomId }}</td>
              <td>{{ room.roomName }}</td>
              <td>¥{{ room.price }}</td>
              <td style="max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
                {{ room.desc || '无描述' }}
              </td>
              <td>{{ room.roomType || '普通房型' }}</td>
              <td>
                <button class="btn" style="padding:4px 8px;font-size:12px;" @click="editRoom(room)">编辑</button>
                <button class="btn btn-red" style="padding:4px 8px;font-size:12px;margin-left:5px;" @click="deleteRoom(room.roomId)">删除</button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 编辑弹窗 -->
    <div class="modal" v-show="showEditModal">
      <div class="modal-content">
        <div class="modal-title">编辑房源信息</div>
        <div class="form-group">
          <label>房源ID（不可修改）</label>
          <input v-model="editForm.roomId" readonly>
        </div>
        <div class="form-group">
          <label>房源名称</label>
          <input v-model="editForm.roomName">
        </div>
        <div class="form-group">
          <label>价格（元/晚）</label>
          <input v-model="editForm.price" type="number">
        </div>
        <div class="form-group">
          <label>房源描述</label>
          <textarea v-model="editForm.desc" rows="3"></textarea>
        </div>
        <div class="form-group">
          <label>房型类型</label>
          <input v-model="editForm.roomType">
        </div>
        <div class="form-group">
          <label>上下架状态</label>
          <select v-model="editForm.status">
            <option :value="1">上架</option>
            <option :value="0">下架</option>
          </select>
        </div>
        <div class="modal-footer">
          <button class="btn" @click="showEditModal = false">取消</button>
          <button class="btn" @click="saveEdit">保存修改</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "HouseList",
  data() {
    return {
      loading: true,
      roomList: [],
      showEditModal: false,
      editForm: {
        roomId: '',
        roomName: '',
        price: '',
        desc: '',
        roomType: '',
        status: 1,
        img: ''
      }
    }
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    const role = localStorage.getItem('userRole')
    if (!userId || !role) {
      this.$router.replace('/login')
      return
    }
    this.getRoomList()
  },
  methods: {
    async getRoomList() {
      try {
        const res = await axios.get("/ssm_springmvc/api/room/staff/list");
        if (res.data.code === 200 || res.data.code === 0) {
          this.roomList = res.data.data;
        } else {
          alert(`接口返回错误：${res.data.msg || '未知错误'}`)
        }
      } catch (err) {
        console.error("请求数据失败", err);
        if (err.message.includes('Network Error') || err.message.includes('ERR_CONNECTION_REFUSED')) {
          alert('❌ 无法连接后端，请检查：\n1. 后端服务是否启动\n2. 端口是否为8080\n3. 跨域是否配置')
        } else {
          alert(`❌ 请求失败：${err.message}`)
        }
      } finally {
        this.loading = false;
      }
    },

    goPage(path) {
      this.$router.push(path)
    },

    editRoom(room) {
      this.editForm = { ...room }
      this.showEditModal = true
    },

    async saveEdit() {
      try {
        const params = new URLSearchParams()
        params.append('roomId', this.editForm.roomId)
        params.append('roomName', this.editForm.roomName)
        params.append('price', this.editForm.price)
        params.append('desc', this.editForm.desc || '')
        params.append('roomType', this.editForm.roomType || '普通房型')
        params.append('status', this.editForm.status)
        params.append('img', this.editForm.img)

        const res = await axios.post("/ssm_springmvc/api/room/staff/update", params)

        if (res.data.code === 200 || res.data.code === 0) {
          alert('修改成功！')
          this.showEditModal = false
          this.getRoomList()
        } else {
          alert('修改失败：' + (res.data.msg || '未知错误'))
        }
      } catch (err) {
        console.error(err);
        alert('保存修改失败，请检查后端服务')
      }
    },

    async deleteRoom(roomId) {
      if (confirm('确定删除该房源吗？')) {
        try {
          const res = await axios.get(`/ssm_springmvc/api/room/staff/delete?roomId=${roomId}`)
          if (res.data.code === 200 || res.data.code === 0) {
            alert('删除成功')
            this.getRoomList()
          } else {
            alert('删除失败：' + (res.data.msg || '未知错误'))
          }
        } catch (err) {
          console.error(err);
          alert('删除失败，请检查后端服务')
        }
      }
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

/* ========== 左侧导航 100% 和 price-setting 一样 ========== */
.sidebar {
  width: 220px;
  background: #fff;
  border-right: 1px solid #f0d9b5;
  padding: 20px 0;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  overflow-y: auto;
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

/* ========== 右侧内容 100% 和 price-setting 一样 ========== */
.main-content {
  flex: 1;
  padding: 20px;
  margin-left: 220px;
  height: 100vh;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}
.main-content::-webkit-scrollbar {
  display: none;
}

.content-header {
  background: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.content-header h3 {
  color: #8b5a2b;
  font-size: 16px;
}
.btn {
  padding: 8px 16px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn:hover {
  background: #e69c2e;
}
.btn-red {
  background: #e76f51;
}
.house-table-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.house-table {
  width: 100%;
  border-collapse: collapse;
}
.house-table th, .house-table td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #f0d9b5;
  font-size: 14px;
  color: #5a4a3f;
}
.house-table th {
  background: #f9f0e1;
  color: #8b5a2b;
}
.house-img {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}
.loading, .empty-tip {
  text-align: center;
  padding: 50px;
  color: #8b5a2b;
}
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.modal-content {
  width: 500px;
  background: #fff;
  border-radius: 10px;
  padding: 25px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.2);
}
.modal-title {
  text-align: center;
  font-size: 18px;
  color: #8b5a2b;
  margin-bottom: 20px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-size: 14px;
  color: #5a4a3f;
}
.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #f0d9b5;
  border-radius: 5px;
  font-size: 14px;
  color: #5a4a3f;
}
.modal-footer {
  text-align: right;
  margin-top: 20px;
}
.modal-footer button {
  margin-left: 10px;
}
</style>