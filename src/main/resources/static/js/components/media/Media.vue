<template>
    <v-card>
        <v-flex v-if="type=== 'href'" xs12 sm6 offset-sm3>
            <v-img v-if="task.linkCover" :src="task.linkCover" aspect-ratio="2.75"></v-img>
            <v-card-title>
                <div>
                    <h3>
                        <a :href="task.link">{{task.linkTitle || task.link }}</a>
                    </h3>
                    <div v-if="task.linkDescription">{{task.linkDescription}}</div>
                </div>
            </v-card-title>
        </v-flex>
        <v-flex v-if="type=== 'image'" xs12 sm6 offset-sm3>
            <a :href="task.link">
                <v-img v-if="task.linkCover" :src="task.linkCover" aspect-ratio="2.75"></v-img>
                {{task.link}}
            </a>
        </v-flex>
        <v-flex v-if="type=== 'youtu'" xs12 sm6 offset-sm3>
            <you-tube v-bind:src="task.link"></you-tube>
        </v-flex>
    </v-card>
</template>

<script>
    import YouTube from "./YouTube.vue";
    export default {
        name: "Media",
        components: {YouTube},
        props: ['task'],
        data(){
            return{
                type:'href'
            }
        },
        beforeMount() {
            if(this.task.link.indexOf('youtu')>-1){
                this.type='youtu'
            }else if (this.task.link.match(/\.(jpeg|jpg|gif|png)$/)!== null){
                this.type = 'image'
            } else{
                this.type='href'
            }
        }
    }
</script>

<style scoped>

</style>