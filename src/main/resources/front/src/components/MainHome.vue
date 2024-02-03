<template>
    <h1>HOME</h1>
    <div>
        <div>{{ isLogin }}</div>
        <div>{{ username }}</div>
    </div>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

const isLogin = computed(() => store.state.isLogin);
const username = computed(() => store.state.username);
</script>

<style scoped>

</style>