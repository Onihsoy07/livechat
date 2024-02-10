<template>
    <div class="chat-wrap">
        <div class="chat-contents-wrap">
            {{ messageContentList }}
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
import { reactive, computed } from "vue";
import { useStore } from "vuex";
import axios from "axios"

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

const sendMessage = () => {
    if (data.message == '' || chatId.value == null) {
        return;
    }

    axios({
        method: 'post',
        url: '/api/message', 
        data: JSON.stringify({
            chatId: 1,
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
</style>