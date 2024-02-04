<template>
    <h1>CHAT_HOME</h1>
    <div class="bg-black" v-if="data.isChatDetailOpen">
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
    <div>
        <button @click="createChatDetailOpen">생성</button>
        <button @click="joinChat">참가</button>
    </div>
</template>

<script setup>
import axios from 'axios';
import { reactive } from 'vue';

const data = reactive({
    isChatDetailOpen: false,
    chatName: '',
    setOpenChat: false,
});

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

    const token = window.localStorage.getItem('token');
    console.log(token);

    axios({
        method: 'post',
        url: '/api/chat', 
        data: JSON.stringify({
            chatName: data.chatName,
            isOpenChat: data.setOpenChat,
        }),
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + token
        }
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
const joinChat = () => {
    //추후 구현
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
</style>