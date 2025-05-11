<template>
  <!-- 增加修改模态框   -->
  <el-dialog
      v-model="visible"
      :title="props.title"
      :width="props.device === DeviceEnum.MOBILE ? '90%' : '50%'"
      destroy-on-close
      draggable
      overflow
      top="30px"
      @close="props.closeDialog"
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
          <el-radio v-for="(value,key) in props.menuDict[DictType.MENU]" :value="Number(key)">{{ value }}</el-radio>
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
        <el-button @click="props.closeDialog()">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {DeviceEnum} from "@/enums/DeviceEnum";
import {MenuTypeEnum} from "@/enums/MenuTypeEnum";
import {EnableStatusEnum, HiddenStatusEnum} from "@/constants/system";
import {MenuForm} from "@/api/system/menu/type";
import {FormInstance, FormRules} from "element-plus";
import {MenuAPI} from "@/api/system/menu";
import {useCrudActions} from "@/hooks/useCrudActions";
import {DictType} from "@/api/system/dict-data/type";

// 组件定义
defineOptions({
  name: "MenuManageDialog",
  inheritAttrs: false,
});
// 组件 props & emits
const props = withDefaults(defineProps<{
  // 父节点Id
  parentId: number | undefined;
  // 当前点击节点ID
  currentClickRowId: number | undefined;
  // 菜单类型
  menuDict: Record<DictType | string, Record<any, string>>;
  // dialog-visible
  visible: boolean;
  // dialog-title
  title: string;
  // 设备
  device?: DeviceEnum;
  // 加载数据
  loadData: (callback?: () => void) => Promise<void>;
  // 关闭弹窗
  closeDialog: (callback?: () => void) => void;
}>(), {device: DeviceEnum.DESKTOP});

const emits = defineEmits<{
  (event: "update:visible", visible: boolean): void
}>()

// hooks
const visible = useVModel(props, 'visible', emits)
const {
  saveData,
  updateData,
} = useCrudActions<number, MenuForm>(MenuAPI.SAVE.request, MenuAPI.UPDATE.request, MenuAPI.DELETE.request, MenuAPI.UPDATE_HIDDEN.request);
// 初始化数据
const initialForm: MenuForm = {
  name: undefined,
  parentId: 0,
  component: undefined,
  icon: undefined,
  permission: undefined,
  path: undefined,
  redirect: undefined,
  hidden: 0,
  sort: 1,
  type: 1,
  showSingleChildren: 0,
  keepAlive: 1,
  affix: 0
}
const rules: FormRules = {
  parentId: [{required: true, message: "请选择顶级菜单", trigger: "blur"}],
  name: [{validator: validateName, trigger: "blur"}],
  type: [{required: true, message: "请选择菜单类型", trigger: "blur"}],
  path: [{validator: validatePath, trigger: "blur"}]
}
// 数据
const menuFormRef = ref<FormInstance | null>(null);          // 菜单表单
const form = ref<MenuForm>({...initialForm});                // menu 表单初始化数据
const menuOptions = ref<OptionType[]>([{value: 0, label: "顶级菜单", children: []}]);  // 菜单操作列表
// 方法
/**
 * 提交表单
 */
async function submitForm() {
  const isValid = await menuFormRef.value?.validate(); // 使用 await 简化验证逻辑
  if (!isValid) return; // 验证未通过，直接返回
  // 校验通过后执行 API 请求
  if (form.value.id) {
    await updateData(form.value.id, form.value, () => {
      props.closeDialog()
      props.loadData()
    })
  } else {
    await saveData(form.value, () => {
      props.closeDialog()
      props.loadData()
    })
  }

  setTimeout(() => {
    ElNotification.warning({
      title: '操作提示',
      message: `<strong>修改【权限标识】</strong>后需要用户重新登录才能生效。`,
      dangerouslyUseHTMLString: true,
    });
  }, 0)

  setTimeout(() => {
    ElNotification.warning({
      title: '操作提示',
      message: `<strong>新增或修改菜单</strong>需要刷新页面以确保变更生效。`,
      dangerouslyUseHTMLString: true,
    });
  }, 0)
}

/**
 * 校验文件路径
 */
function validatePath(rule: any, value: any, callback: any) {
  if (form.value.type === MenuTypeEnum.EXT_LINK && !value) {
    callback(new Error('请输入外联地址'));
  } else if (form.value.type !== MenuTypeEnum.BUTTON && !value) {
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
      if (form.value.type === MenuTypeEnum.BUTTON) {
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
  // 不需要 async , 在弱网环境下可能导致系统加载更慢
  form.value.id = props.currentClickRowId;
  form.value.parentId = props.parentId || 0;
  // 加载表单数据
  MenuAPI.OPTIONS.request().then(({data}) => {
    menuOptions.value[0].children = data;
  })
  if (form.value.id) {
    MenuAPI.FORM.request(form.value.id).then(({data}) => {
      Object.assign(form.value, data);
    })
  }
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
