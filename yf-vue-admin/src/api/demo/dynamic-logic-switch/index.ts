import request from "@/utils/request";
import {AxiosPromise} from "axios";


const API_BASE = '/dynamic-logic-switch';

const API_SUFFIXES = {
    /** 执行逻辑 */
    EXECUTE: '/{number}/execute-logic',
    /** 动态逻辑 */
    LOGIC: '/{number}/together-execute-logic',
    /** 规则执行逻辑 */
    RULES: '/{number}/{userId}/rules-execute-logic'

};


export class DynamicLogicSwitchListAPI {

    static EXECUTE = {
        endpoint: (number: number): string => {
            return `${API_BASE}${API_SUFFIXES.EXECUTE.replace("{number}", number.toString())}`;
        },
        request: (number: number): AxiosPromise => {
            return request({
                url: DynamicLogicSwitchListAPI.EXECUTE.endpoint(number),
                method: 'GET',
            })
        },
        path: `${API_BASE}${API_SUFFIXES.EXECUTE}`
    }

    static LOGIC = {
        endpoint: (number: number): string => {
            return `${API_BASE}${API_SUFFIXES.LOGIC.replace("{number}", number.toString())}`
        },
        request: (number: number): AxiosPromise => {
            return request({
                url: DynamicLogicSwitchListAPI.LOGIC.endpoint(number),
                method: 'GET',
            })
        },
        path: `${API_BASE}${API_SUFFIXES.LOGIC}`
    }

    static RULES = {
        endpoint: (number: number, userId: string): string => {
            // 这里 userId 改为 string 类型，不需要 toString() 转换
            return `${API_BASE}${API_SUFFIXES.RULES.replace("{number}", number.toString()).replace("{userId}", userId)}`

        },
        request: (number: number, userId: string): AxiosPromise => {
            return request({
                url: DynamicLogicSwitchListAPI.RULES.endpoint(number, userId),
                method: 'GET',
            })
        },
        path: `${API_BASE}${API_SUFFIXES.RULES}`
    }

}