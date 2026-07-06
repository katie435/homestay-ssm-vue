<template>
  <div id="my-orders-page">
    <div class="navbar">
      <div class="logo">
        <img src="../assets/logo.png" alt="logo">
        <span>栖宿民宿</span>
      </div>
      <div class="nav-links">
        <a @click="$router.push('/home')">首页</a>
        <a @click="$router.push('/all-rooms')">发现房源</a>
        <a @click="$router.push('/my-orders')" class="active">我的订单</a>
        <a @click="$router.push('/person-center')">个人中心</a>
      </div>
    </div>

    <!-- 待入住 -->
    <div class="section">
      <div class="section-title">待入住</div>
      <div v-if="waitOrders.length === 0" class="empty-section">暂无待入住订单</div>
      <div class="order-card" v-for="item in waitOrders" :key="item.orderId">
        <div class="order-header">
          <div class="order-status">待入住</div>
          <div class="order-time">订单号：{{ item.orderId }}</div>
        </div>
        <div class="tip-text">入住请出示订单</div>
        <div class="order-content">
          <img :src="getRoomImage(item.roomImg, item.roomId)" class="order-img">
          <div class="order-detail">
            <div class="order-room-name">{{ item.roomName }}</div>
            <div class="order-info">入住：{{ item.startTime }}</div>
            <div class="order-info">退房：{{ item.endTime }}</div>
            <div class="order-info">电话：{{ item.contactPhone }}</div>
            <div class="order-pay">
              <div class="order-info">
                支付状态：
                <span :class="item.payStatus == 1 ? 'paid' : 'unpaid'" class="pay-status">
                  {{ item.payStatus == 1 ? '已支付' : '待支付' }}
                </span>
              </div>
              <div class="order-info">支付方式：{{ getPay(item.payMethod) }}</div>
              <div class="order-info">金额：¥{{ item.totalPrice }}</div>
            </div>
            <div class="order-actions">
              <button class="cancel-btn" @click="del(item)">取消订单</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ✅ 新增：已入住 板块 -->
    <div class="section">
      <div class="section-title">已入住</div>
      <div v-if="checkinOrders.length === 0" class="empty-section">暂无已入住订单</div>
      <div class="order-card" v-for="item in checkinOrders" :key="item.orderId">
        <div class="order-header">
          <div class="order-status checkin-status">已入住</div>
          <div class="order-time">订单号：{{ item.orderId }}</div>
        </div>
        <div class="tip-text">正在入住中</div>
        <div class="order-content">
          <img :src="getRoomImage(item.roomImg, item.roomId)" class="order-img">
          <div class="order-detail">
            <div class="order-room-name">{{ item.roomName }}</div>
            <div class="order-info">入住：{{ item.startTime }}</div>
            <div class="order-info">退房：{{ item.endTime }}</div>
            <div class="order-info">电话：{{ item.contactPhone }}</div>
            <div class="order-pay">
              <div class="order-info">
                支付状态：
                <span :class="item.payStatus == 1 ? 'paid' : 'unpaid'" class="pay-status">
                  {{ item.payStatus == 1 ? '已支付' : '待支付' }}
                </span>
              </div>
              <div class="order-info">支付方式：{{ getPay(item.payMethod) }}</div>
              <div class="order-info">金额：¥{{ item.totalPrice }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 已完成 -->
    <div class="section">
      <div class="section-title">已完成</div>
      <div v-if="finishOrders.length === 0" class="empty-section">暂无已完成订单</div>

      <div class="order-card" v-for="item in finishOrders" :key="item.orderId">
        <div class="order-header">
          <div class="order-status finish-status">已完成</div>
        </div>
        <div class="order-content">
          <img :src="getRoomImage(item.roomImg, item.roomId)" class="order-img">
          <div class="order-detail">
            <div class="order-room-name">{{ item.roomName }}</div>
            <div style="display:flex;gap:10px;margin-top:10px;">
              <button class="book-again-btn" @click="goRoom">再次预订</button>
              <button
                  class="review-btn"
                  @click="goReview(item)"
                  v-if="!isEvaluated(item.orderId)"
              >
                去评价
              </button>
              <button
                  class="reviewed-btn"
                  v-else
              >
                已评价
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 已取消 -->
    <div class="section">
      <div class="section-title">已取消</div>
      <div v-if="cancelOrders.length === 0" class="empty-section">暂无已取消订单</div>
      <div class="order-card" v-for="item in cancelOrders" :key="item.orderId">
        <div class="order-header">
          <div class="order-status cancel-status">已取消</div>
        </div>
        <div class="order-content">
          <img :src="getRoomImage(item.roomImg, item.roomId)" class="order-img">
          <div class="order-detail">
            <div class="order-room-name">{{ item.roomName }}</div>
            <button class="book-again-btn" @click="goRoom">再次预订</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "MyOrders",
  data() {
    return {
      userId: "",
      orderList: [],
      evaluateList: [],
    };
  },
  mounted() {
    this.userId = localStorage.getItem("userId");
    if (!this.userId) {
      alert("请先登录！");
      this.$router.push("/login");
      return;
    }
    this.getOrderList();
    this.getEvaluateList();
  },
  watch: {
    $route() {
      this.getOrderList();
      this.getEvaluateList();
    }
  },
  computed: {
    waitOrders() {
      return this.orderList.filter(item => Number(item.orderStatus) === 0);
    },
    // ✅ 新增：已入住
    checkinOrders() {
      return this.orderList.filter(item => Number(item.orderStatus) === 1);
    },
    finishOrders() {
      return this.orderList.filter(item => Number(item.orderStatus) === 4);
    },
    cancelOrders() {
      return this.orderList.filter(item => Number(item.orderStatus) === 3);
    }
  },
  methods: {
    async getEvaluateList() {
      try {
        let res = await axios.get("/ssm_springmvc/api/evaluate/myList", {
          params: { userId: this.userId }
        });
        this.evaluateList = res.data.data || [];
      } catch (e) {
        console.error("加载评价失败", e);
      }
    },
    isEvaluated(orderId) {
      return this.evaluateList.some(e => e.orderId != null && e.orderId === orderId)
    },
    getRoomImage(roomImg) {
      if (!roomImg || roomImg.trim() === '') {
        return "https://picsum.photos/120/100"
      }
      if (roomImg.startsWith('/upload')) {
        return `http://localhost:8080/ssm_springmvc${roomImg}`
      } else {
        return `http://localhost:8080/ssm_springmvc/upload/${roomImg}`
      }
    },
    async getOrderList() {
      try {
        let res = await axios.get('/ssm_springmvc/api/order/getMyOrder', {params: {userId: this.userId}})
        this.orderList = res.data.data || []
      } catch (e) {
        console.error("获取订单失败", e)
        alert("获取订单失败，请刷新重试")
      }
    },
    getPay(m) {
      const map = {wechat: "微信", alipay: "支付宝", cash: "现金"};
      return map[m] || "未知";
    },
    goRoom() {
      this.$router.push("/all-rooms");
    },
    goReview(item) {
      this.$router.push({
        path: "/evaluate",
        query: {
          orderId: item.orderId,
          roomId: item.roomId,
          roomName: item.roomName,
          roomImg: item.roomImg
        }
      });
    },
    async del(item) {
      if (confirm("确定取消该订单吗？")) {
        try {
          await axios.post("/ssm_springmvc/api/order/cancel", {id: item.id});
          this.getOrderList();
          alert("订单已取消");
        } catch (err) {
          console.error(err)
          alert("取消失败");
        }
      }
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

#my-orders-page {
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

.section {
  margin: 30px 20px;
}

.section-title {
  font-size: 20px;
  color: #8b5a2b;
  margin-bottom: 20px;
  font-weight: bold;
}

.empty-section {
  padding: 30px;
  text-align: center;
  color: #888;
  background: #fff;
  border-radius: 12px;
}

.order-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.order-header {
  display: flex;
  justify-content: space-between;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5e8d0;
  margin-bottom: 15px;
}

.order-status {
  color: #f5b041;
  font-weight: bold;
}

/* ✅ 已入住状态颜色 */
.checkin-status {
  color: #3498db;
}
.finish-status {
  color: #2ecc71;
}
.cancel-status {
  color: #e74c3c;
}

.order-time {
  font-size: 12px;
  color: #8c786b;
}

.tip-text {
  color: #f5b041;
  font-size: 12px;
  margin-bottom: 8px;
  font-weight: bold;
}

.order-content {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.order-img {
  width: 120px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
}

.order-room-name {
  font-size: 16px;
  margin-bottom: 8px;
}

.order-info {
  font-size: 13px;
  color: #8c786b;
  margin-bottom: 4px;
}

.order-pay {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px dashed #f0d9b5;
}

.pay-status.paid {
  color: #2ecc71;
}

.pay-status.unpaid {
  color: #e74c3c;
}

.order-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.cancel-btn {
  padding: 6px 12px;
  border: 1px solid #e74c3c;
  color: #e74c3c;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
}

.book-again-btn {
  padding: 6px 12px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.review-btn {
  padding: 6px 12px;
  background: #2a9d8f;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.reviewed-btn {
  padding: 6px 12px;
  background: #95a5a6;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: default;
}
</style>