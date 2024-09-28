<template>
  <div class="festival-avatar-frame">
    <!--  头像框分类  -->
    <div class="festival-avatar-frame-category">
      <el-tag v-for="(avatarFrameItem, index) in avatarFrameList"
              :key="index"
              :size="'large'"
              :type="currentCategoryIndex === index ? 'success' : 'info'"
              @click="currentCategoryIndex = index">
        {{ avatarFrameItem.name }}
      </el-tag>
    </div>

    <!-- 头像框绘制 Canvas -->
    <canvas ref="compositeCanvas" style="display: none;"></canvas>
    <!-- 头像框绘制 Canvas -->
    <canvas ref="avatarFrameCanvas" style="display: none;"></canvas>

    <!-- 头像框选择区 -->
    <div class="festival-avatar-frame-chose">
      <span>头像框 :</span>
      <el-image v-for="(imagesUrl, index) in avatarFrameList[currentCategoryIndex].imageUrls"
                :key="index"
                :class="imagesUrl === selectAvatarFrameUrl ? 'active' : ''"
                :src="imagesUrl"
                lazy
                @click="selectFrame(index)"/>
      <!--  自定义头像框上传    -->
      <div class="festival-avatar-frame-chose__custom" @click="uploadAvatarFrame">
        <el-icon>
          <Plus></Plus>
        </el-icon>
        <input ref="avatarFrameUploaderRef" accept="image/*" name="file" style="display: none" type="file"
               @change="handleFileChange">
      </div>
    </div>

    <div class="festival-avatar-frame-options">
      <!-- 透明度调节 -->
      <div class="festival-avatar-frame-options__opacity">
        <span>头像框透明度 :</span>
        <el-slider v-model="frameOpacity" :max="1" :min="0.1" :step="0.01" @change="drawCompositeAvatar"/>
      </div>

      <!-- 颜色过滤选项 -->
      <div class="festival-avatar-frame-options__color-filter">
        <span>头像框颜色过滤 :</span>
        <el-color-picker v-model="colorFilter.color" color-format="rgb" @change="drawCompositeAvatar"/>
      </div>

      <div class="festival-avatar-frame-options__tolerance">
        <span>颜色过滤误差范围 :</span>
        <el-input-number v-model="colorFilter.tolerance" :max="100" :min="0" style="width: 140px"
                         @change="drawCompositeAvatar"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>

import {AvatarFrame, getAvatarFrame} from '@/views/demo/festival-avatar/tool';
import {Ref} from "vue";

defineOptions({
  name: "FestivalAvatarFrame",
  inheritAttrs: false,
});

const props = defineProps<{
  avatarData: string;     // 头像信息,
  compositeAvatar: string // 与头像框组合后的信息
}>();

const emit = defineEmits<{
  (e: 'update:compositeAvatar', value: string): void;  // 向父组件传递合成后的头像数据
  (e: 'update:avatarData', value: string): void;       // 向父组件传递合成后的头像数据
}>();

const compositeCanvas = ref<HTMLCanvasElement | null>(null);   // 用于合并操作的 Canvas
const avatarFrameCanvas = ref<HTMLCanvasElement | null>(null); // 用于头像框操作的 Canvas
const avatarFrameUploaderRef = ref<HTMLInputElement | null>(null);   // 用户自定义头像上传
const avatarFrameList = ref<AvatarFrame[]>(getAvatarFrame());  // 头像框元信息列表
const currentCategoryIndex = ref<number>(0);                   // 当前选中的分类下标
const selectAvatarFrameUrl = ref<string>("");                  // 选中的头像框 URL
const frameOpacity = ref<number>(1);                           // 头像框透明度，范围 0-1
const colorFilter = ref<{ color: string; tolerance: number }>({
  color: 'rgb(255,255,255)', // 要过滤的颜色（RGB 格式）
  tolerance: 0               // 颜色容差范围
});

const compositeAvatar = useVModel(props, 'compositeAvatar', emit); // 双向绑定合成后的头像
const avatarData = useVModel(props, "avatarData", emit);           // 用户监听头像变化

/**
 * 选择头像框
 * @param index 头像框对应的下标
 */
function selectFrame(index: number) {
  if (!avatarData.value) {
    ElMessage.warning('请先上传头像！');
    return;
  }
  selectAvatarFrameUrl.value = avatarFrameList.value[currentCategoryIndex.value].imageUrls[index];
  drawCompositeAvatar(); // 绘制合成头像
}

/**
 * 绘制合成后的头像和头像框
 */
async function drawCompositeAvatar() {
  if (!canCompositeAvatar()) return; // 调用检查方法，防止执行无效操作

  const context = getCanvasContext(compositeCanvas); // 获取主 Canvas 上下文

  clearCanvas(context, compositeCanvas.value); // 清除之前的内容

  const avatarImg = await loadImage(avatarData.value); // 加载头像图片
  resizeCanvas(compositeCanvas, avatarImg); // 调整 Canvas 大小

  // 将头像绘制在 Canvas 上
  context.drawImage(avatarImg, 0, 0, avatarImg.width, avatarImg.height);

  const frameImg = await loadFrameImage(selectAvatarFrameUrl.value); // 加载头像框图片
  const frameContext = getCanvasContext(avatarFrameCanvas); // 获取头像框 Canvas 上下文

  resizeCanvas(avatarFrameCanvas, avatarImg); // 调整头像框 Canvas 大小
  setFrameOpacity(frameContext, frameOpacity.value); // 设置头像框透明度

  // 绘制头像框到 Canvas 上
  frameContext.drawImage(frameImg, 0, 0, avatarImg.width, avatarImg.height);

  if (colorFilter.value.tolerance != 0) {
    // 过滤指定颜色，使其变为透明
    filterColorToTransparent(frameContext, avatarImg.width, avatarImg.height);
  }

  // 将处理后的头像框绘制到主 Canvas 上
  context.drawImage(avatarFrameCanvas.value, 0, 0, avatarImg.width, avatarImg.height);

  // 将合成后的图片转换为数据 URL
  compositeAvatar.value = compositeCanvas.value!.toDataURL('image/png');
}

function uploadAvatarFrame() {
  // 优先上传头像
  if (!avatarData.value) {
    ElMessage.warning('请上传头像！');
    return false;
  }
  // 点击 input
  avatarFrameUploaderRef.value?.click();
}

/**
 * 自定义图片上传功能
 * @param event
 */
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
      selectAvatarFrameUrl.value = e.target?.result as string // 将图片数据传给 vue-cropper
      drawCompositeAvatar()
    }
    reader.readAsDataURL(file)
  }
}

/**
 * 文件上传校验
 * @param file 文件
 */
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

/**
 * 加载图片
 * @param src 图片的 URL 地址
 * @returns Promise 返回加载的图片对象
 */
function loadImage(src: string): Promise<HTMLImageElement> {
  return new Promise((resolve) => {
    const img = new Image();
    img.src = src;
    img.onload = () => resolve(img); // 图片加载完成后返回
  });
}

/**
 * 加载头像框图片
 * @param src 头像框的 URL 地址
 * @returns Promise 返回加载的图片对象
 */
function loadFrameImage(src: string): Promise<HTMLImageElement> {
  return new Promise((resolve) => {
    const img = new Image();
    img.crossOrigin = 'Anonymous'; // 设置跨域，以支持跨域图片加载
    img.src = src;
    img.onload = () => resolve(img); // 图片加载完成后返回
  });
}

/**
 * 获取 Canvas 的 2D 上下文
 * @param canvasRef Canvas 引用
 * @returns CanvasRenderingContext2D | null 返回 Canvas 上下文或 null
 */
function getCanvasContext(canvasRef: Ref<HTMLCanvasElement | null>) {
  return canvasRef.value?.getContext('2d', {willReadFrequently: true});
}

/**
 * 清除 Canvas 内容
 * @param context Canvas 的 2D 上下文
 * @param canvas Canvas 引用
 */
function clearCanvas(context: CanvasRenderingContext2D, canvas: HTMLCanvasElement | null) {
  if (!canvas) return;
  context.clearRect(0, 0, canvas.width, canvas.height); // 清除整个 Canvas 的内容
}

/**
 * 调整 Canvas 的大小
 * @param canvasRef Canvas 引用
 * @param img 图片对象
 */
function resizeCanvas(canvasRef: Ref<HTMLCanvasElement | null>, img: HTMLImageElement) {
  if (canvasRef.value) {
    canvasRef.value.width = img.width;   // 根据图片宽度调整 Canvas 宽度
    canvasRef.value.height = img.height; // 根据图片高度调整 Canvas 高度
  }
}

/**
 * 设置 Canvas 的透明度
 * @param context Canvas 的 2D 上下文
 * @param opacity 透明度值
 */
function setFrameOpacity(context: CanvasRenderingContext2D, opacity: number) {
  context.globalAlpha = opacity; // 设置全局透明度
}

/**
 * 将指定颜色过滤为透明
 * @param context Canvas 的 2D 上下文
 * @param width Canvas 宽度
 * @param height Canvas 高度
 */
function filterColorToTransparent(context: CanvasRenderingContext2D, width: number, height: number) {
  const frameImageData = context.getImageData(0, 0, width, height);
  const data = frameImageData.data;

  const targetColor = rgbStringToArray(colorFilter.value.color); // 将目标颜色转换为 RGB 数组

  // 遍历所有像素点
  for (let i = 0; i < data.length; i += 4) {
    const r = data[i];
    const g = data[i + 1];
    const b = data[i + 2];
    const distance = colorDistance({r, g, b}, targetColor); // 计算当前颜色与目标颜色的差距

    // 如果颜色差距在容差范围内，则将其透明度设为 0
    if (distance <= colorFilter.value.tolerance) {
      data[i + 3] = 0; // 设置 alpha 通道为 0（透明）
    }
  }

  // 将处理后的图像数据放回 Canvas
  context.putImageData(frameImageData, 0, 0);
}

/**
 * 计算两种颜色之间的距离（欧几里得距离）
 * @param color1 颜色 1，包含 r、g、b 属性
 * @param color2 颜色 2，包含 r、g、b 属性
 * @returns number 返回颜色之间的距离
 */
function colorDistance(color1: { r: number; g: number; b: number }, color2: { r: number; g: number; b: number }) {
  return Math.sqrt(
      (color1.r - color2.r) ** 2 +
      (color1.g - color2.g) ** 2 +
      (color1.b - color2.b) ** 2
  );
}

/**
 * 将 RGB 字符串转换为 RGB 数组
 * @param rgbString RGB 字符串（例如 'rgb(255,255,255)'）
 * @returns 返回一个包含 r、g、b 值的对象
 */
function rgbStringToArray(rgbString: string) {
  const result = rgbString.match(/\d+/g)?.map(Number) || [0, 0, 0]; // 匹配并转换 RGB 值
  return {r: result[0], g: result[1], b: result[2]}; // 返回 r、g、b 对象
}

/**
 * 检查是否可以进行头像合成操作
 * @returns boolean 返回是否具备合成头像的条件
 */
function canCompositeAvatar(): boolean {
  if (!avatarData.value) {
    ElMessage.warning('请上传头像！');
    return false;
  }
  if (!selectAvatarFrameUrl.value) {
    ElMessage.warning('请选择头像框！');
    return false;
  }
  return true;
}

watch(avatarData, () => {
  if (avatarData.value && selectAvatarFrameUrl.value) {
    drawCompositeAvatar();
  }
})

</script>

<style lang="scss" scoped>
// 头像框选择
.festival-avatar-frame-chose {
  height: 100px;
  display: flex;
  justify-content: left;
  align-items: center;
  padding: 5px 20px 3px;
  overflow-x: auto;

  &__custom {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #fbfdff;
    padding: 5px;
    border: 1px dashed var(--el-border-color);
    cursor: pointer;
  }

  > span {
    font-size: 16px;
    font-weight: 700;
    margin-right: 28px;
    min-width: 58px;
  }

  .el-image, > div {
    min-width: 72px;
    min-height: 72px;
    max-width: 72px;
    max-height: 72px;
    border-radius: 5%;
    margin: 0 8px;
    transition: all 0.24s;
    cursor: pointer;

    &:hover,
    &.active {
      transform: translateY(-6px);
      box-shadow: 2px 2px 8px 1px #0000001f;
    }
  }
}

.festival-avatar-frame-category {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 16px;

  .el-tag {
    cursor: pointer;
    margin: 10px 10px;
    font-size: 16px;
  }
}

// 头像框操作
.festival-avatar-frame-options {
  padding: 5px 20px 3px;

  > div {
    padding: 10px 0;
  }

  span {
    font-size: 16px;
    font-weight: 700;
    margin-right: 28px;
  }

  // 透明度操作
  &__opacity {
    display: flex;
    justify-content: left;
    align-items: center;


    .el-slider {
      width: 48vw;
    }
  }
}
</style>