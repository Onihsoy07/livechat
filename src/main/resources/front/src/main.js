import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import Axios from 'axios';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client'

createApp(App)
    .use(router)
    .use(store)
    .use(Axios)
    .use(SockJS)
    .use(Stomp)
    .mount('#app')