<template>
    <h1>로그인</h1>
    <input type="text" name="username" id="username" placeholder="username" v-model="data.username" />
    <input type="password" name="password" id="password" placeholder="password" v-model="data.password" />
    <button @click="login">로그인</button>
</template>

<script setup>
import axios from "axios";
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();
const data = reactive({
    username: '',
    password: '',
});


const login = () => {
    axios({
        method: 'post',
        url: 'http://localhost:8080/api/auth/login', 
        data: JSON.stringify({
            username: data.username,
            password: data.password,
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        if (res.data.success) {
            console.log(res);
            window.localStorage.setItem('token', res.data.data.token);
            store.commit('SET_LOGIN');
            router.push('/');
        } else {
            console.log(res.data);
            data.username = '';
            data.password = '';
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
        data.username = '';
        data.password = '';
        alert(error.response.data.message);
    });
}
</script>

<style scoped>

</style>