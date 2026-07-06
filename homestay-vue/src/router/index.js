import Vue from 'vue'
import VueRouter from 'vue-router'

import MainLayout from '../views/Layout.vue'
import OrderEvaluate from '../views/Evaluate.vue'

// 用户
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import AllRooms from '../views/AllRooms.vue'
import MyOrders from '../views/MyOrders.vue'
import PersonCenter from '../views/PersonCenter.vue'
import RoomDetail from '../views/RoomDetail.vue'

Vue.use(VueRouter)

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', component: Login },
    { path: '/home', component: Home },
    { path: '/all-rooms', component: AllRooms },
    { path: '/my-orders', component: MyOrders },
    { path: '/person-center', component: PersonCenter },
    { path: '/room-detail', component: RoomDetail },

    // ✅ 唯一工作台路由（所有页面都跳这里）
    { path: '/dashboard', component: () => import('@/views/AdminDashboard.vue') },

    // 用户板块
    { path: '/house-list', name: 'HouseList', component: () => import('@/views/HouseList.vue') },
    { path: '/house-add', name: 'HouseAdd', component: () => import('@/views/HouseAdd.vue') },
    { path: '/room-status', name: 'RoomStatus', component: () => import('@/views/RoomStatus.vue') },
    { path: '/order-manage', name: 'OrderManage', component: () => import('@/views/OrderManage.vue') },
    { path: '/add-order', name: 'AddOrder', component: () => import('@/views/AddOrder.vue') },
    { path: '/review-manage', name: 'ReviewManage', component: () => import('@/views/ReviewManage.vue') },
    { path: '/storage-manage', name: 'StorageManage', component: () => import('@/views/StorageManage.vue') },
    { path: '/evaluate', component: () => import('@/views/Evaluate.vue')},

    // 客户管理
    { path: '/user-manage', name: 'UserManage', component: () => import('@/views/UserManage.vue') },

    // 员工管理
    { path: '/employee-manage', name: 'EmployeeManage', component: () => import('@/views/EmployeeManage.vue') },

    // 管理员布局（保留但不冲突）
    {
        path: '/admin',
        component: MainLayout,
        redirect: '/admin/dashboard',
        children: [
            { path: 'dashboard', component: () => import('@/views/AdminDashboard.vue'), meta: { title: '工作台' } },
            { path: 'evaluate', component: OrderEvaluate, meta: { title: '评价管理' } }
        ]
    }
]

const router = new VueRouter({
    mode: 'hash',
    routes
})

router.beforeEach((to, from, next) => {
    const userId = localStorage.getItem('userId')
    if (to.path === '/login') {
        next()
        return
    }
    if (!userId) {
        next('/login')
        return
    }
    next()
})

export default router
