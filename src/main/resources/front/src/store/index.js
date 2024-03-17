import { createStore } from "vuex";
import axios from "axios";

const encodePayload = () => {
    let token = window.localStorage.getItem(tokenKeyName);
    if (token === null) {
        token = jwtToken;
    }

    const encodingPayload = token.split('.')[1];
    const payloead = window.atob(encodingPayload);

    return JSON.parse(payloead);
}
const tokenKeyName =  'token';
let jwtToken = '';

export default createStore({
    state: {
        isLogin: false,
        username: '',
        currentChatId: null,
        messageContentList: [],
        chatList: [],
        isChatChange: false,
    },
    mutations: {
        LOGIN_CHECK(state) {
            console.log('login check', state.isLogin);
            console.log('state.username', state.username);
            console.log(window.localStorage);
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
        SET_LOGIN(state, jwt) {
            jwtToken = jwt;
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
                url: '/api/chats/' + chatId + '/messages', 
                headers: {
                    'Content-Type': 'application/json',
                    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
                },
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
                url: '/api/members/' + state.username + '/chats',
                headers: {
                    'Content-Type': 'application/json',
                    'Authentication': 'Bearer ' + window.localStorage.getItem('token')
                }
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
        },
        ADD_MESSAGELIST(state, addMessageList) {
            state.messageContentList = addMessageList.concat(state.messageContentList);
        }
    },
    getters: {

    },
    actions: {

    },
    
});