<template>
    <!--  模态框  -->
    <el-dialog
            v-model="visible"
            :title="props.title"
            :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
            draggable
            overflow
            @close="props.closeDialog()"
    >
        <el-form
                ref="formRef"
                :model="form"
                :rules="rules"
                label-width="100px"
        >
            <el-form-item label="表格展示列名" prop="showName">
                <el-input v-model="form.showName"
                          placeholder="请输入表格展示列名"/>
            </el-form-item>
            <el-form-item label="数据库列名" prop="columnName">
                <el-input v-model="form.columnName"
                          placeholder="请输入数据库列名"/>
            </el-form-item>
            <el-form-item label="数据库类型" prop="columnType">
                <el-input v-model="form.columnType"
                          placeholder="请输入数据库类型"/>
            </el-form-item>
            <el-form-item label="数据库字段描述" prop="columnComment">
                <el-input v-model="form.columnComment"
                          placeholder="请输入数据库字段描述"/>
            </el-form-item>
            <el-form-item label="状态(1-在线；" prop="status">
                <el-input v-model="form.status"
                          placeholder="请输入状态(1-在线；"/>
            </el-form-item>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="props.closeDialog()">取 消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script lang="ts" setup>
import {DfmsTableFieldsForm} from "@/api/dfms/tablefields/type";
import {DfmsTableFieldsAPI} from "@/api/dfms/tablefields";
import {DictType} from "@/api/system/dict-data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useCrudActions} from "@/hooks/useCrudActions";
import {FormInstance, FormRules} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsTableFieldsManageDialog",
    inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
    currentClickRowId: number | undefined;
    visible: boolean;
    title: string;
    dictData?: Record<DictType | string, Record<any, string>>;
    device?: DeviceEnum;
    closeDialog: (callback?: () => void) => void;
    loadData: (callback?: () => void) => Promise<void>;
}>(), {
    device: DeviceEnum.DESKTOP,
    dictData: () => ({})
});

const emits = defineEmits<{
    (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
    saveData,
    updateData
} = useCrudActions<number, DfmsTableFieldsForm>(DfmsTableFieldsAPI.SAVE.request, DfmsTableFieldsAPI.UPDATE.request, undefined, undefined);
// 初始校验规则
const initialForm: DfmsTableFieldsForm = {
    showName: undefined,
    columnName: undefined,
    columnType: undefined,
    columnComment: undefined,
    status: undefined,
}
const rules: FormRules = {
    showName: [{required: true, message: "请输入表格展示列名", trigger: "blur"}],
    columnName: [{required: true, message: "请输入数据库列名", trigger: "blur"}],
    columnType: [{required: true, message: "请输入数据库类型", trigger: "blur"}],
}
// 数据
const formRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<DfmsTableFieldsForm>({...initialForm});

// 方法
async function submitForm() {
    const isValid = await formRef.value?.validate(); // 使用 await 简化验证逻辑
    if (!isValid) return; // 验证未通过，直接返回
    // 校验通过后执行 API 请求
    if (props.currentClickRowId) {
        await updateData(props.currentClickRowId, form.value, () => {
            props.closeDialog()
            props.loadData()
        })
    } else {
        await saveData(form.value, () => {
            props.closeDialog()
            props.loadData()
        })
    }
}

// 生命周期
onMounted(() => {
})
</script>

<style lang="scss" scoped>
    /* 样式 */
</style>
