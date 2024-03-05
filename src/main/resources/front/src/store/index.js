import { createStore } from "vuex";
import axios from "axios";

const encodePayload = () => {
    const token = window.localStorage.getItem(tokenKeyName);

    const encodingPayload = token.split('.')[1];
    const payloead = window.atob(encodingPayload);

    return JSON.parse(payloead);
}
const tokenKeyName =  'token';
const defaultJwtHeader = {
    'Content-Type': 'application/json',
    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
};

export default createStore({
    state: {
        isLogin: false,
        username: '',
        currentChatId: null,
        messageContentList: [],
        chatList: null,
        isChatChange: false,
    },
    mutations: {
        LOGIN_CHECK(state) {
            console.log('login check', state.isLogin);
            console.log('state.username', state.username);
            if (state.isLogin) {
                return;
            }

            const token = window.localStorage.getItem(tokenKeyName);

            if (token == null) {
                console.log('토큰 없음');
                return;
            }
            
            axios({
                method: 'post',
                url: '/api/auth/available', 
                data: JSON.stringify({
                    token: token,
                }),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((res) => {
                console.log('토큰 확인', res);
                if (res.data.success) {
                    const payloead = encodePayload();
                    state.isLogin = true;
                    state.username = payloead.sub;
                    return;
                } else {
                    return;
                }
            }).catch((error) => {
                console.log(error);
            });
        },
        SET_LOGIN(state) {
            const payloead = encodePayload();
            state.isLogin = true;
            state.username = payloead.sub;
        },
        SET_LOGOUT(state) {
            window.localStorage.removeItem(tokenKeyName);
            state.isLogin = false;
            state.username = null;
        },
        SET_CHATDETAILLIST(state, messageContentList) {
            state.messageContentList = messageContentList;
        },
        CLEAR_CHATDETAILLIST(state) {
            state.messageContentList = [];
        },
        SET_CHATID(state, chatId) {
            console.log(chatId);
            state.messageContentList = [];
            state.currentChatId = chatId;

            axios({
                method: 'get',
                url: '/api/message?chat-id=' + chatId, 
                headers: defaultJwtHeader,
            }).then((res) => {
                console.log(res);
                if (res.data.success) {
                    state.messageContentList = res.data.data;
                    return;
                } else {
                    return;
                }
            }).catch((error) => {
                console.log(error);
            });
        },
        CLEAR_CHAT(state) {
            state.currentChatId = null;
            state.messageContentList = [];
        },
        SET_ISCHATCHANGE(state, changeResult) {
            state.isChatChange = changeResult;
        },
        LEAVE_CHAT(state, chatId) {
            state.currentChatId = null;
            for (const item of Object.entries(state.chatList)) {
                if (item[1].id === chatId) {
                    state.chatList.splice(item[0], 1);
                }
            }
        },
        GET_MYCHATLIST(state) {
            axios({
                method: 'get',
                url: '/api/chat/' + state.username,
                headers: defaultJwtHeader
            }).then((res) => {
                console.log(res);
                if (res.data.success) {
                    state.chatList = res.data.data;
                } else {
                    alert(res.data.message);
                }
            }).catch((error) => {
                console.log(error);
            });
        },
        PUSH_CHATLIST(state, chat) {
            state.chatList.push(chat);
        }
    },
    getters: {

    },
    actions: {

    },
    
});