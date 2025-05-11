<#assign api_base_path = "\"@/api/${table.moduleName}/${table.businessName}\"">
<#assign type_base_path = "\"@/api/${table.moduleName}/${table.businessName}/type\"">
<#assign importEnum = '"@/enums/DeviceEnum"'>
<#-- @formatter:off -->
<template>
    <el-card class="table-container">
        <!--  顶部操作    -->
        <template #header>
            <div>
                <el-button v-permission="[${table.className}API.SAVE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           plain
                           type="success"
                           @click="open${table.className}Dialog()">
                    <el-icon>
                        <plus/>
                    </el-icon>
                    <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
                </el-button>
                <el-button v-permission="[${table.className}API.UPDATE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           :disabled="${mapFields.pk[0].javaTsFieldName}s.length !== 1"
                           plain
                           type="warning"
                           @click="open${table.className}Dialog(${mapFields.pk[0].javaTsFieldName}s[0])">
                    <el-icon>
                        <edit/>
                    </el-icon>
                    <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
                </el-button>
                <el-button v-permission="[${table.className}API.DELETE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           :disabled="${mapFields.pk[0].javaTsFieldName}s.length === 0"
                           plain
                           type="danger"
                           @click="deleteData(${mapFields.pk[0].javaTsFieldName}s, undefined, props.loadData)">
                    <el-icon>
                        <delete/>
                    </el-icon>
                    <span v-show="device !== DeviceEnum.MOBILE"> 删除 </span>
                </el-button>
            </div>
            <div>
            </div>
        </template>
        <el-table
                ref="dataTableRef"
                v-loading="props.loading"
                :data="props.dataList"
                border
                highlight-current-row
                row-key="${mapFields.pk[0].javaTsFieldName}"
                @selection-change="handleSelectionChange"
                @cell-dblclick="handleCellDblclick"
        >
            <el-table-column align="center" type="selection" width="50"/>
    <#list mapFields.show as field>
        <#if !field.dictTypeName?has_content>
            <#if field.showType == "text">
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <template #default="scope">
                    <el-text type="primary">{{ scope.row.${field.javaTsFieldName} }}</el-text>
                </template>
            </el-table-column>
            <#elseif field.showType == "tag">
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <template #default="scope">
                    <el-tag type="primary">{{ scope.row.${field.javaTsFieldName} }}</el-tag>
                </template>
            </el-table-column>
            <#elseif field.showType == "rate">
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <template #default="scope">
                    <el-rate v-model="scope.row.${field.javaTsFieldName}"
                             :max="5"
                             show-score
                             score-template="{value}"
                             disabled/>
                </template>
            </el-table-column>
            <#elseif field.showType == "image">
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <template #default="scope">
                    <el-image :preview-src-list="[scope.row.${field.javaTsFieldName}]"
                              :src="scope.row.${field.javaTsFieldName}"
                              fit="cover"
                              lazy
                              preview-teleported
                    />
                </template>
            </el-table-column>
            <#elseif field.showType == "href">
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <template #default="scope">
                    <el-link :href="scope.row.${field.javaTsFieldName}" target="_blank">
                        {{ scope.row.${field.javaTsFieldName}.split('/').pop() }}
                    </el-link>
                </template>
            </el-table-column>
            <#else>
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}"/>
            </#if>
        <#else>
            <el-table-column align="center" label="${field.showName}" prop="${field.javaTsFieldName}">
                <#if field.showType == "text">
                <template #default="scope">
                    <el-text type="primary">
                        {{ props.dictData['${field.dictTypeName}'][scope.row?.${field.javaTsFieldName}] }}
                    </el-text>
                </template>
                <#elseif field.showType == "tag">
                <template #default="scope">
                    <el-tag type="primary">
                        {{ props.dictData['${field.dictTypeName}'][scope.row?.${field.javaTsFieldName}] }}
                    </el-tag>
                </template>
                <#else >
                <template #default="scope">
                    {{ props.dictData['${field.dictTypeName}'][scope.row?.${field.javaTsFieldName}] }}
                </template>
                </#if>
            </el-table-column>
        </#if>
    </#list>
            <el-table-column v-permission="[${table.className}API.UPDATE.permission, ${table.className}API.DELETE.permission]"
                             :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                             width="180">
                <template #default="scope">
                    <el-button
                            v-permission="[${table.className}API.UPDATE.permission]"
                            link
                            size="small"
                            type="warning"
                            @click="open${table.className}Dialog(scope.row.id)"
                    >
                        <el-icon>
                            <edit/>
                        </el-icon>
                        编辑
                    </el-button
                    >
                    <el-button
                            v-permission="[${table.className}API.DELETE.permission]"
                            link
                            size="small"
                            type="danger"
                            @click="deleteData([scope.row.id],scope.row.name,props.loadData)"
                    >
                        <el-icon>
                            <delete/>
                        </el-icon>
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--  底部分页    -->
        <template #footer>
            <el-scrollbar>
                <Pagination v-model:current-page="query.pageNum"
                            v-model:page-size="query.pageSize"
                            :total="props.total" @handle-page-change="props.loadData()"/>
            </el-scrollbar>
        </template>
    </el-card>

    <!-- 交互模态框 -->
    <${table.componentName}-manage-dialog
            v-if="visible"
            v-model:visible="visible"
            :close-dialog="closeDialog"
            :current-click-row-id="currentClickRowId"
            :device="device"
            :dict-data="props.dictData"
            :load-data="props.loadData"
            :title="title"
    />
</template>

<script lang="ts" setup>
import {
    ${table.className}PageQuery,
    ${table.className}PageVO,
    ${table.className}Form
} from ${type_base_path};
import {${table.className}API} from ${api_base_path};
import {DeviceEnum} from ${importEnum};
import {DictType} from "@/api/system/dict-data/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {useTableManagement} from "@/hooks/useTableManagement";
import {TableInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "${table.className}Table",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: ${table.className}PageQuery;
    dataList: ${table.className}PageVO[];
    dictData?: Record<DictType | string, Record<any, string>>;
    total: number;
    loading: boolean;
    loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {dictData: {}});

const emits = defineEmits<{
(event: "update:query", query: ${table.className}PageQuery): void
}>()

// hooks
const query = useVModel(props, 'query', emits)
const {
    visible,
    title,
    openDialog,
    closeDialog
} = useDialogManage();
const {
    deleteData
} = useCrudActions<${mapFields.pk[0].tsType}, ${table.className}Form>(undefined, undefined, ${table.className}API.DELETE.request, undefined);
// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const currentClickRowId = ref<${mapFields.pk[0].tsType} | undefined>();                  // 打开 dialog 点击的 row
const {selectedIds: ${mapFields.pk[0].javaTsFieldName}s, handleCellDblclick, handleSelectionChange} = useTableManagement<${mapFields.pk[0].tsType}>(dataTableRef);

// 方法
/**
 * 打开${table.tableComment}数据模态框
 * @param ${mapFields.pk[0].javaTsFieldName} ${table.className}主键
 */
function open${table.className}Dialog(${mapFields.pk[0].javaTsFieldName}?: ${mapFields.pk[0].tsType}) {
    currentClickRowId.value = ${mapFields.pk[0].javaTsFieldName};
    if (${mapFields.pk[0].javaTsFieldName}) {
        openDialog("修改${table.tableComment}数据");
    } else {
        openDialog("新增${table.tableComment}数据");
    }
}
</script>

<style lang="scss" scoped>
    /* 样式 */
</style>
<#-- @formatter:on -->
