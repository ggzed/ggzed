export function useDialogManagement() {
    const dialog = reactive({visible: false, title: '', loading: true});
    /**
     * 开启对话框
     * @param title 标题
     * @param callback 可选的回调函数
     */
    const openDialog = (title: string, callback?: () => void) => {
        try {
            dialog.loading = true;
            // 先执行可选的回调函数
            if (callback && typeof callback === 'function') {
                callback();
            }
            // 后打开对话框
            dialog.visible = true;
            dialog.title = title;
        } catch (err) {
            console.log(err)
            dialog.loading = false;
        }
    };
    /**
     * 关闭对话框
     * @param callback 可选的回调函数
     */
    const closeDialog = (callback?: () => void) => {
        // 先关闭对话框
        dialog.visible = false;
        // 后执行可选的回调函数
        if (callback && typeof callback === 'function') {
            callback();
        }
    };

    return {
        dialog,
        openDialog,
        closeDialog
    };
}
