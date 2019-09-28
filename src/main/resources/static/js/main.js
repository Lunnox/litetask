
var taskAPI = Vue.resource('/tasks{/id}');

Vue.component('task-row',{
    props:['task'],
    template: '<div>' +
                    '<i>({{ task.id }})</i>  ' +
                    '  {{task.theme}}' +
                '</div>'
});

Vue.component('tasks-list', {
    props: ['tasks'],
    template: '<div>' +
                    '<task-row v-for="task in tasks" v-bind:task="task" v-bind:key="task.id" />' +
                '</div>',
    created: function () {
        taskAPI.get().then(result=>
            result.json().then(data =>
                data.forEach(task => this.tasks.push(task))
            )
        )
    }
})

var app = new Vue({
    el: '#app',
    template:'<tasks-list :tasks="tasks" />',
    data: {
        tasks: []
    }
});