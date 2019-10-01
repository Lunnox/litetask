import Vue from 'vue'
import 'api/resource'
import Vuetify from "vuetify";
import '@babel/polyfill'
import vuetify from "./utils/vuetify";
import router from "./router/router";
import App from 'pages/App.vue'
import store from 'store/store'
import { connect } from "./utils/ws";
import 'vuetify/dist/vuetify.min.css'


if(frontendData.profile){
    connect()
}

Vue.use(Vuetify)

new Vue({
    vuetify,
    store,
    router,
    render: a => a(App)
}).$mount('#app')

