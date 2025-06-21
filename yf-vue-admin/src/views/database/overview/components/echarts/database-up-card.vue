<!-- DatabaseCard.vue -->
<script setup>
import { defineProps, defineOptions } from 'vue';

defineOptions({
  name: "DatabaseUpCard",
  inheritAttrs: false,
});

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  count: {
    type: [String, Number],
    required: true
  },
  capacity: {
    type: String,
    required: true
  },
  status: {
    type: String,
    required: true
  }
});
</script>

<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-menu"></el-button>
    </div>
    <div class="card-content">
      <h1>{{ count }}</h1>
      <p>容量: {{ capacity }}</p>
      <el-button type="primary" round>{{ status }}</el-button>
    </div>
  </el-card>
</template>

<style scoped>
.box-card {
  width: 100%;
  margin-bottom: 20px;
}

.card-content {
  text-align: left;
}

.card-content h1 {
  font-size: 24px;
  margin-bottom: 8px;
}

.card-content p {
  margin-bottom: 16px;
}

.el-button--primary {
  background-color: #000;
  border-color: #000;
  color: #fff;
}
</style>