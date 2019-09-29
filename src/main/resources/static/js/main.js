import Vue from 'vue'
import VueResource from 'vue-resource'
import App from 'pages/App.vue'

Vue.use(VueResource)

new Vue({
    render: a => a(App)

}).$mount('#app')

