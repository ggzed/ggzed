<template>
  <div class="app-container">
    <h1>代码生成模块</h1>
    <!--  搜索框  -->
    目前只能生成基础框架 ( 采用 freemarker 模板进行生成 )
    <br/>
    <br/>
    可生成后端 :
    <br/>
    <br/>
    mybatis -> 未编写
    <br/>
    mybatis-plus (只有基础框架)-> controller.java.ftl , service.java.ftl , serviceImpl.java.ftl , mapper.java.ftl ,
    mapper.xml.ftl ,
    entity.java.ftl , converter.java.ftl , form.java.ftl , query.java.ftl , vo.java.ftl
    <br/>
    <br/>
    可生成前端 :
    <br/>
    <br/>
    vue -> 未编写
    <br/>
    vue-hook -> api.ts.ftl , api_type.ts.ftl , views.vue.ftl
    <br/>
    react -> 未编写
    <br/>
    react-hook -> 未编写
    <br/>
    <br/>
    正在完善增删改查的自动生成中...
  </div>
</template>

<script lang="ts" setup>
// 数据
defineOptions({
  name: "GenCode",
  inheritAttrs: false,
});
// 方法

// 生命周期
onMounted(() => {
  ElMessage.info("【代码生成模块】功能正在完善中...")
})
</script>

<style lang="scss" scoped>
/* 样式 */
</style>
