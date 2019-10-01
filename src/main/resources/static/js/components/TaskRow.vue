<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            {{task.theme}}
        </v-card-text>
        <media v-if="task.link" :task="task"></media>
        <v-card-actions>
            <v-btn small text @click="edit">edit</v-btn>
            <v-btn  @click="del">del</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import {mapActions} from 'vuex'

    import Media from "components/media/Media.vue";

    export default {
        name: "TaskRow",
        components: {Media},
        props:['task','editTask'],
        component:{Media},
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