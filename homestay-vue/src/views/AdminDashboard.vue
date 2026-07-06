<template>
  <div id="app">
    <div class="container">
      <!-- 左侧导航 -->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item active" @click="$router.push('/dashboard')">🏠 工作台</div>

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
        <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧内容 -->
      <div class="main-content">
        <!-- 加高版渐变横幅背景 -->
        <div class="top-banner">
          <div class="banner-text">
            <h2>欢迎使用民宿管理系统</h2>
            <p>用心服务每一位客人，打造温馨的旅居体验💛</p>
          </div>
        </div>

        <div class="wrap-row">
          <div class="left-main">
            <div class="top-card-row">
              <div class="stat-card">
                <div class="card-label">今日订单</div>
                <div class="card-num">{{ todayBook }}</div>
                <div class="card-tip">较昨日 +{{ bookRate }}%</div>
              </div>
              <div class="stat-card">
                <div class="card-label">今日收入</div>
                <div class="card-num">¥{{ todayIncome }}</div>
                <div class="card-tip">较昨日 +{{ incomeRate }}%</div>
              </div>
              <div class="stat-card">
                <div class="card-label">在住房间</div>
                <div class="card-num">{{ livingRoom }}</div>
                <div class="card-tip">当前入住 {{ rate }}%</div>
              </div>
              <div class="stat-card">
                <div class="card-label">房源总数</div>
                <div class="card-num">{{ totalRoom }}</div>
                <div class="card-tip">已上架 {{ onSaleRoom }}套</div>
              </div>
            </div>

            <div class="two-col-row">
              <div class="block-card">
                <div class="block-title">订单趋势（近7天）</div>
                <div id="orderChart" style="width:100%;height:260px;"></div>
              </div>
              <div class="block-card">
                <div class="block-title">房态概览</div>
                <div class="room-tag-wrap">
                  <span class="tag-empty" @click="showRoomList('empty')">空闲 {{ emptyRoom }}</span>
                  <span class="tag-checkin" @click="showRoomList('checkin')">已入住 {{ livingRoom }}</span>
                  <span class="tag-book" @click="showRoomList('book')">已预订 {{ bookRoom }}</span>
                  <span class="tag-clean" @click="showRoomList('clean')">待打扫 {{ cleanRoom }}</span>
                </div>
              </div>
            </div>

            <div class="two-col-row">
              <div class="block-card">
                <div class="block-title">收入统计（近7天）</div>
                <div id="incomeChart" style="width:100%;height:260px;"></div>
              </div>
              <div class="block-card">
                <div class="block-title">房源使用率（近7天）</div>
                <div id="rateChart" style="width:100%;height:260px;"></div>
              </div>
            </div>

          </div>

          <div class="right-aside">
            <div class="aside-card">
              <div class="block-title">快捷操作</div>
              <div class="fast-btn-wrap">
                <button class="fast-btn" @click="$router.push('/house-add')">新增房源</button>
                <button class="fast-btn" @click="$router.push('/add-order')">创建订单</button>
                <button class="fast-btn" @click="$router.push('/room-status')">房态更新</button>
                <button class="fast-btn" @click="$router.push('/order-manage')">订单查询</button>
                <button class="fast-btn" @click="$router.push('/user-manage')">客户管理</button>
                <button class="fast-btn" @click="$router.push('/review-manage')">评价管理</button>
              </div>
            </div>

            <!-- 系统通知 -->
            <div class="aside-card">
              <div class="block-title flex-between">
                <span>系统通知</span>
                <span class="more-text" @click="openNoticeDialog">更多 ></span>
              </div>
              <div class="notice-item" v-for="(item,idx) in noticeList" :key="idx">
                <div class="notice-top">
                  <span>{{item.title}}</span>
                  <span class="notice-time">{{item.time}}</span>
                </div>
                <div class="notice-desc">{{item.content}}</div>
              </div>
            </div>

            <!-- 待处理事项 -->
            <div class="aside-card">
              <div class="block-title">待处理事项</div>
              <div class="todo-item" v-for="(item,idx) in todoList" :key="idx">
                <span>{{item.name}}</span>
                <span class="todo-num" @click="showTodoOrders(item.key)" :class="{ active: activeTodo === item.key }">
                  {{item.count}}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 房间列表弹窗 -->
    <div v-show="showDialog" class="room-dialog-mask">
      <div class="room-dialog">
        <div class="dialog-header">
          <h3>{{ dialogTitle }}</h3>
          <button class="close-btn" @click="closeDialog">×</button>
        </div>
        <div class="dialog-body">
          <div v-if="roomList.length === 0" class="empty-tip">暂无房间</div>
          <div class="room-item" v-for="item in roomList" :key="item.roomId">
            <span>房间ID：{{ item.roomId }}</span>
            <span>房间名称：{{ item.roomName }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 通知弹窗 -->
    <div v-show="showNoticeDialog" class="room-dialog-mask">
      <div class="room-dialog">
        <div class="dialog-header">
          <h3>全部系统通知</h3>
          <button class="close-btn" @click="closeNoticeDialog">×</button>
        </div>
        <div class="dialog-body">
          <div v-if="allNoticeList.length === 0" class="empty-tip">暂无通知</div>
          <div class="notice-item" v-for="(item,idx) in allNoticeList" :key="idx">
            <div class="notice-top">
              <span>{{item.title}}</span>
              <span class="notice-time">{{item.time}}</span>
            </div>
            <div class="notice-desc">{{item.content}}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 待处理订单弹窗 -->
    <div v-show="showTodoDialog" class="room-dialog-mask">
      <div class="room-dialog">
        <div class="dialog-header">
          <h3>{{ todoDialogTitle }}</h3>
          <button class="close-btn" @click="closeTodoDialog">×</button>
        </div>
        <div class="dialog-body">
          <div v-if="!todoOrderList || todoOrderList.length === 0" class="empty-tip">暂无订单</div>
          <div class="room-item" v-for="order in todoOrderList" :key="order.id">
            <span>订单号：{{ order.orderId }}</span>
            <span>房间：{{ order.roomName }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-show="toast" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'
export default {
  name: "AdminDashboard",
  data() {
    return {
      toast: false,
      toastMsg: "",
      todayBook: 0,
      todayIncome: 0,
      livingRoom: 0,
      totalRoom: 0,
      bookRate: 0,
      incomeRate: 0,
      onSaleRoom: 0,
      emptyRoom: 0,
      bookRoom: 0,
      cleanRoom: 0,
      rate: 0,
      showDialog: false,
      dialogTitle: '',
      roomList: [],
      chartData:{
        days: [],
        orderData:[],
        incomeData:[],
        useRate:[]
      },
      showNoticeDialog: false,
      allNoticeList: [],
      noticeList: [
        { title: "新订单#20240520018", time: "10:30", content: "张三预订了山景大床房2晚" },
        { title: "入住通知", time: "09:15", content: "李四办理了入住" },
        { title: "预订通知", time: "昨天18:20", content: "房源103已被预订" }
      ],
      showTodoDialog: false,
      todoDialogTitle: '',
      todoOrderList: [],
      activeTodo: '',
      todoList: [
        { name: "待处理订单", count: 0, key: "pending" },
        { name: "待入住", count: 0, key: "checkin" },
        { name: "待退房", count: 0, key: "checkout" },
        { name: "待评价", count: 0, key: "review" }
      ]
    };
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    const role = localStorage.getItem('userRole')
    if (!userId || !role) {
      this.$router.replace('/login')
      return
    }
    this.loadData()
    this.getSevenData()
    this.loadNoticeData()
  },
  methods: {
    async showRoomList(type) {
      let titleMap = {
        empty: "空闲房间",
        checkin: "已入住房间",
        book: "已预订房间",
        clean: "待打扫房间"
      }
      this.dialogTitle = titleMap[type]
      try {
        const res = await axios.get("/ssm_springmvc/api/room/"+type);
        this.roomList = res.data.data;
      } catch (e) {
        this.roomList = []
      }
      this.showDialog = true
    },
    closeDialog() {
      this.showDialog = false
    },
    async loadNoticeData() {
      try {
        const res = await axios.get("/ssm_springmvc/api/order/notices")
        if (res.data && res.data.data) {
          const processed = res.data.data.map(item => {
            return {
              ...item,
              time: item.time || "未知",
              content: item.content.replace("null", "未知用户")
            }
          })
          this.allNoticeList = processed
          this.noticeList = this.allNoticeList.slice(0, 3)
        }
      } catch (e) {
        console.log("使用本地通知")
      }
    },
    openNoticeDialog() {
      this.showNoticeDialog = true
    },
    closeNoticeDialog() {
      this.showNoticeDialog = false
    },
    async showTodoOrders(key) {
      this.activeTodo = key
      this.todoOrderList = []
      let titleMap = {
        pending: "待处理订单",
        checkin: "待入住订单",
        checkout: "待退房订单",
        review: "待评价订单"
      }
      this.todoDialogTitle = titleMap[key]
      try {
        const res = await axios.get(`/ssm_springmvc/api/order/todo/${key}`)
        this.todoOrderList = res.data?.data || []
      } catch (e) {
        this.todoOrderList = []
      }
      this.showTodoDialog = true
    },
    closeTodoDialog() {
      this.showTodoDialog = false
      this.activeTodo = ''
    },
    async loadData() {
      try {
        const res = await axios.get("/api/orders/dashboard")
        const data = res.data || {}
        this.todayBook = data.todayBook || 1
        this.todayIncome = data.todayIncome || 0.0
        this.livingRoom = data.livingRoom || 2
        this.totalRoom = data.totalRoom || 21
        this.emptyRoom = data.emptyRoom || 14
        this.bookRoom = data.bookRoom || 6
        this.cleanRoom = data.cleanRoom || 0
        this.rate = data.rate || 9.5
        this.bookRate = data.bookRate || 20.0
        this.incomeRate = data.incomeRate || 15.6
        this.onSaleRoom = data.onSaleRoom || 21
        this.todoList[0].count = data.pendingCount || 0
        this.todoList[1].count = data.checkinCount || 0
        this.todoList[2].count = data.checkoutCount || 0
        this.todoList[3].count = data.reviewCount || 0
      } catch (e) {
        console.log(e)
        this.setDefaultData()
      }
    },
    async getSevenData() {
      try {
        const res = await axios.get('/api/orders/sevenData')
        const d = res.data
        this.chartData.days = d.days
        this.chartData.orderData = d.order
        this.chartData.incomeData = d.income
        this.chartData.useRate = d.rate
        this.initAllChart()
      } catch (err) {
        console.log(err)
      }
    },
    initAllChart() {
      let oChart = echarts.init(document.getElementById('orderChart'))
      oChart.setOption({
        tooltip: { trigger: 'axis', backgroundColor: '#fff', textStyle: { color: '#333' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: this.chartData.days, axisLine: { color: '#eee' }, axisLabel: { color: '#666' } },
        yAxis: { type: 'value', axisLine: { color: '#eee' }, axisLabel: { color: '#666' }, splitLine: { color: '#f5f5f5' } },
        series: [{
          name: '订单数', type: 'line', smooth: true, data: this.chartData.orderData,
          itemStyle: { color: '#409EFC' }, lineStyle: { width: 3 },
          areaStyle: { color: 'rgba(64,158,252,0.25)' }
        }]
      })
      let iChart = echarts.init(document.getElementById('incomeChart'))
      iChart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: this.chartData.days },
        yAxis: { type: 'value' },
        series: [{
          name: '收入', type: 'bar', data: this.chartData.incomeData,
          itemStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[
              { offset:0, color:'#59B3F6' },
              { offset:1, color:'#409EFC' }
            ])}
        }]
      })
      let rChart = echarts.init(document.getElementById('rateChart'))
      rChart.setOption({
        tooltip: { formatter: '{b}<br/>使用率：{c}%' },
        xAxis: { type: 'category', data: this.chartData.days },
        yAxis: { name: '%', type: 'value' },
        series: [{
          name: '使用率', type: 'line', smooth: true, data: this.chartData.useRate,
          itemStyle: { color: '#409EFC' }, areaStyle: { color: 'rgba(64,158,252,0.2)' }
        }]
      })
      window.addEventListener('resize', () => {
        oChart.resize()
        iChart.resize()
        rChart.resize()
      })
    },
    setDefaultData() {
      this.todayBook = 0
      this.todayIncome = 0
      this.livingRoom = 0
      this.totalRoom = 0
      this.emptyRoom = 0
      this.bookRoom = 0
      this.cleanRoom = 0
      this.rate = 0
      this.bookRate = 20
      this.incomeRate = 15.6
      this.onSaleRoom = 0
    },
    logout() {
      localStorage.clear()
      this.$router.push("/login")
    }
  }
};
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
  transition: all 0.2s ease;
}

.menu-item.active, .menu-item:hover {
  background: #f5b041;
  color: #fff;
}

.main-content {
  flex: 1;
  padding: 20px;
  margin-left: 220px;
  height: 100vh;
  overflow-y: auto;
}

.top-banner {
  width: 100%;
  height: 200px;
  border-radius: 12px;
  padding: 40px 35px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  background: linear-gradient(to right, #fff9e6 35%, rgba(255, 249, 230, 0) 75%), url("@/assets/house3.png");
  background-size: cover;
  background-position: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.banner-text h2 {
  font-size: 30px;
  color: #8b5a2b;
  margin-bottom: 8px;
}

.banner-text p {
  color: #99826c;
  font-size: 16px;
}

.wrap-row {
  display: flex;
  gap: 20px;
}

.left-main {
  flex: 1;
}

.right-aside {
  width: 320px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top-card-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 24px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.stat-card:hover {
  transform: scale(1.03);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.card-label {
  color: #777;
  font-size: 14px;
}

.card-num {
  font-size: 32px;
  font-weight: bold;
  color: #f5b041;
  margin: 10px 0 6px;
}

.card-tip {
  font-size: 12px;
  color: #999;
}

.two-col-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}

.block-card {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  display: flex;
  flex-direction: column;
}

.block-card:hover {
  transform: scale(1.02);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.block-title {
  font-size: 16px;
  color: #8b5a2b;
  margin-bottom: 18px;
  font-weight: bold;
}

.room-tag-wrap {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 12px;
  flex: 1;
  min-height: 200px;
}

.tag-empty,
.tag-checkin,
.tag-book,
.tag-clean {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 50;
  border-radius: 10px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tag-empty {
  background: #e8f9e8;
  color: #27ae60;
  box-shadow: 0 2px 6px rgba(39, 174, 96, 0.1);
}

.tag-checkin {
  background: #e6f4ff;
  color: #3488eb;
  box-shadow: 0 2px 6px rgba(52, 136, 235, 0.1);
}

.tag-book {
  background: #fff7e6;
  color: #f59e0b;
  box-shadow: 0 2px 6px rgba(245, 158, 11, 0.1);
}

.tag-clean {
  background: #f7f7f7;
  color: #666;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.tag-empty:hover,
.tag-checkin:hover,
.tag-book:hover,
.tag-clean:hover {
  transform: scale(1.03);
}

.aside-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.aside-card:hover {
  transform: scale(1.02);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.flex-between {
  display: flex;
  justify-content: space-between;
}

.more-text {
  font-size: 12px;
  color: #999;
  cursor: pointer;
}

.fast-btn-wrap {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 8px;
}

.fast-btn {
  border: none;
  padding: 10px 8px;
  border-radius: 8px;
  background: #fff9e8;
  color: #8b5a2b;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.25s ease;
}

.fast-btn:hover {
  transform: scale(1.06);
  background: #f5b041;
  color: #fff;
}

.notice-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5efe8;
}

.notice-top {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.notice-time {
  color: #aaa;
  font-size: 12px;
  width: 50px;
  text-align: right;
  flex-shrink: 0;
  white-space: nowrap;
}

.notice-desc {
  font-size: 12px;
  color: #777;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
}

.todo-num {
  background: #ffe8e8;
  color: #e74c3c;
  padding: 4px 10px;
  border-radius: 10px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.todo-num:hover,
.todo-num.active {
  transform: scale(1.1);
}

.room-dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.room-dialog {
  background: #fff;
  width: 500px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.dialog-header {
  padding: 16px 20px;
  background: #f8f9fa;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-header h3 {
  font-size: 16px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #666;
}

.dialog-body {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.room-item {
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 30px 0;
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