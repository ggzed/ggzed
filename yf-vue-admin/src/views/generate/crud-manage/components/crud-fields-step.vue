<template>
  <div v-loading="loading" class="crud-fields-step">
    <el-card class="table-container">
      <el-table
          ref="tableRef"
          :data="forms"
          class="fields-table"
          highlight-current-row
          row-key="id"
      >
        <el-table-column align="center" label="列名" min-width="80px" prop="columnName"/>
        <el-table-column align="center" label="列类型" min-width="80px" prop="columnType"/>
        <el-table-column align="center" label="字段名" min-width="140px" prop="javaTsFieldName">
          <template #default="scope">
            <el-input v-model="scope.row.javaTsFieldName" clearable placeholder="请输入字段名"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="列展示名" min-width="180px" prop="showName">
          <template #default="scope">
            <el-input v-model="scope.row.showName" clearable placeholder="列展示名"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="Java类型" min-width="120px" prop="javaType">
          <template #default="scope">
            <el-select v-model="scope.row.javaType" placeholder="请选择Java类型">
              <el-option v-for="(value,key) in dictData[DictType.JAVA_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="TS类型" min-width="100px" prop="tsType">
          <template #default="scope">
            <el-select v-model="scope.row.tsType" placeholder="请选择TS类型">
              <el-option v-for="(value,key) in dictData[DictType.TS_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="主键" min-width="55px" prop="isPk">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isPk"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="自增" min-width="55px" prop="isIncrement">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isIncrement"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="查询" min-width="55px" prop="isQuery">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isQuery"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="展示" min-width="55px" prop="isShow">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isShow"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="表单" min-width="55px" prop="isForm">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isForm"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="必填" min-width="55px" prop="isRequired">
          <template #default="scope">
            <el-checkbox v-model="scope.row.isRequired"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="表单展示类型" min-width="110px" prop="showType">
          <template #default="scope">
            <el-tag v-if="!scope.row.isShow">None</el-tag>
            <el-select v-else v-model="scope.row.showType" placeholder="表单展示类型">
              <el-option v-for="(value,key) in dictData[DictType.SHOW_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="新增/修改表单类型" min-width="140px" prop="saveFormType">
          <template #default="scope">
            <el-tag v-if="!scope.row.isForm">None</el-tag>
            <el-select v-else v-model="scope.row.saveFormType" placeholder="新增/修改表单类型">
              <el-option v-for="(value,key) in dictData[DictType.FORM_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="查询方式" min-width="100px" prop="queryType">
          <template #default="scope">
            <el-tag v-if="!scope.row.isQuery">None</el-tag>
            <el-select v-else v-model="scope.row.queryType" placeholder="请选择查询方式">
              <el-option v-for="(value,key) in dictData[DictType.QUERY_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="查询类型" min-width="140px" prop="queryFormType">
          <template #default="scope">
            <el-tag v-if="!scope.row.isQuery">None</el-tag>
            <el-select v-else v-model="scope.row.queryFormType" placeholder="请选择查询类型">
              <el-option v-for="(value,key) in dictData[DictType.QUERY_FORM_TYPE]" :label="value"
                         :value="key"/>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column align="center" label="字典类型" min-width="120px" prop="dictTypeName">
          <template #default="scope">
            <el-tag v-if="scope.row.isPk" type="danger">Disabled</el-tag>
            <el-select v-else v-model="scope.row.dictTypeName" placeholder="请选择字典类型">
              <el-option v-for="item in dictType" :label="item.label"
                         :value="item.value"/>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button type="danger" @click="props.quitConfig">退 出</el-button>
        <el-button type="success" @click="saveAndExit">保存并退出</el-button>
        <el-button type="warning" @click="skipStep">跳 过</el-button>
        <el-button type="primary" @click="goToPreStep">返回上一步</el-button>
        <el-button type="primary" @click="goToNextStep">保存进入下一步</el-button>
      </template>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import Sortable, { SortableEvent } from "sortablejs";
import {GenTableFieldsForm} from "@/api/generate/crud-code/type";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";
import {DictTypeAPI} from "@/api/system/dict-type";

// 组件定义
defineOptions({
  name: "CrudFieldsStep",
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
const loading = ref<boolean>(true);
const forms = ref<GenTableFieldsForm[]>([]);
const dictType = ref<OptionType[]>([])
const dictData = await useDictionary([
  DictType.JAVA_TYPE,
  DictType.TS_TYPE,
  DictType.DB_TYPE,
  DictType.FORM_TYPE,
  DictType.QUERY_FORM_TYPE,
  DictType.QUERY_TYPE,
  DictType.SHOW_TYPE,
])           // 数据字典
const tableRef = ref();

// 方法
/**
 * 保存并退出
 */
async function saveAndExit() {
  await save();
  props.quitConfig();
}

/**
 * 进入上一步
 */
function goToPreStep() {
  active.value = active.value - 1;
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
  // 保存修改
  await GenerateCrudAPI.TABLE_FIELDS_UPDATE.request(props.curdManageId, forms.value);
}

function nextActive() {
  // 进入下一步
  active.value = active.value + 1;
}

// 生命周期
onMounted(async () => {
  await GenerateCrudAPI.TABLE_FIELDS_FORM.request(props.curdManageId).then(({data}) => {
    forms.value = data;
  }).finally(() => {
    loading.value = false;
  })
  // 初始化拖拽排序
  const tbody = tableRef.value.$el.querySelector('.el-table__body-wrapper tbody');
  Sortable.create(tbody, {
    animation: 150,
    onEnd: (evt: SortableEvent) => {
      const { oldIndex, newIndex } = evt;
      if (oldIndex !== newIndex && oldIndex !== undefined && newIndex !== undefined) {
        // 交换位置
        const oldItem = forms.value[oldIndex];
        const newItem = forms.value[newIndex];
        if (oldItem && newItem) {
          const temp = oldItem.sort;
          oldItem.sort = newItem.sort;
          newItem.sort = temp;
        }
      }
    }
  });

  DictTypeAPI.OPTIONS.request().then(({data}) => {
    dictType.value = data
  });
  // 提示用户可拖拽
  setTimeout(() => {
    ElNotification.info({title: "温馨提示", message: "字段顺序可拖拽调整"});
  }, 0)
  // 提示用户可缩小浏览器
  setTimeout(() => {
    ElNotification.info({title: "温馨提示", message: "由于可操作内容较多,可以缩小浏览器进行修改"});
  }, 0)
})
</script>

<style lang="scss" scoped>
/* 样式 */
:deep(.el-table__row) {
  cursor: pointer;
}

.fields-table {
  min-height: 200px;
}
</style>
