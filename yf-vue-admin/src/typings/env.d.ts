/// <reference types="vite/client" />
// https://cn.vitejs.dev/guide/env-and-mode

declare module "*.vue" {
    import { DefineComponent } from "vue";
    const component: DefineComponent<{}, {}, any>;
    export default component;
}
interface ImportMetaEnv {
    readonly VITE_APP_PORT: number
    readonly VITE_APP_API_URL : string
    // 更多环境变量...
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}

/**
 * 平台的名称、版本、运行所需的`node`版本、依赖、构建时间的类型提示
 */
declare const __APP_INFO__: {
    pkg: {
        name: string;
        version: string;
    };
};

