<template>
  <div id="room-detail-page">
    <div class="detail-container">
      <img :src="roomImgUrl" class="room-img" alt="房间图片">
      <h1 class="room-title">{{ room.roomName }}</h1>
      <div class="room-price">¥{{ room.price }}/晚</div>
      <div class="room-count">每间最多入住3人</div>
      <div class="room-desc">
        <p>{{ room.desc }}</p>
      </div>

      <div class="booking-form">
        <h3 style="margin-bottom: 15px; color: #8b5a2b;">立即预定</h3>
        <div class="form-item">
          <label>入住日期</label>
          <input type="date" v-model="checkInDate">
        </div>
        <div class="form-item">
          <label>离店日期</label>
          <input type="date" v-model="checkOutDate">
        </div>
        <div class="form-item">
          <label>房间数量</label>
          <input type="number" v-model.number="roomCount" min="1" placeholder="请输入预订间数">
        </div>
        <div class="form-item">
          <label>入住人数</label>
          <input type="number" v-model.number="peopleCount" min="1" :max="maxPeople" :placeholder="`最多${maxPeople}人`">
        </div>
        <div class="form-item">
          <label>联系电话</label>
          <input type="tel" v-model="phone" placeholder="请输入手机号">
        </div>

        <div class="guest-section">
          <div class="guest-title">入住人信息</div>
          <div class="guest-item" v-for="(guest, index) in guests" :key="index">
            <div class="guest-header">
              <span>入住人 {{ index + 1 }}</span>
            </div>
            <div class="guest-form-row">
              <div class="form-item">
                <label>姓名</label>
                <input type="text" v-model="guest.name" placeholder="请输入姓名">
              </div>
              <div class="form-item">
                <label>身份证号</label>
                <input type="text" v-model="guest.idCard" placeholder="请输入身份证号">
              </div>
            </div>
            <div class="guest-form-row">
              <div class="form-item">
                <label>联系电话</label>
                <input type="tel" v-model="guest.phone" placeholder="请输入电话">
              </div>
              <div class="form-item">
                <label>人员类型</label>
                <select v-model="guest.type">
                  <option value="成人">成人</option>
                  <option value="儿童">儿童</option>
                  <option value="老人">老人</option>
                </select>
              </div>
            </div>
            <div class="form-item">
              <label>特殊服务</label>
              <input type="text" v-model="guest.specialService" class="special-service" placeholder="特殊需求注明">
            </div>
          </div>
        </div>

        <div class="payment-section">
          <div class="payment-title">支付方式</div>
          <div class="payment-options">
            <div class="payment-option" :class="{active: paymentMethod === 'wechat'}" @click="payByWechat">
              💳 微信支付
            </div>
            <div class="payment-option" :class="{active: paymentMethod === 'alipay'}" @click="payByAlipay">
              💰 支付宝支付
            </div>
            <div class="payment-option" :class="{active: paymentMethod === 'cash'}" @click="payByCash">
              💵 现金支付
            </div>
          </div>
          <div class="total-price">
            总计：¥{{ totalPrice }} (共{{ days }}晚 × {{ roomCount }}间)
          </div>
        </div>

        <button class="back-btn" @click="$router.push('/home')">返回首页</button>
        <button class="back-btn" @click="$router.push('/all-rooms')">返回房源列表</button>
      </div>

      <div v-if="showPayModal" class="pay-modal">
        <div class="pay-box">
          <div class="pay-title" :class="payResult ? 'pay-success' : 'pay-fail'">
            {{ payResult ? '支付成功' : '提交订单成功' }}
          </div>
          <button class="pay-btn" @click="toOrderPage">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: "RoomDetail",
  data() {
    return {
      room: {},
      checkInDate: '',
      checkOutDate: '',
      peopleCount: 1,
      roomCount: 1,
      phone: '',
      guests: [{name: '', idCard: '', phone: '', type: '成人', specialService: ''}],
      paymentMethod: 'wechat',
      showPayModal: false,
      payResult: false,
      tempPayMethod: '',
      tempPayStatus: 0
    }
  },
  computed: {
    maxPeople() {
      return this.roomCount * 3
    },
    days() {
      if (!this.checkInDate || !this.checkOutDate) return 0
      const start = new Date(this.checkInDate)
      const end = new Date(this.checkOutDate)
      if (isNaN(start.getTime()) || isNaN(end.getTime())) return 0
      const diff = end - start
      if (diff <= 0) return 0
      return Math.ceil(diff / 86400000)
    },
    totalPrice() {
      return this.days * this.roomCount * (this.room.price || 0)
    },
    roomImgUrl() {
      if (!this.room || !this.room.img) return ''
      let imgName = this.room.img
      if (imgName.startsWith('/upload')) {
        return `http://localhost:8080/ssm_springmvc${imgName}`
      } else {
        return `http://localhost:8080/ssm_springmvc/upload/${imgName}`
      }
    }
  },
  watch: {
    peopleCount(newVal, oldVal) {
      if (newVal > oldVal) {
        for (let i = oldVal; i < newVal; i++) {
          this.guests.push({name: '', idCard: '', phone: '', type: '成人', specialService: ''})
        }
      } else {
        this.guests.splice(newVal)
      }
    },
    $route(to) {
      if (to.query.id) {
        this.getRoomDetail()
      }
    }
  },
  mounted() {
    this.getRoomDetail()
  },
  methods: {
    async getRoomDetail() {
      try {
        const roomId = this.$route.query.id
        if (!roomId) return
        const res = await axios.get('/api/api/room/detail',
            {params:{id: roomId}})
        this.room = res.data.data
      } catch (e) {
        console.error("获取房源详情失败", e)
      }
    },
    payByWechat() {
      this.paymentMethod = 'wechat'
      this.payResult = true
      this.tempPayMethod = 'wechat'
      this.tempPayStatus = 1
      this.showPayModal = true
    },
    payByAlipay() {
      this.paymentMethod = 'alipay'
      this.payResult = true
      this.tempPayMethod = 'alipay'
      this.tempPayStatus = 1
      this.showPayModal = true
    },
    payByCash() {
      this.paymentMethod = 'cash'
      this.payResult = false
      this.tempPayMethod = 'cash'
      this.tempPayStatus = 0
      this.showPayModal = true
    },
    async toOrderPage() {
      if (!this.checkInDate || !this.checkOutDate || this.phone.length !== 11) {
        alert('请填写完整预订信息！');
        return;
      }

      let params = {
        orderId: "QS" + Date.now(),
        roomId: this.room.roomId,
        roomName: this.room.roomName,
        roomImg: this.room.img,
        startTime: this.checkInDate,
        endTime: this.checkOutDate,
        roomCount: this.roomCount,
        peopleCount: this.peopleCount,
        contactPhone: this.phone,
        totalPrice: this.totalPrice,
        payMethod: this.tempPayMethod,
        payStatus: this.tempPayStatus,
        orderStatus: 0,
        createTime: new Date().toLocaleString(),
        userId: localStorage.getItem("userId")
      };

      try {
        await axios.post("/ssm_springmvc/api/order/add", params);
        alert("✅ 下单成功！");
        this.$router.push("/my-orders");
      } catch (e) {
        console.error(e)
        alert("❌ 下单失败");
      }
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

#room-detail-page {
  background-color: #fff9e6;
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  padding: 30px 0;
}

.detail-container {
  width: 100%;
  max-width: 900px;
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.room-img {
  width: 100%;
  height: 350px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 20px;
}

.room-title {
  font-size: 24px;
  color: #8b5a2b;
  margin-bottom: 10px;
}

.room-price {
  font-size: 20px;
  color: #d4a373;
  font-weight: bold;
  margin-bottom: 10px;
}

.room-count {
  font-size: 14px;
  color: #8c786b;
  margin-bottom: 20px;
}

.room-desc {
  line-height: 1.8;
  margin-bottom: 30px;
}

.booking-form {
  background-color: #fff9e6;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.form-item label {
  width: 80px;
  font-size: 14px;
  color: #5a4a3f;
}

.form-item input, .form-item select {
  flex: 1;
  padding: 8px;
  border: 1px solid #f0d9b5;
  border-radius: 4px;
  outline: none;
}

.back-btn {
  display: inline-block;
  padding: 10px 25px;
  background-color: #f5b041;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  margin-left: 10px;
  border: none;
  cursor: pointer;
}

.guest-section {
  margin: 20px 0;
  padding-top: 15px;
  border-top: 1px dashed #f0d9b5;
}

.guest-title {
  font-size: 16px;
  color: #8b5a2b;
  margin-bottom: 15px;
  font-weight: bold;
}

.guest-item {
  background: white;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  border: 1px solid #f5e8d0;
}

.guest-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
  color: #8b5a2b;
}

.guest-form-row {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.guest-form-row .form-item {
  flex: 1;
  margin-bottom: 0;
}

.guest-form-row .form-item label {
  width: auto;
  margin-right: 8px;
}

.special-service {
  width: 100% !important;
}

.payment-section {
  margin: 20px 0;
  padding-top: 15px;
  border-top: 1px dashed #f0d9b5;
}

.payment-title {
  font-size: 16px;
  color: #8b5a2b;
  margin-bottom: 15px;
  font-weight: bold;
}

.payment-options {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.payment-option {
  flex: 1;
  padding: 15px;
  border: 2px solid #f0d9b5;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}

.payment-option.active {
  border-color: #f5b041;
  background: #fff9e6;
}

.total-price {
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  color: #d4a373;
  margin-bottom: 15px;
}

.pay-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, .5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.pay-box {
  background: #fff;
  width: 320px;
  padding: 30px;
  border-radius: 12px;
  text-align: center;
}

.pay-title {
  font-size: 18px;
  margin-bottom: 20px;
  font-weight: bold;
}

.pay-success {
  color: #2ecc71;
}

.pay-fail {
  color: #e74c3c;
}

.pay-btn {
  padding: 8px 25px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>