<template>
  <div>
    图片物体检测 : 使用 TensorFlow 或者 PyTorch 等深度学习框架，实现一个物体检测模型，能够对图片中的物体进行检测和识别。
    <br/>
    可能考虑的事情 :
    <br/>
    (目前采用 ffmpeg 进行简单的图片处理)
    <br/>
    1. 图片裁剪问题 : 模型返回的是整个图片的物体检测结果，需要对图片进行裁剪，只保留物体部分。
    <br/>
    2. 物体检测结果的可视化 : 将模型返回的物体检测结果进行可视化，方便用户查看和理解。
    <br/>
    3. 物体检测模型的训练和优化 : 需要对物体检测模型进行训练和优化，以提高检测准确性和性能。
    <br/>
    ...
  </div>
</template>

<script lang="ts" setup>

defineOptions({
  name: "ObjectSearch",
  inheritAttrs: false,
});
// 数据

// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【object-search模块】功能正在开发中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
