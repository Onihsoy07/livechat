<template>
    <h1>회원가입</h1>
    <input type="text" name="username" id="username" placeholder="username" v-model="data.username" />
    <input type="password" name="password" id="password" placeholder="password" v-model="data.password" />
    <button @click="registerMember">생성</button>
</template>

<script setup>
import axios from "axios";
import { reactive } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const data = reactive({
    username: '',
    password: '',
});

const registerMember = () => {
    axios({
        method: 'post',
        url: 'http://localhost:8080/api/auth/register', 
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
            alert('생성 완료');
            router.push('/');
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