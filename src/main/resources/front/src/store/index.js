import { createStore } from "vuex";
import axios from "axios";

export default createStore({
    state: {
        isLogin: false,
        tokenKeyName: 'token',
        memberId: null,
        username: '',
    },
    mutations: {
        LOGIN_CHECK(state) {
            const token = window.localStorage.getItem(state.tokenKeyName);
            
            console.log(token);

            if (token == null) {
                console.log('토큰 없음');
                return;
            }
            
            axios({
                method: 'post',
                url: 'http://localhost:8080/api/auth/available', 
                data: JSON.stringify({
                    token: token,
                }),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((res) => {
                if (res.data.success) {
                    console.log(res);
                    state.memberId = res.data.data.id;
                    state.username = res.data.data.username;
                    state.isLogin = true;
                } else {
                    console.log(res.data);
                }
            }).catch((error) => {
                console.log(error);
            });

        },
        SET_LOGIN(state) {
            state.isLogin = true;
        },
        SET_LOGOUT(state) {
            window.localStorage.removeItem(state.tokenKeyName);
            state.isLogin = false;
            state.memberId = null;
            state.username = '';
        }
    },
    getters: {

    },
    actions: {

    },
    
});