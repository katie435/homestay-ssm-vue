<template>
  <div id="login-page">
    <!-- 只保留：管理人员登录 -->
    <div class="admin-login-entrance" @click="showPage('admin-login')">
      管理人员登录
    </div>

    <div class="login-wrap">
      <div class="title">栖宿民宿 登录入口</div>

      <!-- 顾客登录 -->
      <div class="page" :class="{active: currentPage === 'customer-login'}">
        <div class="role-card">
          <div class="role-title">顾客登录</div>
          <div class="form-group">
            <label>姓名</label>
            <input type="text" v-model="loginForm.username" placeholder="请输入姓名">
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input type="text" v-model="loginForm.phone" placeholder="请输入11位手机号">
            <div class="error-tip" v-show="loginError.phone">手机号必须为11位数字</div>
          </div>
          <div class="form-group">
            <label>密码</label>
            <input type="password" v-model="loginForm.password" placeholder="请输入密码">
            <div class="error-tip" v-show="loginError.pwd">密码不能为空</div>
          </div>
          <div class="form-group">
            <label>验证码</label>
            <div class="captcha-wrap">
              <input type="text" v-model="loginForm.captcha" class="captcha-input" placeholder="请输入验证码">
              <div class="captcha-code" @click="refreshCaptcha">{{ captchaCode }}</div>
            </div>
            <div class="error-tip" v-show="loginError.captcha">验证码错误</div>
          </div>
          <button class="login-btn" @click="customerLogin">顾客登录</button>
          <div class="register-link" @click="showPage('customer-register')">
            注册账号
          </div>
        </div>
      </div>

      <!-- 顾客注册 -->
      <div class="page" :class="{active: currentPage === 'customer-register'}">
        <div class="role-card">
          <div class="role-title">顾客注册</div>
          <div class="form-group">
            <label>姓名</label>
            <input type="text" v-model="regForm.username" placeholder="请输入姓名">
            <div class="error-tip" v-show="regError.name">姓名不能为空</div>
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input type="text" v-model="regForm.phone" placeholder="请输入11位手机号">
            <div class="error-tip" v-show="regError.phone">手机号必须为11位数字</div>
          </div>
          <div class="form-group">
            <label>密码</label>
            <input type="password" v-model="regForm.password" placeholder="请设置密码（不少于6位）">
            <div class="error-tip" v-show="regError.pwd">密码长度不能少于6位</div>
          </div>
          <div class="form-group">
            <label>确认密码</label>
            <input type="password" v-model="regForm.confirmPwd" placeholder="请再次输入密码">
            <div class="error-tip" v-show="regError.confirmPwd">两次输入的密码不一致</div>
          </div>
          <div class="form-group">
            <label>验证码</label>
            <div class="captcha-wrap">
              <input type="text" v-model="regForm.captcha" class="captcha-input" placeholder="请输入验证码">
              <div class="captcha-code" @click="refreshRegCaptcha">{{ regCaptchaCode }}</div>
            </div>
            <div class="error-tip" v-show="regError.captcha">验证码错误</div>
          </div>
          <button class="login-btn" @click="customerRegister">完成注册</button>
          <div class="register-link" @click="showPage('customer-login')">
            返回登录
          </div>
        </div>
      </div>

      <!-- 管理人员登录（保留，对接数据库 staff 表） -->
      <div class="page" :class="{active: currentPage === 'admin-login'}">
        <div class="role-card">
          <div class="role-title">管理人员登录</div>
          <div class="form-group">
            <label>真实姓名</label>
            <input type="text" v-model="adminForm.username" placeholder="请输入真实姓名">
          </div>
          <div class="form-group">
            <label>密码</label>
            <input type="password" v-model="adminForm.password" placeholder="请输入密码">
          </div>
          <div class="form-group">
            <label>手机号</label>
            <input type="text" v-model="adminForm.phone" placeholder="请输入11位手机号">
          </div>
          <button class="login-btn" @click="adminLogin">管理员登录</button>
          <div class="register-link" @click="showPage('customer-login')">
            返回顾客登录
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "LoginPage",
  data() {
    return {
      currentPage: 'customer-login',
      captchaCode: '000000',
      regCaptchaCode: '000000',
      loginForm: { username: '', phone: '', password: '', captcha: '' },
      loginError: { phone: false, pwd: false, captcha: false },
      regForm: { username: '', phone: '', password: '', confirmPwd: '', captcha: '' },
      regError: { name: false, phone: false, pwd: false, confirmPwd: false, captcha: false },
      adminForm: { username: '', password: '', phone: '' }
    }
  },
  mounted() {
    this.refreshCaptcha()
    this.refreshRegCaptcha()
  },
  methods: {
    genCode() {
      return Math.floor(Math.random() * 1000000).toString().padStart(6, '0')
    },
    refreshCaptcha() {
      this.captchaCode = this.genCode()
    },
    refreshRegCaptcha() {
      this.regCaptchaCode = this.genCode()
    },
    showPage(page) {
      this.currentPage = page
    },

    async customerLogin() {
      let {username, phone, password, captcha} = this.loginForm
      this.loginError = {phone: false, pwd: false, captcha: false}

      if (!username) {
        alert("请输入姓名");
        return
      }
      if (!/^\d{11}$/.test(phone)) {
        this.loginError.phone = true;
        return
      }
      if (!password) {
        this.loginError.pwd = true;
        return
      }
      if (captcha !== this.captchaCode) {
        this.loginError.captcha = true;
        this.refreshCaptcha();
        return;
      }
      try {
        let res = await axios.post('/api/user/login', {username, password})
        if (res.data.code === 200) {
          let user = res.data.data
          localStorage.setItem("userId", user.userId);
          localStorage.setItem("userName", user.username);
          localStorage.setItem("userRole", "customer");
          alert("✅ 顾客登录成功！");
          this.$router.push('/home');
        } else {
          alert(res.data.msg)
        }
      } catch (e) {
        alert("登录异常：" + e)
      }
    },

    async customerRegister() {
      let {username, phone, password, confirmPwd, captcha} = this.regForm
      this.regError = {name: false, phone: false, pwd: false, confirmPwd: false, captcha: false}

      if (!username) {
        this.regError.name = true;
        return
      }
      if (!/^\d{11}$/.test(phone)) {
        this.regError.phone = true;
        return
      }
      if (password.length < 6) {
        this.regError.pwd = true;
        return
      }
      if (password !== confirmPwd) {
        this.regError.confirmPwd = true;
        return
      }
      if (captcha !== this.regCaptchaCode) {
        this.regError.captcha = true;
        this.refreshRegCaptcha();
        return;
      }
      try {
        let res = await axios.post('/api/user/add', {username, password, phone})
        if (res.data.code === 200) {
          alert("✅ 注册成功！请登录");
          this.showPage('customer-login');
          this.regForm = {username: '', phone: '', password: '', confirmPwd: '', captcha: ''};
        } else {
          alert(res.data.msg)
        }
      } catch (e) {
        alert("注册失败")
      }
    },

    // ======================
    // 管理员登录 → 对接 staff 表
    // ======================
    async adminLogin() {
      let {username, password, phone} = this.adminForm
      if (!username || !password || !phone) {
        alert("请填写完整信息")
        return
      }
      try {
        let res = await axios.post('/ssm_springmvc/api/staff/login', {
          username,
          password,
          phone
        })
        if (res.data.code === 200) {
          localStorage.setItem("userId", res.data.data.staffId);
          localStorage.setItem("userName", res.data.data.staffName);
          localStorage.setItem("userRole", "admin");
          alert("✅ 管理员登录成功！");
          this.$router.push('/dashboard');
        } else {
          alert(res.data.msg);
        }
      } catch (e) {
        alert("管理员登录失败：接口404，请核对账号或后端接口");
        console.error(e)
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
#login-page {
  background: url('../assets/house.png') no-repeat center center fixed;
  background-size: cover;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
}
#login-page::before {
  content: "";
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 245, 235, 0.7);
  z-index: -1;
}
.admin-login-entrance {
  position: fixed;
  top: 20px;
  right: 20px;
  background: rgba(255, 255, 255, 0.8);
  padding: 8px 16px;
  border-radius: 20px;
  color: #8b5a2b;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 100;
  border: 1px solid #f0d9b5;
}
.admin-login-entrance:hover {
  background: #f5b041;
  color: white;
  transform: translateY(-2px);
}

.login-wrap {
  text-align: center;
  width: 100%;
  max-width: 900px;
  position: relative;
  z-index: 1;
}
.title {
  font-size: 28px;
  color: #8b5a2b;
  margin-bottom: 40px;
  font-weight: bold;
}
.login-box {
  display: flex;
  gap: 30px;
  justify-content: center;
}
.role-card {
  width: 460px;
  padding-bottom: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  padding: 30px;
  border: 1px solid rgba(245, 232, 208, 0.8);
  transition: all 0.3s ease;
  margin: 0 auto;
}
.role-title {
  font-size: 20px;
  color: #8b5a2b;
  font-weight: bold;
  margin-bottom: 25px;
}
.form-group {
  margin-bottom: 20px;
  text-align: left;
}
.form-group label {
  display: block;
  font-size: 14px;
  color: #5a4a3f;
  margin-bottom: 8px;
  font-weight: 500;
}
.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #f0d9b5;
  border-radius: 8px;
  outline: none;
  font-size: 15px;
  background: rgba(255, 255, 255, 0.95);
  transition: border-color 0.2s ease;
}
.form-group input:focus {
  border-color: #f5b041;
  box-shadow: 0 0 0 3px rgba(245, 176, 65, 0.1);
}
.captcha-wrap {
  display: flex;
  gap: 10px;
  align-items: center;
}
.captcha-input {
  flex: 1;
}
.captcha-code {
  background: #f5b041;
  color: #fff;
  padding: 12px 15px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s ease;
}
.captcha-code:hover {
  background-color: #e69c2e;
}
.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #f5b041;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
  transition: all 0.3s ease;
}
.login-btn:hover {
  background-color: #e69c2e;
  transform: translateY(-2px);
}
.error-tip {
  color: #e74c3c;
  font-size: 12px;
  margin-top: 5px;
}
.register-link {
  margin-top: 15px;
  font-size: 14px;
  color: #8b5a2b;
  cursor: pointer;
  transition: all 0.2s ease;
  display: inline-block;
}
.register-link:hover {
  font-weight: bold;
  font-size: 15px;
  color: #f5b041;
}
.page {
  display: none;
}
.page.active {
  display: block;
}
</style>