<template>
  <div id="app">
    <div class="container">
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
        <div class="menu-item" @click="$router.push('/house-add')">➕ 房源录入</div>
        <div class="menu-item" @click="$router.push('/room-status')">🏷️ 房态管理</div>

        <div class="menu-group-title">订单管理</div>
        <div class="menu-item active">📋 预订管理</div>
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

      <div class="main-content">
        <div class="content-header">
          <h3>📋 订单管理</h3>
          <button class="btn btn-default" @click="loadOrders">刷新</button>
        </div>

        <div class="filter-bar">
          <div class="filter-item">
            <label>订单号/房间：</label>
            <input v-model="searchKey" placeholder="输入搜索" />
          </div>
          <div class="filter-item">
            <label>状态：</label>
            <select v-model="searchStatus">
              <option value="">全部</option>
              <option value="0">待入住</option>
              <option value="1">已入住</option>
              <option value="2">需打扫</option>
              <option value="3">已取消</option>
              <option value="4">已完成</option>
            </select>
          </div>
          <button class="btn" @click="loadOrders">搜索</button>
          <button class="btn btn-default" @click="reset">重置</button>
        </div>

        <div class="table-container">
          <div v-if="loading" class="loading">加载中...</div>
          <table v-else>
            <thead>
            <tr>
              <th>序号</th>
              <th>订单号</th>
              <th>房间ID</th>
              <th>房间名</th>
              <th>入住时间</th>
              <th>退房时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(o, i) in orderList" :key="o.id">
              <td>{{ i+1 }}</td>
              <td>{{ o.orderId }}</td>
              <td>{{ o.roomId }}</td>
              <td>{{ o.roomName }}</td>
              <td>{{ o.startTime }}</td>
              <td>{{ o.endTime }}</td>
              <td>
                  <span :class="orderTagClass(o.orderStatus)">
                    {{ orderStatusName(o.orderStatus) }}
                  </span>
              </td>
              <td>
                <div class="operate-btns">
                  <button v-if="o.orderStatus == 0" class="btn btn-success" @click="openCheckIn(o)">办理入住</button>
                  <button v-if="o.orderStatus == 1" class="btn btn-danger" @click="openCheckOut(o)">办理退房</button>
                  <button v-if="o.orderStatus == 2" class="btn" style="background:#666;color:#fff" @click="openClean(o)">打扫完成</button>
                  <button v-if="o.orderStatus == 0" class="btn" style="background:#999;color:#fff" @click="openCancel(o)">取消订单</button>
                  <button class="btn btn-default" @click="showDetail(o)">详情</button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 统一确认小窗口 -->
    <div v-if="confirmVisible" class="confirm-box">
      <div class="confirm-content">
        <h4>{{ confirmTitle }}</h4>
        <p>{{ confirmMessage }}</p>
        <div class="confirm-btns">
          <button class="btn btn-default" @click="confirmVisible=false">取消</button>
          <button class="btn" @click="doConfirm">确认</button>
        </div>
      </div>
    </div>

    <!-- 详情窗口 + 新增预订人 -->
    <div v-if="showDetailFlag" class="detail-box">
      <div class="box-content">
        <h3 class="box-title">订单详情</h3>
        <div class="box-body">
          <p>订单号：{{ detail.orderId }}</p>
          <p>房间：{{ detail.roomName }} (ID:{{ detail.roomId }})</p>
          <p>入住：{{ detail.startTime }}</p>
          <p>退房：{{ detail.endTime }}</p>
          <p>联系人：{{ detail.contactPhone }}</p>
          <p>预订人：{{ detail.userName || '无' }}</p>
          <p>总价：{{ detail.totalPrice }}元</p>
          <p>状态：{{ orderStatusName(detail.orderStatus) }}</p>
        </div>
        <button class="close-btn" @click="showDetailFlag=false">关闭</button>
      </div>
    </div>

    <div v-show="toast" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "OrderManage",
  data() {
    return {
      loading: false,
      orderList: [],
      searchKey: "",
      searchStatus: "",
      showDetailFlag: false,
      detail: {},
      toast: false,
      toastMsg: "",

      confirmVisible: false,
      confirmTitle: "",
      confirmMessage: "",
      confirmType: "",
      currentOrder: null
    }
  },
  mounted() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const res = await axios.get("/ssm_springmvc/api/order/list")
        let list = res.data.data || []

        // ====================== 搜索过滤 ======================
        // 1. 状态过滤
        if (this.searchStatus !== "") {
          list = list.filter(item => item.orderStatus == this.searchStatus)
        }
        // 2. 关键词过滤（订单号 / 房间名）
        if (this.searchKey.trim() !== "") {
          const key = this.searchKey.trim()
          list = list.filter(item =>
              (item.orderId && item.orderId.includes(key)) ||
              (item.roomName && item.roomName.includes(key))
          )
        }

        this.orderList = list
      } catch (e) {
        this.toastMsg = "加载失败"
        this.toast = true
      } finally {
        this.loading = false
      }
    },

    reset() {
      this.searchKey = ""
      this.searchStatus = ""
      this.loadOrders()
    },

    openCheckIn(order) {
      this.currentOrder = order
      this.confirmTitle = "入住确认"
      this.confirmMessage = "确认办理入住？"
      this.confirmType = "checkIn"
      this.confirmVisible = true
    },
    openCheckOut(order) {
      this.currentOrder = order
      this.confirmTitle = "退房确认"
      this.confirmMessage = "确认退房？房间将变为需打扫"
      this.confirmType = "checkOut"
      this.confirmVisible = true
    },
    openClean(order) {
      this.currentOrder = order
      this.confirmTitle = "打扫确认"
      this.confirmMessage = "确认已打扫完成？房间可重新预订"
      this.confirmType = "clean"
      this.confirmVisible = true
    },
    openCancel(order) {
      this.currentOrder = order
      this.confirmTitle = "取消订单"
      this.confirmMessage = "确定要取消该订单吗？取消后无法恢复！"
      this.confirmType = "cancel"
      this.confirmVisible = true
    },

    async doConfirm() {
      this.confirmVisible = false
      const order = this.currentOrder
      try {
        if (this.confirmType === "checkIn") {
          await axios.post("/ssm_springmvc/api/order/checkIn", {id: order.id})
          this.toastMsg = "办理入住成功"
        } else if (this.confirmType === "checkOut") {
          await axios.post("/ssm_springmvc/api/order/checkOut", {id: order.id})
          this.toastMsg = "退房成功，房间待打扫"
        } else if (this.confirmType === "clean") {
          await axios.post("/ssm_springmvc/api/order/cleanFinish", {id: order.id})
          this.toastMsg = "打扫完成"
        } else if (this.confirmType === "cancel") {
          await axios.post("/ssm_springmvc/api/order/cancel", {id: order.id})
          this.toastMsg = "订单已取消"
        }
        this.toast = true
        this.loadOrders()
      } catch (e) {
        this.toastMsg = "操作失败"
        this.toast = true
      }
    },

    showDetail(item) {
      this.detail = item
      this.showDetailFlag = true
    },

    orderStatusName(s) {
      if (s === 0) return "待入住"
      if (s === 1) return "已入住"
      if (s === 2) return "需打扫"
      if (s === 3) return "已取消"
      if (s === 4) return "已完成"
      return "未知状态"
    },

    orderTagClass(s) {
      if (s === 0) return "tag-wait"
      if (s === 1) return "tag-living"
      if (s === 2) return "tag-wait"
      if (s === 3) return "tag-cancel"
      return "tag-done"
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
  scrollbar-width: none;
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

.btn-success {
  background: #2a9d8f;
  color: #fff;
}

.btn-danger {
  background: #e76f51;
  color: #fff;
}

.filter-bar {
  background: #fff;
  padding: 16px 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  gap: 15px;
  align-items: center;
}

.table-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  width: 100%;
}

th, td {
  padding: 16px 20px;
  border-bottom: 1px solid #f5f5f5;
  font-size: 14px;
}

.tag-wait {
  background: #fff4e5;
  color: #ff9800;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.tag-living {
  background: #e3f2fd;
  color: #2196f3;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.tag-done {
  background: #e8f5e9;
  color: #2e7d32;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.tag-cancel {
  background: #f5f5f5;
  color: #888;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

/* 确认小窗口 */
.confirm-box {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.confirm-content {
  width: 360px;
  background: #fff;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.15);
  text-align: center;
}

.confirm-content h4 {
  margin-bottom: 12px;
  font-size: 16px;
}

.confirm-content p {
  margin-bottom: 20px;
}

.confirm-btns {
  display: flex;
  justify-content: center;
  gap: 10px;
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
  position: absolute;
  right: 16px;
  bottom: 16px;
  padding: 10px 26px;
  font-size: 16px;
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