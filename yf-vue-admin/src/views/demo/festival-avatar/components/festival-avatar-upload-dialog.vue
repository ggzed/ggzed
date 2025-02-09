<template>
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="device === DeviceEnum.MOBILE ? '95%' : '42%'"
      class="festival-avatar-upload-dialog"
      destroy-on-close
      draggable
      overflow
  >
    <div style="height: 45vh">
      <vue-cropper
          ref="cropper"
          :autoCrop="true"
          :centerBox="true"
          :fixed="true"
          :fixedNumber="[1,1]"
          :img="imgTemp"
          :outputType="'png'"
      />
    </div>
    <template #footer>
      <div class="festival-avatar-dialog-options">
        <div>
          <!--  文件上传          -->
          <el-button @click="uploadAvatar">
            <el-icon style="margin-right: 5px;">
              <UploadFilled/>
            </el-icon>
            上传头像
            <input ref="avatarUploaderRef" accept="image/*" class="avatar-uploader" name="file" type="file"
                   @change="handleFileChange">
          </el-button>
          <!-- 左右旋转功能 -->
          <el-button @click="rotateLeft">
            <el-icon>
              <RefreshLeft/>
            </el-icon>
          </el-button>
          <el-button @click="rotateRight">
            <el-icon>
              <RefreshRight/>
            </el-icon>
          </el-button>
        </div>
        <div>
          <!-- 确定和取消按钮 -->
          <el-button type="primary" @click="submitForm">提 交</el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {DeviceEnum} from "@/enums/DeviceEnum";
import {useSystemStore} from "@/store/modules/system";

defineOptions({
  name: "FestivalAvatarUploadDialog",
  inheritAttrs: false,
});

const props = withDefaults(defineProps<{
  visible: boolean,
  title: string,
  img: string
}>(), {});

const emit = defineEmits<{
  (event: 'update:img', value: string): void;
  (event: 'update:visible', value: boolean): void;
  (event: 'avatarChanged', value: string): void;    // 头像修改事件
}>();

const img = useVModel(props, 'img', emit)
const visible = useVModel(props, 'visible', emit)
const imgTemp = ref<string>("")

const device = computed(() => useSystemStore().app.device)    // 设备类型

const cropper = ref() // 裁剪实例
const avatarUploaderRef = ref<HTMLInputElement | null>(null) // 上传头像-input实例


function uploadAvatar() {
  // 点击 input
  avatarUploaderRef.value?.click();
}

// 上传之前校验图片格式
const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt20M = file.size / 1024 / 1024 < 20

  if (!isImage) {
    ElMessage.error('上传头像图片只能是图片格式!')
  }
  if (!isLt20M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isImage && isLt20M
}

// 处理图片上传
function handleFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  if (input.files && input.files[0]) {
    const file = input.files[0]

    // 校验文件类型
    if (!beforeAvatarUpload(file)) {
      return
    }

    // 使用 FileReader 读取文件
    const reader = new FileReader()
    reader.onload = (e: ProgressEvent<FileReader>) => {
      imgTemp.value = e.target?.result as string // 将图片数据传给 vue-cropper
    }
    reader.readAsDataURL(file)
  }
}

// 向左旋转图片
function rotateLeft() {
  cropper.value?.rotateLeft()
}

// 向右旋转图片
const rotateRight = () => {
  cropper.value?.rotateRight()
}

// 提交并获取裁剪后的图片数据
const submitForm = () => {
  cropper.value?.getCropData((data: string) => {
    img.value = data
    visible.value = false;

    // 触发 avatarChanged 事件
    emit('avatarChanged', data);
  });
}

</script>

<style lang="scss" scoped>

.festival-avatar-upload-dialog {

  .festival-avatar-dialog-options {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .avatar-uploader {
      display: none;
      width: 100%;
      height: 100%;
    }
  }
}

</style>
