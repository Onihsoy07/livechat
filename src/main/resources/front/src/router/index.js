import { createWebHistory, createRouter } from "vue-router";

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

    ]
});

export default router;