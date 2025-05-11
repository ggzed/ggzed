<template>
  <div class="file-upload">
    <el-upload
        ref="fileUpload"
        v-model="props.modelValue"
        v-model:file-list="fileList"
        :before-upload="beforeUpload"
        :disabled="props.disabled"
        :drag="props.listType == 'text'"
        :http-request="uploadFile"
        :limit="props.limit"
        :list-type="props.listType"
        :multiple="props.limit > 1"
        :on-exceed="onExceed"
        :on-preview="onPreview"
        :on-remove="onRemove"
        :show-file-list="props.showFileList"
    >
      <slot>
        <template v-if="props.listType == 'text'">
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            拖拽或者<em>点击上传</em>
          </div>
        </template>
        <template v-else-if="props.listType == 'picture'">
          <el-button type="primary">点击上传
            <el-icon class="el-icon--right">
              <Upload/>
            </el-icon>
          </el-button>
        </template>
        <template v-else-if="props.listType == 'picture-card'">
          <el-icon class="el-icon--upload">
            <Plus/>
          </el-icon>
        </template>
      </slot>

      <!-- 上传提示 -->
      <template v-if="props.showTip" #tip>
        <div class="el-upload__tip">
          <b>提示：</b>支持上传
          <b>{{ props.limit }}</b> 个文件，大小不超过
          <b>{{ props.fileSize }}MB</b>
          <template v-if="props.fileNameSize">
            ，文件名长度不超过 <b>{{ props.fileNameSize }}</b> 个字符
          </template>
          <template v-if="props.fileType.length">
            ，格式为 <b>{{ props.fileType.join('/') }}</b>
          </template>
        </div>
      </template>
    </el-upload>
    <!--  预览图  -->
    <el-image-viewer
        v-if="showPreview"
        :url-list="[previewImg]"
        @close="showPreview = false"
    />
  </div>
</template>

<script lang="ts" setup>
import {useUserStore} from "@/store/modules/user";
import {UploadFile, UploadInstance, UploadRawFile, UploadRequestOptions, UploadUserFile} from "element-plus";
import axios from "axios";
import {RequestConstant} from "@/constants/request";

defineOptions({
  name: "FileUpload",
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  modelValue: string | string[]           // v-model 绑定
  api?: string                            // 上传接口
  limit?: number                          // 数量限制
  attribute?: Record<string, any>         // 属性
  fileKey?: string                        // 文件上传 key
  fileNameSize?: number                   // 文件名长度限制
  fileSize?: number                       // 大小限制(MB)
  fileType?: string[]                     // 文件类型限制
  showTip?: boolean                       // 显示提示
  showFileList?: boolean                  // 显示文件列表
  listType?: 'text' | 'picture' | 'picture-card'  // 列表类型
  disabled?: boolean                      // 是否禁用
}>(), {
  api: '/file/unsafe/upload',
  limit: 1,
  attribute: () => ({}),
  fileKey: 'file',
  fileNameSize: 50,
  fileSize: 20,
  fileType: () => [],
  showTip: true,
  showFileList: true,
  listType: 'text',
  disabled: false
})

const emit = defineEmits<{
  (event: 'update:modelValue', value: string | string[]): void;
  (event: 'onFileRemove', uploadFile: UploadFile): void;
}>();

const userStore = useUserStore()
const showPreview = ref(false)
const previewImg = ref<string>("")
const fileUpload = ref<UploadInstance>()
const fileList = ref<UploadUserFile[]>([])

/**
 * 上传前处理
 * @param rawFile 文件
 */
function beforeUpload(rawFile: UploadRawFile) {
  const extension = rawFile.name.split('.').pop()?.toLowerCase() || ''

  if (props.fileType.length && !props.fileType.includes(extension)) {
    ElMessage.error(`仅支持 ${props.fileType.join('/')} 格式文件`)
    return false
  }

  const isLtSize = rawFile.size / 1024 / 1024 < props.fileSize
  if (!isLtSize) {
    ElMessage.error(`文件大小不能超过 ${props.fileSize}MB!`)
    return false
  }

  if (rawFile.name.length <= 0 || rawFile.name.length > props.fileNameSize) {
    ElMessage.error('头像文件名不能超过100个字符')
    return false
  }

  return true
}

/**
 * 上传文件
 * @param options 文件
 */
function uploadFile(options: UploadRequestOptions) {
  const formData = new FormData()
  formData.append(props.fileKey, options.file)

  // 构建其他请求参数
  if (props.attribute && props.attribute.length > 0) {
    Object.entries(props.attribute).forEach(([key, value]) => {
      formData.append(key, value)
    })
  }

  // 请求上传
  return new Promise((resolve) => {
    axios.post(import.meta.env.VITE_APP_API_URL + props.api, formData, {
      headers: {
        Authorization: RequestConstant.Header.AuthorizationPrefix + userStore.authInfo.accessToken,
        'Content-Type': 'multipart/form-data'
      }
    }).then((res) => {
      const data = res.data.data
      // 更新数据
      if (Array.isArray(props.modelValue)) {
        const newValue = [...props.modelValue, data]; // 避免直接修改 props
        emit("update:modelValue", newValue);
      } else {
        emit("update:modelValue", data);
      }
      // 更新文件URL
      const fileIndex = fileList.value.findIndex(item => item.uid === options.file.uid);
      if (fileIndex !== -1) {
        fileList.value[fileIndex].url = data;
      }
      resolve(data);
    }).catch((err) => {
      ElMessage.error('上传失败')
      return Promise.reject(err)
    })
  })
}

/**
 * 文件超出个数限制
 * @param files 文件集合
 * @param uploadFiles 上传文件集合
 */
function onExceed(files: File[], uploadFiles: UploadUserFile[]) {
  if (props.limit > 1) {
    ElMessage.error(`只能上传 ${props.limit} 个文件`)
  } else if (props.limit == 1) {
    // 删除旧文件
    fileUpload.value!.handleRemove(uploadFiles[0] as UploadFile)
    // 清空file-list
    fileUpload.value!.clearFiles()
    // 更新 modelValue
    if (Array.isArray(props.modelValue)) {
      emit('update:modelValue', [])
    } else {
      emit('update:modelValue', '')
    }
    // 上传文件
    const file = files[0] as UploadRawFile
    fileUpload.value!.handleStart(file)
  } else {
    ElMessage.error('文件数量配置错误')
  }
}

/**
 * 预览文件
 * @param file 文件
 */
function onPreview(file: UploadUserFile) {
  // 如果是图片类型，尝试预览
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'tiff', 'heif'].includes(file.name.split('.').pop() || '')) {
    // 如果file.url为空，尝试从modelValue中获取
    const fileUrl = file.url || (Array.isArray(props.modelValue) 
      ? props.modelValue.find(url => url.includes(file.name))
      : props.modelValue);
      
    if (fileUrl) {
      showPreview.value = true;
      previewImg.value = fileUrl;
    } else {
      ElMessage.warning('无法预览：文件URL不存在');
    }
  } else {
    // 非图片文件，尝试下载
    const fileUrl = file.url || (Array.isArray(props.modelValue) 
      ? props.modelValue.find(url => url.includes(file.name))
      : props.modelValue);
      
    if (fileUrl) {
      const a = document.createElement('a');
      a.href = fileUrl;
      a.download = file.name;
      a.click();
    } else {
      ElMessage.warning('无法下载：文件URL不存在');
    }
  }
}

/**
 * 移除文件
 */
function onRemove(uploadFile: UploadFile) {
  emit('onFileRemove', uploadFile)  // 触发移除事件
}

// 生命周期
onMounted(() => {
  // 校验 modelValue 类型
  if (props.limit > 1 && !Array.isArray(props.modelValue)) {
    throw new Error('modelValue 类型错误，limit > 1 时 modelValue 必须为数组')
  }

  // 如果需要展示文件列表
  if (props.showFileList && props.modelValue) {
    // 初始化文件列表
    if (Array.isArray(props.modelValue) && props.modelValue.length > 0) {
      props.modelValue.forEach((item: string) => {
        fileList.value.push({
          name: item.split('/').pop() || '',
          url: item
        })
      })
    } else if (typeof props.modelValue === 'string' && props.modelValue !== '') {
      fileList.value.push({
        name: props.modelValue.split('/').pop() || '',
        url: props.modelValue
      })
    }
  }
})
</script>

<style lang="scss" scoped>
.el-upload__tip {
  font-size: 12px;
  color: #606266;
  margin-top: 7px;

  b {
    color: #f56c6c;
  }
}
</style>
