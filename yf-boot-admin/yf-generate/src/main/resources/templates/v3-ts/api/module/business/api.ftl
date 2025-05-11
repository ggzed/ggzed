<#-- @formatter:off -->
import request from "@/utils/request";
import {AxiosPromise} from "axios";
import {${table.className}Form, ${table.className}PageQuery, ${table.className}PageVO} from "./type";

const API_BASE = '/${table.tableName}';

const API_SUFFIXES = {
    PAGE: '/page',
    FORM: '/{${mapFields.pk[0].javaTsFieldName}}/form',
    SAVE: '',
    DELETE: '/{${mapFields.pk[0].javaTsFieldName}s}',
    UPDATE: '/{${mapFields.pk[0].javaTsFieldName}}',
};

// 定义 ${table.className}API 类
export class ${table.className}API {
    /**
     * 分页查询 ${table.tableComment} 分页数据
     * @param query 分页参数
     */
    static PAGE = {
        endpoint: `${r"${API_BASE}${API_SUFFIXES.PAGE}"}`,
        permission: "${table.moduleName}:${table.businessName}:list",
        request: (query: ${table.className}PageQuery): AxiosPromise<PageResult<${table.className}PageVO[]>> => {
            return request<PageResult<${table.className}PageVO[]>>({
                url: ${table.className}API.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    /**
     * 获取 ${table.tableComment} 表单数据
     * @param ${mapFields.pk[0].javaTsFieldName} ${table.className} 主键
     */
    static FORM = {
        endpoint: (${mapFields.pk[0].javaTsFieldName}: ${mapFields.pk[0].tsType}): string => {
            return `${r"${API_BASE}${API_SUFFIXES.FORM"}.replace("{${mapFields.pk[0].javaTsFieldName}}", ${mapFields.pk[0].javaTsFieldName}.toString())}`;
        },
        permission: "${table.moduleName}:${table.businessName}:update",
        request: (${mapFields.pk[0].javaTsFieldName}: ${mapFields.pk[0].tsType}): AxiosPromise<${table.className}Form> => {
            return request<${table.className}Form>({
                url: ${table.className}API.FORM.endpoint(${mapFields.pk[0].javaTsFieldName}),
                method: "get",
            })
        }
    }

    /**
     * 保存 ${table.tableComment}
     * @param userForm 表单数据
     * @return ${table.className} 主键
     */
    static SAVE = {
        endpoint: `${r"${API_BASE}${API_SUFFIXES.SAVE}"}`,
        permission: "${table.moduleName}:${table.businessName}:save",
        request: (form: ${table.className}Form): AxiosPromise<${mapFields.pk[0].tsType}> => {
            return request<${mapFields.pk[0].tsType}>({
                url: ${table.className}API.SAVE.endpoint,
                method: "post",
                data: form
            })
        }
    }

    /**
     * 删除 ${table.tableComment}
     * @param ${mapFields.pk[0].javaTsFieldName}s ${table.className} 主键集合,以 "," 分隔
     */
    static DELETE = {
        endpoint: (${mapFields.pk[0].javaTsFieldName}s: string): string => {
            return `${r"${API_BASE}${API_SUFFIXES.DELETE"}.replace("{${mapFields.pk[0].javaTsFieldName}s}", ${mapFields.pk[0].javaTsFieldName}s)}`;
        },
        permission: "${table.moduleName}:${table.businessName}:delete",
        request: (${mapFields.pk[0].javaTsFieldName}s: string): AxiosPromise<void> => {
            return request<void>({
                    url: ${table.className}API.DELETE.endpoint(${mapFields.pk[0].javaTsFieldName}s),
                    method: "delete"
                }
            )
        }
    }

    /**
     * 修改 ${table.tableComment}
     * @param ${mapFields.pk[0].javaTsFieldName} ${table.className} 主键
     * @param userForm ${table.className} 表单
     */
    static UPDATE = {
        endpoint: (${mapFields.pk[0].javaTsFieldName}: ${mapFields.pk[0].tsType}): string => {
            return `${r"${API_BASE}${API_SUFFIXES.UPDATE"}.replace("{${mapFields.pk[0].javaTsFieldName}}", ${mapFields.pk[0].javaTsFieldName}.toString())}`;
        },
        permission: "${table.moduleName}:${table.businessName}:update",
        request: (${mapFields.pk[0].javaTsFieldName}: ${mapFields.pk[0].tsType}, form: ${table.className}Form) => {
            return request<void>({
                url: ${table.className}API.UPDATE.endpoint(${mapFields.pk[0].javaTsFieldName}),
                method: "put",
                data: form
            })
        }
    }

}
<#-- @formatter:on -->