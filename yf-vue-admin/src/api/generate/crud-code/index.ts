import {AxiosPromise} from "axios";
import request from "@/utils/request";
import {
    DBTablePageQuery,
    DBTableVO,
    GenCrudTablePageQuery,
    GenCrudTableVO,
    GenTableFieldsForm,
    GenTableForm,
    GenTableMenuForm,
    PreviewGenCodeTreeVO
} from "@/api/generate/crud-code/type";

const API_BASE = '/crud/code';

const API_SUFFIXES = {
    /** 查询 Crud 表 */
    PAGE: '/page',
    /** 查询数据库表 */
    DB_PAGE: '/db/page',
    /** Curd 生成字典 */
    COMMON_OPTIONS: "/common/options",
    /** 导入数据库表 */
    IMPORT_DB_TABLE: '/import/{dbTableNames}',
    /** 生成 Crud 代码 Zip 文件 */
    EXPORT_CRUD_CODE: '/generate/{tableId}',
    /** 预览 Crud 代码 */
    PREVIEW_CRUD_CODE: '/preview/{tableId}',
    /** 展示 Crud 代码 */
    DISPLAY_CRUD_CODE: '/display/{tableId}',
    /** Crud 表表单 */
    TABLE_FORM: '/{tableId}/form',
    /** Crud 表列表 */
    TABLE_FIELDS_FORM: '/{tableId}/fields/form',
    /** 修改 Crud 表 */
    TABLE_UPDATE: '/{tableId}',
    /** 修改 Crud Fields 表 */
    TABLE_FIELDS_UPDATE: '/{tableId}/fields',
    /** 删除 Crud 表 */
    TABLE_DELETE: '/{tableIds}',
    /** 新增菜单信息 */
    MENU: '/{tableId}/menu',
    /** 新增菜单可选项 */
    MENU_OPTIONS: '/menu/options'
};

// 定义 GenerateCrudAPI 类
export class GenerateCrudAPI {

    static PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.PAGE}`,
        permission: "generate:crud:list",
        request: (query: GenCrudTablePageQuery): AxiosPromise<PageResult<GenCrudTableVO[]>> => {
            return request<PageResult<GenCrudTableVO[]>>({
                url: GenerateCrudAPI.PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    static DB_PAGE = {
        endpoint: `${API_BASE}${API_SUFFIXES.DB_PAGE}`,
        permission: "generate:crud:import",
        request: (query: DBTablePageQuery): AxiosPromise<PageResult<DBTableVO[]>> => {
            return request<PageResult<DBTableVO[]>>({
                url: GenerateCrudAPI.DB_PAGE.endpoint,
                method: "get",
                params: query,
            })
        }
    };

    static COMMON_OPTIONS = {
        endpoint: `${API_BASE}${API_SUFFIXES.COMMON_OPTIONS}`,
        permission: "",
        request: (): AxiosPromise<Record<string, OptionType[]>> => {
            return request<Record<string, OptionType[]>>({
                url: GenerateCrudAPI.COMMON_OPTIONS.endpoint,
                method: "get"
            })
        }
    };

    static IMPORT_DB_TABLE = {
        endpoint: (dbTableNames: string): string => {
            return `${API_BASE}${API_SUFFIXES.IMPORT_DB_TABLE.replace("{dbTableNames}", dbTableNames)}`;
        },
        permission: "generate:crud:import",
        request: (dbTableNames: string): AxiosPromise<number> => {
            return request<number>({
                url: GenerateCrudAPI.IMPORT_DB_TABLE.endpoint(dbTableNames),
                method: "post"
            })
        }
    };

    static EXPORT_CRUD_CODE = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.EXPORT_CRUD_CODE.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:zip",
        request: (tableId: number): AxiosPromise<void> => {
            return request<void>({
                url: GenerateCrudAPI.EXPORT_CRUD_CODE.endpoint(tableId),
                method: "post",
                responseType: "arraybuffer"
            })
        }
    };

    static PREVIEW_CRUD_CODE = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.PREVIEW_CRUD_CODE.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:preview",
        request: (tableId: number): AxiosPromise<PreviewGenCodeTreeVO> => {
            return request<PreviewGenCodeTreeVO>({
                url: GenerateCrudAPI.PREVIEW_CRUD_CODE.endpoint(tableId),
                method: "get"
            })
        }
    };

    static DISPLAY_CRUD_CODE = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.DISPLAY_CRUD_CODE.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:preview",
        request: (tableId: number): AxiosPromise<string> => {
            return request<string>({
                url: GenerateCrudAPI.DISPLAY_CRUD_CODE.endpoint(tableId),
                method: "get"
            })
        }
    };
    static TABLE_FORM = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.TABLE_FORM.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:list",
        request: (tableId: number): AxiosPromise<GenTableForm> => {
            return request<GenTableForm>({
                url: GenerateCrudAPI.TABLE_FORM.endpoint(tableId),
                method: "get"
            })
        }
    };

    static TABLE_FIELDS_FORM = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.TABLE_FIELDS_FORM.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:list",
        request: (tableId: number): AxiosPromise<GenTableFieldsForm[]> => {
            return request<GenTableFieldsForm[]>({
                url: GenerateCrudAPI.TABLE_FIELDS_FORM.endpoint(tableId),
                method: "get"
            })
        }
    };

    static TABLE_UPDATE = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.TABLE_UPDATE.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:update",
        request: (tableId: number, form: GenTableForm): AxiosPromise<GenTableForm> => {
            return request<GenTableForm>({
                url: GenerateCrudAPI.TABLE_UPDATE.endpoint(tableId),
                method: "put",
                data: form
            })
        }
    };

    static TABLE_FIELDS_UPDATE = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.TABLE_FIELDS_UPDATE.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:update",
        request: (tableId: number, forms: GenTableForm[]): AxiosPromise<void> => {
            return request<void>({
                url: GenerateCrudAPI.TABLE_FIELDS_UPDATE.endpoint(tableId),
                method: "put",
                data: forms
            })
        }
    };

    static TABLE_DELETE = {
        endpoint: (tableIds: string): string => {
            return `${API_BASE}${API_SUFFIXES.TABLE_DELETE.replace("{tableIds}", tableIds)}`;
        },
        permission: "generate:crud:delete",
        request: (tableIds: string): AxiosPromise<void> => {
            return request<void>({
                url: GenerateCrudAPI.TABLE_DELETE.endpoint(tableIds),
                method: "delete"
            })
        }
    };

    static MENU = {
        endpoint: (tableId: number): string => {
            return `${API_BASE}${API_SUFFIXES.MENU.replace("{tableId}", tableId.toString())}`;
        },
        permission: "generate:crud:menu",
        request: (tableId: number, form: GenTableMenuForm): AxiosPromise<void> => {
            return request<void>({
                url: GenerateCrudAPI.MENU.endpoint(tableId),
                method: "post",
                data: form
            })
        }
    };

    static MENU_OPTIONS = {
        endpoint: `${API_BASE}${API_SUFFIXES.MENU_OPTIONS}`,
        permission: "generate:crud:menu",
        request: (): AxiosPromise<OptionType[]> => {
            return request<OptionType[]>({
                url: GenerateCrudAPI.MENU_OPTIONS.endpoint,
                method: "get",
            });
        }
    }
}
