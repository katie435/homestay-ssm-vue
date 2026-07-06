<template>
  <div id="home-page">
    <div class="navbar">
      <div class="logo">
        <img src="../assets/logo.png" alt="logo">
        <span>栖宿民宿</span>
      </div>

      <div class="search-box">
        <input
            type="text"
            placeholder="搜索房型"
            v-model="searchKey"
            @keyup.enter="doSearch"
        >
        <button @click="doSearch">搜索</button>
      </div>

      <div class="nav-links">
        <a href="javascript:;" class="active" @click="goHome">首页</a>
        <a @click="$router.push('/all-rooms')">发现房源</a>
        <a @click="$router.push('/my-orders')">我的订单</a>
        <a @click="$router.push('/person-center')">个人中心</a>
      </div>

      <div class="user-info" @click="changeAvatar">
        <img :src="currentAvatar" alt="头像">
        <span style="font-weight: bold">{{ userName }}</span>
      </div>
    </div>

    <div class="banner">
      <div class="banner-text">
        <h1>遇见美好 · 栖宿时光</h1>
        <p>舒适民宿，温暖如家</p>
        <div class="search-bar">
          <div class="search-item">
            <span>入住日期</span>
            <input type="date">
          </div>
          <div class="search-item">
            <span>离店日期</span>
            <input type="date">
          </div>
          <div class="search-item">
            <span>人数</span>
            <input type="number" placeholder="1" style="width:40px;">
          </div>
        </div>
      </div>
    </div>

    <div class="intro-section">
      <div class="intro-title">民宿简介</div>
      <div class="intro-box">
        <div class="intro-text">
          <div class="intro-content">
            栖宿民宿是一座依山傍海的独栋庭院民宿，坐拥一线山海景观，独享静谧私密的度假氛围。民宿配备独立私家花园，绿植环绕、鸟语花香，整体设计温馨自然，融合原木风与海景特色。远离城市喧嚣，环境清幽安静，无论是情侣度假、家庭出游还是朋友小聚都十分合适。我们致力于为每一位客人提供舒适、贴心、有温度的住宿体验，让您在山海之间，卸下疲惫，悠然享受悠闲慢时光。
          </div>
        </div>
        <img class="intro-img" src="../assets/house3.png" alt="民宿实景">
      </div>
    </div>

    <div class="section">
      <div class="section-title">精选房源</div>
      <div class="room-grid">
        <div class="room-card" v-for="room in showRooms" :key="room.roomId" @click="goToDetail(room.roomId)">
          <img :src="getSafeImg(room.img)" alt="房间图片">
          <div class="room-info">
            <div class="room-name">{{room.roomName}}</div>
            <div class="room-price">¥{{room.price}}/晚</div>
          </div>
        </div>
      </div>
    </div>

    <div class="more-rooms-btn-wrap">
      <a @click="$router.push('/all-rooms')" class="more-rooms-btn">更多房型</a>
    </div>

    <div class="comment-section">
      <div class="comment-title">客户评价</div>
      <div class="comment-slider">
        <div class="comment-item">
          <div class="comment-text">“{{currentComment.text}}”</div>
          <div class="comment-user">—— {{currentComment.user}}</div>
        </div>
      </div>
    </div>

    <div class="surround-section">
      <div class="section-title">周边环境 & 民宿设施</div>
      <div class="surround-wrap">
        <div class="surround-card">
          <div class="surround-title env">周边环境</div>
          <ul class="surround-list">
            <li>步行直达原生态海岸线，尽享海景风光</li>
            <li>背靠青山森林公园，天然氧吧环绕</li>
            <li>周边网红打卡点、观景台近在咫尺</li>
            <li>远离闹市，环境清幽，私密性极佳</li>
            <li>日出日落、山海全景尽收眼底</li>
          </ul>
        </div>
        <div class="surround-card">
          <div class="surround-title fac">民宿设施</div>
          <ul class="surround-list">
            <li>独立私家花园，免费休闲打卡</li>
            <li>全屋中央空调、24小时恒温热水</li>
            <li>高速WiFi全覆盖，智能设备配套</li>
            <li>免费专属停车位、自助洗衣房</li>
            <li>管家式服务，满足个性化需求</li>
          </ul>
        </div>
      </div>
    </div>

    <div class="service-bar">
      <div class="service-item">
        <img src="../assets/s-p.png" alt="">
        <span>免费停车</span>
      </div>
      <div class="service-item">
        <img src="../assets/s-zaocan.png" alt="">
        <span>早餐供应</span>
      </div>
      <div class="service-item">
        <img src="../assets/s-wifi.png" alt="">
        <span>独立WiFi</span>
      </div>
      <div class="service-item">
        <img src="../assets/s-kongtiao.png" alt="">
        <span>空调</span>
      </div>
      <div class="service-item">
        <img src="../assets/s-xiyifang.png" alt="">
        <span>洗衣房</span>
      </div>
    </div>

    <div class="bottom-service">
      <div class="service-card">
        <img src="../assets/s-anx.png" alt="安心入住">
        <div class="title">安心入住</div>
        <div class="desc">7×24小时服务</div>
      </div>
      <div class="service-card">
        <img src="../assets/s-youhui.png" alt="优惠多多">
        <div class="title">优惠多多</div>
        <div class="desc">会员专享折扣</div>
      </div>
      <div class="service-card">
        <img src="../assets/s-price.png" alt="价格透明">
        <div class="title">价格透明</div>
        <div class="desc">无隐形消费</div>
      </div>
      <div class="service-card">
        <img src="../assets/s-tiex.png" alt="贴心服务">
        <div class="title">贴心服务</div>
        <div class="desc">管家式服务</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: "HomePage",
  data() {
    return {
      searchKey: '',
      userName: '欢迎登录',
      avatarList: [
        require("../assets/user-avatar.png")
      ],
      currentAvatar: require("../assets/user-avatar.png"),
      currentAvatarIndex: 0,
      roomList: [],
      comments: [
        { text: "环境超棒，服务贴心，下次还来！", user: "张先生" },
        { text: "房间干净整洁，海景绝美，非常推荐！", user: "李女士" },
        { text: "民宿位置很好，安静舒适，体验满分", user: "王先生" },
        { text: "管家很热情，周边游玩也很方便", user: "陈同学" }
      ],
      currentComment: {},
      commentIndex: 0
    }
  },
  mounted() {
    this.initUserInfo();
    this.startCommentSlider();
    this.getRoomData();
  },
  computed: {
    showRooms() {
      const uniqueMap = {}
      const finalList = []
      this.roomList.forEach(item => {
        if (!uniqueMap[item.roomName]) {
          uniqueMap[item.roomName] = item
          finalList.push(item)
        }
      })
      return finalList.slice(0, 4)
    }
  },
  methods: {
    async getRoomData() {
      try {
        const res = await axios.get('/api/api/room/client/list')
        this.roomList = res.data.data
      } catch (e) {
        console.log("获取精选房源失败", e)
      }
    },
    getSafeImg(imgName) {
      if (!imgName || imgName === 'default.png') {
        return ''
      }
      if (imgName.startsWith('/upload')) {
        return `http://localhost:8080/ssm_springmvc${imgName}`
      } else {
        return `http://localhost:8080/ssm_springmvc/upload/${imgName}`
      }
    },
    initUserInfo() {
      const userName = localStorage.getItem("userName");
      if (userName) {
        this.userName = userName;
      }
      const savedAvatarIndex = localStorage.getItem("avatarIndex");
      if (savedAvatarIndex !== null) {
        this.currentAvatarIndex = parseInt(savedAvatarIndex);
        this.currentAvatar = this.avatarList[this.currentAvatarIndex];
      }
    },
    doSearch() {
      if (!this.searchKey.trim()) return;
      const room = this.showRooms.find(item => item.roomName.includes(this.searchKey.trim()));
      if (room) {
        this.$router.push('/room-detail?id=' + room.roomId);
      } else {
        alert("暂无该房型！");
      }
    },
    goHome() {
      alert("您当前正在首页");
    },
    changeAvatar() {
      return;
    },
    goToDetail(roomId) {
      this.$router.push('/room-detail?id=' + roomId);
    },
    startCommentSlider() {
      this.currentComment = this.comments[0];
      setInterval(() => {
        this.commentIndex = (this.commentIndex + 1) % this.comments.length;
        this.currentComment = this.comments[this.commentIndex];
      }, 4000);
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

#home-page {
  background-color: #fff9e6;
  color: #5a4a3f;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  background-color: #fff;
  border-bottom: 1px solid #f5e8d0;
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
  margin-left: 20px;
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

.search-box {
  flex: 1;
  max-width: 400px;
  margin: 0 20px;
  display: flex;
  align-items: center;
}

.search-box input {
  flex: 1;
  height: 32px;
  padding: 0 12px;
  border: 1px solid #f0d9b5;
  border-right: none;
  border-radius: 4px 0 0 4px;
  background-color: #fffbf5;
  outline: none;
  font-size: 13px;
}

.search-box button {
  height: 32px;
  padding: 0 14px;
  border: none;
  background-color: #f5b041;
  color: #fff;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-size: 13px;
}

.user-info {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #5a4a3f;
  cursor: pointer;
}

.user-info img {
  width: 24px;
  height: 24px;
  margin-right: 6px;
  border-radius: 50%;
  object-fit: cover;
}

.banner {
  margin: 10px 20px;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
  min-height: 300px;
  background: url('../assets/house.png') no-repeat right center;
  background-size: cover;
}

.banner::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 55%;
  height: 100%;
  background: linear-gradient(to right,
  #fff5eb 0%,
  rgba(255, 245, 235, 0.8) 40%,
  rgba(255, 245, 235, 0) 100%);
  z-index: 1;
}

.banner-text {
  position: relative;
  z-index: 2;
}

.banner-text h1 {
  color: #8b5a2b;
  font-size: 22px;
  margin-bottom: 8px;
}

.banner-text p {
  color: #8c786b;
  font-size: 14px;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  background: white;
  padding: 8px;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  z-index: 1;
}

.search-item {
  display: flex;
  align-items: center;
  margin: 0 10px;
}

.search-item span {
  font-size: 12px;
  color: #8c786b;
  margin-right: 4px;
}

.search-item input {
  font-size: 12px;
  width: 100px;
  border: none;
  outline: none;
}

.search-btn {
  background-color: #f5b041;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  font-size: 13px;
}

.intro-section {
  margin: 30px 20px;
  background: #fff;
  border-radius: 12px;
  padding: 25px 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #f5e8d0;
  max-width: 1100px;
  margin-left: auto;
  margin-right: auto;
}

.intro-title {
  font-size: 20px;
  color: #8b5a2b;
  margin-bottom: 20px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.intro-title::before {
  content: "🏡";
  font-size: 22px;
}

.intro-box {
  display: flex;
  gap: 30px;
  align-items: center;
}

.intro-text {
  flex: 1;
}

.intro-content {
  font-size: 17px;
  line-height: 1.8;
  color: #5a4a3f;
}

.intro-img {
  width: 420px;
  height: 280px;
  border-radius: 10px;
  object-fit: cover;
}

.section {
  margin: 30px 20px;
}

.section-title {
  font-size: 20px;
  color: #8b5a2b;
  margin-bottom: 20px;
  font-weight: bold;
  max-width: 1100px;
  margin-left: auto;
  margin-right: auto;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  max-width: 1100px;
  margin: 0 auto;
}

.room-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  cursor: pointer;
}

.room-card img {
  width: 100%;
  height: 220px;
  object-fit: cover;
}

.room-info {
  padding: 15px;
}

.room-name {
  font-size: 16px;
  color: #5a4a3f;
  margin-bottom: 8px;
}

.room-price {
  font-size: 18px;
  color: #d4a373;
  font-weight: bold;
}

.more-rooms-btn-wrap {
  text-align: center;
  margin: 15px 20px 30px;
}

.more-rooms-btn {
  display: inline-block;
  padding: 8px 20px;
  background-color: #f5b041;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s ease;
  cursor: pointer;
}

.more-rooms-btn:hover {
  background-color: #e69c2e;
}

.comment-section {
  margin: 40px 20px;
  max-width: 1100px;
  margin-left: auto;
  margin-right: auto;
}

.comment-title {
  font-size: 20px;
  color: #8b5a2b;
  margin-bottom: 20px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-title::before {
  content: "💬";
}

.comment-slider {
  background: #fff;
  border-radius: 12px;
  padding: 25px 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  border: 1px solid #f5e8d0;
  min-height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.comment-item {
  animation: fadeComment 0.8s ease;
}

.comment-text {
  font-size: 16px;
  color: #5a4a3f;
  line-height: 1.7;
  margin-bottom: 12px;
  font-style: italic;
}

.comment-user {
  font-size: 14px;
  color: #8b5a2b;
  font-weight: bold;
}

@keyframes fadeComment {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.surround-section {
  margin: 0 20px 30px;
}

.surround-wrap {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  max-width: 1100px;
  margin: 0 auto;
}

.surround-card {
  background: #fff;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #f5e8d0;
}

.surround-title {
  font-size: 18px;
  color: #8b5a2b;
  margin-bottom: 15px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.surround-title.env::before {
  content: "🌊";
}

.surround-title.fac::before {
  content: "🏞️";
}

.surround-list {
  list-style: none;
}

.surround-list li {
  font-size: 14px;
  color: #5a4a3f;
  line-height: 2;
  padding-left: 10px;
  position: relative;
  transition: all 0.25s ease;
  cursor: default;
}

.surround-list li::before {
  content: "•";
  color: #f5b041;
  font-weight: bold;
  position: absolute;
  left: 0;
}

.surround-list li:hover {
  font-weight: bold;
  transform: scale(1.02);
}

.service-bar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: #fff;
  margin: 20px;
  padding: 15px 10px;
  border-radius: 8px;
}

.service-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px 10px;
  transition: transform 0.3s ease;
  cursor: default;
}

.service-item:hover {
  transform: scale(1.08);
}

.service-item img {
  width: 20px;
  height: 20px;
}

.service-item span {
  font-size: 12px;
  color: #666;
}

.bottom-service {
  display: flex;
  justify-content: space-around;
  align-items: center;
  background: #fff;
  margin: 20px;
  padding: 25px 10px;
  border-radius: 8px;
  flex-wrap: wrap;
}

.service-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 25%;
  position: relative;
  padding: 10px 0;
  transition: transform 0.3s ease;
  cursor: default;
}

.service-card:hover {
  transform: scale(1.08);
}

.service-card:not(:last-child)::after {
  content: "";
  position: absolute;
  right: 0;
  top: 10px;
  height: 60%;
  width: 1px;
  background-color: #f0d9b5;
}

.service-card img {
  width: 30px;
  height: 30px;
  margin-bottom: 8px;
}

.service-card .title {
  font-size: 14px;
  color: #5a4a3f;
  font-weight: 500;
  margin-bottom: 4px;
}

.service-card .desc {
  font-size: 11px;
  color: #8c786b;
}
</style>