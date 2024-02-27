<template>
    <div class="auth-detail-wrap">
        <div>
            <label for="username" class="auth-label">username : </label>
            <input type="text" v-model="data.username" class="auth-input" disabled="true" />
        </div>
        <div>
            <label for="username" class="auth-label">비밀번호 : </label>
            <input type="text" v-model="data.username" class="auth-input" disabled="true" />
        </div>
        <div>
            <label for="username" class="auth-label">비밀번호 재확인 : </label>
            <input type="text" v-model="data.username" class="auth-input" disabled="true" />
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
});
const memberId = route.params.id;


const passwordUpdate = () => {
    axios({
        method: 'patch',
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