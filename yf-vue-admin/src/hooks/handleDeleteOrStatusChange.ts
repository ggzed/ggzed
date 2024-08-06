import {AxiosResponse} from "axios";

export function useHandleDeleteOrStatusChange(
    updateStatusApi?: (id: any, status: boolean) => Promise<AxiosResponse<void>>,
    deleteDataApi?: (ids: any) => Promise<AxiosResponse<void>>,
    loadData?: () => Promise<void>
) {

    /**
     * 处理简单的修改状态/删除
     * @param ids id集合
     * @param name 数据名
     * @param status 数据修改状态
     */
    async function handleDeleteOrStatusChange(ids: any[], name?: string, status?: boolean) {
        let elMessageBoxTitle = "是否确定删除所选数据项?"
        if (typeof status === 'boolean') {
            elMessageBoxTitle = `是否确认修改名称为【<b>${name}</b>】的数据项状态?`
        } else if (name) {
            elMessageBoxTitle = `是否确认删除名称为【<b>${name}</b>】的数据项?`
        }

        try {
            await ElMessageBox.confirm(elMessageBoxTitle, '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                draggable: true,
                dangerouslyUseHTMLString: true
            });

            if (typeof status === 'boolean') {
                if (updateStatusApi) {
                    await updateStatusApi(ids[0], status);
                    ElMessage.success('修改数据状态成功');
                }
            } else {
                if (deleteDataApi) {
                    await deleteDataApi(ids.join(','));
                    ElMessage.success('删除数据成功');
                }
            }
            // 重新加载数据
            if (loadData) {
                await loadData();
            }
        } catch (error) {
            console.log('Action cancelled or failed', error);
        }
    }

    return {
        handleDeleteOrStatusChange
    }
}
