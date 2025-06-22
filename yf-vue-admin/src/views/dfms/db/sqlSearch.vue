<template>
  <div class="app-container">
    <!-- SQL 查询工具 -->
    <el-card class="sql-query-tool">
      <template #header>
        <div class="card-header">
          <span>SQL 查询工具</span>
          <el-button type="text" size="small">执行 SQL 查询并查看结果</el-button>
        </div>
      </template>

      <!-- 数据库选择 -->
      <el-select v-model="selectedDatabase" placeholder="请选择数据库" style="width: 200px;">
        <el-option
            v-for="db in databases"
            :key="db.id"
            :label="db.name"
            :value="db.id"
        />
      </el-select>

      <!-- SQL 输入框 -->
      <el-input
          v-model="sqlQuery"
          type="textarea"
          :rows="10"
          placeholder="请输入 SQL 查询语句"
          style="margin-top: 10px;"
      />

      <!-- 执行查询按钮 -->
      <el-button type="primary" @click="executeQuery" style="float: right; margin-top: 10px;">
        <i class="el-icon-caret-right"></i> 执行查询
      </el-button>
    </el-card>

    <!-- 查询结果展示 -->
    <el-card class="result-table" v-if="showResult">
      <template #header>
        <div class="card-header">
          <span>查询结果</span>
          <el-button type="text" size="small">共 {{ tableData.length }} 条记录</el-button>
        </div>
      </template>

      <el-table
          :data="tableData"
          border
          stripe
          height="300"
          style="width: 100%;"
          :header-cell-style="{ background: '#f5f7fa' }"
      >
        <!-- 动态列 -->
        <el-table-column
            v-for="(col, index) in columns"
            :key="sqlSearch"
            :prop="col"
            :label="col"
            min-width="120"
        />
      </el-table>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";

// 模拟数据库列表数据
const databases = [
  { id: 1, name: "postgres (13780)" },
  { id: 2, name: "readme_to_recover (43205)" }
];

const selectedDatabase = ref<number | undefined>(undefined);
const sqlQuery = ref<string>("");

const showResult = ref<boolean>(false);
const tableData = ref<any[]>([]);
const columns = ref<string[]>([]);

// 模拟执行 SQL 查询后的返回结果结构
function mockSqlQueryResult() {
  return {
    columns: ["id", "name", "email", "created_at"],
    rows: [
      { id: 1, name: "Alice", email: "alice@example.com", created_at: "2025-06-01" },
      { id: 2, name: "Bob", email: "bob@example.com", created_at: "2025-06-02" },
      { id: 3, name: "Charlie", email: "charlie@example.com", created_at: "2025-06-03" }
    ]
  };
}

// 执行查询逻辑
async function executeQuery() {
  if (!selectedDatabase.value) {
    ElMessage.error("请选择数据库");
    return;
  }

  if (!sqlQuery.value.trim()) {
    ElMessage.error("请输入 SQL 查询语句");
    return;
  }

  // 模拟调用 API 获取动态数据
  const result = mockSqlQueryResult();

  // 设置列名和数据
  columns.value = result.columns;
  tableData.value = result.rows;

  showResult.value = true;
  ElMessage.success("查询已执行");
}

// 生命周期
onMounted(() => {
  // 初始化默认选中第一个数据库
  if (databases.length > 0) {
    selectedDatabase.value = databases[0].id;
  }
});
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .sql-query-tool,
  .result-table {
    width: 100%;
    max-width: 800px;
    margin: 0 auto 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .result-table {
    overflow: hidden;
  }
}
</style>