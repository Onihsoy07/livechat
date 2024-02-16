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
        </div>
        <div class="message-box-wrap">
            <div class="message-box">
                <textarea name="message" v-model="data.message"></textarea>
                <button @click="sendMessage">보내기</button>
                <button @click="connect">연결</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, computed } from "vue";
import { useStore } from "vuex";
import axios from "axios"
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";


const sock = new SockJS("/ws/chat");
const ws = Stomp.over(sock);

const store = useStore();

const data = reactive({
    message: '',
});
const messageContentList = computed(() => store.state.messageContentList);
const chatId = computed(() => store.state.currentChatId);

const defaultJwtHeader = {
    'Content-Type': 'application/json',
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};

const connect = () => {
    ws.connect(
        {},
        frame => {
            ws.subscribe(
                "/sub",
                res => {
                    console.log('frame', frame);
                    console.log('response', res);
                }
            )
        }
    );
};
const sendMessage = () => {
    if (data.message == '' || chatId.value == null) {
        return;
    }

    axios({
        method: 'post',
        url: '/chats/' + chatId.value + '/messages',  //'/api/message', 
        data: JSON.stringify({
            chatId: chatId.value,
            message: data.message
        }),
        headers: defaultJwtHeader
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            alert(res.data.message);
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
};
</script>

<style scoped>
.chat-wrap {
    width: 200px;
}
.chat-contents-wrap {
    border: 1px solid gray;
    width: 100%;
    height: 400px;
}
.message-box-wrap {
    border: 1px solid gray;
    width: 100%;
    height: 100px;
}
.message-box {
    
}
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
