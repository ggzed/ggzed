<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">YF 后台管理系统</h1>
<p align="center">
	<a href="https://gitee.com/fateyifei/yf/stargazers"><img src="https://gitee.com/fateyifei/yf/badge/star.svg?theme=dark"></a>
	<a href="https://gitee.com/fateyifei/yf"><img src="https://img.shields.io/badge/YF-v1.0.0-brightgreen.svg"></a>
	<a href="https://gitee.com/fateyifei/yf/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>
<p align="center" >
<a href="http://yf.wiki/yf-vue-admin" target="_blank">👀 在线预览</a>
 | <a href="#" target="_blank">📖 项目文档</a>
</p>

## 项目简介

- 项目采用多种流行技术 , 其中技术栈包含但不限于
    - SpringBoot3 、SpringSecurity6 、SpringAi、SpringEmail 、SpringCache 、MybatisPlus...
    - Vue3、TypeScript、ElementPlus、VueRouter、Pinia...
    - Mysql、Redis、Minio、Caffeine...
    - TensorFlow、JustAuth、ffmpeg、test4j、sensitive-word、vosk...
    - Alipay、OSS、COS、OBS....

- 项目为学习性后台管理系统 , 有诸多流行技术 , 持续更新 , 免费远程答疑 ,
  可根据基础版 [yf-vue3-admin-base](https://gitee.com/fateyifei/yf-vue3-admin-base) 修改为任意系统。

## 项目特色

- 简单易学 : 在项目关注度高时会在 **bilibili 录制项目讲解视频**
- 授权登录 : 采用JWT颁发授权信息，支持刷新Token，二次封装JustAuth并且通过简单工厂模式+模版方法模式结合SpringSecurity进行多种登录方式。
- 安全可用 : Redis多规则限流、Redis分布式防重复提交、AOP日志处理、数据脱敏、脏词过滤...
- 基础设施完善 : 用户、第三方授权、角色、菜单、字典、部门、日志等完善的权限系统功能。
- 文件模块 : 支持多种平台文件上传MinIo、OSS、COS、OBS....
- AI助手 : 使用SpringAi对接ChatGPT、ollama...
- 聊天室 : 支持集群环境下websocket通讯
- 丰富工具库 : TreeUtil(一行代码构建树形结构) 、TensorFlow 模型访问 、ip解析具体地址...
- 丰富有趣功能 : OCR、ffmpeg、脏词过滤、字幕识别...

## 项目展示

| 地址      | 链接                                                                            |
|---------|-------------------------------------------------------------------------------|
| 网站      | <a href="http://8.137.57.215/yf-vue-admin" target="_blank">yf-vue-admin</a> |
| Gitee源码 | <a href="https://gitee.com/fateyifei/yf" target="_blank">YF</a>               |

## 基础项目页面介绍

所有页面均支持手机、平板、PC

**个人中心**

![PC端项目截图](https://foruda.gitee.com/images/1722956633903082210/719fc801_9026884.png)

**RBAC 模型控制**

![RBAC 模型控制](https://foruda.gitee.com/images/1732125036179701847/e955af66_9026884.png)

**在线聊天室**

![在线聊天室](https://foruda.gitee.com/images/1732125140129842446/ee380e0d_9026884.png)

**操作日志**

![操作日志](https://foruda.gitee.com/images/1732125190079913632/f8592b40_9026884.png)

### 有趣的小功能

**每日热点推荐**

![每日热点推荐](https://foruda.gitee.com/images/1732125235276043992/971c1446_9026884.png)

**每日图片推荐**

![每日图片推荐](https://foruda.gitee.com/images/1732125348620006400/fac03d36_9026884.png)

**定制头像**

![定制头像](https://foruda.gitee.com/images/1732125386236066059/44cf7351_9026884.png)

**图片内容安全检测**

![图片内容安全检测](https://foruda.gitee.com/images/1732125421589284928/4082908e_9026884.png)

**Echarts Hooks**

![Echarts Hooks](https://foruda.gitee.com/images/1732125467813935371/9dac9df9_9026884.png)

**二维码生成**

![二维码生成](https://foruda.gitee.com/images/1732125503989032826/69993cee_9026884.png)

### 平板

**每日热点**

![输入图片说明](https://foruda.gitee.com/images/1729154127712097341/1f20baab_9026884.png "屏幕截图")

### 移动端

**个人主页**

![移动端项目截图](https://foruda.gitee.com/images/1722956662136702771/e725e761_9026884.png)

**Echarts动态修改**

![输入图片说明](https://foruda.gitee.com/images/1729154246629816063/80cba525_9026884.png "屏幕截图")

## 项目文档更新

为方便大家快速掌握项目内容，我整理了非常详尽的文档库，涵盖了前后端开发的各个方面。项目中可能遇到的难点、常见问题，基本都能在这里找到解决方案。但是肯定还有未涉及的细节或问题我会慢慢添加上，欢迎催更！你也可以在开源社区中加我私聊，我每天都在线，乐于解答大家的疑问。您的支持是我最大的动力!!!

### 前端开发文档

* [🎨 想弄一个节日头像，结果全是广告！带你用 Canvas 自己制作节日头像](https://juejin.cn/post/7419223935005605914)
* [🔐 全面解析后台管理系统前端如何实现权限控制 ( 全流程实战 )](https://juejin.cn/post/7415653658707329051)
* [🌐 快速打造动态网站首页：免费 API 集成指南](https://juejin.cn/post/7415751335490781222)
* [💻 快速掌握 Vue3 + Element Plus 一键换肤功能，轻松切换主题](https://juejin.cn/post/7411080402272026650)
* [👥 一文带你入门 SSE | 使用 useEventSource 实现在线用户统计功能](https://juejin.cn/post/7410965438044012582)
* [📊 告别冗余代码：带你封装一个 ECharts Hook](https://juejin.cn/post/7410798727620771850)
* [💡 API 封装技巧：用 Axios 实现高效前端请求管理](https://juejin.cn/post/7407733722881933347)
* [🔄 告别频繁登录：教你用 Axios 实现无感知双 Token 刷新](https://juejin.cn/post/7406992576513589286)
* [🔧 开源 | 自研前端 Vue3 项目的基础模版](https://juejin.cn/post/7362783257257721856)

### 后端开发文档

* [🌐 放弃 Websocket，使用 SSE 轻松实现高效功能](https://juejin.cn/post/7410798727622311946)
* [🌳 一行代码搞定平面数据转树形结构 | 适用于多级菜单等场景](https://juejin.cn/post/7410211257125863451)
* [🔒 Hutool 实现优雅数据脱敏](https://juejin.cn/post/7408847526299418664)
* [📧 SpringBoot 实现邮箱验证码：最佳实践解析](https://juejin.cn/post/7408847526298435624)
* [📂 优雅设计多平台文件上传功能](https://juejin.cn/post/7408759813851611176)
* [🤖 SpringBoot 集成 TensorFlow：图片内容安全检测](https://juejin.cn/post/7408759813850791976)
* [📝 Java 轻松实现 OCR：SpringBoot 集成 Tess4j](https://juejin.cn/post/7408619644195766272)
* [🔄 Redis 集群负载均衡中的 WebSocket 在线通信解决方案](https://juejin.cn/post/7402431315784728610)
* [🔐 一篇文章搞定 SpringBoot 的 Jwt 无感刷新与单账号登录](https://juejin.cn/post/7401144423562625076)
* [🚦 Redis 如何多规则限流与防重复提交](https://juejin.cn/post/7401053200948183055)
* [🔑 Spring Security 源码解析：多种登录方式实现](https://juejin.cn/post/7401027594231480360)
* [📄 AOP 优雅记录 SpringBoot 日志信息](https://juejin.cn/post/7298999306945396748)

### 辅助工具文档

* [🌍 内网穿透神器：免去每次给客户重部署的烦恼](https://juejin.cn/post/7415911893426749440)
* [📊 设计 RESTful API 的最佳实践](https://juejin.cn/post/7418072992846594058)

## 项目运行

### 项目环境

| 环境    | 版本  |
|-------|-----|
| Java  | 17+ |
| Maven | 3+  |
| Node  | 18+ |
| Redis | 5+  |
| Mysql | 5+  |

### 运行命令

```shell
## 将项目下载到本地
git clone https://gitee.com/fateyifei/yf.git
```

### yf-vue-admin ( 前端服务 )

- 前提 : 需要有node环境,系统中安装过yarn

```shell
## 已经安装 yarn 则可不执行
npm install yarn -g
## 切换目录
cd yf/yf-vue-admin
## 安装依赖
yarn install
## 启动项目
yarn run dev

## 接下来会自动打开项目到网页 , 如果未部署后端服务请设置 .env.production 中 VITE_APP_API_URL = http://8.137.57.215/api
```

### yf-boot-admin ( 后端服务 )

- 前提 : 已经配置好 java17、 maven 、redis 、mysql，并且mysql数据库已经导入
- 注意 : 导入数据库后默认管理员账户 => 用户名 : admin 密码 : 12345678

```shell
## 切换目录
cd yf/yf-boot-admin

## 打包
mvn clean package -DskipTests

## 在yf/yf-boot-admin/yf-admin 下找到 jar 包并运行
java -jar target/yf-admin-<version>.jar > xx.log 2>&1 &

```

## 项目部署

```shell
## 前端项目 ( .env.production 可修改部署配置 )
yarn run build
## 后端项目 ( application-prod 可修改部署配置信息  )
mvn clean package -DskipTests
## nginx 配置
### 项目存储在 ( nginx/html/{.env.production中VITE_APP_PUBLIC_PATH}下 , 可以直接修改 dist 文件夹名 )
server {
    # 监听端口80
    listen 80;
    server_name localhost;
    
    # SPA项目 -> yf-vue-admin
    location /yf-vue-admin/ {
        root /www/server/nginx/html;
        index index.html;
        try_files $uri $uri/ /yf-vue-admin/index.html;
    }
    
    # 后台服务 -> yf-boot-admin
    location /api {
          # 后端服务器地址(您的后端地址)
          proxy_pass ????;  
          
          # 设置转发给后端的请求头
          # 使用客户端请求的主机名作为后端请求的Host头
          proxy_set_header Host $host;  
          # 将客户端真实IP地址传递给后端
          proxy_set_header X-Real-IP $remote_addr;  
          # 将客户端和代理服务器链的IP地址传递给后端
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          # 传递使用的协议（HTTP或HTTPS）
          proxy_set_header X-Forwarded-Proto $scheme;  
          # 传递请求的主机名
          proxy_set_header X-Forwarded-Host $host;  
          # 传递请求的服务器端口
          proxy_set_header X-Forwarded-Port $server_port;  
          
          # WebSocket 连接
          # 使用HTTP/1.1协议以支持WebSocket
          proxy_http_version 1.1;  
          # 处理WebSocket协议升级请求
          proxy_set_header Upgrade $http_upgrade;  
          # 处理WebSocket协议升级请求
          proxy_set_header Connection "Upgrade";  
          
          # SSE 连接
          # 禁用代理缓冲以支持Server-Sent Events (SSE)
          proxy_buffering off;  
          # 请求速率限制
          # 设置请求速率限制
          #limit_req zone=one burst=20 nodelay;  
    }
}
### 后端项目任意位置运行 , 请记住端口以及地址 ( 需要服务器开发地址访问权限 ， 后续通过 nginx 进行代理 )
java -jar target/yf-admin-<version>.jar > ??.log 2>&1 &

```

## 项目注意事项

### 提交规范

| 前缀           | 描述                        |
|--------------|---------------------------|
| feat: ✨      | 新功能                       |
| fix: 🐛      | bug修复                     |
| docs: 📚     | 仅文档更改                     |
| style: 🌈    | 不影响代码含义的更改（空白、格式设置、缺失分号等） |
| refactor: ♻️ | 既不修复bug也不添加特性的代码更改        |
| perf: 🚀     | 改进性能的代码更改                 |
| test: 🧪     | 添加缺少的测试或更正现有测试            |
| chore: 🔨    | 对构建过程或辅助工具和库（如文档）的更改      |
| revert: ↩️   | 回滚 commit                 |

### 登录/注册功能

- 后台管理系统一般不会有注册，但是项目为一人一号不方便在线演示，所以您可以通过邮箱、第三方平台进行登录体验

### 部分功能只有后端接口

- 由于家中突发情况，项目先行开源，部分功能仅有后端实现，前端页面并未编写

## 交流群 🚀

欢迎 start , 欢迎交流技术 , 如果有任何不懂的地方可以联系我 , 可以无偿帮您解决 , 相当于大家互相交流学习

QQ群 ： 957226269

微信 （加好友后邀请进群）：

<img src="https://foruda.gitee.com/images/1714351435556602409/7b1d097c_9026884.jpeg" width="235" height="220" alt="微信号"> 
