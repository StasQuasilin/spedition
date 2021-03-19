gm = {
    props: {
        config: Object,
        apikey: String,
        container:HTMLDivElement
    },
    data:function () {
        return{
            google: null,
            map: null,
        }
    },
    mounted:function () {
        const loader = new google.maps.plugins.loader.Loader({
            apiKey: this.apikey,
            // version: "weekly",
            // libraries: ["places"]
        });
        const self= this;
        const mapContainer = self.$refs.googleMap;
        loader
            .load()
            .then(() => {
                new google.maps.Map(self.container, self.config);
                // self.initializeMap()
            })
            .catch(e => {
                console.log(e.message)
            });
        // this.initializeMap()
    },
    methods: {
        initializeMap() {
            const mapContainer = this.$refs.googleMap;
            console.log(this.google);
            this.map = new this.google.maps.Map(
                mapContainer, this.mapConfig
            )
        }
    },
    template:'<div>' +
                '<div class="google-map" rel="googleMap"></div>' +
        '</div>'
};