<template>
    <v-container>
        <v-layout align-space-around justify-start column>
            <task-form :newTask="task"/>
            <task-row v-for="(task,index) in sortedTasks"
                    v-bind:task="task"
                    v-bind:key="`task.id-${index}`"
                    v-bind:editTask="editTask"
                    />
            <lazy-loader></lazy-loader>
        </v-layout>
    </v-container>
</template>

<script>

    import TaskRow from "components/TaskRow.vue";
    import TaskForm from "components/TaskForm.vue";
    import {mapGetters} from 'vuex'
    import LazyLoader from "components/LazyLoader.vue";

    export default {
        name: "TasksList",
        components: {
            LazyLoader,
            TaskRow,
            TaskForm
        },
        data() {
            return {
                task: null
            }
        },
        computed: mapGetters(['sortedTasks']),

        methods: {
            editTask(task) {
                this.task = task
            },

        }
    }
</script>

<style scoped>

</style>