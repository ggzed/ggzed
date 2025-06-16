import {AxiosPromise} from "axios";

/**
 * KEY 主键类型 , T 表单类型
 */
export function useCrudActions<KEY, T>(
    // 新增数据 API
    saveDataApi?: (data: T) => AxiosPromise<number | string>,
    // 修改数据 API
    updateDataApi?: (id: KEY, data: T) => AxiosPromise<void>,
    // 删除数据 API
    deleteDataApi?: (ids: string) => AxiosPromise<void>,
    // 修改数据状态 API
    updateStatusApi?: (id: KEY, status: boolean) => AxiosPromise<void>,
) {

    /**
     * 弹窗确认操作
     * @param message 消息
     */
    async function confirmAction(message: string): Promise<void> {
        await ElMessageBox.confirm(message, '警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            draggable: true,
            dangerouslyUseHTMLString: true,
        });
    }

    /**
     * 通用操作处理方法
     * 根据是否需要确认弹窗调用不同的逻辑
     */
    async function handleAction(
        apiFn: () => AxiosPromise | undefined,
        message: string | null, // 如果有 message 则需要确认操作
        successMessage: string,
        onSuccess?: () => void,
        onError?: (error: any) => void
    ) {
        try {
            // 如果有确认消息，则弹窗确认
            if (message) {
                await confirmAction(message);
            }
            // 调用 API 方法
            await apiFn();
            // 通知成功
            ElMessage.success(successMessage);
            if (onSuccess) onSuccess();
        } catch (error) {
            console.error('操作失败', error);
            if (onError) onError(error);
        }
    }

    /**
     * 修改数据状态
     * @param id 数据 id
     * @param name 弹窗提示名
     * @param status 修改后的状态
     * @param onSuccess 成功回调
     * @param onError 失败回调
     */
    async function updateDataStatus(
        id: KEY,
        name: string,
        status: boolean,
        onSuccess?: () => void,
        onError?: (error: any) => void
    ) {
        const message = `是否确认修改名称为【<b>${name}</b>】的数据项状态?`;
        await handleAction(() => updateStatusApi?.(id, status), message, '修改数据状态成功', onSuccess, onError);
    }

    /**
     * 删除数据
     * @param ids 删除数据的 ids 集合
     * @param name 弹窗提示名
     * @param onSuccess 成功回调
     * @param onError 失败回调
     */
    async function deleteData(
        ids: Array<KEY>,
        name?: string,
        onSuccess?: () => void,
        onError?: (error: any) => void
    ) {
        const message = name
            ? `是否确认删除名称为【<b>${name}</b>】的数据项?`
            : `是否确定删除所选数据项?`;
        await handleAction(() => deleteDataApi?.(ids.join(',')), message, '删除数据成功', onSuccess, onError);
    }

    async function saveData(data: T, onSuccess?: () => void, onError?: (error: any) => void): Promise<void> {
        await handleAction(() => saveDataApi?.(data), null, '新增数据成功', onSuccess, onError);
    }

    async function updateData(id: KEY, data: T, onSuccess?: () => void, onError?: (error: any) => void): Promise<void> {
        await handleAction(() => updateDataApi?.(id, data), null, '修改数据成功', onSuccess, onError);
    }

    return {
        handleAction,
        saveData,
        updateData,
        deleteData,
        updateDataStatus,
    };
}
