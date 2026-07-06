<template>
  <div class="container">
    <div class="sidebar">
      <h2>民宿管理系统</h2>
      <div class="menu-item" :class="{active: $route.path === '/dashboard'}" @click="$router.push('/dashboard')">🏠 工作台</div>
      <div class="menu-item" :class="{active: $route.path === '/evaluate'}" @click="$router.push('/evaluate')">⭐ 评价管理</div>
    </div>
    <div class="main-content">
      <router-view/>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainLayout' // 改成多单词
}
</script>

<style scoped>
.container { display: flex; height: 100vh; }
.sidebar { width: 220px; background: #fff; border-right: 1px solid #f0d9b5; padding: 20px 0; box-shadow: 2px 0 8px rgba(0,0,0,0.05); }
.sidebar h2 { text-align: center; color: #8b5a2b; margin-bottom: 30px; font-size: 18px; padding-bottom: 10px; border-bottom: 1px solid #f0d9b5; }
.menu-item { padding: 10px 20px; font-size: 14px; cursor: pointer; transition: all 0.2s ease; }
.menu-item.active, .menu-item:hover { background: #f5b041; color: #fff; }
.main-content { flex: 1; padding: 20px; overflow-y: auto; background: #fff5eb; }
</style>