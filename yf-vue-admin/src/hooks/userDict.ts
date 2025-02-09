import {DictDataAPI} from "@/api/system/dict-data";
import {DictType} from "@/api/system/dict-data/type";

/**
 *
 *
 * 根据 id 获取 name : 复杂度 O(1) dictMap[number]
 * @param type
 */
export async function useDictionary(type: DictType): Promise<Record<number | string, string>> {
    return await DictDataAPI.DATA_OPTIONS.request(type).then(({data}) => {
        // ref 版本
        // const dictMap = ref<Record<number | string, string>>({});
        // data.forEach((item: OptionType) => {
        //     dictMap.value[item.value] = item.label;
        // });
        // 常量 版本
        const dictMap: Record<number | string, string> = {};
        data.forEach((item: OptionType) => {
            dictMap[item.value] = item.label;
        });
        return dictMap;
    })
}
