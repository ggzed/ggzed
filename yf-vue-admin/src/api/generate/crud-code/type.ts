export interface GenCrudTablePageQuery extends PageQuery {
    /**
     * 作者
     */
    functionAuthor?: string;
    /**
     * 数据库表名
     */
    tableName?: string;
    /**
     * 备注
     */
    remark?: string;
}

export interface DBTablePageQuery extends PageQuery {
    /**
     * 数据库表名
     */
    tableName?: number;
}

export interface GenCrudTableVO {
    /**
     * TableId
     */
    id?: number;

    /**
     * 父菜单名
     */
    menuName?: string;

    /**
     * 作者
     */
    functionAuthor?: string;

    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库表描述
     */
    tableComment?: string;

    /**
     * 类名(根据数据库表名生成)
     */
    className?: string;

    /**
     * 前端 component 名(根据数据库表名生成)
     */
    componentName?: string;

    /**
     * 后端生成类型
     */
    backEndType?: string;

    /**
     * 前端生成类型
     */
    frontEndType?: string;

    /**
     * 主包名
     */
    packageName?: string;

    /**
     * 模块名（外层包名）
     */
    moduleName?: string;

    /**
     * 业务名（内层包名）
     */
    businessName?: string;

    /**
     * 修改时间
     */
    updateTime?: string;

    /**
     * 备注
     */
    remark?: string;
}

export interface DBTableVO {
    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库字段
     */
    tableComment?: string;

    /**
     * engine
     */
    engine?: string;

    /**
     * 数据量
     */
    tableRows?: number;

    /**
     * 创建时间
     */
    createTime?: string;

    /**
     * 修改时间
     */
    updateTime?: string;
}

export interface PreviewGenCodeTreeVO {
    /**
     * 文件路径
     */
    id?: string;  // 文件路径

    /**
     * 文件路径的父级路径
     */
    parentId?: string;

    /**
     * 节点名称，例如文件或文件夹的名称
     */
    name?: string;

    /**
     * 指示该节点是否为文件，true 表示文件，false 表示文件夹
     */
    isFile?: boolean;

    /**
     * 代码语言类型，如 java、vue、ts 等
     */
    codeLanguage?: string;

    /**
     * 节点内容，通常是代码或文件的相关内容
     */
    content?: string;

    /**
     * 子节点列表，包含当前节点的所有子节点
     */
    children?: PreviewGenCodeTreeVO[];
}

export interface GenTableMenuForm {

    /**
     * 父菜单ID
     */
    parentId?: number;

    /**
     * 菜单名称
     */
    menuName?: string;

    /**
     * 菜单图标
     */
    icon?: string;

    /**
     * 是否隐藏
     */
    hidden?: number;
}

export interface GenTableForm {
    /**
     * 菜单Id
     */
    menuId?: number;
    /**
     * 作者
     */
    functionAuthor?: string;

    /**
     * 数据库表名
     */
    tableName?: string;

    /**
     * 数据库表描述
     */
    tableComment?: string;

    /**
     * 类名(根据数据库表名生成)
     */
    className?: string;

    /**
     * 前端 component 名(根据数据库表名生成)
     */
    componentName?: string;

    /**
     * 后端生成类型
     */
    backEndType?: string;

    /**
     * 前端生成类型
     */
    frontEndType?: string;

    /**
     * 主包名
     */
    packageName?: string;

    /**
     * 模块名（外层包名）
     */
    moduleName?: string;

    /**
     * 业务名（内层包名）
     */
    businessName?: string;

    /**
     * 备注
     */
    remark?: string;
}

export interface GenTableFieldsForm {
    /**
     * 主键
     */
    id?: number;

    /**
     * 表格展示列名
     */
    showName?: string;

    /**
     * 数据库列名
     */
    columnName?: string;

    /**
     * 数据库字段描述
     */
    columnComment?: string;

    /**
     * 数据库类型
     */
    columnType?: string;

    /**
     * JAVA类型
     */
    javaType?: string;

    /**
     * JAVA&TS字段名
     */
    javaTsFieldName?: string;

    /**
     * TypeScript类型
     */
    tsType?: string;

    /**
     * 是否主键（1:是,0:否）
     */
    isPk?: boolean;

    /**
     * 是否自增（1:是,0:否）
     */
    isIncrement?: boolean;

    /**
     * 是否必填（1:是,0:否）
     */
    isRequired?: boolean;

    /**
     * 是否表单字段（1:是,0:否）
     */
    isForm?: boolean;

    /**
     * 是否展示字段（1:是,0:否）
     */
    isShow?: boolean;

    /**
     * 是否查询字段（1:是,0:否）
     */
    isQuery?: boolean;

    /**
     * 展示类型 ( JSON、文本、Markdown、Tag、图片... )
     */
    showType?: string;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    queryType?: string;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    queryFormType?: string;

    /**
     * 表单类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    saveFormType?: string;

    /**
     * 字典类型
     */
    dictTypeName?: string;

    /**
     * 排序
     */
    sort?: number;
}
