import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

let stompClient = null
const handlers = []

export function connect() {
    const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(socket)

    stompClient.debug=()=> {}
    stompClient.connect({}, frame=> {
       // setConnected(true)
        // console.log('Connected: ' + frame)
        stompClient.subscribe('/topic/activity', task =>{
            handlers.forEach(handler => handler(JSON.parse(task.body)))
        }) 
    }) 
}

export function addhandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect() 
    }
    console.log("Disconnected") 
}

export function sendTask(task) {
    stompClient.send("/app/changeTask", {}, JSON.stringify(task))
}