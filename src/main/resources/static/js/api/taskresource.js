import Vue from 'vue'

const tasksresource = Vue.resource('/tasks/{id}')

export default {
    add:task=> tasksresource.save({},task),
    update:task=>tasksresource.update({id:task.id}, task),
    remove:id=>tasksresource.remove({id}),
}