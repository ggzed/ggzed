import {ref} from 'vue';

export function useDialogManage() {
    const visible = ref(false); // 是否可见
    const title = ref('');      // 对话框标题
    const loading = ref(true);  // 加载状态

    /**
     * 开启对话框
     * @param dialogTitle 标题
     * @param callback 可选的回调函数
     */
    const openDialog = (dialogTitle: string, callback?: () => void) => {
        try {
            loading.value = true;
            // 先执行可选的回调函数
            if (callback && typeof callback === 'function') {
                callback();
            }
            // 后打开对话框
            visible.value = true;
            title.value = dialogTitle;
        } catch (err) {
            console.error(err);
            loading.value = false;
        }
    };

    /**
     * 关闭对话框
     * @param callback 可选的回调函数
     */
    const closeDialog = (callback?: () => void) => {
        // 先关闭对话框
        visible.value = false;
        // 后执行可选的回调函数
        if (callback && typeof callback === 'function') {
            callback();
        }
    };

    return {
        visible,
        title,
        loading,
        openDialog,
        closeDialog,
    };
}
