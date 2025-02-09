<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      destroy-on-close
      draggable
      overflow
      @close="props.closeDialog"
  >
    <el-form
        ref="deptFormRef"
        :model="form"
        :rules="rules"
        label-width="110px"
    >
      <el-form-item label="父级部门 :" prop="parentId">
        <el-tree-select
            v-model="form.parentId"
            :data="deptOptions"
            :render-after-expand="false"
            check-strictly
            filterable
            placeholder="选择上级部门"
        />
      </el-form-item>

      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入名称"/>
      </el-form-item>

      <el-form-item
          label="状态 :"
          prop="status"
      >
        <el-radio-group v-model="form.status">
          <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
              optionType.label
            }}
          </el-radio>
        </el-radio-group>
      </el-form-item>


      <el-form-item label="排序 :" prop="sort">
        <el-input-number
            v-model="form.sort"
            :min="0"
            controls-position="right"
            style="width: 100px"
        />
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
import {DeviceEnum} from "@/enums/DeviceEnum";
import {EnableStatusEnum} from "@/constants/system";
import {DeptForm} from "@/api/system/dept/type";
import {FormInstance, FormRules} from "element-plus";
import {DeptAPI} from "@/api/system/dept";
import {useCrudActions} from "@/hooks/useCrudActions";
// 组件定义
defineOptions({
  name: "DeptManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 父节点Id
  parentId: number | undefined;
  // 当前点击节点ID
  currentClickRowId: number | undefined;
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  saveData,
  updateData
} = useCrudActions<number, DeptForm>(DeptAPI.SAVE.request, DeptAPI.UPDATE.request, DeptAPI.DELETE.request, DeptAPI.UPDATE_STATUS.request);
// 初始化数据
const initialForm: DeptForm = {
  id: undefined,
  name: "",
  status: 1,
  sort: 1,
}                                                             // dept 表单初始化数据
const rules: FormRules = {
  parentId: [{required: true, message: "请选择顶级部门", trigger: "blur"}],
  name: [{required: true, message: "请输入部门名称", trigger: "blur"}],
}                                                        // dept 表单校验规则
// 数据
const deptFormRef = ref<FormInstance | null>(null);          // 部门表单
const deptOptions = ref<OptionType[]>([{value: 0, label: "顶级菜单", children: []}]);  // 部门操作列表
const form = ref<DeptForm>({...initialForm});

// 方法
async function submitForm() {
  const isValid = await deptFormRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 校验通过后执行 API 请求
  if (form.value.id) {
    await updateData(form.value.id, form.value, () => {
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
  // 不需要 async , 在弱网环境下可能导致系统加载更慢
  form.value.id = props.currentClickRowId;
  form.value.parentId = props.parentId || 0;
  // 加载表单数据
  DeptAPI.OPTIONS.request().then(({data}) => {
    deptOptions.value[0].children = data;
  })
  if (form.value.id) {
    DeptAPI.FORM.request(form.value.id).then(({data}) => {
      Object.assign(form.value, data);
    })
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
