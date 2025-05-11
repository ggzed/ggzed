<template>
  <el-card class="demo-card">
    <template #header>
      <div class="card-header">
        <span>文件上传组件演示</span>
      </div>
    </template>

    <!-- 基础拖拽上传 -->
    <div class="demo-section">
      <h3>基础拖拽上传</h3>
      <p class="section-desc">支持拖拽上传，限制单个文件，默认大小20MB</p>
      <file-upload v-model="singleFile" @on-file-remove="fileRemove"/>
    </div>

    <!-- 图片上传 -->
    <div class="demo-section">
      <h3>图片上传</h3>
      <p class="section-desc">支持图片预览，限制jpg/png格式，大小5MB</p>
      <file-upload
          v-model="imageFile"
          :file-size="5"
          :file-type="['jpg', 'png']"
          list-type="picture"
          @on-file-remove="fileRemove"
      />
    </div>

    <!-- 图片卡片上传 -->
    <div class="demo-section">
      <h3>图片卡片上传</h3>
      <p class="section-desc">支持多图片上传，限制3张，每张2MB</p>
      <file-upload
          v-model="cardImages"
          :file-size="2"
          :limit="3"
          list-type="picture-card"
          @on-file-remove="fileRemove"
      />
    </div>

    <!-- 多文件上传 -->
    <div class="demo-section">
      <h3>多文件上传</h3>
      <p class="section-desc">支持多个文件上传，限制5个文件，每个10MB</p>
      <file-upload
          v-model="multipleFiles"
          :file-size="10"
          :limit="5"
          @on-file-remove="fileRemove"
      />
    </div>
  </el-card>
</template>

<script lang="ts" setup>
// 组件定义
import {UploadFile} from "element-plus";

defineOptions({
  name: "FileUploadDemo",
  inheritAttrs: false,
});

// 数据
const singleFile = ref<string>("");
const imageFile = ref<string>("");
const cardImages = ref<string[]>([]);
const multipleFiles = ref<string[]>([]);

// 方法
function fileRemove(uploadFile: UploadFile) {
  console.log("文件删除", uploadFile)
}

// 生命周期
</script>

<style lang="scss" scoped>
.demo-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
  }
}

.demo-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fff;

  &:last-child {
    margin-bottom: 0;
  }

  h3 {
    margin: 0 0 10px 0;
    font-size: 16px;
    color: #303133;
  }

  .section-desc {
    margin: 0 0 15px 0;
    font-size: 14px;
    color: #606266;
  }
}
</style>
