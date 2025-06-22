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
            <el-form-item label="IP地址" prop="ip">
                <el-input v-model="form.ip"
                          placeholder="请输入IP地址"/>
            </el-form-item>
            <el-form-item label="端口" prop="port">
                <el-input v-model="form.port"
                          placeholder="请输入端口"/>
            </el-form-item>
            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username"
                          placeholder="请输入用户名"/>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="form.password"
                          placeholder="请输入密码"
                          show-password/>
            </el-form-item>
            <el-form-item label="角色(1-主节点" prop="role">
                <el-input v-model="form.role"
                          placeholder="请输入角色(1-主节点"/>
            </el-form-item>
            <el-form-item label="角色状态(1-在" prop="status">
                <el-input v-model="form.status"
                          placeholder="请输入角色状态(1-在"/>
            </el-form-item>
            <el-form-item label="cpu占用" prop="cpu">
                <el-input v-model="form.cpu"
                          placeholder="请输入cpu占用"/>
            </el-form-item>
            <el-form-item label="内存占用" prop="memory">
                <el-input v-model="form.memory"
                          placeholder="请输入内存占用"/>
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
import {DfmsNodeForm} from "@/api/dfms/node/type";
import {DfmsNodeAPI} from "@/api/dfms/node";
import {DictType} from "@/api/system/dict-data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useCrudActions} from "@/hooks/useCrudActions";
import {FormInstance, FormRules} from "element-plus";

// 组件定义
defineOptions({
    name: "DfmsNodeManageDialog",
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
} = useCrudActions<number, DfmsNodeForm>(DfmsNodeAPI.SAVE.request, DfmsNodeAPI.UPDATE.request, undefined, undefined);
// 初始校验规则
const initialForm: DfmsNodeForm = {
    name: undefined,
    ip: undefined,
    port: undefined,
    username: undefined,
    password: undefined,
    role: undefined,
    status: undefined,
    cpu: undefined,
    memory: undefined,
}
const rules: FormRules = {
    name: [{required: true, message: "请输入名称", trigger: "blur"}],
    ip: [{required: true, message: "请输入IP地址", trigger: "blur"}],
    port: [{required: true, message: "请输入端口", trigger: "blur"}],
    username: [{required: true, message: "请输入用户名", trigger: "blur"}],
    password: [{required: true, message: "请输入密码", trigger: "blur"}],
}
// 数据
const formRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<DfmsNodeForm>({...initialForm});

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
