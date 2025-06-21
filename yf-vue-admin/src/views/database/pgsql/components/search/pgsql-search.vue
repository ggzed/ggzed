<template>
  <div class="sql-query-tool">
    <el-form :model="queryForm" label-width="auto">
      <el-form-item label="数据库选择">
        <el-select v-model="queryForm.database" placeholder="请选择数据库">
          <el-option label="PostgreSQL (13780)" value="postgres"></el-option>
          <!-- 可以根据需要添加更多数据库选项 -->
        </el-select>
      </el-form-item>
      <el-form-item label="SQL 查询">
        <el-input type="textarea" v-model="queryForm.sqlQuery" rows="5"></el-input>
      </el-form-item>
      <el-button type="primary" @click="executeQuery">执行查询</el-button>
    </el-form>

    <!-- 查询结果展示 -->
    <el-table :data="queryResult" border style="width: 100%" v-if="queryResult.length > 0">
      <el-table-column
          v-for="column in columns"
          :key="column.prop"
          :prop="column.prop"
          :label="column.label"
      ></el-table-column>
    </el-table>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 组件定义
defineOptions({
  name: "PgsqlSearch",
  inheritAttrs: false,
});

interface Column {
  prop: string
  label: string
}

const queryForm = ref({
  database: 'postgres',
  sqlQuery: 'SELECT * FROM users LIMIT 10;'
})

const queryResult = ref<any[]>([])
const columns = ref<Column[]>([])

// 模拟执行 SQL 并返回结果
const fetchQueryResults = async (sql: string): Promise<any[]> => {
  // 实际应调用 API 接口执行 SQL
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        { id: 1, name: 'Alice', age: 25 },
        { id: 2, name: 'Bob', age: 30 }
      ])
    }, 1000)
  })
}

const executeQuery = async () => {
  try {
    const result = await fetchQueryResults(queryForm.value.sqlQuery)
    queryResult.value = result

    if (result.length > 0) {
      const keys = Object.keys(result[0])
      columns.value = keys.map(key => ({
        prop: key,
        label: key
      }))
    }

    ElMessage.success('查询成功')
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('SQL 查询执行失败，请检查语法或网络连接')
  }
}
</script>

<style lang="scss" scoped>
.sql-query-tool {
  padding: 20px;
  max-width: 800px;
  margin: auto;
}
.el-textarea {
  font-family: monospace;
}
</style>
