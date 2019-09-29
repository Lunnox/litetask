import Vue from 'vue'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'
import { connect } from "./utils/ws";
import Vuetify from "vuetify";
import vuetify from "./utils/vuetify";

import 'vuetify/dist/vuetify.min.css'


if(frontendData.profile){
    connect()
}

Vue.use(Vuetify)
Vue.use(VueResource)

new Vue({
    vuetify,
    render: a => a(App)
}).$mount('#app')

