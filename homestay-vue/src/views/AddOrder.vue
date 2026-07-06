<template>
  <div id="app" class="container">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <h2>民宿员工管理系统</h2>
      <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

      <div class="menu-group-title">房源管理</div>
      <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
      <div class="menu-item" @click="$router.push('/house-add')">➕ 房源录入</div>
      <div class="menu-item" @click="$router.push('/room-status')">🏷️ 房态管理</div>

      <div class="menu-group-title">订单管理</div>
      <div class="menu-item" @click="$router.push('/order-manage')">📋 预订管理</div>
      <div class="menu-item active">➕ 新增预定</div>
      <div class="menu-item" @click="$router.push('/review-manage')">💬 评价管理</div>

      <div class="menu-group-title">客户管理</div>
      <div class="menu-item" @click="$router.push('/user-manage')">👤 客户列表</div>

      <div class="menu-group-title">库房监控</div>
      <div class="menu-item" @click="$router.push('/storage-manage')">📦 库房管理</div>

      <div class="menu-group-title">系统操作</div>
      <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
      <div class="menu-item" @click="logout">🚪 退出登录</div>
    </div>

    <!-- 右侧内容区域 -->
    <div class="main-content">
      <div class="content-header">
        <h3>➕ 新增预定订单</h3>
        <span style="color:#8b4513;font-size:14px;">当前登录员工：{{ userName }}</span>
      </div>

      <div class="form-wrap">
        <div class="form-row">
          <div class="form-item">
            <label>选择房源 <span style="color:red;">*</span></label>
            <select v-model="form.roomId" @change="handleRoomChange">
              <option value="">请选择房源</option>
              <!-- 👇👇 这里修复成 roomId -->
              <option v-for="room in roomList" :key="room.roomId" :value="room.roomId">
                {{ room.roomName }} - ¥{{ room.price }}/晚
              </option>
            </select>
            <div class="room-preview" v-if="selectedRoom">
              <img :src="selectedRoom.img.startsWith('/upload/') ? `http://localhost:8080/ssm_springmvc${selectedRoom.img}` : `http://localhost:8080/ssm_springmvc/upload/${selectedRoom.img}`" :alt="selectedRoom.roomName">
              <div class="room-info">
                <h4>{{ selectedRoom.roomName }}</h4>
                <p>价格：¥{{ selectedRoom.price }}/晚</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label>房间序号（自动带出）</label>
            <input type="text" v-model="form.roomId" disabled style="background:#f5f5f5;">
          </div>
        </div>

        <div class="form-row">
          <div class="form-item">
            <label>客户姓名 <span style="color:red;">*</span></label>
            <input type="text" v-model="form.customerName" placeholder="请输入客户姓名">
          </div>
          <div class="form-item">
            <label>联系电话 <span style="color:red;">*</span></label>
            <input type="text" v-model="form.phone" placeholder="请输入联系电话" maxlength="11">
          </div>
        </div>

        <div class="form-row">
          <div class="form-item">
            <label>身份证号</label>
            <input type="text" v-model="form.idCard" placeholder="请输入身份证号" maxlength="18">
          </div>
          <div class="form-item">
            <label>入住人数（含儿童）</label>
            <input type="number" v-model="form.personCount" min="1" max="4" placeholder="最多4人">
          </div>
        </div>

        <div class="form-row">
          <div class="form-item">
            <label>入住日期 <span style="color:red;">*</span></label>
            <input type="date" v-model="form.checkInDate" @change="calcNights">
          </div>
          <div class="form-item">
            <label>退房日期 <span style="color:red;">*</span></label>
            <input type="date" v-model="form.checkOutDate" @change="calcNights">
          </div>
        </div>

        <div class="form-row">
          <div class="form-item" style="flex:2;">
            <label>备注信息</label>
            <input type="text" v-model="form.remark" placeholder="选填，可输入客户特殊要求">
          </div>
        </div>

        <div class="price-calc" v-if="totalPrice > 0">
          <div class="label">
            共 {{ nights }} 晚 × ¥{{ selectedRoom?.price }}/晚
          </div>
          <div class="value">应付总金额：¥{{ totalPrice }}</div>
        </div>

        <div class="btn-group">
          <button class="btn btn-default" @click="resetForm">重置</button>
          <button class="btn btn-default" @click="goBack">返回列表</button>
          <button class="btn btn-primary" @click="submitOrder">保存预定</button>
        </div>
      </div>
    </div>

    <div v-show="showToast" class="toast">{{ toastText }}</div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "AddOrder",
  data() {
    return {
      userName: localStorage.getItem('userName') || '员工',
      loginUserId: localStorage.getItem('userId') || '',
      roomList: [],
      selectedRoom: null,
      nights: 0,
      totalPrice: 0,
      form: {
        roomId: "",
        customerName: "",
        phone: "",
        idCard: "",
        checkInDate: "",
        checkOutDate: "",
        remark: "",
        personCount: 1
      },
      showToast: false,
      toastText: ""
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
      try {
        const res = await axios.get("/ssm_springmvc/api/room/staff/list")
        if (res.data.code === 0) {
          this.roomList = res.data.data || []
        }
      } catch (e) {
        console.error("加载房间失败", e)
        this.showToastMsg("加载房间失败，请检查Tomcat是否启动")
      }
    },

    goBack() {
      this.$router.push('/order-manage')
    },

    handleRoomChange() {
      this.selectedRoom = this.roomList.find(r => r.roomId == this.form.roomId)
      this.calcNights()
    },

    calcNights() {
      if (!this.form.checkInDate || !this.form.checkOutDate || !this.selectedRoom) {
        this.nights = 0
        this.totalPrice = 0
        return
      }
      const start = new Date(this.form.checkInDate)
      const end = new Date(this.form.checkOutDate)
      if (end <= start) {
        this.showToastMsg("退房日期必须晚于入住日期")
        this.form.checkOutDate = ""
        this.nights = 0
        this.totalPrice = 0
        return
      }
      const diffTime = end - start
      this.nights = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      this.totalPrice = this.nights * this.selectedRoom.price
    },

    resetForm() {
      this.form = {
        roomId: "",
        customerName: "",
        phone: "",
        idCard: "",
        checkInDate: "",
        checkOutDate: "",
        remark: "",
        personCount: 1
      }
      this.selectedRoom = null
      this.nights = 0
      this.totalPrice = 0
    },

    logout() {
      localStorage.clear()
      this.$router.push('/login')
    },

    async submitOrder() {
      if (!this.form.roomId) {
        this.showToastMsg("请选择房源")
        return
      }
      if (!this.form.customerName) {
        this.showToastMsg("请输入客户姓名")
        return
      }
      if (!/^1[3-9]\d{9}$/.test(this.form.phone)) {
        this.showToastMsg("请输入正确的手机号")
        return
      }
      if (!this.form.checkInDate || !this.form.checkOutDate) {
        this.showToastMsg("请选择入住和退房日期")
        return
      }
      if (this.totalPrice <= 0) {
        this.showToastMsg("日期选择无效，无法计算金额")
        return
      }

      const params = {
        roomId: this.form.roomId,
        roomName: this.selectedRoom.roomName,
        roomImg: this.selectedRoom.img,
        startTime: this.form.checkInDate,
        endTime: this.form.checkOutDate,
        roomCount: 1,
        peopleCount: this.form.personCount,
        contactPhone: this.form.phone,
        totalPrice: this.totalPrice,
        payMethod: "cash",
        payStatus: 0,
        orderStatus: 0,
        userId: this.loginUserId,
        customerName: this.form.customerName
      }

      try {
        await axios.post("/ssm_springmvc/api/order/add", params)
        this.showToastMsg("预定新增成功！")
        setTimeout(() => this.goBack(), 1200)
      } catch (e) {
        console.error(e)
        this.showToastMsg("提交失败")
      }
    },

    showToastMsg(text) {
      this.toastText = text
      this.showToast = true
      setTimeout(() => this.showToast = false, 1500)
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

/* ========== 左侧导航栏统一样式 ========== */
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

  /* 隐藏导航栏滚动条 */
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
}

.form-wrap {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  width: 100%;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 25px;
  margin-bottom: 20px;
}

.form-item {
  flex: 1;
  min-width: 280px;
}

.form-item label {
  display: block;
  font-size: 14px;
  color: #5a4a3f;
  margin-bottom: 8px;
}

.form-item input,
.form-item select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #eee;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
}

.form-item input:focus,
.form-item select:focus {
  border-color: #f5b041;
}

.room-preview {
  background: #faf7f2;
  border: 1px solid #f0d9b5;
  border-radius: 8px;
  padding: 15px;
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.room-preview img {
  width: 100px;
  height: 70px;
  object-fit: cover;
  border-radius: 6px;
}

.room-info h4 {
  color: #8b5a2b;
  margin-bottom: 5px;
}

.room-info p {
  font-size: 13px;
  color: #5a4a3f;
}

.price-calc {
  background: #fff9ef;
  border-radius: 8px;
  padding: 15px 20px;
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-calc .label {
  font-size: 14px;
  color: #5a4a3f;
}

.price-calc .value {
  font-size: 20px;
  font-weight: bold;
  color: #f5b041;
}

.btn-group {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.btn {
  padding: 9px 22px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: 0.2s;
}

.btn-default {
  background: #f8f9fa;
  color: #5a4a3f;
  border: 1px solid #eee;
}

.btn-primary {
  background: #f5b041;
  color: #fff;
}

.btn-primary:hover {
  background: #e69c2e;
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