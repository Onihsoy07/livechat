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
    },
    mutations: {
        LOGIN_CHECK(state) {
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
                    console.log(res);
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
            state.username = '';
        },
    },
    getters: {

    },
    actions: {

    },
    
});