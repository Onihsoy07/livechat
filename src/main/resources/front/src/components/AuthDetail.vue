<template>
    <div class="auth-detail-wrap">
        <div>
            <label for="username" class="auth-label">username : </label>
            <input type="text" v-model="data.username" class="auth-input" disabled="true" />
        </div>
        <div>
            <label for="password" class="auth-label">비밀번호 : </label>
            <input type="password" v-model="data.password" class="auth-input" />
        </div>
        <div>
            <label for="passwordCheck" class="auth-label">비밀번호 재확인 : </label>
            <input type="password" v-model="data.passwordCheck" class="auth-input" />
        </div>
        <button @click="passwordUpdate()">수정</button>
    </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

const data = reactive({
    username: null,
    password: '',
    passwordCheck: ''
});
const memberId = route.params.id;


const passwordUpdate = () => {
    axios({
        method: 'patch',
        url: '/api/members/' + memberId,
        data: JSON.stringify({
            'password': data.password,
            'passwordCheck': data.passwordCheck
        }),
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            alert(res.data.message);
            router.push('/');
        } else {
            data.password = '';
            data.passwordCheck = '';
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
        router.push('/');
    });
}

onMounted(() => {
    axios({
        method: 'get',
        url: '/api/members/' + memberId,
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            data.username = res.data.data.username;
        } else {
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
        router.push('/');
    });
});
</script>

<style scoped>
.auth-detail-wrap {
    margin-top: 20px;
}
.auth-detail-wrap div {
    margin: 10px;
}
</style>