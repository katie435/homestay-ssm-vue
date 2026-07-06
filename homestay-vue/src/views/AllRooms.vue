<template>
  <div id="all-rooms-page">
    <div class="navbar">
      <div class="logo">
        <img src="../assets/logo.png" alt="logo">
        <span>栖宿民宿</span>
      </div>
      <div class="nav-links">
        <a @click="$router.push('/home')">首页</a>
        <a @click="$router.push('/all-rooms')" class="active">发现房源</a>
        <a @click="$router.push('/my-orders')">我的订单</a>
        <a @click="$router.push('/person-center')">个人中心</a>
      </div>
    </div>

    <div class="section">
      <div class="search-bar">
        <div class="section-title">所有房源</div>
        <input
            v-model="searchKey"
            class="search-input"
            placeholder="搜索房型"
        />
      </div>

      <div class="room-grid">
        <div
            class="room-card"
            v-for="room in showRooms"
            :key="room.roomId"
            @click="goToDetail(room.roomId)"
        >
          <img :src="getSafeImg(room.img)" alt="房源图" />
          <div class="room-info">
            <div class="room-name">{{ room.roomName }}</div>
            <div class="room-price">¥{{ room.price }}/晚</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "AllRoomsPage",
  data() {
    return {
      searchKey: "",
      roomList: []
    };
  },
  mounted() {
    this.getRooms()
  },
  computed: {
    showRooms() {
      const nameObj = {}
      let arr = []
      // 同名只存第一个，不一样名字全部保留
      this.roomList.forEach(item => {
        if (!nameObj[item.roomName]) {
          nameObj[item.roomName] = true
          arr.push(item)
        }
      })
      // 搜索过滤
      if (this.searchKey) {
        arr = arr.filter(item => item.roomName.includes(this.searchKey))
      }
      return arr
    }
  },
  methods: {
    async getRooms() {
      try {
        const res = await axios.get('/api/api/room/client/list')
        this.roomList = res.data.data
        console.log(this.roomList)
      } catch (e) {
        console.log("获取房源失败", e)
      }
    },
    goToDetail(roomId) {
      this.$router.push("/room-detail?id=" + roomId);
    },
    getSafeImg(imgName) {
      if (!imgName || imgName === 'default.png') {
        return ''
      }
      // 如果字段自带/upload开头，直接拼域名，不再额外加/upload
      if(imgName.startsWith('/upload')){
        return `http://localhost:8080/ssm_springmvc${imgName}`
      }else{
        // 普通文件名（haijin.png）正常拼接
        return `http://localhost:8080/ssm_springmvc/upload/${imgName}`
      }
    },
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

#all-rooms-page {
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

.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  color: #8b5a2b;
  font-weight: bold;
}

.search-input {
  padding: 6px 12px;
  border: 1px solid #f0d9b5;
  border-radius: 4px;
  outline: none;
  width: 200px;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  width: 100%;
}

.room-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease;
  cursor: pointer;
}

.room-card:hover {
  transform: scale(1.03);
}

.room-card img {
  width: 100%;
  height: 240px;
  object-fit: cover;
}

.room-info {
  padding: 15px;
}

.room-name {
  font-size: 16px;
  margin-bottom: 8px;
}

.room-price {
  font-size: 18px;
  color: #d4a373;
  font-weight: bold;
}
</style>