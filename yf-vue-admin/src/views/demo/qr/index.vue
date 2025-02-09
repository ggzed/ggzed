<template>
  <div class="app-container">
    <!--  描述  -->
    <el-link :underline="false" href="https://element-plus.org" target="_blank">
      <el-icon slot="reference" :size="20" style="cursor: pointer">
        <QuestionFilled/>
      </el-icon>
      内容展示介绍 :
    </el-link>
    <el-row :gutter="20">
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24" style="margin: 24px 0;">
        <el-form ref="qrFormRef" :inline="true" :model="form" :rules="rules" label-width="80"
                 @submit.prevent>
          <el-form-item label="内容 :" prop="content">
            <el-input v-model="form.content" placeholder="请输入二维码内容"/>
          </el-form-item>
          <el-form-item label="宽度 :" prop="width">
            <el-input v-model.number="form.width" placeholder="请输入二维码宽度" type="number"/>
          </el-form-item>
          <el-form-item label="高度 :" prop="width">
            <el-input v-model.number="form.height" placeholder="请输入二维码高度" type="number"/>
          </el-form-item>
          <el-form-item label="边距 :" prop="margin">
            <el-input v-model.number="form.margin" placeholder="请输入二维码边距" type="number"/>
          </el-form-item>
          <el-form-item label="前景色 :" prop="foreColorHex">
            <el-color-picker v-model="form.foreColorHex" :predefine="['#000000']"/>
          </el-form-item>
          <el-form-item label="背景色 :" prop="backColorHex">
            <el-color-picker v-model="form.backColorHex" :predefine="['#FFFFFF']"/>
          </el-form-item>
          <el-form-item label="纠错码 :" prop="errorCorrection">
            <el-select v-model="form.errorCorrection" placeholder="请选择纠错级别" style="width: 60px">
              <el-option
                  v-for="(label, value) in ErrorCorrectionLevel"
                  :key="value"
                  :label="label"
                  :value="value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="logo :" prop="logo">
            <el-upload
                :before-upload="beforeFileUpload"
                :http-request="uploadFile"
                :show-file-list="false"
                class="upload-area"
                drag
            >
              <el-image v-if="tempImageUrl != ''" :src="tempImageUrl" style="width: 200px; height: 200px"/>
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
          </el-form-item>
          <el-form-item label="logo缩放系数 :" label-width="120" prop="ratio">
            <el-input v-model.number="form.ratio" placeholder="请输入二维码缩放系数" type="number"/>
          </el-form-item>
        </el-form>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="handleReset">重 置</el-button>
      </el-col>
      <el-col :lg="12" :md="12" :sm="12" :span="12" :xs="24">
        <el-image :src="result" style="border: 3px dashed #797979"></el-image>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import {ErrorCorrectionLevel, GenQrCodeForm} from "@/api/demo/qr/type";
import {FormInstance, FormRules, UploadRawFile, UploadRequestOptions} from "element-plus";
import {QrAPI} from "@/api/demo/qr";

defineOptions({
  name: "Qr",
  inheritAttrs: false,
});

const initForm: GenQrCodeForm = {
  content: "",
  width: 200,
  height: 200,
  margin: 1,
  foreColorHex: "#000000",
  backColorHex: "#ffffff",
  errorCorrection: ErrorCorrectionLevel.H,
  ratio: 5
}

const tempImageUrl = ref<string>("");
const qrFormRef = ref<FormInstance | null>(null);

const rules: FormRules = {
  content: [
    {required: true, message: '生成二维码的内容不能为空', trigger: 'blur'}
  ],
  width: [
    {required: true, message: '二维码宽度不能为空', trigger: 'blur'},
    {type: 'number', min: 100, max: 1000, message: '宽度不能小于100像素', trigger: 'blur'}
  ],
  height: [
    {required: true, message: '二维码高度不能为空', trigger: 'blur'},
    {type: 'number', min: 100, max: 1000, message: '高度不能小于100像素', trigger: 'blur'}
  ],
  margin: [
    {required: true, message: '二维码边距不能为空', trigger: 'blur'},
    {type: 'number', min: 0, message: '边距不能小于0', trigger: 'blur'},
  ],
  foreColorHex: [
    {required: true, message: '前景色不能为空', trigger: 'blur'},
    {pattern: /^#[0-9A-Fa-f]{6}$/, message: '请输入有效的十六进制颜色代码', trigger: 'blur'}
  ],
  backColorHex: [
    {required: true, message: '背景色不能为空', trigger: 'blur'},
    {pattern: /^#[0-9A-Fa-f]{6}$/, message: '请输入有效的十六进制颜色代码', trigger: 'blur'}
  ],
  errorCorrection: [
    {required: true, message: '请选择二维码纠错级别', trigger: 'change'}
  ],
  logo: [
    {required: false, message: 'Logo 文件可选', trigger: 'change'},
  ],
  ratio: [
    {
      validator: (rule, value, callback) => {
        if (form.logo && !value) {
          callback(new Error('有Logo时必须填写缩放系数'));
        } else {
          callback();
        }
      }
    },
    {
      required: false, message: 'logo缩放系数可选', trigger: 'blur'
    },
    {
      type: 'number', min: 5, max: 20, message: "logo缩放系数范围为5-10", trigger: 'blur'
    }
  ]
}

// 数据
const form = ref<GenQrCodeForm>({...initForm});
const result = ref<string>("");  // base64编码


function beforeFileUpload(rawFile: UploadRawFile) {
  if (!QrAPI.GEN.allowedFileTypes.includes(rawFile.type)) {
    ElMessage.error('文件格式错误')
    return false
  } else if (rawFile.size > QrAPI.GEN.maxFileSize) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  } else if (rawFile.name.length <= 0 || rawFile.name.length >= 100) {
    ElMessage.error('文件名不能超过100个字符')
    return false
  }
  return true;
}

function uploadFile(options: UploadRequestOptions) {
  // 注 : 对于上传成功 , 使用 on-success 也可行，其中参数 response 可获取到当前 resolve(unknown) 值
  return new Promise((resolve) => {
    // 1. 执行上传操作
    const file = options.file;
    form.logo = file;
    // 2. 删除临时文件，新增新文件
    if (tempImageUrl.value != "") {
      URL.revokeObjectURL(tempImageUrl.value);
    }
    tempImageUrl.value = URL.createObjectURL(file);
    // 3. data 传给 on-success
    resolve(tempImageUrl.value)
  })
}

function submitForm() {
  QrAPI.GEN.request(form).then(({data}) => {
    result.value = data;
  })
}

async function handleReset() {
  await qrFormRef.value?.clearValidate();
  await qrFormRef.value?.resetFields();
  result.value = "";
  Object.assign(form.valu, {...initForm})
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
