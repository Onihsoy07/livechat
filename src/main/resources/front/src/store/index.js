import { createStore } from "vuex";
import axios from "axios";

const encodePayload = () => {
    const token = window.localStorage.getItem(tokenKeyName);

    const encodingPayload = token.split('.')[1];
    const payloead = window.atob(encodingPayload);

    return JSON.parse(payloead);
}
const tokenKeyName =  'token';

export default createStore({
    state: {
        isLogin: false,
        username: '',
        currentChatId: null,
        messageContentList: [],

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
            state.currentChatId = chatId;

            axios({
                method: 'get',
                url: '/api/message?chat-id=' + chatId, 
                headers: {
                    'Content-Type': 'application/json',
                    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
                }
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
        }
    },
    getters: {

    },
    actions: {

    },
    
});