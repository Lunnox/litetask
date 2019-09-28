
function getIndex(list,id){
    for (var i=0;i<list.length;i++){
        if(list[i].id===id){
            return i;
        }
    }
    return -1;
}

var taskAPI = Vue.resource('/tasks{/id}');

Vue.component('task-from',{
    props:['tasks','newTask'],
    data: function() {
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
    template:
        '<div>' +
            '<input type="text" placeholder="Theme task" v-model="theme"/>' +
            '<input type="button" value="Add" @click="add"/>' +
        '</div>',
    methods:{
        add: function () {
            var task ={theme: this.theme};

            if (this.id){
                taskAPI.update({id:this.id}, task).then(result=>
                    result.json().then(data=>{
                        var index=getIndex(this.tasks,data.id);

                        this.tasks.splice(index,1,data)

                    })
                )
            }else {
                taskAPI.save({}, task).then(result =>
                    result.json().then(data => {
                        this.tasks.push(data);

                    })
                )
            }
            this.theme = '';
            this.id = '';
        }
    }
});

Vue.component('task-row',{
    props:['task','editMethod','tasks'],
    template:
        '<div>' +
        '<i>({{ task.id }})</i>   {{task.theme}}' +
        '<span> ' +
            '<input type="button" value="Edit" @click="edit"/>' +
            '<input type="button" value="del" @click="del"/>' +
        '</span>' +
        '</div>',
    methods:{
        edit: function () {
            this.editMethod(this.task);
        },
        del: function () {
            taskAPI.remove({id: this.task.id}).then(result=>{
                    if(result.ok){
                        this.tasks.splice(this.tasks.indexOf(this.task),1)
                    }
            })
        }
    }
});

Vue.component('tasks-list', {
    props: ['tasks'],
    data:function(){
      return{
          task:null
      }
    },
    template:
        '<div>' +
        '<task-from v-bind:tasks="tasks" :newTask="task"/>' +
        '<task-row v-for="task in tasks" ' +
                    'v-bind:task="task" ' +
                    'v-bind:key="task.id" ' +
                    'v-bind:editMethod="editMethod"' +
                    'v-bind:tasks="tasks"/>' +
        '</div>',
    created: function () {
        taskAPI.get().then(result=>
            result.json().then(data =>
                data.forEach(task => this.tasks.push(task))
            )
        )
    },
    methods:{
        editMethod:function (task) {
            this.task=task;
        }
    }
})

var app = new Vue({
    el: '#app',
    template:'<tasks-list :tasks="tasks" />',
    data: {
        tasks: []
    }
});