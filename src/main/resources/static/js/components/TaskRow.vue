<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            {{task.theme}}
        </v-card-text>
        <v-card-actions>
            <v-btn small text @click="edit">edit</v-btn>
            <v-btn  @click="del">del</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: "TaskRow",
        props:['task','editTask'],


        methods:{
            ...mapActions(['deleteTaskAction']),
            edit() {
                this.editTask(this.task)
            },
            del() {
                this.deleteTaskAction(this.task)
            }
        }
    }
</script>

<style scoped>

</style>