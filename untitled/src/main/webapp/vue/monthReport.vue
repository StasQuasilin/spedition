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
        idx:0,
        loading:false
    },
    mounted:function(){
        this.date.setDate(1);
    },
    methods:{
        show:function(id){
            loadModal(this.api.show,{id: id});
        },
        dateOffset:function (dir) {
            this.date.setMonth(this.date.getMonth() + dir);
            this.getReports();
        },
        getReports:function () {
            let data = {
                month:this.date.getMonth() + 1,
                year: this.date.getFullYear()
            };
            const self = this;
            this.loading = true;
            PostApi(this.api.getReports, data, function (a) {
                if (a.status === 'success'){
                    self.reports = a.reports;
                    self.loading = false;
                } else {
                    console.log(a);
                }
            }, function (e) {
                self.loading = false;
            })
        }
    }
});