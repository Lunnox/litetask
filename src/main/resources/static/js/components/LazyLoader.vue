<template>
    <span>

    </span>
</template>

<script>
    import {mapActions} from 'vuex'
    export default {
        name: "LazyLoader",
        methods: mapActions(['loadPageAction']),
        mounted() {
            window.onscroll= ()=>{
                const elm= document.documentElement
                const isEndPage=elm.scrollTop+window.innerHeight=== elm.offsetHeight
                if(isEndPage){
                    this.loadPageAction()
                }
            }
        },
        beforeDestroy() {
            window.onscroll=null
        },
    }
</script>

<style scoped>

</style>