<template>
    <el-card class="table-container">
        <!--  顶部操作    -->
        <template #header>
            <div>
                <el-button v-permission="[DfmsTableFieldsAPI.SAVE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           plain
                           type="success"
                           @click="openDfmsTableFieldsDialog()">
                    <el-icon>
                        <plus/>
                    </el-icon>
                    <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
                </el-button>
                <el-button v-permission="[DfmsTableFieldsAPI.UPDATE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           :disabled="ids.length !== 1"
                           plain
                           type="warning"
                           @click="openDfmsTableFieldsDialog(ids[0])">
                    <el-icon>
                        <edit/>
                    </el-icon>
                    <span v-show="device !== DeviceEnum.MOBILE"> 修改 </span>
                </el-button>
                <el-button v-permission="[DfmsTableFieldsAPI.DELETE.permission]"
                           :circle="device === DeviceEnum.MOBILE"
                           :disabled="ids.length === 0"
                           plain
                           type="danger"
                           @click="deleteData(ids, undefined, props.loadData)">
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
                row-key="id"
                @selection-change="handleSelectionChange"
                @cell-dblclick="handleCellDblclick"
        >
            <el-table-column align="center" type="selection" width="50"/>
            <el-table-column align="center" label="表格展示列名" prop="showName"/>
            <el-table-column align="center" label="数据库列名" prop="columnName"/>
            <el-table-column align="center" label="数据库类型" prop="columnType">
                <template #default="scope">
                    <el-tag type="primary">{{ scope.row.columnType }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" label="数据库字段描述" prop="columnComment">
                <template #default="scope">
                    <el-text type="primary">{{ scope.row.columnComment }}</el-text>
                </template>
            </el-table-column>
            <el-table-column align="center" label="状态(1-在线；" prop="status">
                <template #default="scope">
                    <el-tag type="primary">{{ scope.row.status }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column v-permission="[DfmsTableFieldsAPI.UPDATE.permission, DfmsTableFieldsAPI.DELETE.permission]"
                             :fixed="device === DeviceEnum.MOBILE ? false : 'right'" align="center" label="操作"
                             width="180">
                <template #default="scope">
                    <el-button
                            v-permission="[DfmsTableFieldsAPI.UPDATE.permission]"
                            link
                            size="small"
                            type="warning"
                            @click="openDfmsTableFieldsDialog(scope.row.id)"
                    >
                        <el-icon>
                            <edit/>
                        </el-icon>
                        编辑
                    </el-button
                    >
                    <el-button
                            v-permission="[DfmsTableFieldsAPI.DELETE.permission]"
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
    <dfms-table-fields-manage-dialog
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
    DfmsTableFieldsPageQuery,
    DfmsTableFieldsPageVO,
    DfmsTableFieldsForm
} from "@/api/dfms/tablefields/type";
import {DfmsTableFieldsAPI} from "@/api/dfms/tablefields";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {DictType} from "@/api/system/dict-data/type";
import {useDialogManage} from "@/hooks/useDialogManage";
import {useCrudActions} from "@/hooks/useCrudActions";
import {useSystemStore} from "@/store/modules/system";
import {useTableManagement} from "@/hooks/useTableManagement";
import {TableInstance} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsTableFieldsTable",
    inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
    query: DfmsTableFieldsPageQuery;
    dataList: DfmsTableFieldsPageVO[];
    dictData?: Record<DictType | string, Record<any, string>>;
    total: number;
    loading: boolean;
    loadData: (callback?: () => void) => Promise<void>;   // 加载数据函数
}>(), {dictData: () => ({})});

const emits = defineEmits<{
(event: "update:query", query: DfmsTableFieldsPageQuery): void
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
} = useCrudActions<number, DfmsTableFieldsForm>(undefined, undefined, DfmsTableFieldsAPI.DELETE.request, undefined);
// 数据
const device = computed(() => useSystemStore().app.device)            // 设备类型
const dataTableRef = ref<TableInstance | null>(null);                 // 数据Table
const currentClickRowId = ref<number | undefined>();                  // 打开 dialog 点击的 row
const {selectedIds: ids, handleCellDblclick, handleSelectionChange} = useTableManagement<number>(dataTableRef);

// 方法
/**
 * 打开数据表字段信息数据模态框
 * @param id DfmsTableFields主键
 */
function openDfmsTableFieldsDialog(id?: number) {
    currentClickRowId.value = id;
    if (id) {
        openDialog("修改数据表字段信息数据");
    } else {
        openDialog("新增数据表字段信息数据");
    }
}
</script>

<style lang="scss" scoped>
    /* 样式 */
</style>
