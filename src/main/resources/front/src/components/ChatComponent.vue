<template>
    <div class="chat-wrap">
        <div class="chat-contents-wrap" ref="messageWrap">
            <div class="chat-contents" v-for="(messageData, idx) in messageContentList" :key="idx">
                <div class="message-user">
                    {{ messageData.sender.username }}
                </div>
                <div class="message-contents">
                    {{ messageData.contents }}
                </div>
            </div>
            <div class="chat-contents" v-for="(messageDto, idx) in data.messageList" :key="idx">
                <div class="message-user">
                    {{ messageDto.sender.username }}
                </div>
                <div class="message-contents">
                    {{ messageDto.contents }}
                </div>
            </div>
        </div>
        <div class="message-box-wrap">
            <div class="message-box">
                <textarea name="message" v-model="data.message"></textarea>
                <button @click="sendMessage">보내기</button>
                <button @click="downloadFile">파일다운</button>
                <input type="file" ref="fileMessage" @change="sendFile()" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, computed, onMounted, onUnmounted, onUpdated,  ref, defineProps } from "vue";
import { useStore } from "vuex";
import Stomp from 'webstomp-client';
import SockJS from "sockjs-client";
import axios from "axios";


const sock = new SockJS("http://localhost:8080/ws/chat");
const ws = Stomp.over(sock);

const store = useStore();

const props = defineProps({
    chatId: {
        type: Number,
        required: true
    }
});
const data = reactive({
    initCheck: true,
    newMessageCheck: false,
    message: '',
    messageList: [],
});

const messageContentList = computed(() => store.state.messageContentList);
const isChatChange = computed(() => store.state.isChatChange);
const messageWrap = ref(null);
const fileMessage = ref(null);

const jwtToken = 'Bearer ' + window.localStorage.getItem('token');
const defaultJwtHeader = {
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};


const connect = () => {
    console.log('sock', sock);
    console.log('ws', ws);
    console.log("connect");
    console.log(props.chatId);
    ws.connect(
        defaultJwtHeader,
        frame => {
            console.log('소켓 연결 성공', frame);
            ws.subscribe(
                "/sub/chat/" + props.chatId,
                res => {
                    console.log('구독으로 받은 메시지 입니다.', res);
                    console.log('body', res.body);
                    data.messageList.push(JSON.parse(res.body));
                    data.newMessageCheck = true;
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
    if (data.message == '' || props.chatId == null) {
        return;
    }

    console.log('ws 전송 시작');
    if (ws && ws.connected) {
        const body = JSON.stringify({
            chatId: props.chatId,
            message: data.message,
            messageType: 'MESSAGE'
        });
        console.log(body);
        ws.send("/pub/api/message", body, defaultJwtHeader);
    }

    data.message = '';
};
const sendFile = () => {
    const file = fileMessage.value.files[0];
    const messageData = {
        chatId: props.chatId,
        message: "file save"
    };
    const formData = new FormData();
    formData.append('file', file);
    formData.append('message', new Blob([JSON.stringify(messageData)], { 
        type: 'application/json' 
    }));
    console.log(file);

    axios({
        method: 'post',
        url: '/api/attachs', 
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authentication': jwtToken
        }
    }).then((res) => {
        if (res.data.success) {
            console.log(res);
        } else {
            console.log(res.data);
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });


    // console.log('ws 전송 시작');
    // if (ws && ws.connected) {
    //     ws.send("/pub/api/message", body, defaultJwtHeader);
    // }
};
const downloadFile = () => {
    axios({
        method: 'get',
        url: '/api/attachs/23080b81-7b86-444e-94c6-d065e5c31387.xlsx', 
        headers: defaultJwtHeader,
        responseType: 'blob'
    }).then((res) => {
        console.log(res);
        if (res.data) {
            const fileName = extractDownloadFilename(res);
            const url = window.URL.createObjectURL(new Blob([res.data]));
            const link = document.createElement('a');

            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
        } else {
            alert("파일 저장에 문제가 생겼습니다.");
        }
    }).catch((error) => {
        console.log(error);
    });
};
const messageWrapScrollDown = () => {
    messageWrap.value.scrollTop = messageWrap.value.scrollHeight;
};
const isMessageWrapScrollBottom = () => {
    return messageWrap.value.scrollTop === messageWrap.value.scrollHeight;
};
// content-disposition에서 파일이름 추출
const extractDownloadFilename = (response) => {
    const disposition = response.headers["content-disposition"];
    const fileName = decodeURI(
    disposition
        .match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1]
        .replace(/['"]/g, "")
        );
        return fileName;
};


onMounted(() => {
    connect();
    messageWrapScrollDown();
});
onUpdated(() => {
    if (data.initCheck) {
        messageWrapScrollDown();
        data.initCheck = false;
    }
    if (data.newMessageCheck) {
        messageWrapScrollDown();
        data.newMessageCheck = false;
    }

    isMessageWrapScrollBottom();
});
onUnmounted(() => {
    ws.disconnect(null, defaultJwtHeader);
    if (!isChatChange.value) {
        console.log("대화방 ID 초기화!!!");
        store.commit("CLEAR_CHAT");
    }
    store.commit("SET_ISCHATCHANGE", false);
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
