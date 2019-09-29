<template>
    <v-layout row>
    <v-text-field placeholder="Theme task" v-model="theme" label="New Task"/>
        <v-btn  @click="add">
            Add
        </v-btn>
    </v-layout>
</template>

<script>
    import {sendTask} from "utils/ws";


    export default {
        name: "TaskForm",
        props:['tasks','newTask'],
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
            add() {
                sendTask({id:this.id,theme: this.theme})
                this.theme = ''
                this.id = ''
            }
        /*     {
                var task ={theme: this.theme}

                if (this.id){
                    this.$resource('/tasks/{id}').update({id:this.id}, task).then(result=>
                        result.json().then(data=>{
                            const index=getIndex(this.tasks,data.id)

                            this.tasks.splice(index,1,data)

                        })
                    )
                }else {
                    this.$resource('/tasks/{id}').save({}, task).then(result =>
                        result.json().then(data => {
                            this.tasks.push(data)

                        })
                    )
                }

            }*/
        }
    }
</script>

<style scoped>

</style>