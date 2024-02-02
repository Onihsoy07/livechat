<template>
    <h1>회원가입</h1>
    <input type="text" name="username" id="username" placeholder="username" v-model="username" />
    <input type="password" name="password" id="password" placeholder="password" v-model="password" />
    <button @click="registerMember">생성</button>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";

const username = ref('');
const password = ref('');

const registerMember = () => {
    axios({
        method: 'post',
        url: 'http://localhost:8080/api/auth/register', 
        data: JSON.stringify({
            username: username.value,
            password: password.value,
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        if (res.data.success) {
            console.log(res);
            alert('생성 완료');
        } else {
            console.log(res.data);
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
    });
};
</script>

<style scoped>

</style>