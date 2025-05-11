<template>
  <div class="app-container">
    <el-card>
      <el-steps :active="active" align-center finish-status="success">
        <el-step v-permission="[GenerateCrudAPI.TABLE_UPDATE.permission]" description="Crud生成基础配置" title="表配置">
          <template #icon>
            <svg-icon icon-class="gen-code-config" size="1.8rem"/>
          </template>
        </el-step>
        <el-step v-permission="[GenerateCrudAPI.TABLE_FIELDS_UPDATE.permission]" description="Crud生成字段配置"
                 title="字段配置">
          <template #icon>
            <svg-icon icon-class="gen-code-config" size="1.8rem"/>
          </template>
        </el-step>
        <el-step v-permission="[GenerateCrudAPI.DISPLAY_CRUD_CODE.permission]" description="代码生成粗略图展示"
                 title="粗略图展示">
          <template #icon>
            <svg-icon icon-class="gen-display" size="1.8rem"/>
          </template>
        </el-step>
        <el-step v-permission="[GenerateCrudAPI.MENU.permission]" description="Crud生成后所属菜单"
                 title="新增菜单">
          <template #icon>
            <svg-icon icon-class="menu" size="1.8rem"/>
          </template>
        </el-step>
        <el-step
            v-permission="[GenerateCrudAPI.PREVIEW_CRUD_CODE.permission,GenerateCrudAPI.EXPORT_CRUD_CODE.permission]"
            description="预览并生成代码" title="预览与生成">
          <template #icon>
            <svg-icon icon-class="code-preview" size="1.8rem"/>
          </template>
        </el-step>
      </el-steps>

      <div class="step-container">
        <crud-table-step v-if="active === 0" v-model:active="active"
                         v-permission="[GenerateCrudAPI.TABLE_UPDATE.permission]"
                         :curd-manage-id="curdManageId"
                         :quit-config="quitConfig"
        />
        <crud-fields-step v-else-if="active === 1" v-model:active="active"
                          v-permission="[GenerateCrudAPI.TABLE_FIELDS_UPDATE.permission]"
                          :curd-manage-id="curdManageId"
                          :quit-config="quitConfig"
        />
        <crud-display-step v-else-if="active === 2" v-model:active="active"
                           v-permission="[GenerateCrudAPI.DISPLAY_CRUD_CODE.permission]"
                           :curd-manage-id="curdManageId"
                           :quit-config="quitConfig"
        />
        <crud-menu-step v-else-if="active === 3" v-model:active="active"
                        v-permission="[GenerateCrudAPI.MENU.permission]"
                        :curd-manage-id="curdManageId"
                        :quit-config="quitConfig"
        />
        <crud-preview-step
            v-else-if="active === 4"
            v-model:active="active"
            v-permission="[GenerateCrudAPI.PREVIEW_CRUD_CODE.permission,GenerateCrudAPI.EXPORT_CRUD_CODE.permission]"
            :curd-manage-id="curdManageId"
            :quit-config="quitConfig"
        />
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {TagView, useTagsViewStore} from "@/store/modules/tagsView";
import {GenerateCrudAPI} from "@/api/generate/crud-code";
import {useSystemStore} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";

// 组件定义
defineOptions({
  name: "CrudManage/:id",
  inheritAttrs: false,
});
// 数据
const active = ref(0);
const route = useRoute();
const router = useRouter();
const device = computed(() => useSystemStore().app.device)                // 设备类型
const tagsViewStore = useTagsViewStore();                                 // 页签
const curdManageId = computed(() => Number(route.params.id || -1))  // 字典类型ID
// 方法
function quitConfig() {
  const tagView: TagView = {
    name: route.name as string,
    title: route.meta.title || "",
    path: route.path,
    fullPath: route.fullPath,
  }
  tagsViewStore.removeTagView(tagView);
  tagsViewStore.removeCachedView(tagView);
  // TODO 优化硬编码
  tagView.name = "Crud";
  tagsViewStore.removeCachedView(tagView);
  router.push({path: "/generate/crud"});
}

// 生命周期
onMounted(() => {
  if (device.value === DeviceEnum.MOBILE) {
    ElNotification.warning({title: '温馨提示', message: '代码生成未适配移动端'})
  }
})
</script>

<style lang="scss" scoped>
.step-container {
  padding: 20px; // 容器内边距，避免与边缘接触
  transition: all 0.3s ease; // 添加平滑过渡效果
}
</style>
