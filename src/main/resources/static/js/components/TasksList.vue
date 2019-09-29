<template>
    <v-layout align-space-around justify-start column>
        <task-form v-bind:tasks="tasks" :newTask="task"/>
        <task-row v-for="task in sortedTasks"
                v-bind:task="task"
                v-bind:key="task.id"
                v-bind:editTask="editTask"
                v-bind:deleteTask="deleteTask"
                v-bind:tasks="tasks"/>

    </v-layout>
</template>

<script>

    import TaskRow from "components/TaskRow.vue";
    import TaskForm from "components/TaskForm.vue";
    export default {
        name: "TasksList",
        components: {
            TaskRow,
            TaskForm
        },
        props: ['tasks'],
        data() {
            return {
                task: null
            }
        },
        computed:{
          sortedTasks(){
              return this.tasks.sort((a,b)=>-(a.id -b.id))
          }
        },
        methods: {
            editTask(task) {
                this.task = task
            },
            deleteTask(task) {
                this.$resource('/tasks/{id}').remove({id: task.id}).then(result => {
                    if (result.ok) {
                        this.tasks.splice(this.tasks.indexOf(task), 1)
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>