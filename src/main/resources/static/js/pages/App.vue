<template>
    <v-app>
        <v-app-bar app>
            <v-toolbar-title>Lite Task</v-toolbar-title>
           <v-spacer></v-spacer>
            <span v-if="profile">{{profile.name}}</span>&nbsp;
            <v-btn   v-if="profile" icon href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-app-bar>
        <v-content>
            <v-container v-if="!profile">Need auth in <a href="/login">Google</a></v-container>
            <v-container v-if="profile">
                <tasks-list  :tasks="tasks"/>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>

    import {addhandler} from "utils/ws";
    import {getIndex} from "utils/collections";
    import TasksList from "components/TasksList.vue";
    export default {
        name: "App",
        components: {TasksList},
        component:{
            TasksList
        },
        data() {
            return {
                tasks: frontendData.tasks,
                profile: frontendData.profile
            }
        },
        created() {
            addhandler(data =>{
                let index = getIndex(this.tasks,data.id)
                if(index>-1){
                    this.tasks.splice(index,1,data)
                } else{
                    this.tasks.push(data)
                }
            })
        }
    }
</script>

<style scoped>

</style>