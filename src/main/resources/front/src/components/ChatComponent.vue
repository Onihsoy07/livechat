<template>
    <div class="chat-wrap">

        <div class="bg-black invite-member" v-if="data.isInviteWinOpen">
            <div class="bg-white">
                <div>
                    <label for="invite-username">닉네임</label>
                    <input type="text" name="invite-username" v-model="data.inviteUsername">
                </div>
                <div>
                    <button @click="searchUsername">검색</button>
                    <button @click="closeInviteDetail">취소</button>
                </div>
                <div>
                    <div v-if="data.memberSearchResult" class="chat-search-result-wrap">
                        <div>{{ data.memberSearchResult.username }}</div>
                        <button @click="inviteMember(data.memberSearchResult.id, data.memberSearchResult.username)">초대</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="chat-contents-wrap" ref="messageWrap">
            <div class="chat-contents" v-for="(messageData, idx) in messageContentList" :key="idx">

                <div v-if="messageData.messageType === 'ENTER'" class="enter-message-wrap">
                    <div>{{ messageData.contents }}</div>
                </div>

                <div v-else-if="messageData.sender.username === username" class="message-wrap my-area">
                    <div v-if="messageData.messageType === 'ATTACH'" class="message-inner">
                        <div class="message-create-date">
                            <div class="date-date">
                                {{ messageData.createDate.slice(0, 8) }}
                            </div>
                            <div class="date-time">
                                {{ messageData.createDate.slice(8) }}
                            </div>
                        </div>
                        <div class="message-contents attach-contents" @click="downloadFile(messageData.attachDto.storeFileName)">
                            {{ messageData.attachDto.uploadFileName }}
                        </div>
                    </div>

                    <div v-else class="message-inner">
                        <div class="message-create-date">
                            <div class="date-date">
                                {{ messageData.createDate.slice(0, 8) }}
                            </div>
                            <div class="date-time">
                                {{ messageData.createDate.slice(8) }}
                            </div>
                        </div>
                        <div class="message-contents">
                            {{ messageData.contents }}
                        </div>
                    </div>
                </div>

                <div v-else class="message-wrap">
                    <div v-if="messageData.messageType === 'ATTACH'" class="message-inner">
                        <div class="message-user">
                            {{ messageData.sender.username }}
                        </div>
                        <div class="message-contents attach-contents" @click="downloadFile(messageData.attachDto.storeFileName)">
                            {{ messageData.attachDto.uploadFileName }}
                        </div>
                        <div class="message-create-date">
                            <div class="date-date">
                                {{ messageData.createDate.slice(0, 8) }}
                            </div>
                            <div class="date-time">
                                {{ messageData.createDate.slice(8) }}
                            </div>
                        </div>
                    </div>

                    <div v-else class="message-inner">
                        <div class="message-user">
                            {{ messageData.sender.username }}
                        </div>
                        <div class="message-contents">
                            {{ messageData.contents }}
                        </div>
                        <div class="message-create-date">
                            <div class="date-date">
                                {{ messageData.createDate.slice(0, 8) }}
                            </div>
                            <div class="date-time">
                                {{ messageData.createDate.slice(8) }}
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </div>
        <div class="message-box-wrap">
            <div class="message-box">
                <textarea name="message" v-model="data.message"></textarea>
                <button @click="sendMessage">보내기</button>
                <button @click="openFile">파일</button>
                <button @click="inviteWinOpen">초대</button>
                <button @click="leaveChat">나가기</button>
                <input class="file-input" type="file" ref="fileMessage" @change="sendFile()" />
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
    isInviteWinOpen: false,
    inviteUsername: '',
    memberSearchResult: null,
});

const username = computed(() => store.state.username);
const messageContentList = computed(() => store.state.messageContentList);
const isChatChange = computed(() => store.state.isChatChange);
const messageWrap = ref(null);
const fileMessage = ref(null);

const jwtToken = 'Bearer ' + window.localStorage.getItem('token');
const defaultJwtHeader = {
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};
const defaultJsonJwtHeader = {
    'Content-Type': 'application/json',
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
}


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
                    messageContentList.value.push(JSON.parse(res.body));
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
const openFile = () => {
    fileMessage.value.click();
}
const sendFile = () => {
    const file = fileMessage.value.files[0];
    const messageData = {
        chatId: props.chatId,
        message: 'file save',
        messageType: 'ATTACH'
    };
    const formData = new FormData();
    formData.append('file', file);
    formData.append('message',
                    new Blob(
                        [JSON.stringify(messageData)],
                        {
                            type: 'application/json'
                        }
                    ));
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
        alert('파일의 크기가 100MB를 초과하였습니다.');
    });
};
const downloadFile = (fileName) => {
    axios({
        method: 'get',
        url: '/api/attachs/' + fileName, 
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
const inviteWinOpen = () => {
    data.isInviteWinOpen = true;
};
const closeInviteDetail = () => {
    data.isInviteWinOpen = false;
    data.inviteUsername = '';
    data.memberSearchResult = null;
}
const searchUsername = () => {
    data.memberSearchResult = null;
    axios({
        method: 'get',
        url: '/api/members?username=' + data.inviteUsername, 
        headers: defaultJsonJwtHeader,
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            console.log(res.data.data);
            data.memberSearchResult = res.data.data;
            data.inviteUsername = '';
        } else {
            data.inviteUsername = '';
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
}
const inviteMember = (memberId, username) => {
    axios({
        method: 'post',
        url: '/api/chat/' + props.chatId, 
        data: JSON.stringify({
            memberId: memberId,
            username: username
        }),
        headers: defaultJsonJwtHeader,
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            console.log(res.data.data);
            closeInviteDetail();
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
}
const leaveChat = () => {
    axios({
        method: 'delete',
        url: '/api/chat/' + props.chatId,
        headers: defaultJsonJwtHeader,
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            console.log(res.data.data);
            ws.disconnect(null, defaultJwtHeader);
            store.commit('LEAVE_CHAT', props.chatId);
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
}



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
    width: 300px;
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
    margin-top: 5px;
    width: 95%;
    height: 65px;
    resize: none;
    text-align: left;
    white-space: pre-wrap;
    word-break: break-all;
}
.message-box textarea::-webkit-scrollbar {
  display: none;
}
.message-box button {
    position: relative;
    left: 17%;
}
.chat-contents {
    display: flow-root;
}
.message-wrap {
    display: flex;
    float: left;
    padding: 5px;
}
.my-area {
    float: right;
}
.message-inner {
    display: flex;
}
.enter-message-wrap {
    background-color: #EAEAEA;
    margin: 5px 0px;
}
.message-user {
    margin-top: 5px;
    border: 1px solid gray;
    border-radius: 50%;
    font-size: 12px;
    line-height: 20px;
    width: 20px;
    height: 20px;
}
.message-contents {
    margin-left: 10px;
    padding: 5px;
    border: 1px solid gray;
    border-radius: 10px;
    max-width: 60%;
    text-align: left;
    font-size: 16px;
    word-break: break-all;
}
.my-area .message-contents {
    margin: 0px;
    max-width: 75%;
}
.attach-contents {
    background-color: #EAEAEA;
}
.attach-contents:hover {
    background-color: #BCBCBC;
    cursor: pointer;
}
.file-input {
    display: none;
}
.message-create-date {
    padding-top: 3px;
    margin: 0px 5px;
    font-size: 10px;
    width: 50px;
    vertical-align: bottom;
}
.message-create-date {
    position: relative;
}
.date-date {
    position: absolute;
    bottom: 11px;
    left: 0px;
}
.date-time {
    position: absolute;
    bottom: 0px;
    left: 0px;
}
.my-area .date-date {
    position: absolute;
    bottom: 11px;
    right: 0px;
    text-align: right;
}
.my-area .date-time {
    position: absolute;
    bottom: 0px;
    right: 0px;
    text-align: right;
}
.chat-search-result-wrap {
    display: ruby;
}
.chat-search-result-wrap div {
    margin-right: 5px;
}
</style>
