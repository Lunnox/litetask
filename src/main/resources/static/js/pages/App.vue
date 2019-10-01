<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Lite Task</v-toolbar-title>
            <v-btn text v-if="profile" :disabled="$route.path==='/'" @click="showTasks">
                My Tasks
            </v-btn>
           <v-spacer></v-spacer>
            <v-btn text v-if="profile" :disabled="$route.path==='/profile'" @click="showProfile">{{profile.name}}</v-btn>&nbsp;
            <v-btn   v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import {addhandler} from "utils/ws";
    import {mapState, mapMutations} from 'vuex'

    export default {
        name: "App",
        computed: mapState (['profile']),
        methods: {
            ...mapMutations([
                'addTaskMutation',
                'updateTaskMutation',
                'deleteTaskMutation'
                ]),
            showTasks(){
                this.$router.push('/')
            },
            showProfile(){
                this.$router.push('/profile')
            }
        },
        created() {
            addhandler(data =>{
                if(data.objectType==='TASK'){
                    switch(data.eventType){
                        case 'CREATE':
                            this.addTaskMutation(data.body)
                            break
                        case 'UPDATE':
                            this.updateTaskMutation(data.body)
                            break
                        case 'REMOVE':
                            this.deleteTaskMutation(data.body)
                            break
                        default: console.log(`Undefined event type "${data.eventType}"`)
                    }
                }else{
                    console.log(`Undefined object type "${data.objectType}"`)
                }
            })
        },
        beforeMount() {
            if (!this.profile){
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style scoped>

</style>