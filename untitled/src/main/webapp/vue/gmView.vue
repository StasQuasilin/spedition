gmView = new Vue({
    el:'#gm',
    components:{
        'google-map':gm,
    },
    mounted:function(){
        this.container = this.$refs.googleMap;
    },
    data:{
        apiKey:'AIzaSyDLSNZ7XRulXlSDjGEoDoW8pfajVTKPH80',
        container:null,
        mapConfig:{
            center: {
                lat: 50.861816,
                lng: 34.9475
            },
            zoom: 10
        }
    }
});