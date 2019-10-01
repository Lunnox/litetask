<template>
    <v-layout row>
    <v-text-field placeholder="Theme task" v-model="theme" label="New Task" @keyup.enter="add"/>
        <v-btn  @click="add">
            Add
        </v-btn>
    </v-layout>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        name: "TaskForm",
        props:['newTask'],
        data() {
            return {
                theme: '',
                id:''
            }
        },
        watch:{
            newTask: function(newVal, oldVal){
                this.theme=newVal.theme;
                this.id=newVal.id
            }
        },
        methods:{
            ...mapActions(['addTaskAction','updateTaskAction']),
            add() {
                const task ={id:this.id, theme: this.theme}
                if (this.id){
                    this.updateTaskAction( task)
                }else {
                    this.addTaskAction( task)
                }
                this.theme = ''
                this.id = ''
            }
        }
    }
</script>

<style scoped>

</style>