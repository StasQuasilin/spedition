let cell = {
    props:{
        data:Object,
        functions:Object
    },
    methods:{
        buildRoute(){
            let split = this.data.route.split(',');
            let route = [];
            for (let i = 0; i < split.length; i++){
                route.push(split[i]);
                if (i < split.length - 1){
                    route.push(' ➞ ');
                }
            }
            return route;
        },
        onClick:function(){
            this.functions.open(this.data.id);
        }
    },
    template:'<div class="cell" v-on:click="onClick()">' +
            '<div class="cell-content" v-if="data">' +
                '<div style="display: inline-block" v-for="route in buildRoute()">' +
                    '{{(route).toUpperCase()}}' +
                '</div>' +
                '<div>' +
                    '{{data.product.name}}' +
                '</div>' +
            '</div>' +
        '</div>'
};