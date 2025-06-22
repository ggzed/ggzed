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
            <el-form-item label="名称" prop="name">
                <el-input v-model="form.name"
                          placeholder="请输入名称"/>
            </el-form-item>
            <el-form-item label="字符集" prop="charset">
                <el-input v-model="form.charset"
                          placeholder="请输入字符集"/>
            </el-form-item>
            <el-form-item label="排序规则" prop="collation">
                <el-input v-model="form.collation"
                          placeholder="请输入排序规则"/>
            </el-form-item>
            <el-form-item label="大小" prop="size">
                <el-input v-model="form.size"
                          placeholder="请输入大小"/>
            </el-form-item>
            <el-form-item label="表数量" prop="tables">
                <el-input v-model="form.tables"
                          placeholder="请输入表数量"/>
            </el-form-item>
            <el-form-item label="类型(1-pgs" prop="type">
                <el-input v-model="form.type"
                          placeholder="请输入类型(1-pgs"/>
            </el-form-item>
            <el-form-item label="保留策略(tim" prop="retentionPolicy">
                <el-input v-model="form.retentionPolicy"
                          placeholder="请输入保留策略(tim"/>
            </el-form-item>
            <el-form-item label="序列数(time" prop="sequenceNumber">
                <el-input-number v-model="form.sequenceNumber"
                                 placeholder="请输入序列数(time"/>
            </el-form-item>
            <el-form-item label="数据点数(tim" prop="dataPointsNumber">
                <el-input-number v-model="form.dataPointsNumber"
                                 placeholder="请输入数据点数(tim"/>
            </el-form-item>
            <el-form-item label="维度(vecto" prop="dimension">
                <el-input v-model="form.dimension"
                          placeholder="请输入维度(vecto"/>
            </el-form-item>
            <el-form-item label="向量数量(vec" prop="vectorNumber">
                <el-input-number v-model="form.vectorNumber"
                                 placeholder="请输入向量数量(vec"/>
            </el-form-item>
            <el-form-item label="索引类型(vec" prop="indexType">
                <el-input v-model="form.indexType"
                          placeholder="请输入索引类型(vec"/>
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
import {DfmsDbForm} from "@/api/dfms/db/type";
import {DfmsDbAPI} from "@/api/dfms/db";
import {DictType} from "@/api/system/dict-data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useCrudActions} from "@/hooks/useCrudActions";
import {FormInstance, FormRules} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsDbVectorManageDialog",
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
} = useCrudActions<number, DfmsDbForm>(DfmsDbAPI.SAVE.request, DfmsDbAPI.UPDATE.request, undefined, undefined);
// 初始校验规则
const initialForm: DfmsDbForm = {
    name: undefined,
    charset: undefined,
    collation: undefined,
    size: undefined,
    tables: undefined,
    type: 3,
    retentionPolicy: undefined,
    sequenceNumber: undefined,
    dataPointsNumber: undefined,
    dimension: undefined,
    vectorNumber: undefined,
    indexType: undefined,
    status: undefined,
}
const rules: FormRules = {
    name: [{required: true, message: "请输入名称", trigger: "blur"}],
    charset: [{required: true, message: "请输入字符集", trigger: "blur"}],
    collation: [{required: true, message: "请输入排序规则", trigger: "blur"}],
    size: [{required: true, message: "请输入大小", trigger: "blur"}],
    tables: [{required: true, message: "请输入表数量", trigger: "blur"}],
    retentionPolicy: [{required: true, message: "请输入保留策略(tim", trigger: "blur"}],
    sequenceNumber: [{required: true, message: "请输入序列数(time", trigger: "blur"}],
    dataPointsNumber: [{required: true, message: "请输入数据点数(tim", trigger: "blur"}],
    dimension: [{required: true, message: "请输入维度(vecto", trigger: "blur"}],
    vectorNumber: [{required: true, message: "请输入向量数量(vec", trigger: "blur"}],
    indexType: [{required: true, message: "请输入索引类型(vec", trigger: "blur"}],
}
// 数据
const formRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<DfmsDbForm>({...initialForm});

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
