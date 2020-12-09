monthReport = new Vue({
    el:'#report',
    components:{
        cell:cell,
        'user-cell':userCell
    },
    data:{
        api:{},
        reports:{},
        date:new Date(),
        idx:0
    },
    mounted:function(){
        console.log('Mount');
        this.date.setDate(1);
    },
    methods:{
        dateOffset:function (dir) {
            this.date.setMonth(this.date.getMonth() + dir);
        },
        getReports:function () {
            let data = {
                month:this.date.getMonth(),
                year: this.date.getFullYear()
            };
            const self = this;
            PostApi(this.api.getReports, data, function (a) {
                if (a.status === 'success'){
                    self.reports = a.reports;
                } else {
                    console.log(a);
                }
            })
        }
    }
});