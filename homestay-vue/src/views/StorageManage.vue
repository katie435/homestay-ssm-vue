<template>
  <div id="app">
    <div class="container">
      <!-- 左侧菜单 -->
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
        <div class="menu-item" @click="$router.push('/review-manage')">💬 评价管理</div>

        <div class="menu-group-title">客户管理</div>
        <div class="menu-item" @click="$router.push('/user-manage')">👤 客户列表</div>

        <div class="menu-group-title">库房监控</div>
        <div class="menu-item active">📦 库房管理</div>

        <div class="menu-group-title">系统操作</div>
        <div class="menu-item" @click="$router.push('/employee-manage')">👨‍💼 员工管理</div>
        <div class="menu-item" @click="logout">🚪 退出登录</div>
      </div>

      <!-- 右侧主内容 -->
      <div class="main-content">
        <div class="content-header">
          <h3>📦 库房物品管理</h3>
          <div>
            <button class="btn btn-green" @click="openAddModal">➕ 新增物品</button>
            <button class="btn btn-blue" @click="openInModal">📥 物品入库</button>
            <button class="btn btn-red" @click="openOutModal">📤 物品出库</button>
          </div>
        </div>

        <div class="stat-grid">
          <div class="stat-card">
            <div class="stat-icon" style="background:#e8f5e9;color:#2a9d8f">📦</div>
            <div class="stat-text">
              <div class="num">{{ totalItems }}</div>
              <div class="label">物品总数</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background:#fff4e5;color:#f59e0b">⚠️</div>
            <div class="stat-text">
              <div class="num">{{ warnItems }}</div>
              <div class="label">库存预警</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background:#e3f2fd;color:#457b9d">📥</div>
            <div class="stat-text">
              <div class="num">{{ inTotal }}</div>
              <div class="label">今日入库</div>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon" style="background:#fef0f0;color:#e76f51">📤</div>
            <div class="stat-text">
              <div class="num">{{ outTotal }}</div>
              <div class="label">今日出库</div>
            </div>
          </div>
        </div>

        <div class="filter-bar">
          <div class="filter-item">
            <label>物品名称：</label>
            <input v-model="filter.name" placeholder="搜索物品">
          </div>
          <div class="filter-item">
            <label>物品分类：</label>
            <select v-model="filter.type">
              <option value="">全部</option>
              <option value="清洁用品">清洁用品</option>
              <option value="布草用品">布草用品</option>
              <option value="洗漱用品">洗漱用品</option>
              <option value="电子用品">电子用品</option>
            </select>
          </div>
          <div class="filter-item">
            <label>库存状态：</label>
            <select v-model="filter.stockStatus">
              <option value="">全部</option>
              <option value="warning">库存预警</option>
            </select>
          </div>
          <button class="btn" @click="search">搜索</button>
          <button class="btn btn-gray" @click="reset">重置</button>
        </div>

        <div class="table-container">
          <table>
            <thead>
            <tr>
              <th>ID</th>
              <th>物品名称</th>
              <th>分类</th>
              <th>库存数量</th>
              <th>预警线</th>
              <th>单位</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in list" :key="item.id">
              <td>{{ item.id }}</td>
              <td>{{ item.name }}</td>
              <td>{{ item.type }}</td>
              <td>{{ item.stock }}</td>
              <td>{{ item.warnLine }}</td>
              <td>{{ item.unit }}</td>
              <td>
                <span class="tag" :class="item.stock <= item.warnLine ? 'tag-danger' : 'tag-normal'">
                  {{ item.stock <= item.warnLine ? '库存不足' : '正常' }}
                </span>
              </td>
              <td>
                <div class="action-group">
                  <button class="btn btn-blue" style="padding:5px 10px;font-size:12px" @click="openEditModal(item)">编辑</button>
                  <button class="btn btn-red" style="padding:5px 10px;font-size:12px" @click="del(item)">删除</button>
                </div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-show="showAdd" class="modal-mask">
      <div class="modal-box">
        <div class="modal-header">{{ form.id ? '编辑物品' : '新增物品' }}</div>
        <div class="modal-body">
          <div class="form-item">
            <label>物品名称</label>
            <input v-model="form.name" placeholder="请输入">
          </div>
          <div class="form-item">
            <label>物品分类</label>
            <select v-model="form.type">
              <option>清洁用品</option>
              <option>布草用品</option>
              <option>洗漱用品</option>
              <option>电子用品</option>
            </select>
          </div>
          <div class="form-item">
            <label>单位</label>
            <input v-model="form.unit" placeholder="个/套/瓶">
          </div>
          <div class="form-item">
            <label>初始库存</label>
            <input v-model.number="form.stock" type="number">
          </div>
          <div class="form-item">
            <label>库存预警线</label>
            <input v-model.number="form.warnLine" type="number">
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-gray" @click="showAdd=false">取消</button>
          <button class="btn" @click="saveAdd">确认保存</button>
        </div>
      </div>
    </div>

    <!-- 入库弹窗 -->
    <div v-show="showIn" class="modal-mask">
      <div class="modal-box">
        <div class="modal-header">物品入库</div>
        <div class="modal-body">
          <div class="form-item">
            <label>选择物品</label>
            <select v-model="inId">
              <option v-for="item in originList" :key="item.id" :value="item.id">
                {{ item.name }}（当前：{{ item.stock }}{{ item.unit }}）
              </option>
            </select>
          </div>
          <div class="form-item">
            <label>入库数量</label>
            <input v-model.number="inNum" type="number" min="1">
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-gray" @click="showIn=false">取消</button>
          <button class="btn btn-blue" @click="saveIn">确认入库</button>
        </div>
      </div>
    </div>

    <!-- 出库弹窗 -->
    <div v-show="showOut" class="modal-mask">
      <div class="modal-box">
        <div class="modal-header">物品出库</div>
        <div class="modal-body">
          <div class="form-item">
            <label>选择物品</label>
            <select v-model="outId">
              <option v-for="item in originList" :key="item.id" :value="item.id">
                {{ item.name }}（当前：{{ item.stock }}{{ item.unit }}）
              </option>
            </select>
          </div>
          <div class="form-item">
            <label>出库数量</label>
            <input v-model.number="outNum" type="number" min="1">
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-gray" @click="showOut=false">取消</button>
          <button class="btn btn-red" @click="saveOut">确认出库</button>
        </div>
      </div>
    </div>

    <!-- 提示 -->
    <div v-show="toastShow" class="toast">{{ toastMsg }}</div>
  </div>
</template>

<script>
export default {
  name: "StorageManage",
  data() {
    return {
      filter: { name: '', type: '', stockStatus: '' },
      form: { name: '', type: '清洁用品', unit: '', stock: 0, warnLine: 0 },
      list: [],
      originList: [],
      showAdd: false,
      showIn: false,
      showOut: false,
      inId: '',
      inNum: 1,
      outId: '',
      outNum: 1,
      toastShow: false,
      toastMsg: '',
      inTotal: 32,
      outTotal: 18
    }
  },
  mounted() {
    const userId = localStorage.getItem('userId')
    const role = localStorage.getItem('userRole')
    if (!userId || !role) {
      this.$router.replace('/login')
      return
    }
    this.initData()
    this.loadList()
  },
  computed: {
    totalItems() {
      return this.originList.length
    },
    warnItems() {
      return this.originList.filter(i => i.stock <= i.warnLine).length
    }
  },
  methods: {
    goPage(path) {
      this.$router.push(path)
    },
    logout() {
      localStorage.clear()
      this.$router.push('/login')
    },
    initData() {
      this.originList = [
        { id: 1, name: '一次性牙刷', type: '洗漱用品', stock: 120, warnLine: 30, unit: '个' },
        { id: 2, name: '一次性梳子', type: '洗漱用品', stock: 95, warnLine: 25, unit: '把' },
        { id: 3, name: '洗漱套装', type: '洗漱用品', stock: 68, warnLine: 20, unit: '套' },
        { id: 4, name: '沐浴露', type: '洗漱用品', stock: 12, warnLine: 15, unit: '瓶' },
        { id: 5, name: '洗发水', type: '洗漱用品', stock: 10, warnLine: 15, unit: '瓶' },
        { id: 6, name: '香皂', type: '洗漱用品', stock: 156, warnLine: 40, unit: '块' },
        { id: 7, name: '浴巾', type: '布草用品', stock: 45, warnLine: 20, unit: '条' },
        { id: 8, name: '毛巾', type: '布草用品', stock: 86, warnLine: 30, unit: '条' },
        { id: 9, name: '枕套', type: '布草用品', stock: 122, warnLine: 50, unit: '个' },
        { id: 10, name: '床单', type: '布草用品', stock: 36, warnLine: 18, unit: '张' },
        { id: 11, name: '被罩', type: '布草用品', stock: 28, warnLine: 15, unit: '张' },
        { id: 12, name: '洗衣液', type: '清洁用品', stock: 16, warnLine: 10, unit: '瓶' },
        { id: 13, name: '马桶刷', type: '清洁用品', stock: 9, warnLine: 10, unit: '个' },
        { id: 14, name: '拖把', type: '清洁用品', stock: 7, warnLine: 8, unit: '把' },
        { id: 15, name: '抹布', type: '清洁用品', stock: 42, warnLine: 20, unit: '块' },
        { id: 16, name: '消毒液', type: '清洁用品', stock: 8, warnLine: 12, unit: '桶' },
        { id: 17, name: '垃圾袋', type: '清洁用品', stock: 210, warnLine: 80, unit: '卷' },
        { id: 18, name: '遥控器电池', type: '电子用品', stock: 5, warnLine: 8, unit: '节' },
        { id: 19, name: '一次性插座', type: '电子用品', stock: 14, warnLine: 10, unit: '个' },
        { id: 20, name: '数据线', type: '电子用品', stock: 11, warnLine: 12, unit: '根' }
      ]
      this.list = [...this.originList]
    },
    loadList() {
      this.search()
    },
    search() {
      let data = [...this.originList]
      if (this.filter.name) {
        data = data.filter(i => i.name.includes(this.filter.name))
      }
      if (this.filter.type) {
        data = data.filter(i => i.type === this.filter.type)
      }
      if (this.filter.stockStatus === 'warning') {
        data = data.filter(i => i.stock <= i.warnLine)
      }
      this.list = data
    },
    reset() {
      this.filter = { name: '', type: '', stockStatus: '' }
      this.loadList()
    },
    getNewId() {
      if (this.originList.length === 0) return 1
      return Math.max(...this.originList.map(i => i.id)) + 1
    },
    openAddModal() {
      this.form = { name: '', type: '清洁用品', unit: '', stock: 0, warnLine: 0 }
      this.showAdd = true
    },
    openEditModal(item) {
      this.form = { ...item }
      this.showAdd = true
    },
    saveAdd() {
      if (!this.form.name) {
        this.tip('请输入物品名称')
        return
      }
      if (this.form.id) {
        let idx = this.originList.findIndex(i => i.id === this.form.id)
        if (idx > -1) this.originList[idx] = { ...this.form }
        this.tip('修改成功')
      } else {
        this.form.id = this.getNewId()
        this.originList.push({ ...this.form })
        this.tip('新增物品成功')
      }
      this.showAdd = false
      this.loadList()
    },
    del(item) {
      if (!confirm('确定删除该物品？')) return
      this.originList = this.originList.filter(i => i.id !== item.id)
      this.tip('删除成功')
      this.loadList()
    },
    openInModal() {
      if (this.originList.length === 0) {
        this.tip('请先添加物品')
        return
      }
      this.showIn = true
      this.inId = this.originList[0].id
      this.inNum = 1
    },
    saveIn() {
      let item = this.originList.find(i => i.id === this.inId)
      if (!item) return
      item.stock += this.inNum
      this.showIn = false
      this.tip('入库成功')
      this.loadList()
    },
    openOutModal() {
      if (this.originList.length === 0) {
        this.tip('请先添加物品')
        return
      }
      this.showOut = true
      this.outId = this.originList[0].id
      this.outNum = 1
    },
    saveOut() {
      let item = this.originList.find(i => i.id === this.outId)
      if (!item) return
      if (this.outNum > item.stock) {
        this.tip('出库数量不能大于现有库存')
        return
      }
      item.stock -= this.outNum
      this.showOut = false
      this.tip('出库成功')
      this.loadList()
    },
    tip(msg) {
      this.toastMsg = msg
      this.toastShow = true
      setTimeout(() => this.toastShow = false, 1500)
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
  min-height: 100vh;
}
.container {
  display: flex;
  height: 100vh;
}

/* ========== 统一导航栏 ========== */
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

/* ========== 右侧删除滚动条 ========== */
.main-content {
  flex: 1;
  margin-left: 220px;
  padding: 24px;
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
  padding: 20px 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.content-header h3 {
  color: #8b5a2b;
  font-size: 19px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
}
.btn {
  padding: 9px 18px;
  background: #f5b041;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.25s ease;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}
.btn:hover {
  background: #e69c2e;
  transform: translateY(-1px);
}
.btn-green { background: #2a9d8f; }
.btn-blue  { background: #457b9d; }
.btn-red   { background: #e76f51; }
.btn-gray  { background: #909399; }
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}
.stat-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: 0.25s ease;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}
.stat-text .num {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 4px;
}
.stat-text .label {
  font-size: 14px;
  color: #999;
}
.filter-bar {
  background: #fff;
  padding: 18px 22px;
  border-radius: 12px;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}
.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}
.filter-item label {
  font-size: 14px;
  color: #5a4a3f;
}
.filter-item input, .filter-item select {
  padding: 9px 12px;
  border: 1px solid #eee;
  border-radius: 8px;
  outline: none;
  font-size: 14px;
  min-width: 160px;
  pointer-events: auto !important;
}
.table-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 14px rgba(0,0,0,0.06);
  overflow: hidden;
}
table {
  width: 100%;
  border-collapse: collapse;
}
thead {
  background: #f9f0e1;
}
th {
  padding: 16px 20px;
  text-align: left;
  font-weight: bold;
  color: #8b5a2b;
  font-size: 14px;
}
td {
  padding: 16px 20px;
  border-bottom: 1px solid #f7f7f7;
  font-size: 14px;
}
tbody tr:hover {
  background: #fff9ef;
}
.tag {
  padding: 5px 10px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}
.tag-normal { background: #e8f5e9; color: #2a9d8f; }
.tag-danger { background: #fef0f0; color: #e76f51; }
.action-group {
  display: flex;
  gap: 8px;
}
.modal-mask {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-box {
  width: 480px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 4px 25px rgba(0,0,0,0.15);
  overflow: hidden;
}
.modal-header {
  padding: 18px 24px;
  background: #f5b041;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
}
.modal-body {
  padding: 28px;
}
.form-item {
  margin-bottom: 18px;
}
.form-item label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #5a4a3f;
}
.form-item input, .form-item select, .form-item textarea {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #eee;
  border-radius: 8px;
  outline: none;
  font-size: 14px;
  pointer-events: auto !important;
}
.modal-footer {
  padding: 18px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid #f5f5f5;
}
.toast {
  position: fixed;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  background: #2a9d8f;
  color: #fff;
  padding: 12px 28px;
  border-radius: 8px;
  z-index: 9999;
}
@media (max-width: 1200px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>