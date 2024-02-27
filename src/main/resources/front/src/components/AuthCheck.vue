<template>
    <div>
        <div class="auth-title">비밀번호 확인</div>
        <div>
            <label for="password" class="auth-label">password :</label>
            <input type="password" class="auth-input" v-model="data.password" />
            <button @click="passwordCheck()" class="btn">확인</button>
        </div>
    </div>
</template>

<script setup>
import axios from "axios";
import { reactive } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const data = reactive({
    password: '',

});

const passwordCheck = () => {
    axios({
        method: 'post',
        url: '/api/members/recheck', 
        data: JSON.stringify({
            password: data.password,
        }),
        headers: {
            'Content-Type': 'application/json',
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        if (res.data.success) {
            let memberId = res.data.data;
            router.push('/user/' + memberId);
        } else {
            data.password = '';
            alert(res.data.message);
        }
        console.log(res);
    }).catch((error) => {
        console.log(error);
    });
}


</script>

<style scoped>

</style>