import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';
import type {App} from "vue";

/**
 * 注册 VueJsonPretty 解析 json
 * @param app
 */
export function setupVueJsonPretty(app: App<Element>) {
    app.component("vue-json-pretty", VueJsonPretty)
}
