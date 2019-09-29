<template>
    <div>
    <i>({{ task.id }})</i>   {{task.theme}}
    <span>
            <input type="button" value="Edit" @click="edit"/>
            <input type="button" value="del" @click="del"/>
        </span>
    </div>
</template>

<script>
    export default {
        name: "TaskRow",
        props:['task','editTask','deleteTask','tasks'],


        methods:{
            edit() {
                this.editTask(this.task)
            },
            del() {
                this.deleteMessage(this.task)
            }
        }
    }
</script>

<style scoped>

</style>