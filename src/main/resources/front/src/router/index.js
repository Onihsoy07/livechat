import { createWebHistory, createRouter } from "vue-router";

const requireAuth = () => (from, to, next) => {
    const token = localStorage.getItem('token')
    if (token) {
    //   store.state.isLogin = true
      return next();
    } // isLogin === true면 페이지 이동
    next('/login') // isLogin === false면 다시 로그인 화면으로 이동
  }

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "index",
            component: () => import("@/components/MainHome.vue")
        },
        {
            path: "/register",
            name: "register",
            component: () => import("@/components/AuthRegister.vue")
        },
        {
            path: "/login",
            name: "login",
            component: () => import("@/components/AuthLogin.vue")
        },
        {
            path: "/chat",
            name: "chat",
            component: () => import("@/components/ChatHome.vue"),
            beforeEnter: requireAuth(),
        },

    ]
});

export default router;