<template>
  <!--  模态框  -->
  <el-dialog
      v-model="visible"
      :title="props.title"
      width="30%"
  draggable
  overflow
  @close="props.closeDialog()"
  >
  <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
    <el-form-item label="名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入名称" />
    </el-form-item>

    <!-- 字符集 下拉选择 -->
    <el-form-item label="字符集" prop="charset">
      <el-select v-model="form.charset" placeholder="请选择字符集" style="width: 100%">
        <el-option
            v-for="item in charsetOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
    </el-form-item>

    <!-- 排序规则 下拉选择 -->
    <el-form-item label="排序规则" prop="collation">
      <el-select v-model="form.collation" placeholder="请选择排序规则" style="width: 100%">
        <el-option
            v-for="item in collationOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
    </el-form-item>

    <!-- 保留策略 下拉选择 -->
    <el-form-item label="保留策略" prop="retentionPolicy">
      <el-select v-model="form.retentionPolicy" placeholder="请选择保留策略" style="width: 100%">
        <el-option
            v-for="item in retentionPolicyOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
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
import { DfmsDbForm } from "@/api/dfms/db/type";
import { DfmsDbAPI } from "@/api/dfms/db";
import { DictType } from "@/api/system/dict-data/type";
import { DeviceEnum } from "@/enums/DeviceEnum";
import { useCrudActions } from "@/hooks/useCrudActions";
import { FormInstance, FormRules } from "element-plus";

// 组件定义
defineOptions({
  name: "pgSave",
  inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(
    defineProps<{
      currentClickRowId: number | undefined;
      visible: boolean;
      title: string;
      dictData?: Record<DictType | string, Record<any, string>>;
      device?: DeviceEnum;
      closeDialog: (callback?: () => void) => void;
      loadData: (callback?: () => void) => Promise<void>;
    }>(),
    {
      device: DeviceEnum.DESKTOP,
      dictData: () => ({}),
    }
);

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void;
}>();

// hooks
const visible = useVModel(props, "visible", emits);
const {
  saveData,
  updateData
} = useCrudActions<number, DfmsDbForm>(
    DfmsDbAPI.SAVE.request,
    DfmsDbAPI.UPDATE.request,
    undefined,
    undefined
);

// PostgreSQL 字符集选项
const charsetOptions = [
  { label: "UTF8", value: "UTF8" },
  { label: "SQL_ASCII", value: "SQL_ASCII" },
  { label: "LATIN1", value: "LATIN1" },
  { label: "LATIN2", value: "LATIN2" },
  { label: "WIN1250", value: "WIN1250" },
];

// PostgreSQL 排序规则选项
const collationOptions = [
  { label: "default", value: "default" },
  { label: "en_US.utf8", value: "en_US.utf8" },
  { label: "zh_CN.utf8", value: "zh_CN.utf8" },
  { label: "de_DE.utf8", value: "de_DE.utf8" },
  { label: "C", value: "C" },
];

// 保留策略选项
const retentionPolicyOptions = [
  { label: "30天", value: "30" },
  { label: "90天", value: "90" },
  { label: "180天", value: "180" },
  { label: "360天", value: "360" },
  { label: "永久", value: "forever" },
];

// 初始校验规则
const initialForm: DfmsDbForm = {
  name: undefined,
  charset: undefined,
  collation: undefined,
  size: undefined,
  tables: undefined,
  type: 2,
  retentionPolicy: undefined,
  sequenceNumber: undefined,
  dataPointsNumber: undefined,
  dimension: undefined,
  vectorNumber: undefined,
  indexType: undefined,
  status: undefined,
};

const rules: FormRules = {
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  charset: [{ required: true, message: "请选择字符集", trigger: "change" }],
  collation: [{ required: true, message: "请选择排序规则", trigger: "change" }],
  retentionPolicy: [{ required: true, message: "请选择保留策略", trigger: "change" }],
};

// 数据
const formRef = ref<FormInstance | null>(null); // 表单引用
const form = ref<DfmsDbForm>({ ...initialForm });

// 方法
async function submitForm() {
  const isValid = await formRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回

  // 校验通过后执行 API 请求
  if (props.currentClickRowId) {
    await updateData(props.currentClickRowId, form.value, () => {
      props.closeDialog();
      props.loadData();
    });
  } else {
    await saveData(form.value, () => {
      props.closeDialog();
      props.loadData();
    });
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.el-select {
  width: 100%;
}
</style>