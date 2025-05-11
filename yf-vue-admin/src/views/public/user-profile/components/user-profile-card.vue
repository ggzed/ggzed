<template>
  <div class="user-profile">
    <!-- 顶部操作栏   -->
    <div class="user-profile-options">
      <el-button circle icon="Download" @click="exportPdf()"></el-button>
      <el-button circle icon="Edit" @click="openUserProfileDialog"></el-button>
    </div>

    <!--    头像      -->
    <file-upload
        v-model="avatar"
        :api="UserProfileAPI.UPLOAD_AVATAR.endpoint"
        :file-key="'avatar'"
        :file-name-size="100"
        :file-type="['png', 'jpg', 'jpeg', 'gif']"
        :limit="1"
        :list-type="'picture'"
        :show-file-list="false"
        :show-tip="false"
    >
      <el-avatar :size="120"
                 :src="avatar">
        {{ userProfileInfo?.username }}
      </el-avatar>

      <div class="upload-mask">
        <el-icon color="#fff">
          <Plus></Plus>
        </el-icon>
      </div>
    </file-upload>


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
          {{ userDict[DictType.GENDER][userProfileInfo?.gender || 0] }}
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
      :load-data="props.loadData"
      :title="title"
      :user-dict="userDict"
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
import {useDialogManage} from "@/hooks/useDialogManage";

defineOptions({
  name: "UserProfileInfo",
  inheritAttrs: false,
});
// props & emits
const props = withDefaults(defineProps<{
  userProfileInfo: UserProfileInfoVO;
  loadData: (callback?: () => void) => Promise<void>;
}>(), {});

const emits = defineEmits<{
  (event: 'update:userProfileInfo', value: UserProfileInfoVO): void;
}>();

// hooks
const userProfileInfo = useVModel(props, 'userProfileInfo', emits)
const avatar = computed({
  get: () => userProfileInfo.value.avatar || '',
  set: (value) => userProfileInfo.value.avatar = value
})
const {
  visible,
  title,
  openDialog,
  closeDialog
} = useDialogManage();
// 数据
const userDict = await useDictionary([DictType.GENDER])          // 用户模块所需字典
const userStore = useUserStore()                                      // 用户信息
const systemStore = useSystemStore()                                  // 系统信息
// 方法
/**
 * 导出PDF
 */
function exportPdf() {
  ElNotification.info('个人名片导出正在开发中...')
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

.file-upload {
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
