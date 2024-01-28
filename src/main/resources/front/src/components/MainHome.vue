<template>
    <h1>HOME</h1>
    <input type="text" name="username" id="username" placeholder="username" v-model="username" />
    <button @click="registerMember">생성</button>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";

const username = ref('');

const registerMember = () => {
    axios({
        method: 'post',
        url: 'http://localhost:8080/members', 
        data: JSON.stringify({
            username: username.value
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        if (res.data.success) {
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