<template>
  <div id="review-manage">
    <div class="container">
      <!-- 侧边栏 保留！-->
      <div class="sidebar">
        <h2>民宿员工管理系统</h2>
        <div class="menu-item" @click="$router.push('/dashboard')">🏠 工作台</div>

        <div class="menu-group-title">房源管理</div>
        <div class="menu-item" @click="$router.push('/house-list')">🏘️ 房源列表</div>
        <div class="menu-item" @click="$router.push('/house-add')">➕ 房源录入</div>
        <div class="menu-item" @click="$router.push('/room-status')">🏷️ 房态管理</div>

        <div class="menu-group-title">订单管理</div>
        <div class="menu-item" @click="$router.push('/order-manage')">📋 预订管理</div>
        <div class="menu-item" @click="$router.push('/add-order')">➕ 新增预定</div>
        <div class="menu-item active">💬 评价管理</div>

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
          <h3>评价管理</h3>
        </div>

        <div class="filter-card">
          <div class="filter-item" :class="{active: filterType === 'all'}" @click="filterType = 'all'">
            全部评价 ({{ allReviews.length }})
          </div>
          <div class="filter-item" :class="{active: filterType === 'pending'}" @click="filterType = 'pending'">
            待回复 ({{ pendingReviews.length }})
          </div>
          <div class="filter-item" :class="{active: filterType === 'replied'}" @click="filterType = 'replied'">
            已回复 ({{ repliedReviews.length }})
          </div>
        </div>

        <div class="review-list">
          <div class="review-card" v-for="item in showReviews" :key="item.evaId">
            <!-- 头部：订单+房型+用户信息 -->
            <div class="review-top">
              <div class="order-info">
                <span class="order-label">订单编号</span>
                <span class="order-val">{{ item.orderId || '-' }}</span>
                <span class="time-val">{{ item.evaTime }}</span>
              </div>
              <span class="status-badge" :class="item.reply ? 'replied' : 'pending'">
                {{ item.reply ? '已回复' : '待回复' }}
              </span>
            </div>

            <!-- 中部：基础信息+评价文案 -->
            <div class="review-middle">
              <div class="info-block">
                <p><span class="lab">房型：</span>{{ item.roomName }}</p>
                <p><span class="lab">客户姓名：</span>{{ item.userName || '匿名访客' }}</p>
                <p><span class="lab">综合评分：</span>{{ item.score }} ⭐</p>
              </div>
              <div class="content-block">
                <div class="c-title">用户评价</div>
                <p class="c-text">{{ item.content }}</p>
              </div>
            </div>

            <!-- 下部：用户上传实拍图片区域 -->
            <div class="img-block" v-if="getImgList(item.evaImg).length > 0">
              <div class="img-title">用户实拍</div>
              <div class="img-wrap">
                <div class="img-item" v-for="(src,idx) in getImgList(item.evaImg)" :key="idx">
                  <img :src="src" alt="评价配图">
                </div>
              </div>
            </div>

            <!-- 回复区域 -->
            <div class="reply-block" v-if="!item.reply">
              <textarea v-model="item.replyContent" placeholder="填写商家回复内容，友好回复客户"></textarea>
              <button class="submit-reply" @click="submitReply(item)">提交回复</button>
            </div>
            <div class="reply-block replied" v-else>
              <div class="reply-lab">商家回复</div>
              <p class="reply-text">{{ item.reply }}</p>
            </div>
          </div>

          <div class="empty-tip" v-if="showReviews.length === 0">
            暂无评价数据
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: "ReviewManage",
  data() {
    return {
      filterType: "all",
      allReviews: []
    };
  },
  mounted() {
    const userId = localStorage.getItem('userId');
    const role = localStorage.getItem('userRole');
    if (!userId || !role) {
      this.$router.replace('/login');
      return;
    }
    this.loadAllReviews();
  },
  computed: {
    pendingReviews() {
      return this.allReviews.filter((r) => !r.reply);
    },
    repliedReviews() {
      return this.allReviews.filter((r) => r.reply);
    },
    showReviews() {
      if (this.filterType === "pending") return this.pendingReviews;
      if (this.filterType === "replied") return this.repliedReviews;
      return this.allReviews;
    },
  },
  methods: {
    getImgList(imgStr) {
      if (!imgStr || imgStr.trim() === '') return []
      return imgStr.split(',').map(i => i.trim()).filter(i => i !== '')
    },

    // 加载评价
    async loadAllReviews() {
      try {
        let res = await axios.get('/ssm_springmvc/api/evaluate/allList')
        this.allReviews = res.data.data || []
      } catch (e) {
        console.error(e)
        alert('加载评价失败')
      }
    },

    // 提交回复
    async submitReply(item) {
      if (!item.replyContent || !item.replyContent.trim()) {
        alert("请输入回复内容");
        return;
      }
      try {
        await axios.post('/ssm_springmvc/api/evaluate/reply', {
          evaId: item.evaId,
          reply: item.replyContent
        })
        alert("回复成功！")
        this.loadAllReviews()
      } catch (e) {
        console.error(e)
        alert("回复失败")
      }
    },

    goStaffAdmin() {
      this.$router.push('/dashboard');
    },
    logout() {
      localStorage.clear();
      this.$router.push("/login");
    },
  },
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

#review-manage {
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
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
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
  transition: all 0.2s;
}

.menu-item.active, .menu-item:hover {
  background: #f5b041;
  color: #fff;
}

.main-content {
  flex: 1;
  padding: 20px;
  height: 100vh;
  overflow-y: auto;
  margin-left: 220px;
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
}

.content-header h3 {
  color: #8b5a2b;
  font-size: 16px;
}

.filter-card {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.filter-item {
  background: #fff;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  color: #8b5a2b;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.filter-item.active {
  background: #f5b041;
  color: #fff;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(139, 90, 43, 0.07);
  transition: all 0.3s ease;
}

.review-card:hover {
  box-shadow: 0 8px 22px rgba(139, 90, 43, 0.12);
  transform: translateY(-2px);
}

.review-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 14px;
  border-bottom: 1px solid #f3e8d8;
  margin-bottom: 16px;
}

.order-info {
  display: flex;
  gap: 22px;
  font-size: 14px;
  color: #8b5a2b;
}

.order-label {
  font-weight: 500;
}

.status-badge {
  font-size: 12px;
  padding: 5px 12px;
  border-radius: 20px;
}

.status-badge.pending {
  background: #fff6e6;
  color: #f29900;
}

.status-badge.replied {
  background: #eaf8f3;
  color: #279e7c;
}

.review-middle {
  display: flex;
  gap: 24px;
  margin-bottom: 18px;
}

.info-block {
  min-width: 220px;
}

.lab {
  color: #8b5a2b;
  font-weight: 500;
}

.info-block p {
  line-height: 28px;
  font-size: 14px;
}

.content-block {
  flex: 1;
}

.c-title {
  font-size: 14px;
  color: #8b5a2b;
  font-weight: 500;
  margin-bottom: 6px;
}

.c-text {
  color: #555;
  line-height: 1.7;
}

/* 图片区域 */
.img-block {
  margin-bottom: 18px;
}

.img-title {
  font-size: 14px;
  color: #8b5a2b;
  margin-bottom: 10px;
  font-weight: 500;
}

.img-wrap {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.img-item {
  width: 90px;
  height: 90px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #eee;
}

.img-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.reply-block textarea {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #f0d9b5;
  border-radius: 8px;
  font-size: 14px;
  margin-bottom: 12px;
  outline: none;
}

.submit-reply {
  padding: 7px 18px;
  background: #457b9d;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.reply-block.replied {
  background: #f9f2e6;
  padding: 14px;
  border-radius: 8px;
}

.reply-lab {
  font-size: 14px;
  color: #8b5a2b;
  margin-bottom: 6px;
  font-weight: 500;
}

.reply-text {
  color: #555;
}

.empty-tip {
  text-align: center;
  padding: 50px;
  color: #999;
}
</style>