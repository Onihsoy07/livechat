<template>
    <div class="chat-collection-wrap">
        <div v-if="chatList.length == 0" class="chat-zero-alert">
            대화방이 없습니다.
        </div>
        <div v-else v-for="(chat, idx) in chatList" :key="idx" class="chat-room-wrap" @click="openChat(chat.id)">
            <div v-if="chat.id === currentChatId" class="chat-name chat-open">{{ chat.chatName }}</div>
            <div v-else class="chat-name">{{ chat.chatName }}</div>
        </div>
    </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useStore } from 'vuex';


const store = useStore();
const chatList = computed(() => store.state.chatList);
const currentChatId = computed(() => store.state.currentChatId);

const openChat = (chatId) => {
    if (currentChatId.value != chatId) {
        console.log(chatId);
        store.commit('SET_CHATID', chatId);
    }
};

onMounted(() => {
    store.commit('GET_MYCHATLIST');
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
.chat-open {
    background: #DCDCDC;
}
.chat-zero-alert {
    height: 50px;
    line-height: 50px;
}
</style>