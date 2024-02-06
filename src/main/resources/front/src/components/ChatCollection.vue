<template>
    <div>
        <div v-for="(chat, idx) in data.chatCollection" :key="idx">
            <div>{{ chat.chatName }}</div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';

const store = useStore();
const data = reactive({
    chatCollection: [],
});

onMounted(() => {
    axios({
        method: 'get',
        url: '/api/chat/' + store.state.username,
        data: JSON.stringify({
            chatName: data.chatName,
            isOpenChat: data.setOpenChat,
        }),
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.chatCollection = res.data.data;
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
});

</script>

<style scoped>

</style>