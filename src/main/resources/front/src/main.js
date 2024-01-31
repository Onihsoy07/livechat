import { createApp } from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import Axios from 'axios';

createApp(App)
    .use(VueRouter)
    .use(Axios)
    .mount('#app')