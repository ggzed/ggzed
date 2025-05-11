import {DictDataAPI} from "@/api/system/dict-data";

/**
 * 根据 id 获取 name : 复杂度 O(1) dictMap[number]
 *
 * @param type
 */
export async function useDictionary(type: string[]): Promise<Record<string, Record<any, string>>> {
    return await DictDataAPI.DATA_OPTIONS.request(type.join(",")).then(({data}) => {
        // ref 版本
        // const dictMap = ref<Record<DictType, Record<any, string>>>({});
        // 常量 版本
        const dictMap: Record<string, Record<any, string>> = {};

        // 遍历数据，将每个类型的选项映射到 dictMap
        type.forEach(dictType => {
            // 初始化每个 dictType 对应的映射对象
            dictMap[dictType] = {};

            // data 是以 dictType 为键，选项数组为值的对象
            const options = data[dictType] || [{}];
            options.forEach((option: OptionType) => {
                // 假设每个 option 都有 id 和 name 字段
                dictMap[dictType][option.value] = option.label;
            });
        });
        return dictMap;
    })
}
