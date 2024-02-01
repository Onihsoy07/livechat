import { createApp } from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import store from './store';
import Axios from 'axios';

createApp(App)
    .use(VueRouter)
    .use(store)
    .use(Axios)
    .mount('#app')