<template>
  <div class="app-container">
    <!--  描述  -->
    <el-link :underline="false" href="https://juejin.cn/post/7407983735662362658" target="_blank">
      <el-icon slot="reference" :size="20" style="cursor: pointer">
        <QuestionFilled/>
      </el-icon>
      内容展示介绍 :
    </el-link>
    <el-row :gutter="20">
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
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
      </el-col>
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
        <el-text :style="{ whiteSpace: 'pre-line' }">{{ result }}</el-text>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>

import {UploadRawFile, UploadRequestOptions} from "element-plus";
import {OcrAPI} from "@/api/demo/ocr";

defineOptions({
  name: "Ocr",
  inheritAttrs: false,
});
// 数据
const imageUrl = ref<string>("");
const result = ref<string>("");

// 方法
function beforeFileUpload(rawFile: UploadRawFile) {
  if (!OcrAPI.DETECTION.allowedFileTypes.includes(rawFile.type)) {
    ElMessage.error('文件格式错误')
    return false
  } else if (rawFile.size > OcrAPI.DETECTION.maxFileSize) {
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
    OcrAPI.DETECTION.request(file).then(({data}) => {
      // 2. 获取分析结果
      result.value = data;
      // 3. 删除临时文件，新增新文件
      if (imageUrl.value != "") {
        URL.revokeObjectURL(imageUrl.value);
      }
      imageUrl.value = URL.createObjectURL(file);
      // 4. data 传给 on-success
      resolve(data)
    }).catch(() => {
      ElMessage.error("目前不支持测试");
    })
  })
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
