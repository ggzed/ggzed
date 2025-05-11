<template>
  <div class="search-container">
    <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
      <el-form-item label="用户名 :" prop="username">
        <el-input
            v-model="query.username"
            clearable
            placeholder="用户名"
            @keyup.enter="loadData"
        />
      </el-form-item>
      <el-form-item label="三方平台 :" prop="platformName">
        <el-select
            v-model="query.platformName"
            clearable
            placeholder="第三方平台"
            @change="loadData"
        >
          <el-option
              v-for="platform in platformList"
              :key="platform"
              :label="platform"
              :value="platform"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间 :" prop="startTime">
        <el-date-picker
            v-model="query.startTime"
            format="YYYY/MM/DD hh:mm:ss"
            placeholder="创建时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="截至时间 :" prop="endTime">
        <el-date-picker
            v-model="query.endTime"
            format="YYYY/MM/DD hh:mm:ss"
            placeholder="创建时间"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="props.loadData()">
          <template #icon>
            <search/>
          </template>
          搜索
        </el-button>
        <el-button plain type="primary" @click="props.resetQuery()">
          <template #icon>
            <refresh/>
          </template>
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
// 组件 props & emits
import {FormInstance} from "element-plus";
import {OauthPageQuery} from "@/api/system/oauth/type";
import {OauthAPI} from "@/api/system/oauth";

const props = withDefaults(defineProps<{
  query: OauthPageQuery;
  loadData: (callback?: () => void) => Promise<void>;                    // 加载数据函数
  resetQuery: (callback?: () => void) => Promise<void>;                  // 重置查询条件
}>(), {});

const emits = defineEmits<{
  (event: "update:query", query: OauthPageQuery): void
}>()
// hooks
const query = useVModel(props, 'query', emits)
// 数据
const queryFormRef = ref<FormInstance | null>(null);         // 查询表单
const platformList = ref<string[]>([]);
// 生命周期
onMounted(() => {
  OauthAPI.SUPPORT_PLATFORMS.request().then((res) => {
    platformList.value = res.data;
  })
})
</script>

<style lang="scss" scoped>
/* 样式 */
.search-container .el-input {
  --el-input-width: 120px;
}
</style>
