import Vue from 'vue'
import 'api/resource'
import Vuetify from "vuetify";
import vuetify from "./utils/vuetify";
import App from 'pages/App.vue'
import { connect } from "./utils/ws";
import 'vuetify/dist/vuetify.min.css'


if(frontendData.profile){
    connect()
}

Vue.use(Vuetify)

new Vue({
    vuetify,
    render: a => a(App)
}).$mount('#app')

