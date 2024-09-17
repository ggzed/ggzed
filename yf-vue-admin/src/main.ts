import {createApp} from 'vue'
import App from './App.vue'
import "./styles/index.css" // 全局样式
import "animate.css"; // 引入 animate.css
import "virtual:svg-icons-register"; // 本地SVG图标
import "element-plus/theme-chalk/dark/css-vars.css"; // element - dark 样式
import {setupElIcons, setupVueCropper, setupVueDraggable, setupVueJsonPretty} from "./plugins";
import {setupStore} from "@/store";
import {setupRouter} from "@/router";
import {setupDirective} from "@/directive";


const app = createApp(App);
setupDirective(app);    // 全局指令
setupVueDraggable(app)  // 全局注册vue-draggable
setupVueCropper(app);   // 全局注册vue-cropper
setupVueJsonPretty(app) // 全局注册vue-json-pretty
setupElIcons(app);      // 全局注册Element-plus图标
setupStore(app);        // 状态管理
setupRouter(app);       // router
app.mount("#app");
