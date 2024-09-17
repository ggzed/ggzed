import Draggable from 'vuedraggable';
import type {App} from "vue";

/**
 * 注册 VueJsonPretty 解析 json
 * @param app
 */
export function setupVueDraggable(app: App<Element>) {
    app.component("vue-draggable", Draggable)
}
