<template>
  <div class="crud-menu-step">
    <el-card class="table-container">
      <template #header>
        <el-text v-if="allowEdit" type="info">更多配置请绑定后到菜单页面进行修改</el-text>
        <el-text v-else type="warning">菜单已绑定,请前往菜单管理页面修改具体菜单 ( 注 : 以下内容不为实际菜单信息 )
        </el-text>
      </template>
      <el-row :gutter="20">
        <el-col :span="14">
          <el-form
              ref="formRef"
              :model="form"
              :rules="rules"
              label-width="110px">
            <el-form-item label="父级菜单 :" prop="parentId">
              <el-tree-select
                  v-model="form.parentId"
                  :data="menuOptions"
                  :disabled="!allowEdit"
                  :render-after-expand="false"
                  check-strictly
                  filterable
                  placeholder="选择上级菜单"
              />
            </el-form-item>
            <el-form-item label="菜单名 :" prop="menuName">
              <el-input v-model="form.menuName" :disabled="!allowEdit" clearable placeholder="请输入菜单名"/>
            </el-form-item>
            <el-form-item label="显示状态 :" prop="hidden">
              <el-radio-group v-model="form.hidden" :disabled="!allowEdit">
                <el-radio v-for="optionType in HiddenStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                    optionType.label
                  }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="图标 :" prop="icon">
              <icon-selector v-model="form.icon" :disabled="!allowEdit"/>
            </el-form-item>
            <el-form-item label="路由路径 :">
              <el-input v-model="routePath" disabled></el-input>
            </el-form-item>
            <el-form-item label="组件路径 :">
              <el-input v-model="componentPath" disabled></el-input>
            </el-form-item>

            <el-form-item label="权限标识 :">
              <el-input v-model="listPermission" disabled></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="10">
          <el-card class="button-list-card">
            <template #header>
              <div class="card-header">
                <span class="title">菜单对应按钮</span>
                <el-tag size="small" type="info">权限配置</el-tag>
              </div>
            </template>
            <div class="button-card">
              <div class="button-card__header">
                <el-icon>
                  <Plus/>
                </el-icon>
                <span class="button-name">新增{{ genTableForm.tableComment }}</span>
              </div>
              <div class="button-card__content">
                <el-tag size="small" type="success">权限标识</el-tag>
                <el-tag type="primary">{{ genTableForm.moduleName }}:{{ genTableForm.businessName }}:save</el-tag>
              </div>
            </div>
            <div class="button-card">
              <div class="button-card__header">
                <el-icon>
                  <Delete/>
                </el-icon>
                <span class="button-name">删除{{ genTableForm.tableComment }}</span>
              </div>
              <div class="button-card__content">
                <el-tag type="success">权限标识</el-tag>
                <el-tag type="danger">{{ genTableForm.moduleName }}:{{ genTableForm.businessName }}:delete</el-tag>
              </div>
            </div>
            <div class="button-card">
              <div class="button-card__header">
                <el-icon>
                  <Edit/>
                </el-icon>
                <span class="button-name">编辑{{ genTableForm.tableComment }}</span>
              </div>
              <div class="button-card__content">
                <el-tag type="success">权限标识</el-tag>
                <el-tag type="warning">{{ genTableForm.moduleName }}:{{ genTableForm.businessName }}:update</el-tag>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <template #footer>
        <el-button type="warning" @click="skipStep">跳过该步骤</el-button>
        <el-button type="primary" @click="goToPreStep">返回上一步</el-button>
        <el-button type="primary" @click="goToNextStep">绑定进入下一步</el-button>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {GenTableForm, GenTableMenuForm} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {FormInstance, FormRules} from "element-plus";
import {Delete, Edit, Plus} from '@element-plus/icons-vue'
import {HiddenStatusEnum} from "@/constants/system";

// 组件定义
defineOptions({
  name: "CrudMenuStep",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 目前展示的 step
  active: number;
  // 当前修改的 id
  curdManageId: number;
  // 删除退出当前页
  quitConfig: () => void;
}>(), {});

const emits = defineEmits<{
  (event: "update:active", active: number): void
}>()

// hooks
const active = useVModel(props, 'active', emits)
// 数据
const formRef = ref<FormInstance | null>(null);          // 部门表单
const form = ref<GenTableMenuForm>({});
const genTableForm = ref<GenTableForm>({});
const rules: FormRules = {
  parentId: [{required: true, message: "请选择目录", trigger: "blur"}],
  menuName: [{required: true, message: "请填写菜单名", trigger: "blur"}],
}
const allowEdit = ref<boolean>(false);
const menuOptions = ref<OptionType[]>();  // 菜单操作列表

// 计算路由路径
const routePath = computed(() => {
  return `${genTableForm.value?.moduleName || ''}/${genTableForm.value?.businessName || ''}`
})
// 计算组件路径
const componentPath = computed(() => {
  return `${genTableForm.value?.moduleName || ''}/${genTableForm.value?.businessName || ''}/index`
})
// 计算权限标识
const listPermission = computed(() => {
  return `${genTableForm.value?.moduleName || ''}:${genTableForm.value?.businessName || ''}:list`
})

// 方法
/**
 * 跳过
 */
function skipStep() {
  nextActive();
}

/**
 * 进入下一步
 */
async function goToNextStep() {
  await save()
  nextActive();
}

function goToPreStep() {
  active.value = active.value - 1;
}

async function save() {
  if (allowEdit.value) {
    const isValid = await formRef.value?.validate(); // 使用 await 简化验证逻辑
    if (!isValid) return; // 验证未通过，直接返回
    // 保存修改
    await GenerateCrudAPI.MENU.request(props.curdManageId, form.value);
  }
}

function nextActive() {
  active.value += 1;
}

// 生命周期
onMounted(async () => {
  // 加载表单数据
  GenerateCrudAPI.MENU_OPTIONS.request().then(({data}) => {
    menuOptions.value = data;
  })

  await GenerateCrudAPI.TABLE_FORM.request(props.curdManageId).then(({data}) => {
    genTableForm.value = data;
    allowEdit.value = data.menuId == null;
    form.value.menuName = data.tableComment + "管理";
    form.value.hidden = Number(HiddenStatusEnum.OPTIONS_RADIO[0].value);
  })
})
</script>

<style lang="scss" scoped>
.button-list-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      font-size: 16px;
      font-weight: 600;
    }
  }

  .button-card {
    background-color: var(--el-fill-color-blank);
    border: 1px solid var(--el-border-color-light);
    border-radius: 4px;
    padding: 12px;
    margin-bottom: 12px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }

    &:last-child {
      margin-bottom: 0;
    }

    &__header {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      .el-icon {
        margin-right: 8px;
        font-size: 16px;
        color: var(--el-color-primary);
      }

      .button-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--el-text-color-primary);
      }
    }

    &__content {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }
}
</style>
