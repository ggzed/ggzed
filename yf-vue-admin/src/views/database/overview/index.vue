<!-- ParentComponent.vue -->
<template>
  <el-row :gutter="20">
    <el-col :span="6" v-for="(card, index) in cards" :key="index">
      <database-up-card
          :title="card.title"
          :count="card.count"
          :capacity="card.capacity"
          :status="card.status"
      />
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12" style="margin-bottom: 10px;">
      <database-type-card/>
    </el-col>
    <el-col :span="12" style="margin-bottom: 10px;">
      <database-size-card/>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref } from 'vue';

const cards = ref([
  { title: '关系型数据库', count: '5', capacity: '2.5TB', status: '运行中' },
  { title: '时序数据库', count: '3', capacity: '1.8TB', status: '运行中' },
  { title: '地理空间数据库', count: '2', capacity: '950GB', status: '运行中' },
  { title: '向量数据库', count: '4', capacity: '1.2TB', status: '运行中' }
]);
</script>