<template>
  <div v-loading="logging" class="login-container">
    <!-- 登录头部 -->
    <div class="login-header">
      <div>登录页面后续会更改</div>
      <div>
        <div class="settings-options">
          <span>暗黑模式:</span>
          <div class="theme-settings">
            <el-switch
                v-model="isDark"
                :active-action-icon="Sunny"
                :inactive-action-icon="Moon"
                @change="changeTheme"
            />
          </div>
        </div>
      </div>
    </div>
    <!--  登录表单  -->
    <div class="login-form" @submit.prevent="handleLogin">
      <form class="form">
        <div class="title">Welcome,<br><br><span>Login in to continue</span></div>
        <el-tabs v-model="loginType">
          <el-tab-pane label="用户名密码登录" name="USERNAME_PASSWORD">
            <input v-model.trim.lazy="loginForm.username" :required="loginType == 'USERNAME_PASSWORD'" class="input"
                   minlength="4" name="username"
                   placeholder="用户名"
                   type="text">
            <input v-model.trim.lazy="loginForm.password" :required="loginType == 'USERNAME_PASSWORD'" class="input"
                   minlength="6"
                   name="password"
                   placeholder="密码"
                   type="password">
            <div class="form-code">
              <input v-model.trim.lazy="loginForm.verifyCode" :required="loginType == 'USERNAME_PASSWORD'" class="input"
                     minlength="4" name="code"
                     placeholder="验证码"
                     type="text">
              <div class="button-code" @click="refreshCaptcha">
                <img :src="captchaBase64" alt="验证码">
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="邮箱登录" name="EMAIL">
            <input v-model.trim.lazy="loginForm.email" :required="loginType == 'EMAIL'" class="input" minlength="5"
                   name="email"
                   placeholder="邮箱"
                   type="email">
            <div class="form-code">
              <input v-model.trim.lazy="loginForm.emailCode" :required="loginType == 'EMAIL'" class="input"
                     minlength="4"
                     name="code"
                     placeholder="邮箱验证码"
                     type="text">
              <div class="button-code" @click="sendEmail(loginForm.email || '')">
                <b>发送验证码</b>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="手机号登录" name="PHONE">
            <input v-model.trim.lazy="loginForm.phoneNumber" :required="loginType == 'PHONE'" class="input"
                   minlength="7" name="phone"
                   placeholder="手机号"
                   type="text">
            <div class="form-code">
              <input v-model.trim.lazy="loginForm.smsCode" :required="loginType == 'PHONE'" class="input" minlength="4"
                     name="code"
                     placeholder="短信验证码"
                     type="text">
              <div class="button-code">
                <b>发送验证码</b>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        <!--   第三方登录     -->
        <div class="login-with">
          <div v-for="platform in Object.values(supportOauthPlatforms)" :key="platform" class="button-log">
            <svg-icon :icon-class="platform" size="26px" @click="redirectOauth(platform)"></svg-icon>
          </div>
        </div>
        <button class="button-confirm">Let`s go →</button>
      </form>
    </div>
  </div>
</template>

<script lang="ts" setup>
// 数据
import {Moon, Sunny} from "@element-plus/icons-vue";
import {ThemeEnum} from "@/enums/ThemeEnum";
import {useSystemStore} from "@/store/modules/system";
import {AuthAPI} from "@/api/auth";
import {LoginParams} from "@/api/auth/type";
import {LocationQuery, LocationQueryValue, useRoute} from "vue-router";
import {useUserStore} from "@/store/modules/user";
import {RouteConstant} from "@/constants/route";
import {useOauthStore} from "@/store/modules/oauth";
import {validateEmail} from "@/utils/validation";
import {OauthAPI} from "@/api/system/oauth";


defineOptions({
  name: "Login",
  inheritAttrs: false,
});
// 暗黑模式属性
const systemStore = useSystemStore();
const isDark = ref<boolean>(systemStore.settings.theme === ThemeEnum.LIGHT);
// 路由属性
const route = useRoute()
const router = useRouter()
// 登录认证属性
const loginType = ref<string>('USERNAME_PASSWORD') // 登录方式
const userStore = useUserStore();                     // 存储用户信息
const oauthStore = useOauthStore();                   // oauth 信息
const captchaBase64 = ref<string>('');                // USERNAME_PASSWORD 登录方式中验证码
const loginForm = ref<LoginParams>({                  // 登录表单信息
  username: "demo_admin",
  password: "12345678"
});
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
  // 1. TODO 校验 ( 等待首页页面更改 )

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
function sendEmail(email: string) {
  if (validateEmail(email)) {
    AuthAPI.EMAIL_CODE.request(email).then(() => {
      ElMessage.success("邮箱发送成功,请耐心等待")
    })
  } else {
    ElMessage.error("邮箱格式不正确")
  }

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
})
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.login-header {
  display: flex;
  justify-content: space-around;
  height: $navbar-height;
  line-height: $navbar-height;
  font-weight: 700;
  font-size: 16px;
}

.login-form {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  width: 100vw;
}

.form {
  --input-focus: #2d8cf0;
  --font-color: #323232;
  --font-color-sub: #666;
  --bg-color: beige;
  --main-color: black;
  padding: 36px;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  gap: 20px;
  border-radius: 5px;
  border: 2px solid var(--main-color);
  box-shadow: 4px 4px var(--main-color);

  :deep(.el-tab-pane) {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 20px;
    padding: 16px 0;
  }

  :deep(.el-tabs__active-bar) {
    background-color: var(--main-color);
  }

  :deep(.el-tabs__item.is-active) {
    color: inherit;
  }

  :deep(.el-tabs__item:hover) {
    color: var(--el-color-info);
  }
}

.title {
  color: var(--font-color);
  font-weight: 900;
  font-size: 20px;
}

.title span {
  color: var(--font-color-sub);
  font-weight: 600;
  font-size: 17px;
}

.input {
  width: 280px;
  height: 40px;
  border-radius: 5px;
  border: 2px solid var(--main-color);
  background-color: var(--bg-color);
  box-shadow: 4px 4px var(--main-color);
  font-size: 15px;
  font-weight: 600;
  color: var(--font-color);
  padding: 5px 10px;
  outline: none;
}

.input::placeholder {
  color: var(--font-color-sub);
  opacity: 0.8;
}

.input:focus {
  border: 2px solid var(--input-focus);
}

.login-with {
  display: flex;
  width: 100%;
  justify-content: center;
  gap: 20px;
}

.button-log {
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 100%;
  border: 2px solid var(--main-color);
  background-color: var(--bg-color);
  box-shadow: 4px 4px var(--main-color);
  color: var(--font-color);
  font-size: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon {
  width: 24px;
  height: 24px;
  fill: var(--main-color);
}

.button-log:active, .button-confirm:active {
  box-shadow: 0 0 var(--main-color);
  transform: translate(3px, 3px);
}

.button-confirm {
  margin: 20px auto 0 auto;
  width: 120px;
  height: 40px;
  border-radius: 5px;
  border: 2px solid var(--main-color);
  background-color: var(--bg-color);
  box-shadow: 4px 4px var(--main-color);
  font-size: 17px;
  font-weight: 600;
  color: var(--font-color);
  cursor: pointer;
}

.settings-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  gap: 20px;
}

.form-code {
  display: flex;
  width: 280px;
  justify-content: space-between;
  gap: 20px;

  > input {
    width: 160px;
  }

  > .button-code {
    img {
      height: 100%;
      width: 100%;
    }
  }
}

.button-code {
  cursor: pointer;
  width: 100px;
  height: 40px;
  overflow: hidden;
  border: 2px solid var(--main-color);
  background-color: var(--bg-color);
  box-shadow: 4px 4px var(--main-color);
  color: var(--font-color);
  display: flex;
  justify-content: center;
  align-items: center;
}

.button-code:active {
  box-shadow: 0 0 var(--main-color);
  transform: translate(3px, 3px);
}
</style>
