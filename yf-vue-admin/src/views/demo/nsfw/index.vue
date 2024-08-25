<template>
  <div class="app-container">
    <!--  描述  -->
    <el-link :underline="false" href="https://element-plus.org" target="_blank">
      <el-icon slot="reference" :size="20" style="cursor: pointer">
        <QuestionFilled/>
      </el-icon>
      内容展示介绍 :
    </el-link>
    <div class="nsfw-container">
      <!--    头像      -->
      <el-upload
          :before-upload="beforeFileUpload"
          :http-request="uploadFile"
          :show-file-list="false"
          class="upload-area"
          drag
      >
        <el-image v-if="imageUrl != ''" :src="imageUrl"/>
        <div v-else class="upload-tip">
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            Drop file here or <em>click to upload</em>
          </div>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            该文件不会存储到服务器(支持 jpg、png、jpeg 格式)
          </div>
        </template>
      </el-upload>
      <div class="analyzer-container">
        <div v-for="(value, key) in analyzerResult" :key="key" class="analyzer-item">
          <el-tag effect="dark" type="info">{{ key }}</el-tag>
          <el-tag effect="plain" type="success">{{ value }}</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>

import {UploadRawFile, UploadRequestOptions} from "element-plus";
import {NsfwAPI} from "@/api/demo/nsfw";

defineOptions({
  name: "Nsfw",
  inheritAttrs: false,
});
// 数据
const loading = ref(false);
const analyzerResult = ref<Record<string, string>>({});
const imageUrl = ref<string>("");


// 方法
function beforeFileUpload(rawFile: UploadRawFile) {
  if (!NsfwAPI.CHECK.allowedFileTypes.includes(rawFile.type)) {
    ElMessage.error('文件格式错误')
    return false
  } else if (rawFile.size > NsfwAPI.CHECK.maxFileSize) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  } else if (rawFile.name.length <= 0 || rawFile.name.length >= 100) {
    ElMessage.error('文件名不能超过100个字符')
    return false
  }
  return true
}

function uploadFile(options: UploadRequestOptions) {
  // 注 : 对于上传成功 , 使用 on-success 也可行，其中参数 response 可获取到当前 resolve(unknown) 值
  return new Promise((resolve, reject) => {
    // 1. 执行上传操作
    const file = options.file;
    NsfwAPI.CHECK.request(file).then(({data}) => {
      // 2. 获取分析结果
      analyzerResult.value = data;
      // 3. 删除临时文件，新增新文件
      if (imageUrl.value != "") {
        URL.revokeObjectURL(imageUrl.value);
      }
      imageUrl.value = URL.createObjectURL(file);
      // 4. data 传给 on-success
      resolve(data)
    })
  })
}
</script>

<style lang="scss" scoped>
/* 样式 */
.nsfw-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 40px;

  .el-image {
    max-width: 48vw;
  }

  .upload-tip {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    min-height: 42vh;
  }
}

.upload-area {
  min-width: 48vw;
}

.analyzer-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 10px;

  .analyzer-item {
    display: flex;
    align-items: center;
    gap: 5px;
  }
}
</style>
