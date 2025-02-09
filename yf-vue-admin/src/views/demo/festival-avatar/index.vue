<template>
  <div class="festival-avatar-container">
    <!--  预览区  -->
    <div class="festival-avatar-preview">
      <div class="festival-avatar-preview__plus" @click="openAvatarDialog('上传头像')">
        <!--   展示合成后的头像     -->
        <img v-if="compositeAvatar" :src="compositeAvatar" alt="合成头像"/>

        <!--   展示目前上传的头像    -->
        <img v-else-if="avatarData" :src="avatarData" alt="头像"/>

        <!--   展示头像未上传符号   -->
        <el-icon v-else color="#8c939d" size="28">
          <Plus></Plus>
        </el-icon>
      </div>
    </div>

    <!-- 头像框操作 -->
    <festival-avatar-frame v-model:avatar-data="avatarData" v-model:composite-avatar="compositeAvatar"/>

    <div style="display: flex;justify-content: center">
      <el-button plain type="primary" @click="downloadAvatar">保 存 头 像</el-button>
    </div>

    <!--  头像上传-模态框  -->
    <festival-avatar-upload-dialog v-model:dialog="avatarDialog" v-model:img="avatarData"/>

  </div>
</template>

<script lang="ts" setup>
import {useDialogManage} from "@/hooks/useDialogManage";

const {
  dialog: avatarDialog,
  openDialog: openAvatarDialog
} = useDialogManage();

const avatarData = ref<string>("");                           // 上传的头像
const compositeAvatar = ref<string>("");                      // 合成后的头像

/**
 * 下载头像
 */
function downloadAvatar() {
  if (!compositeAvatar.value) {
    ElMessage.warning("还没有制作好头像哦,请制作好后点击我!");
    return;
  }
  // 创建一个临时的 <a> 元素
  const link = document.createElement("a");
  // 将头像数据赋值给 href 属性，确保它是 base64 格式的图片数据
  link.href = compositeAvatar.value;
  // 设置下载文件名
  link.download = "avatar.png";
  // 触发下载
  link.click();
}

</script>

<style lang="scss" scoped>

.festival-avatar-preview {
  display: flex;
  justify-content: center;
  align-items: center;

  &__plus {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    width: 200px;
    background-color: #fbfdff;
    padding: 5px;
    border: 1px dashed var(--el-border-color);
    cursor: pointer;

    > img {
      width: 100%;
      height: 100%;
      border-radius: 5%;
      overflow: hidden;
    }
  }
}
</style>
