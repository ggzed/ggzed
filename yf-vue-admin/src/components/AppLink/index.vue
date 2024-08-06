<template>
  <component :is="type" v-bind="linkProps(to)">
    <slot></slot>
  </component>
</template>

<script setup lang="ts">
import {validateURL} from "@/utils/validation";

const props = withDefaults(defineProps<{
  to:string
}>(), {});

const isExternalLink = computed(() => validateURL(props.to));

const type = computed(() => {
  return isExternalLink.value ? "a" : "router-link";
});

const linkProps = (to: string) => {
  return isExternalLink.value
    ? { href: to, target: "_blank", rel: "noopener noreferrer" }
    : { to };
};
</script>
