<#assign api_base_path = "\"@/api/${table.moduleName}/${table.businessName}\"">
<#assign type_base_path = "\"@/api/${table.moduleName}/${table.businessName}/type\"">
<#-- @formatter:off -->
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
        <#list mapFields.form as field>
            <el-form-item label="${field.showName}" prop="${field.javaTsFieldName}">
            <#if field.saveFormType == "input_number">
                <el-input-number v-model="form.${field.javaTsFieldName}"
                                 placeholder="请输入${field.showName}"/>
            <#elseif field.saveFormType == "input_password">
                <el-input v-model="form.${field.javaTsFieldName}"
                          placeholder="请输入${field.showName}"
                          show-password/>
            <#elseif field.saveFormType == "slider">
                <el-slider v-model="form.${field.javaTsFieldName}"
                           :min="0"
                           :max="100"
                           :show-input="true"
                           :show-stops="true"
                           :step="1"/>
            <#elseif field.saveFormType == "rate">
                <el-rate v-model="form.${field.javaTsFieldName}"
                         :max="5"/>
            <#elseif field.saveFormType == "color">
                <el-color-picker v-model="form.${field.javaTsFieldName}"/>
            <#elseif field.saveFormType == "select" && field.dictTypeName?has_content>
                <el-select v-model="form.${field.javaTsFieldName}"
                           placeholder="请选择${field.showName}">
                    <el-option
                            v-for="(value,key) in props.dictData['${field.dictTypeName}']"
                            :key="key"
                            :value="value"
                            :label="value"/>
                </el-select>
            <#elseif field.saveFormType == "radio" && field.dictTypeName?has_content>
                <el-radio-group v-model="form.${field.javaTsFieldName}">
                    <el-radio
                            v-for="(value,key) in props.dictData['${field.dictTypeName}']"
                            :key="key"
                            :value="value">
                        {{ value }}
                    </el-radio>
                </el-radio-group>
            <#elseif field.saveFormType == "switch" && field.dictTypeName?has_content>
            <#--  只获取字典的前两个字段   -->
                <el-switch v-model="form.${field.javaTsFieldName}"
                           :active-text="props.dictData['${field.dictTypeName}'][0].label"
                           :inactive-text="props.dictData['${field.dictTypeName}'][1].label"/>
            <#elseif field.saveFormType == "file">
                <file-upload
                        v-model="form.${field.javaTsFieldName}"
                        :limit="1"
                        :show-file-list="true"
                        :show-tip="true"
                />
            <#elseif field.saveFormType == "image">
                <file-upload
                        v-model="form.${field.javaTsFieldName}"
                        :file-type="['png', 'jpg', 'jpeg', 'gif','svg','webp']"
                        :limit="1"
                        :list-type="'picture-card'"
                        :show-file-list="true"
                        :show-tip="true"
                />
            <#elseif field.saveFormType == "markdown">
                <Markdown v-model="form.${field.javaTsFieldName}"
                          height="400px"
                          mode="edit"
                          placeholder="请输入${field.showName}"
                          save-path="demo/markdown"/>
            <#elseif field.saveFormType == "text_area">
                <el-input type="textarea" v-model="form.${field.javaTsFieldName}"
                          placeholder="请输入${field.showName}"/>
            <#elseif field.saveFormType == "date">
                <el-date-picker v-model="form.${field.javaTsFieldName}"
                                format="YYYY/MM/DD"
                                type="date"
                                placeholder="选择${field.showName}"
                                value-format="YYYY-MM-DD"/>
            <#elseif field.saveFormType == "datetime">
                <el-date-picker v-model="form.${field.javaTsFieldName}"
                                format="YYYY/MM/DD HH:mm:ss"
                                type="datetime"
                                placeholder="选择${field.showName}"
                                value-format="YYYY-MM-DD HH:mm:ss"/>
            <#else>
                <el-input v-model="form.${field.javaTsFieldName}"
                          placeholder="请输入${field.showName}"/>
            </#if>
            </el-form-item>
        </#list>
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
import {${table.className}Form} from ${type_base_path};
import {${table.className}API} from ${api_base_path};
import {DictType} from "@/api/system/dict-data/type";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useCrudActions} from "@/hooks/useCrudActions";
import {FormInstance, FormRules} from "element-plus";

// 组件定义
defineOptions({
    name: "${table.className}ManageDialog",
    inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
    currentClickRowId: ${mapFields.pk[0].tsType} | undefined;
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
} = useCrudActions<${mapFields.pk[0].tsType}, ${table.className}Form>(${table.className}API.SAVE.request, ${table.className}API.UPDATE.request, undefined, undefined);
// 初始校验规则
const initialForm: ${table.className}Form = {
<#list mapFields.form as field>
    ${field.javaTsFieldName}: undefined,
</#list>
}
const rules: FormRules = {
<#list mapFields.form as field>
    <#if field.isRequired>
    ${field.javaTsFieldName}: [{required: true, message: "请输入${field.showName}", trigger: "blur"}],
    </#if>
</#list>
}
// 数据
const formRef = ref<FormInstance | null>(null);          // 字典数据表单
const form = ref<${table.className}Form>({...initialForm});

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
<#-- @formatter:on -->
