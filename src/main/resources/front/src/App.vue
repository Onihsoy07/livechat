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
          <button @click="memberDetail" class="btn-nav btn-logout">{{ username }}</button>
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
const memberDetail = () => {
  //추후 구현
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
.btn-member-detail {

}
.btn-logout {
}
.btn-logout:hover {
  color: white;
  cursor: pointer;
  background-color: #A6A6A6;
}

</style>
