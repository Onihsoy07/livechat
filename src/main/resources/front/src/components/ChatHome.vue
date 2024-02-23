<template>
    <h1>CHAT_HOME</h1>
    
    <div class="bg-black create-chat" v-if="data.isChatDetailOpen">
        <div class="bg-white">
            <div>
                <label for="chat_name">방이름</label>
                <input type="text" name="chat_name" v-model="data.chatName">
            </div>
            <div>
                <label for="setOpenchat">오픈채팅</label>
                <input type="checkbox" name="setOpenchat" v-model="data.setOpenChat">
            </div>
            <div>
                <button @click="createChat">생성</button>
                <button @click="closeChatDetail">취소</button>
            </div>
        </div>
    </div>
    
    <div class="bg-black join-chat" v-if="data.isChatSearchOpen">
        <div class="bg-white">
            <div>
                <label for="chat_name">방이름 검색 </label>
                <input type="text" name="chat_name" v-model="data.searchChatName">
            </div>
            <div>
                <button @click="searchChatName">검색</button>
                <button @click="closeSearchChatName">취소</button>
            </div>
            <div>
                <div v-for="(chat, idx) in data.searchChatList" :key="idx">
                    <div>{{ chat }}</div>
                    <button @click="joinChat(chat.id)">참가</button>
                </div>
            </div>
        </div>
    </div>

    <div>
        <button @click="createChatDetailOpen">생성</button>
        <button @click="openSearchChatName">참가</button>
    </div>

    <div class="chat-outer">
        <ChatCollection></ChatCollection>
    
        <ChatComponent v-if="chatId"></ChatComponent>
    </div>

</template>

<script setup>
import axios from 'axios';
import { reactive, computed } from 'vue';
import { useStore } from 'vuex';
import ChatCollection from './ChatCollection.vue';
import ChatComponent from './ChatComponent.vue';

const store = useStore();
const chatId = computed(() => store.state.currentChatId);

const data = reactive({
    isChatDetailOpen: false,
    isChatSearchOpen: false,
    chatName: '',
    searchChatName: '',
    setOpenChat: false,
    searchChatList: [],
});
const defaultJwtHeader = {
    'Content-Type': 'application/json',
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};

const createChatDetailOpen = () => {
    data.isChatDetailOpen = true;
};
const closeChatDetail = () => {
    data.isChatDetailOpen = false;
};
const createChat = () => {
    if (data.chatName == '') {
        alert('방이름을 작성해주세요.');
        return;
    }

    axios({
        method: 'post',
        url: '/api/chat', 
        data: JSON.stringify({
            chatName: data.chatName,
            isOpenChat: data.setOpenChat,
        }),
        headers: defaultJwtHeader
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.isChatDetailOpen = false;
            data.chatName = '';
            data.setOpenChat = false;
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
};
const openSearchChatName = () => {
    data.isChatSearchOpen = true;
};
const closeSearchChatName = () => {
    data.isChatSearchOpen = false;
};
const searchChatName = () => {
    data.searchChatList = [];
    axios({
        method: 'get',
        url: '/api/chat?name=' + data.searchChatName, 
        headers: defaultJwtHeader
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.searchChatList = res.data.data;
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
};
const joinChat = (chatId) => {
    alert(chatId);
    axios({
        method: 'post',
        url: '/api/chat/' + chatId, 
        headers: defaultJwtHeader
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.isChatSearchOpen = false;
            data.searchChatName = '';
            data.setOpenChat = false;
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
};
</script>

<style scoped>
.bg-black {
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    position: fixed;
    top: 0;
    left: 0;
    padding-top: 120px;
}
.bg-white {
    width: 300px;
    background: white;
    border-radius: 8px;
    margin: 0 auto;
    padding: 10px;
}
.bg-white div {
    margin: 10px 0px;
}
.chat-outer {
    display: inline-flex;
    margin-top: 20px;
}
</style>