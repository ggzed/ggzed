<template>
  <div class="crud-table-step">
    <el-card class="table-container">
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          inline
          label-width="110px">
        <el-form-item label="作者:" prop="functionAuthor">
          <el-input v-model="form.functionAuthor" clearable placeholder="请输入作者名"/>
        </el-form-item>
        <el-form-item label="数据库表名:" prop="tableName">
          <el-input v-model="form.tableName" clearable disabled placeholder="数据库表名"/>
        </el-form-item>
        <el-form-item label="数据库备注:" prop="tableComment">
          <el-input v-model="form.tableComment" clearable disabled placeholder="数据库备注"/>
        </el-form-item>
        <el-form-item prop="className">
          <template #label>
            <div class="form-label">
              类名:
              <el-tooltip effect="light" placement="bottom">
                <template #content>
                  生成对象名 , 例如: com.${包名}.${模块名}.${业务名}.service.I<b>${类名}</b>Service
                </template>
                <el-icon class="form-label-icon">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input v-model="form.className" clearable placeholder="请输入类名"/>
        </el-form-item>
        <el-form-item prop="componentName">
          <template #label>
            <div class="form-label">
              组件名:
              <el-tooltip effect="light" placement="bottom">
                <template #content>
                  组件对应文件名 , 例如: <b>${组件名}</b>-table.vue 、<b>${组件名}</b>-search.vue
                </template>
                <el-icon class="form-label-icon">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input v-model="form.componentName" clearable placeholder="组件名"/>
        </el-form-item>
        <el-form-item label="后端生成类型:" prop="backEndType">
          <el-select v-model="form.backEndType" clearable placeholder="后端生成类型">
            <el-option v-for="(value,key) in dictData[DictType.BACK_END]" :label="value"
                       :value="key"/>
          </el-select>
        </el-form-item>
        <el-form-item label="前端生成类型:" prop="frontEndType">
          <el-select v-model="form.frontEndType" clearable placeholder="前端生成类型">
            <el-option v-for="(value,key) in dictData[DictType.FRONT_END]" :label="value"
                       :value="key"/>
          </el-select>
        </el-form-item>
        <el-form-item label="基础包名:" prop="packageName">
          <template #label>
            <div class="form-label">
              基础包名:
              <el-tooltip effect="light" placement="bottom">
                <template #content>
                  生成后端代码时的基础包名 , 例如: com.<b>${包名}</b>.${模块名}.${业务名}.service.I${类名}Service
                </template>
                <el-icon class="form-label-icon">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input v-model="form.packageName" clearable placeholder="请输入基础包名"/>
        </el-form-item>
        <el-form-item label="模块名:" prop="packageName">
          <template #label>
            <div class="form-label">
              模块名:
              <el-tooltip effect="light" placement="bottom">
                <template #content>
                  生成后端代码时的模块名 , 例如: com.${包名}.<b>${模块名}</b>.${业务名}.service.I${类名}Service
                </template>
                <el-icon class="form-label-icon">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input v-model="form.moduleName" clearable placeholder="请输入模块名"/>
        </el-form-item>
        <el-form-item label="业务名:" prop="businessName">
          <template #label>
            <div class="form-label">
              业务名:
              <el-tooltip effect="light" placement="bottom">
                <template #content>
                  生成后端代码时的模块名 , 例如: com.${包名}.${模块名}.<b>${业务名}</b>.service.I${类名}Service
                </template>
                <el-icon class="form-label-icon">
                  <QuestionFilled/>
                </el-icon>
              </el-tooltip>
            </div>
          </template>
          <el-input v-model="form.businessName" clearable placeholder="请输入业务名"/>
        </el-form-item>
        <el-form-item label="备注:" prop="remark">
          <el-input v-model="form.remark" clearable placeholder="备注信息" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="danger" @click="props.quitConfig">退 出</el-button>
        <el-button type="success" @click="saveAndExit">保存并退出</el-button>
        <el-button type="warning" @click="skipStep">跳 过</el-button>
        <el-button type="primary" @click="goToNextStep">保存进入下一步</el-button>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {GenTableForm} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {FormInstance, FormRules} from "element-plus";
import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {RouteConstant} from "@/constants/route";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";

// 组件定义
defineOptions({
  name: "CrudTableStep",
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
const route = useRoute();
const router = useRouter();
const tagsViewStore = useTagsViewStore();                                 // 页签
const form = ref<GenTableForm>({});
const formRef = ref<FormInstance | null>(null);          // 部门表单
const dictData = await useDictionary([DictType.BACK_END, DictType.FRONT_END])           // 数据字典
const rules: FormRules = {
  functionAuthor: [
    {type: 'string', min: 1, max: 64, message: '作者长度应在 1 到 64 个字符之间', trigger: 'blur'},
  ],
  tableName: [
    {required: true, message: '数据库表名不能为空', trigger: 'blur'},
  ],
  tableComment: [
    {required: true, message: '数据库表描述不能为空', trigger: 'blur'},
  ],
  className: [
    {required: true, message: '类名不能为空', trigger: 'blur'},
    {type: 'string', min: 1, max: 64, message: '类名长度应在 1 到 64 个字符之间', trigger: 'blur'},
  ],
  componentName: [
    {required: true, message: '前端 component 名不能为空', trigger: 'blur'},
    {type: 'string', min: 1, max: 64, message: '前端 component 名长度应在 1 到 64 个字符之间', trigger: 'blur'},
  ],
  backEndType: [
    {required: true, message: '后端生成类型不能为空', trigger: 'blur'},
  ],
  frontEndType: [
    {required: true, message: '前端生成类型不能为空', trigger: 'blur'},
  ],
  packageName: [
    {required: true, message: '主包名不能为空', trigger: 'blur'},
    {type: 'string', min: 1, max: 32, message: '主包名长度应在 1 到 32 个字符之间', trigger: 'blur'},
  ],
  moduleName: [
    {required: true, message: '模块名不能为空', trigger: 'blur'},
    {type: 'string', min: 1, max: 64, message: '模块名长度应在 1 到 64 个字符之间', trigger: 'blur'},
  ],
  businessName: [
    {required: true, message: '业务名不能为空', trigger: 'blur'},
    {type: 'string', min: 1, max: 64, message: '业务名长度应在 1 到 64 个字符之间', trigger: 'blur'},
  ]
};

// 方法
/**
 * 保存并退出
 */
async function saveAndExit() {
  await save();
  props.quitConfig();
}

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

async function save() {
  const isValid = await formRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 保存修改
  await GenerateCrudAPI.TABLE_UPDATE.request(props.curdManageId, form.value);
}

function nextActive() {
  active.value += 1;
}

// 生命周期
onMounted(async () => {
  await GenerateCrudAPI.TABLE_FORM.request(props.curdManageId).then(({data}) => {
    // 正常情况
    form.value = data;
    // 修改当前标签页的标题
    tagsViewStore.updateViewTitle(route.fullPath, "【" + data.tableName + "】Crud 生成配置");
  }).catch(() => {
    // 异常情况
    // 1. 删除标签
    const tagView: TagView = {
      name: route.name as string,
      title: route.meta.title || "",
      path: route.path,
      fullPath: route.fullPath,
    }
    tagsViewStore.removeTagView(tagView);
    tagsViewStore.removeCachedView(tagView);
    // 2. 跳转 404
    router.push(RouteConstant.NOT_FOUND_PATH);
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
.el-input {
  --el-input-width: 220px;
}

.el-select {
  --el-select-width: 220px;
}

.el-textarea {
  width: 220px;
}

.form-label {
  display: flex;
  align-items: center;
  padding-left: 3px;
}

.form-label-icon {
  margin-left: 2px;
  cursor: pointer;
}
</style>
