import {DictDataAPI} from "@/api/system/dict-data";
import {DfmsDbAPI} from "@/api/dfms/db";

/**
 * 根据 id 获取 name : 复杂度 O(1) dictMap[number]
 *
 * @param type
 */
export async function userDdmsDbOverview(): Promise<Record<string, Record<any, string>>> {
    console.log(1111)
    return await DfmsDbAPI.OVERVIEW.request().then(({data}) => {
        console.log(data)
        return
    })
}
