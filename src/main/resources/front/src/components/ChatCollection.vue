<template>
    <div class="chat-collection-wrap">
        <div v-for="(chat, idx) in chatList" :key="idx" class="chat-room-wrap" @click="openChat(chat.id)">
            <div class="chat-name">{{ chat.chatName }}</div>
        </div>
    </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';


const store = useStore();
const chatList = computed(() => store.state.chatList);
const data = reactive({
    chatCollection: [],
});

const openChat = (chatId) => {
    console.log(chatId);
    store.commit('SET_CHATID', chatId);
};

onMounted(() => {
    axios({
        method: 'get',
        url: '/api/chat/' + store.state.username,
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.chatCollection = res.data.data;
            store.commit('SET_CHATLIST', res.data.data);
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
});

</script>

<style scoped>
.chat-collection-wrap {
    width: 200px;
    border: 1px solid gray;
    display: block;
}
.chat-room-wrap {
    display: block;
}
.chat-name {
    height: 15px;
    line-height: 15px;
    padding: 8px 0px;
    border: 1px solid gray;
}
.chat-name:hover {
    background: #DCDCDC;
    cursor: pointer;
}
</style>