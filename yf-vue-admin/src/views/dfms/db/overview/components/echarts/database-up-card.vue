<template>
  <el-card class="table-container">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(card, index) in props.dataList" :key="index">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ card.name }}</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-menu"></el-button>
          </div>
          <div class="card-content">
            <h1>{{ card.num }}</h1>
            <p>容量: {{ card.size }}</p>
            <!-- 动态设置按钮颜色 -->
            <el-button
                :type="getButtonColor(card.status)"
                round>{{ card.status }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script lang="ts" setup>
import { DfmsDbPageQuery, DfmsDbPageVO, DfmsDbForm, DfmsDbOverviewVO } from "@/api/dfms/db/type";
import { DfmsDbAPI } from "@/api/dfms/db";
import { DeviceEnum } from "@/enums/DeviceEnum";
import { DictType } from "@/api/system/dict-data/type";
import { useDialogManage } from "@/hooks/useDialogManage";
import { useCrudActions } from "@/hooks/useCrudActions";
import { useSystemStore } from "@/store/modules/system";
import { useTableManagement } from "@/hooks/useTableManagement";
import { TableInstance } from "element-plus";

// 组件定义
defineOptions({
  name: "DfmsDbOverUp",
  inheritAttrs: false,
});

// 组件 props & emits
const props = withDefaults(defineProps<{
  dataList: DfmsDbOverviewVO[];
}>(), {dictData: () => ({})});

// 根据状态返回不同的按钮类型
const getButtonColor = (status: string): string => {
  switch (status) {
    case '运行中':
      return 'primary';
    case '停止':
      return 'danger';
    case '维护中':
      return 'warning';
    default:
      return 'info';
  }
}
</script>

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

/* 自定义按钮颜色 */
.el-button--primary {
  background-color: #007bff;
  border-color: #007bff;
  color: #fff;
}

.el-button--danger {
  background-color: #dc3545;
  border-color: #dc3545;
  color: #fff;
}

.el-button--warning {
  background-color: #ffc107;
  border-color: #ffc107;
  color: #000;
}

.el-button--info {
  background-color: #6c757d;
  border-color: #6c757d;
  color: #fff;
}
</style>