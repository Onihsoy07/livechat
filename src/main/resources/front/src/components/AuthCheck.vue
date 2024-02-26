<template>
    <div>
        <div>비밀번호 확인</div>
        <div>{{ route.params.id }}</div>
        <div>
            <label for="password">password</label>
            <input type="password" v-model="data.password" />
        </div>
        <div>
            <button @click="passwordCheck()">확인</button>
        </div>
    </div>
</template>

<script setup>
import axios from "axios";
import { reactive } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

const data = reactive({
    password: '',

});

const passwordCheck = () => {
    axios({
        method: 'post',
        url: '/api/memeber', 
        data: JSON.stringify({
            password: data.password,
        }),
        headers: {
            'Authentication': 'Bearer ' + window.localStorage.getItem('token')
        }
    }).then((res) => {
        console.log(res);
    }).catch((error) => {
        console.log(error);
    });
}


</script>

<style scoped>

</style>