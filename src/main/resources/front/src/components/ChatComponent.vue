<template>
    <div class="chat-wrap">
        <div class="chat-contents-wrap">
            <div class="chat-contents" v-for="(messageData, idx) in messageContentList" :key="idx">
                <div class="message-user">
                    {{ messageData.sender.username }}
                </div>
                <div class="message-contents">
                    {{ messageData.contents }}
                </div>
            </div>
            <div v-for="(messageDto, idx) in data.messageList" :key="idx">
                <div>{{ messageDto }}</div>      
            </div>
        </div>
        <div class="message-box-wrap">
            <div class="message-box">
                <textarea name="message" v-model="data.message"></textarea>
                <button @click="sendMessage">보내기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, computed, onMounted } from "vue";
import { useStore } from "vuex";
// import axios from "axios";
import Stomp from 'webstomp-client';
import SockJS from "sockjs-client";


const sock = new SockJS("http://localhost:8080/ws/chat");
const ws = Stomp.over(sock);

const store = useStore();

const data = reactive({
    message: '',
    messageList: [],
});
const messageContentList = computed(() => store.state.messageContentList);
const chatId = computed(() => store.state.currentChatId);

const defaultJwtHeader = {
    // 'Content-Type': 'application/json',
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};


const connect = () => {
    console.log('sock', sock);
    console.log('ws', ws);
    console.log("connect");
    ws.connect(
        defaultJwtHeader,
        frame => {
            console.log('소켓 연결 성공', frame);
            ws.subscribe(
                "/sub/chat/" + chatId.value, 
                res => {
                    console.log('구독으로 받은 메시지 입니다.', res);
                    console.log('body', res.body);
                    data.messageList.push(res.body);
                },
                defaultJwtHeader
            );
        },
        error => {
        console.log('소켓 연결 실패', error);
        } 
    );
};
const sendMessage = () => {
    console.log('sendMessage');
    if (data.message == '' || chatId.value == null) {
        return;
    }

    console.log('ws 전송 시작');
    if (ws && ws.connected) {
        const body = JSON.stringify({
            chatId: chatId.value,
            message: data.message
        });
        console.log(body);
        ws.send("/pub/api/message", body, defaultJwtHeader);
    }

    data.message = '';
};


onMounted(() => {
    connect();
});
</script>

<style scoped>
.chat-wrap {
    width: 200px;
}
.chat-contents-wrap {
    border: 1px solid gray;
    width: 100%;
    height: 400px;
    overflow-x: hidden;
    overflow-y: scroll;
}
.message-box-wrap {
    border: 1px solid gray;
    width: 100%;
    height: 100px;
}
/* .message-box {
    
} */
.message-box textarea {
    all: unset;
    width: 100%;
    height: 70px;
    border: 1px solid gray;
    resize: none;
    text-align: left;
}
.message-box button {
    position: relative;
    top: -30px;
    left: 65px;
}
.chat-contents {
    display: flex;
    padding: 10px 5px;
}
.message-user {
    border: 1px solid gray;
    border-radius: 50%;
    font-size: 12px;
    line-height: 20px;
    width: 20px;
    height: 20px;
}
.message-contents {
    margin-left: 20px;
    border: 1px solid gray;
    border-radius: 10%;
    width: 80%;
    text-align: left;
}
</style>
