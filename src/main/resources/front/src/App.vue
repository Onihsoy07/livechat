<template>
  <div>
    <div class="nav-wrap">
      <div class="nav">
        <div>
          <router-link to="/" class="router-link">
            <p>HOME</p>
          </router-link>
        </div>

        <div v-if="isLogin">
          <router-link to="/chat" class="router-link">
            <p>채팅</p>
          </router-link>
          <router-link to="/user" class="router-link">
            <p>{{ username }}</p>
          </router-link>
          <button @click="logout" class="btn-nav btn-logout">로그아웃</button>
        </div>

        <div v-else>
          <router-link to="/register" class="router-link">
            <p>회원가입</p>
          </router-link>
          <router-link to="/login" class="router-link">
            <p>로그인</p>
          </router-link>
        </div>
      </div>     
    </div>
    <router-view></router-view>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const router = useRouter();
const store = useStore();
const isLogin = computed(() => store.state.isLogin);
const username = computed(() => store.state.username);


const logout = () => {
  store.commit('SET_LOGOUT');
  router.push('/');
};


onMounted(() => {
  if (!isLogin.value) {
    store.commit('LOGIN_CHECK');
  }
});

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.nav-wrap {
  display: flex;
  height: 40px;
  background-color: gray;
}
.nav {
  display: flex;
  margin: 0px auto;
}
.nav div {
  display: flex;
  text-align: center;
  line-height: 40px;
}
.router-link {
  all: unset;
  color: black;
  font-size: 16px;
  height: 100%;
}
.router-link p {
  margin: 0px 10px;
}
.router-link:hover {
  color: white;
  cursor: pointer;
  background-color: #A6A6A6;
}
.btn-nav {
  all: unset;
  color: black;
  font-size: 16px;
  padding: 0px 10px;
}
/*
.btn-logout {

} */
.btn-logout:hover {
  color: white;
  cursor: pointer;
  background-color: #A6A6A6;
}
.auth-title {
    font-size: 30px;
    margin: 15px;
}
.auth-label {
    margin-right: 10px;
}
.auth-input {
    margin-right: 10px;
}
.bg-black {
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    position: fixed;
    top: 0;
    left: 0;
    padding-top: 120px;
    z-index: 10;
}
.bg-white {
    width: 300px;
    background: white;
    border-radius: 8px;
    margin: 0 auto;
    padding: 10px;
    z-index: 11;
}
.bg-white div {
    margin: 10px 0px;
}
</style>
