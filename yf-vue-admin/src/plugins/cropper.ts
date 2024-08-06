import type {App} from "vue";
import VueCropper from 'vue-cropper';
import 'vue-cropper/dist/index.css';

// 注册 VueCropper
export function setupVueCropper(app: App<Element>) {
    app.use(VueCropper)
}
