<template>
  <div id="app">
    <div class="container">
      <!-- 左侧导航栏 -->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
        <div class="menu-item active">➕ 房源录入</div>
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

      <!-- 右侧：房源录入表单 -->
      <div class="main-content">
        <div class="content-header">
          <h3>房源录入</h3>
          <button class="btn" @click="goPage('/house-list')">返回列表</button>
        </div>

        <div class="form-container">
          <div class="form-item">
            <label>房源主图</label>
            <div class="upload-area" @click="triggerUpload">
              <img v-if="previewImg" :src="previewImg" alt="房源预览图">
              <div class="placeholder" v-else>
                📷 点击上传房源主图
                <small>支持JPG/PNG格式，大小不超过5MB，建议比例16:9</small>
              </div>
            </div>
            <input
                type="file"
                ref="imgInput"
                style="display:none;"
                accept="image/*"
                @change="handleImageChange"
            >
          </div>

          <div class="form-item">
            <label>房间ID（房号）* </label>
            <input
                v-model="form.roomId"
                type="number"
                placeholder="请输入房间ID（例如：101、202、808）"
            >
          </div>

          <div class="form-grid">
            <div class="form-item">
              <label>房源名称 *</label>
              <input v-model="form.roomName" placeholder="请输入房源名称（如：山景大床房）">
            </div>
            <div class="form-item">
              <label>房源价格（元/晚） *</label>
              <input v-model="form.price" type="number" placeholder="请输入房源价格">
            </div>
          </div>

          <div class="form-grid">
            <div class="form-item">
              <label>房型类型</label>
              <input v-model="form.roomType" placeholder="如：大床房/双床房/套房">
            </div>
            <div class="form-item">
              <label>上下架状态</label>
              <select v-model="form.status">
                <option :value="1">上架（可预订）</option>
                <option :value="0">下架（不可预订）</option>
              </select>
            </div>
          </div>

          <div class="form-item">
            <label>房源描述</label>
            <textarea v-model="form.desc" placeholder="请输入房源详细描述，包括设施、周边环境等"></textarea>
          </div>

          <div class="form-footer">
            <button class="btn btn-reset" @click="resetForm">重置</button>
            <button class="btn" @click="submitHouse">提交保存</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "HouseAdd",
  data() {
    return {
      form: {
        roomId: '',
        roomName: '',
        price: '',
        desc: '',
        roomType: '',
        status: 1,
        img: 'default.png'
      },
      previewImg: '',
      imgFile: null
    }
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    const role = localStorage.getItem('userRole')
    if (!userId || !role) {
      this.$router.replace('/login')
      return
    }
  },
  methods: {
    goPage(path) {
      this.$router.push(path)
    },

    triggerUpload() {
      this.$refs.imgInput.click()
    },

    handleImageChange(e) {
      const file = e.target.files[0]
      if (!file) return
      if (!file.type.startsWith('image/')) {
        alert('请上传图片')
        return
      }
      if (file.size > 5 * 1024 * 1024) {
        alert('图片不能大于5M')
        return
      }
      this.imgFile = file
      this.form.img = file.name
      let reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = res => {
        this.previewImg = res.target.result
      }
    },

    resetForm() {
      this.form = {
        roomId: '',
        roomName: '',
        price: '',
        desc: '',
        roomType: '',
        status: 1,
        img: 'default.png'
      }
      this.previewImg = ''
      this.imgFile = null
      this.$refs.imgInput.value = ''
    },

    async checkRoomIdExists(roomId) {
      try {
        const res = await axios.get(`/ssm_springmvc/api/room/detail?id=${roomId}`)
        return res.data.data != null
      } catch (e) {
        return false
      }
    },

    async submitHouse() {
      if (!this.form.roomId) {
        alert('❌ 房间ID（房号）不能为空！')
        return
      }
      if (!this.form.roomName || !this.form.price) {
        alert('房源名称和价格不能为空！')
        return
      }

      const exists = await this.checkRoomIdExists(this.form.roomId)
      if (exists) {
        alert('❌ 该房间ID已存在，不能重复添加！')
        return
      }

      try {
        const formData = new FormData()
        formData.append('roomId', this.form.roomId)
        formData.append('roomName', this.form.roomName)
        formData.append('price', this.form.price)
        formData.append('desc', this.form.desc || '')
        formData.append('roomType', this.form.roomType || '普通房型')
        formData.append('status', this.form.status)

        if (this.imgFile) {
          formData.append('imgFile', this.imgFile)
        }

        const res = await axios.post("/ssm_springmvc/api/room/staff/add", formData)

        if (res.data.code === 0) {
          alert('✅ 房源录入成功！')
          this.resetForm()
          this.goPage('/house-list')
        } else {
          alert('❌ 失败：' + res.data.msg)
        }
      } catch (err) {
        console.error(err)
        alert('❌ 接口请求失败')
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
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
.btn-reset {
  background: #f0f0f0;
  color: #5a4a3f;
}
.btn-reset:hover {
  background: #e0e0e0;
}
.form-container {
  background: #fff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  width: 100%;
}
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 24px;
}
.form-item {
  margin-bottom: 24px;
}
.form-item label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #5a4a3f;
  font-weight: bold;
}
.form-item input,
.form-item select,
.form-item textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #f0d9b5;
  border-radius: 5px;
  font-size: 14px;
  outline: none;
}
.form-item input:focus,
.form-item textarea:focus {
  border-color: #f5b041;
}
.form-item textarea {
  resize: vertical;
  min-height: 100px;
}
.upload-area {
  border: 2px dashed #f0d9b5;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;
}
.upload-area:hover {
  border-color: #f5b041;
  background: #fffaf0;
}
.upload-area img {
  max-height: 180px;
  max-width: 100%;
  border-radius: 6px;
}
.upload-area .placeholder {
  color: #8b5a2b;
  font-size: 14px;
}
.upload-area .placeholder small {
  color: #aaa;
  font-size: 12px;
  display: block;
  margin-top: 8px;
}
.form-footer {
  text-align: right;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #f0d9b5;
}
.form-footer button {
  width: 120px;
  margin-left: 10px;
}
</style>