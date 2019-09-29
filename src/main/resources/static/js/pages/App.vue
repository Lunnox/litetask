<template>
    <div>
    <div v-if="!profile">Need auth in <a href="/login">Google</a></div>
    <div v-else>
        <div>{{profile.name}}&nbsp; <a href="/logout">logout</a></div>
        <tasks-list  :tasks="tasks"/>
        </div>
    </div>
</template>

<script>


    import TasksList from "../components/TasksList.vue";
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
    }
</script>

<style scoped>

</style>