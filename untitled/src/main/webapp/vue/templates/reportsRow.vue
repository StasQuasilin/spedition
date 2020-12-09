let row = {
    components:{
        cell:cell
    },
    props:{
        date:Date,
        owners:Array
    },
    methods:{
        getReport:function(owner, date){
            return reports.getReport(owner, date)
        }
    },
    template:
        '<div class="report-row">' +
            '<div class="cell date-cell">' +
                '{{date.toLocaleDateString()}}' +
            '</div>' +
            '<cell v-for="o in owners" :report="getReport(o, date)"></cell>' +
        '</div>'
};