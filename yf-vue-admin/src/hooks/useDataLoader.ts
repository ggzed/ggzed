import {AxiosResponse} from "axios";
import {Ref} from "vue";
import {FormInstance} from "element-plus";

interface PageDataLoader<T, E extends PageQuery> {
    /**
     * 数据集合
     */
    dataList: Ref<T[]>;
    /**
     * 分页总条数
     */
    total: Ref<number>;
    /**
     * 分页查询
     */
    query: E;
    /**
     * 是否正在加载
     */
    loading: Ref<boolean>;
    /**
     * 重置查询条件
     */
    resetQuery: () => Promise<void>;
    /**
     * 加载数据函数
     */
    loadData: () => Promise<void>;
}

export function usePageDataLoader<T, E extends PageQuery>(fetchData: (...params: any) => Promise<AxiosResponse<PageResult<T[]>>>, initialQuery: E, formRef: Ref<FormInstance | null>, param?: any): PageDataLoader<T, E> {
    const dataList = ref<T[]>([]) as Ref<T[]>;
    const total = ref(0);
    const query = reactive({...initialQuery}) as E;
    const loading = ref(false);

    async function loadData(): Promise<void> {
        loading.value = true;
        try {
            const {data} = await fetchData(query, param);
            dataList.value = data.list;
            total.value = data.total;
        } catch (error) {
            // 处理错误，可以在这里记录或显示错误信息
            console.error('Failed to load data', error);
            throw error;  // 将错误抛出，以便外部调用方可以捕获
        } finally {
            loading.value = false;
        }
    }

    async function resetQuery(): Promise<void> {
        // 清空表单
        if (formRef.value) {
            formRef.value.resetFields();
        }
        // 重置查询
        Object.assign(query, initialQuery);
        // 重写加载数据
        await loadData();
    }

    return {
        dataList,
        total,
        query,
        loading,
        resetQuery,
        loadData
    };
}

interface DataLoader<T, E extends object> {
    /**
     * 数据集合
     */
    dataList: Ref<T[]>;
    /**
     * 分页查询
     */
    query: E;
    /**
     * 是否正在加载
     */
    loading: Ref<boolean>;
    /**
     * 重置查询条件
     */
    resetQuery: () => Promise<void>;
    /**
     * 加载数据函数
     */
    loadData: () => Promise<void>;
}

export function useDataLoader<T, E extends object>(fetchData: (...params: any) => Promise<AxiosResponse<T[]>>, initialQuery: E, formRef: Ref<FormInstance | null>, param?: any): DataLoader<T, E> {
    const dataList = ref<T[]>([]) as Ref<T[]>;
    const query = reactive({...initialQuery}) as E;
    const loading = ref(false);

    async function loadData(): Promise<void> {
        loading.value = true;
        try {
            const {data} = await fetchData(query, param);
            dataList.value = data;
        } catch (error) {
            // 处理错误，可以在这里记录或显示错误信息
            console.error('Failed to load data', error);
            throw error;  // 将错误抛出，以便外部调用方可以捕获
        } finally {
            loading.value = false;
        }
    }

    async function resetQuery(): Promise<void> {
        // 清空表单
        if (formRef.value) {
            formRef.value.resetFields();
        }
        // 重置查询
        Object.assign(query, initialQuery);
        // 重写加载数据
        await loadData();
    }

    return {
        dataList,
        query,
        loading,
        resetQuery,
        loadData
    };
}
