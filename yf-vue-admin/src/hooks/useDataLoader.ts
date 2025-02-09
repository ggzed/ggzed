import {Ref} from "vue/dist/vue";
import {AxiosResponse} from "axios";

/**
 * 数据加载器
 * T : 响应数据类型
 */
interface DataLoader<T, E> {
    /**
     * 分页查询
     */
    query: E;
    /**
     * 数据集合
     */
    dataList: Ref<T[]>;
    /**
     * 分页总条数
     */
    total: Ref<number>;
    /**
     * 数据是否正在加载
     */
    loading: Ref<boolean>;
    /**
     * 加载数据函数
     */
    loadData: (callback?: () => void) => Promise<void>;
    /**
     * 重置查询条件
     */
    resetQuery: (callback?: () => void) => Promise<void>;
}

/**
 * 数据加载器 :
 */
export function useDataLoader<T, E>(fetchData: (...params: any) => Promise<AxiosResponse<any>>, initialQuery: E): DataLoader<T, E> {
    const dataList = ref<T[]>([]) as Ref<T[]>;
    const total = ref(0);
    const loading = ref(false);
    const query = reactive({...initialQuery}) as E;

    /**
     * 处理回调函数
     * @param callback 回调函数
     */
    function handleCallback(callback?: () => void): void {
        if (callback && typeof callback === 'function') {
            callback();
        }
    }

    /**
     * 加载数据
     * @param callback 数据更新后的回调
     */
    async function loadData(callback?: () => void): Promise<void> {
        loading.value = true;
        try {
            const {data} = await fetchData(query);
            if (Array.isArray(data)) {
                // data 是一个数组 (无分页)
                dataList.value = data;
                total.value = data.length;
            } else if (data.list) {
                // data 是分页数据
                dataList.value = data.list;
                total.value = data.total || 0;
            } else {
                console.error('Unexpected data format');
            }
            handleCallback(callback);
        } catch (error) {
            console.error('Failed to load data:', error);
            throw error; // 抛出错误让调用者处理
        } finally {
            loading.value = false;
        }
    }

    /**
     * 重置查询条件
     * @param callback 重置回调
     */
    async function resetQuery(callback?: () => void): Promise<void> {
        // 重置查询条件
        Object.assign(query, {...initialQuery});
        // 加载数据
        await loadData(callback);
    }

    return {
        query,
        dataList,
        total,
        loading,
        loadData,
        resetQuery
    };
}
