import Vue from 'vue'
import Vuex from 'vuex'
import taskApi from 'api/taskresource'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        tasks,
        ...frontendData
    },
    getters: {
        sortedTasks: state => (state.tasks ||[]).sort((a, b)=> -(a.id - b.id))
    },
    mutations: {
        addTaskMutation(state, task){
            if(!state.tasks.find(it=>it.id === task.id)) {
                state.tasks = [
                    ...state.tasks,
                    task
                ]
            }
        },
        updateTaskMutation(state, task){
            const index = (state.tasks ||{}).findIndex(item=> item.id === task.id)
            state.tasks = [
                ...state.tasks.slice(0,index),
                task,
                ...state.tasks.slice(index+1)
            ]
        },
        deleteTaskMutation(state, task){
            const index = state.tasks.findIndex(item=> item.id === task.id)
            if(index>-1){
                state.tasks = [
                    ...state.tasks.slice(0,index),
                    ...state.tasks.slice(index+1)
                ]
            }
        },
        addTaskPageMutation(state, tasks){
            const targetTasks = state.tasks.concat(tasks).reduce((res,val)=>{
                res[val.id]=val
                return res
            },{})
            state.tasks=Object.values(targetTasks)
        },
        updateTotalPagesMutation(state,totalPage){
            state.totalPage=totalPage
        },
        updateCurrentPageMutation(state, currentPage){
            state.currentPage=currentPage
        },
    },
    actions:{
        async addTaskAction({commit,state}, task){
            const result = await taskApi.add( task)
            const data=  await result.json()
            const index=state.tasks.findIndex(item => item.id===data.id)

            if(index>-1){
                commit('addTaskMutation',data)
            }else{
                commit('updateTaskMutation',data)
            }
        },
        async updateTaskAction({commit}, task){
            const result = await taskApi.update( task)
            const data=  await result.json()

            commit('updateTaskMutation',data)
        },
        async deleteTaskAction({commit}, task){
            const result = await taskApi.remove(task.id)
            if (result.ok) {
                commit('deleteTaskMutation',task)
            }
        },
        async loadPageAction({commit,state}){
            const response = await taskApi.page(state.currentPage+1)
            const data =await response.json()

            commit('addTaskPageMutation',data.tasks)
            commit('updateTotalPagesMutation',data.totalPage)
            commit('updateCurrentPageMutation',Math.min(data.currentPage,data.totalPage))
        },

    }
})