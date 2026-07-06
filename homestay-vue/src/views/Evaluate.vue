<template>
  <div id="evaluate-page">
    <div class="back-box">
      <button class="back-btn" @click="$router.push('/person-center')">← 返回个人中心</button>
    </div>

    <div class="content-header">
      <h3>⭐ 我的评价中心</h3>
      <span class="desc">待评价：{{canEvaluateOrders.length}}单｜已评价：{{listFormat.length}}单</span>
    </div>

    <!-- ✅ 已评价的订单 不会再出现在这里 -->
    <div class="card eval-order-card" v-if="canEvaluateOrders.length>0">
      <h4>📋 待评价订单</h4>
      <div class="table-wrap">
        <table class="table">
          <thead>
          <tr>
            <th>房型图片</th>
            <th>房型名称</th>
            <th>订单状态</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in canEvaluateOrders" :key="item.orderId">
            <td>
              <img :src="getImg(item.roomImg)" class="table-img"/>
            </td>
            <td>{{item.roomName}}</td>
            <td><span class="tag-finish">已完成</span></td>
            <td>
              <button class="btn-eval" @click="openEval(item)">去评价</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="card" v-if="listFormat.length === 0 && canEvaluateOrders.length === 0">
      <div class="empty">暂无评价记录，快去体验民宿发布评价吧</div>
    </div>
    <div class="history-card-item" v-for="item in listFormat" :key="item.evaId">
      <div class="history-top">
        <span class="room-name">{{ item.roomName||"未记录房型" }}</span>
        <div class="star-box">
          <span class="star" v-for="s in item.score" :key="s">★</span>
          <span class="eva-time">{{item.evaTime}}</span>
        </div>
      </div>
      <div class="history-content">
        <p class="eva-text">{{ item.content || '无评价内容' }}</p>
        <div class="img-row" v-if="item.imgArr.length>0">
      <span v-for="(src,i) in item.imgArr" :key="i" @click="previewBigImg(src)" class="mini-img">
        <img :src="src">
      </span>
        </div>
        <p class="reply-txt">商家回复：{{ item.reply || '商家未回复' }}</p>
      </div>
    </div>

    <div class="mask" v-if="showModal" @click.self="closeModal">
      <div class="eval-box">
        <div class="order-room-top">
          <img :src="getImg(currentOrder.roomImg)" class="top-room-img">
          <div class="info-text">
            <h4>{{currentOrder.roomName}}</h4>
            <p>订单号：{{currentOrder.orderId}}</p>
          </div>
        </div>

        <div class="score-box">
          <span>综合评分：</span>
          <span class="star-click"
                v-for="i in 5"
                :key="i"
                :class="{active:evalScore>=i}"
                @click="evalScore=i">★</span>
          <span class="score-num">{{evalScore}}分</span>
        </div>

        <textarea v-model="evalContent" placeholder="分享入住体验、环境、卫生、服务等真实感受..." class="textarea"></textarea>

        <div class="upload-box">
          <p class="up-title">上传实拍图（最多6张）</p>
          <div class="img-show-row">
            <div class="up-item" v-for="(url,idx) in uploadImgList" :key="idx">
              <img :src="url" @click="previewBigImg(url)">
              <i class="del-icon" @click="uploadImgList.splice(idx,1)">×</i>
            </div>
            <label class="add-up-btn" v-if="uploadImgList.length<6">
              <input type="file" accept="image/*" hidden @change="fileChange($event)">
              <span>+</span>
            </label>
          </div>
        </div>

        <div class="btns">
          <button class="cancel-btn" @click="closeModal">取消</button>
          <button class="sub-btn" @click="submit">提交评价</button>
        </div>
      </div>
    </div>

    <div class="img-preview-mask" v-if="previewSrc" @click="previewSrc=''">
      <img :src="previewSrc" class="big-preview">
    </div>
  </div>
</template>

<script>
import axios from 'axios'
/* eslint-disable vue/multi-word-component-names */
export default {
  name: "Evaluate",
  data() {
    return {
      list: [],
      allOrders: [],
      canEvaluateOrders: [],
      showModal: false,
      currentOrder: null,
      evalScore: 5,
      evalContent: "",
      uploadImgList: [],
      previewSrc: ""
    };
  },
  computed: {
    listFormat() {
      return this.list.map(item => {
        let arr = [];
        if (item.evaImg && item.evaImg.trim() !== '') {
          arr = item.evaImg.split(",");
        }
        return { ...item, imgArr: arr };
      });
    }
  },
  mounted() {
    this.loadList();
    this.loadAllOrders();

    const queryOrder = this.$route.query;
    if (queryOrder.orderId) {
      const order = {
        orderId: queryOrder.orderId,
        roomId: queryOrder.roomId,
        roomName: queryOrder.roomName,
        roomImg: queryOrder.roomImg
      };
      this.openEval(order);
    }
  },
  methods: {
    getImg(img) {
      if (!img || img.trim() === '') return "https://picsum.photos/300/180";
      if (img.startsWith('/upload')) {
        return `http://localhost:8080/ssm_springmvc${img}`;
      } else {
        return `http://localhost:8080/ssm_springmvc/upload/${img}`;
      }
    },
    previewBigImg(src) {
      this.previewSrc = src;
    },
    async fileChange(e) {
      const file = e.target.files[0];
      if (!file) return;
      if (this.uploadImgList.length >= 6) {
        alert("最多上传6张图片");
        return;
      }
      let formData = new FormData();
      formData.append("file", file);
      try {
        let res = await axios.post("/ssm_springmvc/api/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" }
        });
        if (res.data.code === 200) {
          this.uploadImgList.push(res.data.data);
        } else {
          alert("图片上传失败");
        }
      } catch (err) {
        alert("上传异常，请稍后再试");
      }
      e.target.value = '';
    },
    async loadList() {
      try {
        const userId = localStorage.getItem("userId");
        if (!userId) {
          alert("请先登录");
          this.$router.push("/login");
          return;
        }
        const res = await axios.get("/ssm_springmvc/api/evaluate/myList", {
          params: { userId }
        });
        this.list = res.data.data || [];
        this.refreshCanEvaluate();
      } catch (e) {
        console.error("加载评价失败", e);
      }
    },
    async loadAllOrders() {
      let uid = localStorage.getItem('userId');
      let res = await axios.get('/ssm_springmvc/api/order/getMyOrder', { params: { userId: uid } });
      this.allOrders = res.data.data || [];
      this.refreshCanEvaluate();
    },
    refreshCanEvaluate() {
      // ✅ 核心：已评价订单自动过滤，不再显示待评价
      const finished = this.allOrders.filter(o => Number(o.orderStatus) === 4);
      this.canEvaluateOrders = finished.filter(order=>{
        return !this.list.some(eva=> eva.orderId === order.orderId)
      })
    },
    openEval(item) {
      this.currentOrder = item;
      this.evalScore = 5;
      this.evalContent = "";
      this.uploadImgList = [];
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
    },
    async submit() {
      if (!this.evalContent.trim()) {
        alert('请填写评价内容');
        return;
      }
      let params = {
        userId: localStorage.getItem('userId'),
        userName: localStorage.getItem('userName'),
        score: this.evalScore,
        content: this.evalContent,
        roomId: this.currentOrder.roomId,
        roomName: this.currentOrder.roomName,
        orderId: this.currentOrder.orderId,
        imgArr: this.uploadImgList
      };
      try {
        await axios.post('/ssm_springmvc/api/evaluate/add', params);
        alert('评价发布成功！');
        this.showModal = false;
        await this.loadList();
        await this.loadAllOrders();
      } catch (e) {
        console.error(e);
        alert('评价提交失败');
      }
    }
  }
};
</script>

<style scoped>
#evaluate-page {
  background: #fff9e6;
  min-height: 100vh;
  padding: 24px;
}
.back-box{margin-bottom:12px;}
.back-btn{
  background:#fff;
  border:1px solid #8b5a2b;
  color:#8b5a2b;
  padding:6px 12px;
  border-radius:6px;
  cursor:pointer;
}
.content-header {
  background: #fff;
  padding: 18px 24px;
  border-radius: 12px;
  margin-bottom: 22px;
  box-shadow: 0 2px 12px rgba(139,90,43,0.08);
  color: #8b5a2b;
}
.desc {
  font-size: 13px;
  color: #999;
  margin-top: 4px;
}
.card {
  background: #fff;
  padding: 22px;
  border-radius: 12px;
  margin-bottom: 22px;
  box-shadow: 0 2px 14px rgba(139,90,43,0.07);
}
.card h4 {
  margin-bottom: 16px;
  color: #5a4a3f;
}
.table-wrap {
  overflow-x: auto;
}
.table {
  width: 100%;
  border-collapse: collapse;
}
.table th,
.table td {
  padding: 14px 12px;
  border-bottom: 1px solid #f0e3d0;
  text-align: left;
  font-size: 14px;
}
.table th {
  background: #fef8ee;
  color: #8b5a2b;
}
.table-img {
  width: 70px;
  height: 50px;
  border-radius: 6px;
  object-fit: cover;
}
.tag-finish {
  padding: 3px 10px;
  background: #e8f5e9;
  color: #2e7d32;
  border-radius: 12px;
  font-size: 12px;
}
.btn-eval {
  background: #2a9d8f;
  color: #fff;
  border: none;
  padding: 5px 12px;
  border-radius: 5px;
  cursor: pointer;
}
.empty {
  padding: 40px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
.star {
  color: #f5b041;
  margin-right: 2px;
}
.star-box {
  display: flex;
  align-items: center;
  gap: 10px;
}
.eva-time {
  font-size: 12px;
  color: #999;
}
.mini-img {
  width: 50px;
  height: 40px;
  margin-right: 6px;
  cursor: pointer;
}
.mini-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}
.img-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  margin: 8px 0;
}
.history-card-item {
  background: #fff;
  padding: 18px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(139,90,43,0.07);
  margin-bottom: 16px;
}
.history-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.room-name {
  font-size: 16px;
  font-weight: 500;
  color: #5a4a3f;
}
.eva-text {
  color: #555;
  line-height: 1.6;
}
.reply-txt {
  margin-top: 8px;
  color: #888;
  font-size: 13px;
}
.mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 998;
}
.eval-box {
  width: 520px;
  background: #fff;
  padding: 26px;
  border-radius: 14px;
  box-shadow: 0 4px 30px rgba(0,0,0,0.15);
}
.order-room-top {
  display: flex;
  gap: 16px;
  margin-bottom: 18px;
  padding-bottom: 18px;
  border-bottom: 1px solid #f0e3d0;
}
.top-room-img {
  width: 130px;
  height: 90px;
  border-radius: 8px;
  object-fit: cover;
}
.info-text h4 {
  margin: 0 0 6px;
  color: #5a4a3f;
}
.info-text p {
  font-size: 13px;
  color: #999;
  margin: 0;
}
.score-box {
  margin: 14px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}
.star-click {
  font-size: 28px;
  color: #ccc;
  cursor: pointer;
  margin: 0 2px;
  transition: all 0.2s;
}
.star-click.active {
  color: #f5b041;
}
.score-num {
  color: #f5b041;
  font-weight: bold;
}
.textarea {
  width: 100%;
  height: 110px;
  padding: 12px;
  border: 1px solid #e6e0d5;
  border-radius: 8px;
  resize: none;
  outline: none;
  font-size: 14px;
}
.upload-box {
  margin: 18px 0;
}
.up-title {
  margin-bottom: 10px;
  font-size: 14px;
  color: #5a4a3f;
}
.img-show-row {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
.up-item {
  width: 76px;
  height: 76px;
  border-radius: 6px;
  position: relative;
  border: 1px solid #eee;
}
.up-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
}
.del-icon {
  position: absolute;
  right: -6px;
  top: -6px;
  width: 18px;
  height: 18px;
  background: #e74c3c;
  color: #fff;
  border-radius: 50%;
  text-align: center;
  line-height: 18px;
  font-size: 13px;
  cursor: pointer;
}
.add-up-btn {
  width: 76px;
  height: 76px;
  border: 1px dashed #ccc;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: #aaa;
  cursor: pointer;
}
.btns {
  margin-top: 22px;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}
.cancel-btn {
  padding: 7px 20px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 6px;
  cursor: pointer;
}
.sub-btn {
  background: #f5b041;
  color: #fff;
  border: none;
  padding: 7px 22px;
  border-radius: 6px;
  cursor: pointer;
}
.img-preview-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.85);
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
}
.big-preview {
  max-width: 90%;
  max-height: 90vh;
}
</style>