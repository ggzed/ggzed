import {reactive, Ref} from 'vue';
import type {FormInstance, FormRules} from 'element-plus';

interface FormManagement<T> {
    /**
     * 表单实例
     */
    form: T;
    /**
     * 表单规则
     */
    rules: FormRules;
    /**
     * 重置表单信息
     */
    resetForm: () => void;
    /**
     * 处理表单提交
     * @param onSubmit
     */
    handleSubmit: (onSubmit: (form: T) => void) => void;
}

export function useFormManagement<T extends Record<string, any>>(initialForm: T, initialRules: FormRules, formRef: Ref<FormInstance | null>): FormManagement<T> {
    const form = reactive({...initialForm}) as T;

    const rules = reactive({...initialRules});

    function resetForm() {
        // 表单重置
        formRef.value?.resetFields();
        formRef.value?.clearValidate();
        // 数据重置
        Object.assign(form, initialForm);
    }

    function handleSubmit(onSubmit: (form: T) => void) {
        formRef.value?.validate((isValid: boolean) => {
            if (isValid && typeof onSubmit === 'function') {
                onSubmit(form);
            }
        });
    }

    return {
        form,
        rules,
        resetForm,
        handleSubmit
    };
}
