let reports = new Vue({
    el:'#reports',
    components:{
        cell:cell,
        'user-cell':userCell
    },
    data:{
        api:{},
        users:[],
        reports:{},
        scroll:0,
        opacity:0,
        indices:{},
        tableLength:21
    },
    methods:{
        handle(data){
            console.log(data);
            for (let a in data.add){
                if (data.add.hasOwnProperty(a)){
                    let add = data.add[a];
                    this.update(add);
                }
            }
            if (data.update){
                this.update(data.update);
            }
        },
        update:function(item){
            let owner = item.owner;
            if (!this.reports[owner.id]){
                Vue.set(this.reports, owner.id, {})
            }
            let reports = this.reports[owner.id];
            let done = item.done ? new Date(item.done) : new Date();
            let doneDate = done.toLocaleDateString();
            if (!reports[doneDate]){
                Vue.set(reports, doneDate, {});
            }
            let report = reports[doneDate];
            let leave = new Date(item.leave);
            report.length = Math.floor((done - leave) / 1000 / 60 / 60 / 24) + 1;
            Vue.set(report, 'data', {
                id:item.id,
                driver:item.driver,
                route : item.route,
                product : item.product,
                length:Math.floor((done - leave) / 1000 / 60 / 60 / 24) + 1,
                done:done,
                leave:leave
            });
        },
        nowDate:function(){
            return new Date();
        },
        dateOffset:function(date, offset){
            return new Date(date.setDate(date.getDate() - offset));
        },
        getReportLength(report, date){
            // let date = this.dateOffset(new Date(), index).toLocaleDateString();
            let length = this.getSomeFromReport(report, date, 'length');
            if (length != null){
                return length;
            }
            return 1;
        },
        getDates:function(report){
            let dates = [];
            let now = new Date();
            for (let i = 0; i < this.getReportCells(report); i++){
                let date = now.toLocaleDateString();
                dates.push(date);
                let length = this.getReportLength(report, date);
                now.setDate(now.getDate() - length);
            }
            return dates;
        },
        getReportData:function(report, date){
            return this.getSomeFromReport(report, date, 'data');
        },
        getSomeFromReport:function(report, date, field){
            if (this.reports.hasOwnProperty(report)){
                let reports = this.reports[report];
                if (reports.hasOwnProperty(date)){
                    return reports[date][field];
                }
            }
            return null;
        },
        getReportCells:function (report) {
            let length = this.tableLength;
            if (this.reports.hasOwnProperty(report)){
                let reports = this.reports[report];
                for (let i in reports){
                    if (reports.hasOwnProperty(i)){
                        let index = reports[i];
                        length -= (index.length - 1);
                    }
                }
            }
            return length;
        },
        scrollListener:function(){
            let scroll = this.$refs.reportContainer.scrollLeft;
            this.scroll = scroll;
            this.opacity = scroll / 100;
        },
        backgroundColor:function(){
            return 'rgba(248, 248, 248,' + this.opacity + ')';
        },
        open:function(id){
            loadModal(this.api.show, {id:id});
        },
        getColumnIndex:function(row){
            return 1;
        },
        sortUsers:function(){
            this.users.sort(function (a,b) {
                let sort = a.person.surname.localeCompare(b.person.surname);
                if (sort === 0){
                    sort = a.person.forename.localeCompare(b.person.forename);
                }
                return sort;
            })
        }
    }
});