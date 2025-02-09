<template>
  <div class="user-profile">
    <!-- 顶部操作栏   -->
    <div class="user-profile-options">
      <el-button circle icon="Download" @click="exportPdf()"></el-button>
      <el-button circle icon="Edit" @click="openUserProfileDialog"></el-button>
    </div>

    <!--    头像      -->
    <el-upload
        :before-upload="beforeAvatarUpload"
        :http-request="uploadAvatar"
        :show-file-list="false"
        class="upload-area"
    >
      <el-avatar :size="120"
                 :src="userProfileInfo.avatar">
        {{ userProfileInfo?.username }}
      </el-avatar>

      <div class="upload-mask">
        <el-icon color="#fff">
          <Plus></Plus>
        </el-icon>
      </div>
    </el-upload>
    <!--  个人信息描述  -->
    <div style="width: 100%;margin-top: 20px;">
      <el-descriptions
          :column="1"
          border
      >
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <user/>
              </el-icon>
              用户名
            </div>
          </template>
          {{ userProfileInfo?.username }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <user-filled/>
              </el-icon>
              用户昵称
            </div>
          </template>
          {{ userProfileInfo?.nickname }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <View/>
              </el-icon>
              性别
            </div>
          </template>
          {{ genderDict[userProfileInfo?.gender || 0] }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <Iphone/>
              </el-icon>
              手机号
            </div>
          </template>
          {{ userProfileInfo?.phoneNumber }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <Message/>
              </el-icon>
              邮箱
            </div>
          </template>
          {{ userProfileInfo?.email }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <Notebook/>
              </el-icon>
              部门
            </div>
          </template>
          {{ userProfileInfo?.deptName }}
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <Avatar/>
              </el-icon>
              角色
            </div>
          </template>
          <el-tag v-for="role in userProfileInfo.roles" :key="role" class="user-profile__role" type="success">
            {{ role }}
          </el-tag>
        </el-descriptions-item>

        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon>
                <Clock/>
              </el-icon>
              创建时间
            </div>
          </template>
          {{ userProfileInfo?.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
  <user-profile-manage-dialog
      v-if="visible"
      :close-dialog="closeDialog"
      :gender-dict="genderDict"
      :load-data="props.loadData"
      :title="title"
      :user-profile-info="props.userProfileInfo"
      :visible="visible"
  />
</template>

<script lang="ts" setup>
// 组件定义
import {useDictionary} from "@/hooks/userDict";
import {DictType} from "@/api/system/dict-data/type";
import {useUserStore} from "@/store/modules/user";
import {useSystemStore} from "@/store/modules/system";
import {UserProfileInfoVO} from "@/api/system/user-profile/type";
import {UserFilled} from "@element-plus/icons-vue";
import {UserProfileAPI} from "@/api/system/user-profile";
import {UploadRawFile, UploadRequestOptions} from "element-plus";
import {useDialogManage} from "@/hooks/useDialogManage";

defineOptions({
  name: "UserProfileInfo",
  inheritAttrs: false,
});
// props & emits
const props = withDefaults(defineProps<{
  userProfileInfo: UserProfileInfoVO,
  loadData: () => void,
}>(), {});

const emits = defineEmits<{
  (event: 'update:userProfileInfo', value: UserProfileInfoVO): void;
}>();

// hooks
const userProfileInfo = useVModel(props, 'userProfileInfo', emits)
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
// 数据
const genderDict = await useDictionary(DictType.GENDER)               // 性别字典数据
const userStore = useUserStore()                                      // 用户信息
const systemStore = useSystemStore()                                  // 系统信息
// 方法
/**
 * 导出PDF
 */
function exportPdf() {
  ElNotification.info('个人名片导出正在开发中...')
}

/**
 * 上传文件限制
 * @param rawFile 文件信息
 */
function beforeAvatarUpload(rawFile: UploadRawFile) {
  if (!UserProfileAPI.UPLOAD_AVATAR.allowedFileTypes.includes(rawFile.type)) {
    ElMessage.error('头像文件格式错误')
    return false
  } else if (rawFile.size > UserProfileAPI.UPLOAD_AVATAR.maxFileSize) {
    ElMessage.error('头像文件大小不能超过 10MB!')
    return false
  } else if (rawFile.name.length <= 0 || rawFile.name.length >= 100) {
    ElMessage.error('头像文件名不能超过100个字符')
    return false
  }
  return true
}

/**
 * 上传头像
 * @param options 操作项
 */
function uploadAvatar(options: UploadRequestOptions) {
  // 注 : 对于上传成功 , 使用 on-success 也可行，其中参数 response 可获取到当前 resolve(unknown) 值
  return new Promise((resolve) => {
    // 1. 执行上传操作
    UserProfileAPI.UPLOAD_AVATAR.request(options.file).then(({data}) => {
      // 2. 更改当前页头像数据
      userProfileInfo.value.avatar = data;
      // 3. 更改缓存中头像数据
      userStore.setUserInfo({permissions: [], roles: [], ...userStore.userInfo, avatar: data})
      // 4. data 传给 on-success
      resolve(data)
    })
  })
}

function openUserProfileDialog() {
  openDialog("修改个人信息");
}

// 生命周期
</script>

<style lang="scss" scoped>
/* 样式 */
.user-profile {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 10px;
  box-shadow: var(--el-box-shadow-light);

  .user-profile-options {
    width: 100%;
    padding: 8px 16px;
    display: flex;
    justify-content: flex-end;
  }

  &__role {
    margin-right: 4px;
  }

  .cell-item {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 6px;
  }
}

.upload-area {
  position: relative;

  .upload-mask {
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.5); // 遮罩层颜色，可以根据需要调整透明度
    justify-content: center;
    align-items: center;
    color: white;
    font-size: 18px;
    font-weight: bold;

  }

  &:hover .upload-mask {
    display: flex; // 鼠标悬停时显示遮罩层
  }
}
</style>
