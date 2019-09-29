<template>
    <v-layout row>
    <v-text-field placeholder="Theme task" v-model="theme" label="New Task"/>
        <v-btn  @click="add">
            Add
        </v-btn>
    </v-layout>
</template>

<script>
    import taskApi from "api/taskresource";


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
                const task ={id:this.id, theme: this.theme}

                if (this.id){
                    taskApi.update( task).then(result=>
                        result.json().then(data=>{
                            const index=this.tasks.findIndex(item => item.id===data.id)

                            this.tasks.splice(index,1,data)

                        })
                    )
                }else {
                    taskApi.add( task).then(result =>
                        result.json().then(data => {
                            const index=this.tasks.findIndex(item => item.id===data.id)
                            if(index>-1){
                                this.tasks.splice(index,1,data)
                            }else{
                                this.tasks.push(data)
                            }
                        })
                    )
                }

                this.theme = ''
                this.id = ''
            }
        }
    }
</script>

<style scoped>

</style>