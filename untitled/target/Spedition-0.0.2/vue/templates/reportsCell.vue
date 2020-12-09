let cell = {
    props:{
        report:Object
    },
    computed:{
        route:function(){
            return this.report.route.split(',');
        }
    },
    template:
        '<div class="cell">' +
            '<div class="report-cell" v-if="report">' +
                '<div class="report-cell-content">' +
                    '<span v-for="(r, i) in route">' +
                        '{{r.toUpperCase()}}' +
                        '<span v-if="i < route.length - 1">' +
                            '&#10142;' +
                        '</span>' +
                    '</span>' +
                    '<div>{{report.product.name}}</div>' +
                '</div>' +
            '</div>' +
            '<span v-else>' +
                '&nbsp;' +
            '</span>' +
        '</div>'
};