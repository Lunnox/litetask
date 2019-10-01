import Vue from 'vue'
import router from 'vue-router'
import TasksList from "pages/TasksList.vue";
import auth from "pages/auth.vue";
import profile from "pages/Profile.vue"

Vue.use(router)

const routes=[
    {path:'/', component: TasksList},
    {path:'/auth', component: auth},
    {path:'/profile', component:profile},
    {path:'*', component: TasksList}
]

export default new router({
    mode:'history',
    routes
})


