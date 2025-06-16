<template>
  <div :class="{'dark-mode': isDark}" class="app-container">
    <div v-loading="logging" class="login-container">
      <div class="login-left">
        <button class="theme-toggle" @click="changeTheme">
          <svg-icon :icon-class="isDark ? 'moon' : 'sun'" size="20px"/>
        </button>

        <div class="login-header">
          <h1><img src="/logo.png" style="width: 40px;height: 40px;">后台管理系统</h1>
          <p>前沿技术驱动，安全高效的管理体验</p>
        </div>

        <div class="login-tabs">
          <button
              :class="{ active: loginType === 'USERNAME_PASSWORD' }"
              class="tab-btn"
              @click="loginType = 'USERNAME_PASSWORD'"
          >
            <svg-icon icon-class="account"/>
            账号密码登录
          </button>
          <button
              :class="{ active: loginType === 'EMAIL' }"
              class="tab-btn"
              @click="loginType = 'EMAIL'"
          >
            <svg-icon icon-class="email-fill"/>
            邮箱登录
          </button>
          <button
              :class="{ active: loginType === 'PHONE' }"
              class="tab-btn"
              @click="loginType = 'PHONE'"
          >
            <svg-icon icon-class="phone"/>
            手机号登录
          </button>
        </div>

        <!-- 账号登录表单 -->
        <div :class="{ active: loginType === 'USERNAME_PASSWORD' }" class="tab-content">
          <div class="form-group">
            <label for="username">用户名</label>
            <div class="input-with-icon">
              <svg-icon icon-class="account"/>
              <input id="username" v-model="loginForm.username" placeholder="请输入用户名" type="text">
            </div>
          </div>

          <div class="form-group">
            <label for="password">密码</label>
            <div class="input-with-icon">
              <svg-icon icon-class="password"/>
              <input id="password" v-model="loginForm.password" placeholder="请输入密码" type="password">
            </div>
          </div>

          <div class="form-group">
            <label for="captcha">验证码</label>
            <div class="input-with-icon" style="display: flex;">
              <svg-icon icon-class="captcha"/>
              <input id="captcha" v-model="loginForm.verifyCode" placeholder="请输入验证码"
                     style="border-top-right-radius: 0; border-bottom-right-radius: 0; width: 70%;"
                     type="text">
              <div
                  style="width: 30%; height: 38px; display: flex; align-items: center; justify-content: center;
                  background: #f9fafb; border: 1px solid #e2e8f0; border-left: none; border-top-right-radius: 10px;
                  border-bottom-right-radius: 10px; overflow: hidden;cursor: pointer;" @click="refreshCaptcha">
                <img :src="captchaBase64" alt="验证码" style="height: 100%; object-fit: contain;">
              </div>
            </div>
          </div>

          <button class="login-btn" @click="throttledLoginFn">登录系统</button>
        </div>

        <!-- 邮箱登录表单 -->
        <div :class="{ active: loginType === 'EMAIL' }" class="tab-content">
          <div class="form-group">
            <label for="email">邮箱地址</label>
            <div class="input-with-icon">
              <svg-icon icon-class="email-fill"/>
              <input id="email" v-model="loginForm.email" placeholder="请输入您的邮箱" type="email">
            </div>
          </div>

          <div class="form-group">
            <label for="emailCode">邮箱验证码</label>
            <div class="input-with-icon email-code-container">
              <svg-icon icon-class="email-captcha"/>
              <input id="emailCode" v-model="loginForm.emailCode" class="email-code-input"
                     placeholder="请输入邮箱验证码"
                     type="text">
              <button
                  :disabled="emailCountdown > 0"
                  class="email-code-btn"
                  @click="sendEmailCode"
              >
                {{ emailCountdown > 0 ? `${emailCountdown}秒后重发` : '获取验证码' }}
              </button>
            </div>
          </div>

          <button class="login-btn" @click="handleLogin">登录系统</button>
        </div>

        <!-- 手机登录表单 -->
        <div :class="{ active: loginType === 'PHONE' }" class="tab-content">
          <div class="form-group">
            <label for="phone">手机号码</label>
            <div class="input-with-icon">
              <svg-icon icon-class="phone"/>
              <input id="phone" v-model="loginForm.phoneNumber" placeholder="请输入手机号码" type="tel">
            </div>
          </div>

          <div class="form-group">
            <label for="phoneCode">手机验证码</label>
            <div class="input-with-icon email-code-container">
              <svg-icon icon-class="phone-captcha"/>
              <input id="phoneCode" v-model="loginForm.smsCode" class="email-code-input"
                     placeholder="请输入手机验证码"
                     type="text">
              <button
                  :disabled="phoneCountdown > 0"
                  class="email-code-btn"
                  @click="sendPhoneCode"
              >
                {{ phoneCountdown > 0 ? `${phoneCountdown}秒后重发` : '获取验证码' }}
              </button>
            </div>
          </div>

          <button class="login-btn" @click="handleLogin">登录系统</button>
        </div>

        <div class="other-options">
          <span>三方登录方式</span>
        </div>

        <div class="social-login">
          <button v-for="platform in Object.values(supportOauthPlatforms)" :key="platform" class="social-btn"
                  @click="redirectOauth(platform)">
            <svg-icon :icon-class="platform" size="26px"></svg-icon>
          </button>
        </div>

        <p style="text-align: center; color: var(--text-light); font-size: 0.85rem;">
          除演示账户外 , 用户可自行注册账号 , 平台将自动注册账号
        </p>
      </div>

      <div class="login-right">
        <div class="right-content">
          <h2>企业级开源管理系统</h2>
          <p>基于SpringBoot3+Vue3的全栈解决方案，集成AI助手、多平台文件服务、实时通讯及企业级安全体系，赋能高效管理</p>

          <div class="features">
            <div class="feature-item">
              <svg-icon icon-class="security" size="2rem"/>
              <h3>安全可靠</h3>
              <p>JWT认证、限流防刷、按钮级权限控制、NSFW 图片鉴黄、脏词过滤、数据脱敏</p>
            </div>
            <div class="feature-item">
              <svg-icon icon-class="ai-fill" size="2rem"/>
              <h3>AI智能助手</h3>
              <p>通过 Spring AI 集成 DeepSeek 智能引擎</p>
            </div>
            <div class="feature-item">
              <svg-icon icon-class="file-cloud" size="2rem"/>
              <h3>云存储支持</h3>
              <p>多平台适配，通过配置文件选择多种云存储平 ( e.g Local/MinIO/七牛云/OSS 等 )</p>
            </div>
            <div class="feature-item">
              <svg-icon icon-class="comments" size="2rem"/>
              <h3>实时协作</h3>
              <p>集群级 WebSocket 通讯 , 支持多人聊天 , 消息推送</p>
            </div>
          </div>
        </div>
        <div class="system-info">Copyright © yf.wiki All Rights Reserved. <a
            href="https://beian.miit.gov.cn/#/Integrated/index">蜀ICP备2024093013号-1</a></div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {ThemeEnum} from "@/enums/ThemeEnum";
import {useSystemStore} from "@/store/modules/system";
import {AuthAPI} from "@/api/auth";
import {LoginParams} from "@/api/auth/type";
import {LocationQuery, LocationQueryValue, useRoute} from "vue-router";
import {useUserStore} from "@/store/modules/user";
import {RouteConstant} from "@/constants/route";
import {useOauthStore} from "@/store/modules/oauth";
import {validateEmail, validatePhoneNumber} from "@/utils/validation";
import {OauthAPI} from "@/api/system/oauth";


defineOptions({
  name: "Login",
  inheritAttrs: false,
});
// 暗黑模式属性
const systemStore = useSystemStore();
const isDark = computed(() => systemStore.settings.theme === ThemeEnum.DARK);
// 路由属性
const route = useRoute()
const router = useRouter()
// 登录认证属性
const loginType = ref<string>('USERNAME_PASSWORD')    // 登录方式
const userStore = useUserStore();                     // 存储用户信息
const oauthStore = useOauthStore();                   // oauth 信息
const captchaBase64 = ref<string>('');                // USERNAME_PASSWORD 登录方式中验证码
const loginForm = ref<LoginParams>({                  // 登录表单信息
  username: "demo_admin",
  password: "12345678"
});
const emailCountdown = ref(0);
let emailCountdownTimer: ReturnType<typeof setInterval> | null = null;
const phoneCountdown = ref(0);
let phoneCountdownTimer: ReturnType<typeof setInterval> | null = null;
const supportOauthPlatforms = ref<string[]>([]) // 支持的 oauth 平台
const logging = ref<boolean>(false);

// 暗黑模式方法
function changeTheme() {
  systemStore.setTheme(isDark.value ? ThemeEnum.LIGHT : ThemeEnum.DARK);
}

// 登录认证方法
/**
 * 节流登录
 */
const throttledLoginFn = useThrottleFn(() => {
  logging.value = true;
  AuthAPI.LOGIN.request(loginType.value, loginForm.value).then(({data}) => {
    // 1. 存储 token 到 pinia 中
    userStore.setAuthInfo(data)
    // 2. 跳转页面
    const query: LocationQuery = route.query;
    // 2.1 获取 query 中的 redirect 参数 （ 是否是从其他页面重定向到当前页面 ）
    const redirect = (query.redirect as LocationQueryValue) ?? RouteConstant.HOME_PATH;
    // 2.2 获取 query 中的其他参数一并发送跳转
    let otherQueryParams = {}
    if (redirect !== RouteConstant.HOME_PATH) {
      // accumulator 用于累积计算的结果 , currentKey 表示正在被处理的数组元素
      otherQueryParams = Object.keys(query).reduce(
          (accumulator: any, currentKey: string) => {
            if (currentKey !== "redirect") {
              accumulator[currentKey] = query[currentKey];
            }
            return accumulator;
          },
          {}
      );
    }
    router.replace({path: redirect, query: otherQueryParams});
  }).catch(() => {
    // 异常则刷新验证码
    if (!(loginType.value === "PHONE" || loginType.value === "EMAIL")) {
      loginType.value = "USERNAME_PASSWORD"
      refreshCaptcha()
    }
  }).finally(() => {
    // 解除加载中
    logging.value = false;
  })
}, 1000)

/**
 * 节流刷新普通验证码
 * 手动点击比较快的时候 1s -> 6次 , 压缩为 1s -> 2次 （这样用户体验效果不会太差,也能减少服务器压力）
 */
const throttledCaptchaCodeFn = useThrottleFn(() => {
  AuthAPI.CAPTCHA_CODE.request().then(({data}) => {
    loginForm.value.verifyCodeKey = data.verifyCodeKey;
    captchaBase64.value = data.captchaImgBase64;
  })
}, 500)

/**
 * 登录处理
 */
function handleLogin() {
  // 1. 校验
  if (loginType.value === "USERNAME_PASSWORD") {
    if (!loginForm.value.username || !loginForm.value.password) {
      ElMessage.error("请输入用户名和密码");
      return;
    }
    if (!loginForm.value.verifyCode) {
      ElMessage.error("请输入验证码");
      return;
    }
    if (loginForm.value.username.length > 4 || loginForm.value.username.length < 24) {
      ElMessage.error("用户名长度必须在 4 到 24 个字符之间");
      return;
    }
    if (loginForm.value.username.length > 8 || loginForm.value.username.length < 16) {
      ElMessage.error("密码长度必须在 8 到 16 个字符之间");
      return;
    }
  } else if (loginType.value === "EMAIL") {
    if (!loginForm.value.email) {
      ElMessage.error("请输入邮箱地址");
      return;
    }
    if (!loginForm.value.emailCode) {
      ElMessage.error("请输入邮箱验证码");
      return;
    }
    if (!validateEmail(loginForm.value.email)) {
      ElMessage.error("邮箱格式不正确");
      return;
    }
  } else if (loginType.value === "PHONE") {
    if (!loginForm.value.phoneNumber) {
      ElMessage.error("请输入手机号码");
      return;
    }
    if (!loginForm.value.smsCode) {
      ElMessage.error("请输入手机验证码");
      return;
    }
    if (!validatePhoneNumber(loginForm.value.phoneNumber)) {
      ElMessage.error("手机号码格式不正确");
      return;
    }
  }
  // 2. 节流登录
  throttledLoginFn()
}

/**
 * 刷新 USERNAME_PASSWORD 验证码
 */
function refreshCaptcha() {
  throttledCaptchaCodeFn()
}

/**
 * 发送邮箱
 */
// 发送邮箱验证码
function sendEmailCode() {
  if (!loginForm.value.email) {
    ElMessage.error("请输入邮箱");
    return;
  }

  if (!validateEmail(loginForm.value.email)) {
    ElMessage.error("邮箱格式不正确");
    return;
  }

  // 防止重复发送
  if (emailCountdown.value > 0) return;

  AuthAPI.EMAIL_CODE.request(loginForm.value.email).then(() => {
    ElMessage.success("验证码发送成功");
    emailCountdown.value = 60;
    emailCountdownTimer = setInterval(() => {
      emailCountdown.value--;
      if (emailCountdown.value <= 0) {
        if (emailCountdownTimer) clearInterval(emailCountdownTimer);
      }
    }, 1000);
  }).catch(() => {
    // 发送失败处理
  });
}

/**
 * 发送手机验证码
 */
function sendPhoneCode() {
  if (!loginForm.value.phoneNumber) {
    ElMessage.error("请输入手机号码");
    return;
  }

  if (!validatePhoneNumber(loginForm.value.phoneNumber)) {
    ElMessage.error("手机号码格式不正确");
    return;
  }

  // 防止重复发送
  if (phoneCountdown.value > 0) return;

  AuthAPI.PHONE_CODE.request(loginForm.value.phoneNumber).then(() => {
    ElMessage.success("验证码发送成功");
    phoneCountdown.value = 60;
    phoneCountdownTimer = setInterval(() => {
      phoneCountdown.value--;
      if (phoneCountdown.value <= 0) {
        if (phoneCountdownTimer) clearInterval(phoneCountdownTimer);
      }
    }, 1000);
  }).catch(() => {
    // 发送失败处理
  });
}

/**
 * 跳转 oauth 登录
 * @param type 类型
 */
function redirectOauth(type: string) {
  AuthAPI.REDIRECT_LOGIN.request(type).then(({data}) => {
    window.location.href = data;
  })
}

// 生命周期
onBeforeMount(async () => {
  // 1. 设置回调地址为当前地址
  oauthStore.setOauthRedirectUri(route.path)
  // 2. 获取 route 中的登录类型
  if (oauthStore.type) {
    loginType.value = oauthStore.type;
    loginForm.value.oauth = oauthStore.params;
    await handleLogin();
  }
})

onMounted(() => {
  refreshCaptcha()
  OauthAPI.SUPPORT_PLATFORMS.request().then(({data}) => {
    supportOauthPlatforms.value = data
  })

})

onUnmounted(() => {
  oauthStore.resetOauth();
  if (emailCountdownTimer) {
    clearInterval(emailCountdownTimer);
    emailCountdownTimer = null;
  }
  if (phoneCountdownTimer) {
    clearInterval(phoneCountdownTimer);
    phoneCountdownTimer = null;
  }
});
</script>

<style lang="scss">
:root {
  --primary: #4361ee;
  --primary-dark: #3a56d4;
  --secondary: #06d6a0;
  --text: #2b2d42;
  --text-light: #8d99ae;
  --light: #e0e4e6;
  --white: #ffffff;
  --dark-bg: #121826;
  --dark-card: #1e293b;
  --dark-text: #e2e8f0;
  --dark-text-light: #94a3b8;
  --transition: all 0.3s ease;
  --shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  --shadow-dark: 0 10px 30px rgba(0, 0, 0, 0.4);
  --card-radius: 18px;
}
</style>

<style lang="scss" scoped>
// 记住 : 样式是借助 app-container 来控制暗黑模式的 ，因为两种不同模式的暗黑模式冲突了
.app-container {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea, #764ba2, #6B8DD6, #9d4be1);
  background-size: 400% 400%;
  animation: gradientBG 10s ease infinite;
  padding: 20px;
  transition: var(--transition);
}

.app-container .dark-mode {
  background: linear-gradient(135deg, #0f172a, #1e293b, #334155, #475569);
  background-size: 400% 400%;
  animation: gradientBG 10s ease infinite;
}

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.login-container {
  width: 66vw;
  display: flex;
  background: var(--white);
  border-radius: var(--card-radius);
  overflow: hidden;
  box-shadow: var(--shadow);
  transition: var(--transition);
  position: relative;
  max-height: 88vh;
}

.app-container .dark-mode .login-container {
  background: var(--dark-card);
  box-shadow: var(--shadow-dark);
}

.login-left {
  flex: 1;
  height: 88vh;
  padding: 0 25px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
}

.login-right {
  flex: 1;
  height: 88vh;
  background: linear-gradient(rgba(59, 85, 208, 0.8), rgba(55, 80, 194, 0.9)), url('@/assets/images/login-right.avif') center/cover no-repeat;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 25px;
  color: var(--white);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.app-container .dark-mode .login-right {
  background: linear-gradient(rgba(30, 41, 59, 0.8), rgba(30, 41, 59, 0.9)), url('@/assets/images/login-right.avif') center/cover no-repeat;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-header h1 {
  font-size: 1.8rem;
  color: var(--text);
  margin-bottom: 18px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
}

.login-header h1 img {
  width: 40px;
  height: 40px;
  object-fit: contain;
  display: inline-block;
}

.app-container .dark-mode .login-header h1 {
  color: var(--dark-text);
}

.login-header p {
  color: var(--text-light);
  font-size: 0.9rem;
}

.app-container .dark-mode .login-header p {
  color: var(--dark-text-light);
}

.login-tabs {
  display: flex;
  gap: 6px;
  margin-bottom: 20px;
  background: var(--light);
  border-radius: 10px;
  padding: 4px;
}

.app-container .dark-mode .login-tabs {
  background: #2d3748;
}

.tab-btn {
  flex: 1;
  padding: 8px;
  background: transparent;
  border: none;
  border-radius: 8px;
  font-size: 0.85rem;
  font-weight: 500;
  color: var(--text-light);
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.app-container .dark-mode .tab-btn {
  color: var(--dark-text-light);
}

.tab-btn.active {
  background: var(--primary);
  color: var(--white);
  box-shadow: 0 4px 10px rgba(67, 97, 238, 0.3);
}

.app-container .dark-mode .tab-btn.active {
  background: var(--primary);
  color: var(--white);
  box-shadow: 0 4px 10px rgba(67, 97, 238, 0.5);
}

.tab-content {
  display: none;
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.tab-content.active {
  display: block;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: var(--text);
  font-size: 0.9rem;
}

.app-container .dark-mode .form-group label {
  color: var(--dark-text);
}

.input-with-icon {
  position: relative;
}

.input-with-icon .svg-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-light);
  font-size: 0.95rem;
}

.app-container .dark-mode .input-with-icon i {
  color: var(--dark-text-light);
}

.input-with-icon input {
  width: 100%;
  padding: 10px 10px 10px 35px;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  font-size: 0.9rem;
  transition: var(--transition);
  background: #f9fafb;
}

.app-container .dark-mode .input-with-icon input {
  background: #2d3748;
  border-color: #4a5568;
  color: var(--dark-text);
}

.input-with-icon input:focus {
  border-color: var(--primary);
  outline: none;
  box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
}

.app-container .dark-mode .input-with-icon input:focus {
  box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.4);
}

.login-btn {
  width: 100%;
  padding: 10px;
  background: var(--primary);
  color: var(--white);
  border: none;
  border-radius: 10px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
  margin-bottom: 12px;
  box-shadow: 0 4px 12px rgba(67, 97, 238, 0.3);
}

.login-btn:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(67, 97, 238, 0.4);
}

.other-options {
  margin: 20px 0 12px;
  text-align: center;
  position: relative;
}

.other-options::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e2e8f0;
  z-index: 1;
}

.app-container .dark-mode .other-options::before {
  background: #4a5568;
}

.other-options span {
  background: var(--white);
  position: relative;
  z-index: 2;
  padding: 0 15px;
  color: var(--text-light);
  font-size: 0.9rem;
}

.app-container .dark-mode .other-options span {
  background: var(--dark-card);
  color: var(--dark-text-light);
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-bottom: 20px;
}

.social-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--light);
  border: none;
  font-size: 1rem;
  cursor: pointer;
  transition: var(--transition);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.app-container .dark-mode .social-btn {
  background: #2d3748;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.theme-toggle {
  position: absolute;
  top: 15px;
  right: 15px;
  background: var(--light);
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: var(--shadow);
  transition: var(--transition);
  z-index: 10;
}

.app-container .dark-mode .theme-toggle {
  background: #2d3748;
}

.theme-toggle:hover {
  transform: rotate(15deg);
}

.theme-toggle i {
  font-size: 1.2rem;
  color: var(--text);
  transition: var(--transition);
}

.app-container .dark-mode .theme-toggle i {
  color: var(--dark-text);
}

.right-content {
  position: relative;
  z-index: 2;
}

.right-content h2 {
  font-size: 1.7rem;
  margin-bottom: 15px;
  font-weight: 700;
}

.right-content p {
  font-size: 0.9rem;
  line-height: 1.5;
  margin-bottom: 25px;
  max-width: 450px;
  font-weight: 300;
  text-align: center;
  margin-left: auto;
  margin-right: auto;
}

.features {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  justify-content: center;
  margin-top: 25px;
}

.feature-item {
  background: rgba(255, 255, 255, 0.1);
  padding: 15px;
  border-radius: 10px;
  width: calc(50% - 8px);
  text-align: center;
  transition: var(--transition);
  backdrop-filter: blur(5px);
  cursor: pointer;
}

.feature-item:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.2);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
}

.feature-item .svg-icon {
  margin-bottom: 12px;
  color: var(--secondary);
}

.feature-item h3 {
  font-size: 1rem;
  margin-bottom: 8px;
  font-weight: 600;
}

.feature-item p {
  font-size: 0.8rem;
  margin-bottom: 0;
  opacity: 0.9;
}

.system-info {
  position: absolute;
  bottom: 15px;
  left: 0;
  right: 0;
  text-align: center;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.75rem;
  z-index: 2;
}

.email-code-container {
  display: flex;
  align-items: stretch;
  gap: 0;
}

.email-code-input {
  flex: 1;
  border-top-right-radius: 0 !important;
  border-bottom-right-radius: 0 !important;
  height: 100%;
}

.email-code-btn {
  white-space: nowrap;
  padding: 10px 12px;
  background: var(--primary);
  color: var(--white);
  border: none;
  border-radius: 0 10px 10px 0;
  font-size: 0.85rem;
  cursor: pointer;
  transition: var(--transition);
  min-width: 100px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.email-code-btn:disabled {
  background: var(--text-light);
  cursor: not-allowed;
}

@media (max-width: 992px) {
  .login-container {
    width: 90vw;
    max-height: 95vh;
    border-radius: var(--card-radius);
  }

  .login-left {
    height: auto;
    padding: 25px 20px;
  }

  .login-right {
    display: none;
  }
}

@media (max-width: 768px) {
  .login-container {
    width: 95vw;
  }

  .login-tabs {
    flex-wrap: wrap;
  }

  .tab-btn {
    flex: 1 0 calc(50% - 4px);
    font-size: 0.8rem;
    padding: 6px;
  }

  .login-header h1 {
    font-size: 1.6rem;
  }

  .email-code-btn {
    min-width: 90px;
    font-size: 0.8rem;
    padding: 8px 10px;
  }
}

@media (max-width: 480px) {
  .app-container {
    padding: 10px;
  }

  .login-header {
    margin-bottom: 6px;
  }

  .login-header h1 {
    margin-bottom: 12px;
  }

  .login-container {
    width: 100vw;
    border-radius: var(--card-radius);
  }

  .login-left {
    padding: 20px 15px;
  }

  .login-header h1 {
    font-size: 1.5rem;
  }

  .form-group {
    margin-bottom: 12px;
  }

  .tab-btn {
    flex: 1 0 100%;
  }

  .email-code-btn {
    min-width: 80px;
    font-size: 0.75rem;
    padding: 12px;
  }
}

@media (min-width: 1920px) {
  .login-container {
    width: 80vw;
    max-width: 1800px;
  }

  .login-header h1 {
    font-size: 2.2rem;
  }

  .login-header p {
    font-size: 1.1rem;
  }

  .tab-btn {
    font-size: 1rem;
    padding: 12px;
  }

  .form-group label {
    font-size: 1.1rem;
  }

  .input-with-icon input {
    font-size: 1.1rem;
    padding: 12px 12px 12px 40px;
  }

  .login-btn {
    font-size: 1.1rem;
    padding: 12px;
  }

  .other-options span {
    font-size: 1.1rem;
  }

  .social-btn {
    width: 48px;
    height: 48px;
  }

  .right-content h2 {
    font-size: 2.2rem;
  }

  .right-content p {
    font-size: 1.1rem;
    max-width: 600px;
  }

  .feature-item {
    padding: 20px;
  }

  .feature-item h3 {
    font-size: 1.2rem;
  }

  .feature-item p {
    font-size: 1rem;
  }

  .system-info {
    font-size: 0.9rem;
  }
}

@media (min-width: 2560px) {
  .login-container {
    width: 70vw;
    max-width: 2000px;
  }

  .login-header h1 {
    font-size: 2.5rem;
  }

  .login-header p {
    font-size: 1.2rem;
  }

  .tab-btn {
    font-size: 1.1rem;
    padding: 14px;
  }

  .form-group label {
    font-size: 1.2rem;
  }

  .input-with-icon input {
    font-size: 1.2rem;
    padding: 14px 14px 14px 45px;
  }

  .login-btn {
    font-size: 1.2rem;
    padding: 14px;
  }

  .other-options span {
    font-size: 1.2rem;
  }

  .social-btn {
    width: 52px;
    height: 52px;
  }

  .right-content h2 {
    font-size: 2.5rem;
  }

  .right-content p {
    font-size: 1.2rem;
    max-width: 700px;
  }

  .feature-item {
    padding: 25px;
  }

  .feature-item h3 {
    font-size: 1.3rem;
  }

  .feature-item p {
    font-size: 1.1rem;
  }

  .system-info {
    font-size: 1rem;
  }
}

</style>
