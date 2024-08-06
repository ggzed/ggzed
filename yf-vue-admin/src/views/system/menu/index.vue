<template>
  <div class="app-container">
    <!--  搜索框  -->
    <div class="search-container">
      <el-form ref="queryFormRef" :inline="true" :model="query" label-width="80" @submit.prevent>
        <el-form-item label="关键词 :" prop="keywords">
          <el-input
              v-model="query.keywords"
              clearable
              placeholder="菜单名称"
              @keyup.enter="loadData"
          />
        </el-form-item>
        <el-form-item label="菜单类型 :" prop="type">
          <el-select v-model="query.type" placeholder="菜单类型选择">
            <el-option v-for="(value,key) in menuDict" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="隐藏类型 :" prop="hidden">
          <el-select v-model="query.hidden" placeholder="菜单是否隐藏">
            <el-option v-for="(value,key) in HiddenStatusEnum.OPTIONS" :label="value" :value="Number(key)"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <template #icon>
              <search/>
            </template>
            搜索
          </el-button>
          <el-button plain type="primary" @click="resetQuery">
            <template #icon>
              <refresh/>
            </template>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 主体  -->
    <el-card class="table-container">
      <!--   头部   -->
      <template #header>
        <div>
          <el-button v-permission="[MenuAPI.SAVE.permission]"
                     :circle="device === DeviceEnum.MOBILE"
                     plain
                     type="success"
                     @click="openMenuDialog(0)">
            <el-icon>
              <plus/>
            </el-icon>
            <span v-show="device !== DeviceEnum.MOBILE"> 新增 </span>
          </el-button>
        </div>
      </template>
      <!--   表格    -->
      <el-table
          v-loading="loading"
          :data="dataList"
          border
          row-key="id"
      >
        <el-table-column label="菜单名称" min-width="200">
          <template #default="scope">
            <svg-icon :icon-class="scope.row.icon"/>
            {{ scope.row.name }}
          </template>
        </el-table-column>

        <el-table-column align="center" label="类型" min-width="80">
          <template #default="scope">
            <el-tag :type="MENU_STYLE[scope.row.type % 4]">{{ menuDict[scope.row.type] }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="路由路径" min-width="160">
          <template #default="scope">
            {{ scope.row.path }}
          </template>
        </el-table-column>

        <el-table-column label="组件路径" min-width="180">
          <template #default="scope">
            {{ scope.row.component }}
          </template>
        </el-table-column>

        <el-table-column label="权限标识" min-width="160">
          <template #default="scope">
            {{ scope.row.permission }}
          </template>
        </el-table-column>

        <el-table-column v-permission="[MenuAPI.UPDATE_HIDDEN.permission]" align="center" label="是否隐藏"
                         min-width="80">
          <template #default="scope">
            <el-tag v-show="scope.row.hidden !== null"
                    :type="HiddenStatusEnum.TAG_STYLE[scope.row.hidden % 2]"
                    @click="handleDeleteOrStatusChange([scope.row.id],scope.row.name,!scope.row.hidden)">
              {{ HiddenStatusEnum.OPTIONS[scope.row.hidden] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column align="center" label="排序" min-width="60">
          <template #default="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <el-table-column v-permission="[MenuAPI.UPDATE.permission, MenuAPI.DELETE.permission]"
                         :fixed="device === DeviceEnum.MOBILE ? false : 'right'"
                         align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
                v-if="scope.row.type !== MenuTypeEnum.BUTTON && scope.row.type !== MenuTypeEnum.EXT_LINK"
                v-permission="[MenuAPI.SAVE.permission]"
                link
                size="small"
                type="primary"
                @click.stop="openMenuDialog(scope.row.id)"
            >
              <el-icon>
                <edit/>
              </el-icon>
              新增子菜单
            </el-button>
            <el-button
                v-permission="[MenuAPI.UPDATE.permission]"
                link
                size="small"
                type="warning"
                @click.stop="openMenuDialog(undefined, scope.row.id)"
            >
              <el-icon>
                <edit/>
              </el-icon>
              编辑
            </el-button>
            <el-button
                v-permission="[MenuAPI.DELETE.permission]"
                link
                size="small"
                type="danger"
                @click.stop="handleDeleteOrStatusChange([scope.row.id],scope.row.name)"
            >
              <el-icon>
                <delete/>
              </el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页  -->

    <!-- 增加修改模态框   -->
    <el-dialog
        v-model="dialog.visible"
        :title="dialog.title"
        :width="device === DeviceEnum.MOBILE ? '90%' : '50%'"
        destroy-on-close
        draggable
        overflow
        top="30px"
        @close="closeMenuDialog"
    >
      <el-form
          ref="menuFormRef"
          :model="form"
          :rules="rules"
          label-width="110px"
      >
        <el-form-item label="父级菜单 :" prop="parentId">
          <el-tree-select
              v-model="form.parentId"
              :data="menuOptions"
              :render-after-expand="false"
              check-strictly
              filterable
              placeholder="选择上级菜单"
          />
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称"/>
        </el-form-item>

        <el-form-item label="菜单类型 :" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio v-for="(value,key) in menuDict" :value="Number(key)">{{ value }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
            v-show="
            form.type == MenuTypeEnum.CATALOG ||
            form.type == MenuTypeEnum.MENU
          "
            label="路由路径 :"
            prop="path"
        >
          <el-input v-model="form.path" :placeholder="form.type == MenuTypeEnum.CATALOG ? 'system' : 'user'"/>
        </el-form-item>

        <el-form-item
            v-if="form.type == MenuTypeEnum.MENU"
            label="页面路径 :"
            prop="component"
        >
          <el-input
              v-model="form.component"
              placeholder="system/user/index"
          >
            <template v-if="form.type == MenuTypeEnum.MENU" #prepend
            >src/views/
            </template
            >
            <template v-if="form.type == MenuTypeEnum.MENU" #append
            >.vue
            </template
            >
          </el-input>
        </el-form-item>

        <el-form-item
            v-show="form.type !== MenuTypeEnum.BUTTON"
            label="显示状态 :"
            prop="hidden"
        >
          <el-radio-group v-model="form.hidden">
            <el-radio v-for="optionType in HiddenStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
            v-show="
            form.type === MenuTypeEnum.CATALOG ||
            form.type === MenuTypeEnum.MENU
          "
        >
          <template #label>
            <div>
              <el-tooltip effect="dark" placement="bottom">
                是否始终显示 :
                <template #content>
                  当设置为始终显示时，即使只有一个子菜单也会显示
                </template>
              </el-tooltip>
            </div>
          </template>

          <el-radio-group v-model="form.showSingleChildren">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
            v-if="form.type === MenuTypeEnum.MENU"
            label="是否缓存 :"
            prop="keepAlive"
        >
          <el-radio-group v-model="form.keepAlive">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
            v-if="form.type === MenuTypeEnum.MENU"
            label="固定到页签 :"
            prop="affix"
        >
          <el-radio-group v-model="form.affix">
            <el-radio v-for="optionType in EnableStatusEnum.OPTIONS_RADIO" :value="Number(optionType.value)">{{
                optionType.label
              }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="排序 :" prop="sort">
          <el-input-number
              v-model="form.sort"
              :min="0"
              controls-position="right"
              style="width: 100px"
          />
        </el-form-item>

        <el-form-item
            v-if="form.type == MenuTypeEnum.BUTTON || form.type == MenuTypeEnum.MENU"
            label="权限标识 :"
            prop="permission"
        >
          <el-input v-model="form.permission" placeholder="sys:user:add"/>
        </el-form-item>

        <el-form-item
            v-if="form.type !== MenuTypeEnum.BUTTON"
            label="图标 :"
            prop="icon"
        >
          <!-- TODO 图标选择器 -->
          <icon-selector v-model="form.icon"/>
        </el-form-item>
        <el-form-item
            v-if="form.type == MenuTypeEnum.CATALOG"
            label="跳转路由 :"
            prop="redirect"
        >
          <el-input v-model="form.redirect" placeholder="跳转路由"/>
        </el-form-item>

        <el-form-item
            v-if="form.type == MenuTypeEnum.EXT_LINK"
            label="外链地址 :"
            prop="path"
        >
          <el-input v-model="form.path" placeholder="请输入外链完整路径"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="closeMenuDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {MenuTypeEnum} from "@/enums/MenuTypeEnum";
import {useDictionary} from "@/hooks/userDict";
import {useSystemStore} from "@/store/modules/system";
import {DeviceEnum} from "@/enums/DeviceEnum";
import {MENU_STYLE, MenuForm, MenuPageQuery} from "@/api/system/menu/type";
import {MenuAPI} from "@/api/system/menu";
import {EnableStatusEnum, HiddenStatusEnum} from "@/constants/system";
import {DictType} from "@/api/system/dict/data/type";
import {FormInstance, FormRules} from "element-plus";
import {useDataLoader} from "@/hooks/useDataLoader";
import {useFormManagement} from "@/hooks/useFormManagement";
import {useHandleDeleteOrStatusChange} from "@/hooks/handleDeleteOrStatusChange";
import {useDialogManagement} from "@/hooks/useDialogManagement";

// 定义组件名
defineOptions({
  name: "Menu",
  inheritAttrs: false,
});

const queryFormRef = ref<FormInstance | null>(null);          // 查询表单
const menuFormRef = ref<FormInstance | null>(null);           // 菜单表单
const device = computed(() => useSystemStore().app.device)    // 设备类型
const menuDict = await useDictionary(DictType.MENU)           // 菜单类型字典
const pageQueryApi = (query: MenuPageQuery) => MenuAPI.PAGE.request(query)  // hooks => 查询API
const updateStatusApi = (id: number, status: boolean) => MenuAPI.UPDATE_HIDDEN.request(id, status) // hooks => 修改状态API
const handleDeleteApi = (ids: string) => MenuAPI.DELETE.request(ids)        // hooks => 删除API

const initialQuery: MenuPageQuery = {}                        // 初始化查询条件
const initialForm: MenuForm = {
  id: undefined,
  name: undefined,
  component: undefined,
  icon: undefined,
  permission: undefined,
  path: undefined,
  redirect: undefined,
  parentId: 0,
  hidden: 0,
  sort: 1,
  type: 1,
  showSingleChildren: 0,
  keepAlive: 1,
  affix: 0
}                                                              // 菜单表单初始化数据
const initialRules: FormRules = {
  parentId: [{required: true, message: "请选择顶级菜单", trigger: "blur"}],
  name: [{validator: validateName, trigger: "blur"}],
  type: [{required: true, message: "请选择菜单类型", trigger: "blur"}],
  path: [{validator: validatePath, trigger: "blur"}]
}
const menuOptions = ref<OptionType[]>();                       // 菜单操作列表
const {
  dataList,
  query,
  loading,
  resetQuery,
  loadData
} = useDataLoader(pageQueryApi, initialQuery, queryFormRef);
const {
  form,
  rules,
  resetForm,
  handleSubmit
} = useFormManagement(initialForm, initialRules, menuFormRef);
const {handleDeleteOrStatusChange} = useHandleDeleteOrStatusChange(updateStatusApi, handleDeleteApi, loadData);
const {dialog, openDialog, closeDialog} = useDialogManagement();

/**
 * 打开 增加修改 模态框
 */
function openMenuDialog(parentId?: number, menuId?: number) {
  // 1. 查询菜单options
  MenuAPI.OPTIONS.request().then(({data}) => {
    menuOptions.value = [{value: 0, label: "顶级菜单", children: data}];
  }).then(() => {
    if (menuId) {
      openDialog("编辑菜单", () => {
        MenuAPI.FORM.request(menuId).then(({data}) => {
          form.id = menuId;
          Object.assign(form, data);
        })
      });
    } else {
      openDialog("新增菜单", () => {
        console.log(parentId)
        if (parentId != null) {
          form.parentId = parentId;
        }
      })
    }
  })
}

/**
 * 关闭 增加修改 模态框
 */
function closeMenuDialog() {
  closeDialog(() => resetForm());
}

/**
 * 增加修改请求提交
 */
function submitForm() {
  handleSubmit((form) => {
    const menuId = form.id;
    // 根据 id 判断是新增还是修改
    if (menuId) {
      MenuAPI.UPDATE.request(menuId, form).then(() => {
        ElMessage.success("修改数据成功");
        closeMenuDialog();
        loadData();
      });
    } else {
      MenuAPI.SAVE.request(form).then(() => {
        ElMessage.success("新增数据成功");
        closeMenuDialog();
        loadData();
      });
    }
  })
}

/**
 * 校验文件路径
 */
function validatePath(rule: any, value: any, callback: any) {
  if (form.type === MenuTypeEnum.EXT_LINK && !value) {
    callback(new Error('请输入外联地址'));
  } else if (form.type !== MenuTypeEnum.BUTTON && !value) {
    callback(new Error('请输入路由路径'));
  } else {
    callback();
  }
}

/**
 * 校验名字
 */
function validateName(rule: any, value: any, callback: any) {
  if (!value) {
    if (!value) {
      if (form.type === MenuTypeEnum.BUTTON) {
        callback(new Error('请输入按钮名称'));
      } else {
        callback(new Error('请输入菜单名称'));
      }
    } else {
      callback();
    }
  } else {
    callback();
  }
}

// 生命周期
onMounted(() => {
  loadData();
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
