<template>
    <div>
    <input type="text" placeholder="Theme task" v-model="theme"/>
    <input type="button" value="Add" @click="add"/>
    </div>
</template>

<script>

    function getIndex(list,id){
        for (var i=0;i<list.length;i++){
            if(list[i].id===id){
                return i
            }
        }
        return -1
    }
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
                this.theme = ''
                this.id = ''
            }
        }
    }
</script>

<style scoped>

</style>