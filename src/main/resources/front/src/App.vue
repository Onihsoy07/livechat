<template>
  <div>
    <div>
      <router-link to="/" style="margin: 10px;">HOME</router-link>
      <router-link to="/register" style="margin: 10px;">회원가입</router-link>
      <router-link to="/login" style="margin: 10px;">로그인</router-link>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {
  }
}
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
</style>
