let reports = new Vue({
    el:'#reports',
    components:{
        row:row
    },
    data:{
        api:{},
        reports:{}
    },
    computed:{
        maxDate:function(){
            console.log('Compute max date');
            let now = new Date();
            let reports = this.reports;
            if (reports) {
                let values = Object.values(reports);
                for (let i in values) {
                    if (values.hasOwnProperty(i)) {
                        let value = values[i];
                        for (let j in value.reports){
                            if (value.reports.hasOwnProperty(j)){
                                let report = value.reports[j];
                                let date = new Date(report.leave);
                                if (date > now) {
                                    now = date;
                                }
                            }
                        }
                    }
                }
            }
            return now;
        },
        owners:function(){
            let owners = {};
            let values = Object.values(this.reports);
            for (let i in values){
                if (values.hasOwnProperty(i)){
                    let value = values[i].owner;
                    if (!owners[value.id]){
                        owners[value.id] = value;
                    }
                }
            }
            console.log(owners);
            return Object.values(owners);
        }
    },
    methods:{
        getReport:function(owner, date){
            let reports = this.reports[owner.id];
            for (let i in reports){
                if (reports.hasOwnProperty(i)){
                    for (let j in reports[i]){
                        if (reports[i].hasOwnProperty(j)){
                            let report = reports[i][j];
                            let leave = new Date(report.leave);
                            if (leave.toLocaleDateString() === date.toLocaleDateString()){
                                return report;
                            }
                        }
                    }
                }
            }
        },
        sumDate:function(date, days){
            let d = new Date();
            d.setDate(date.getDate() - days);
            return d;//.setDate(date.getDate() + days);
        },
        show:function(item){
            let data = {
                id:item.id
            };
            loadModal(this.api.show, data);
        },
        handler:function (data) {
            console.log(data);
            for (let i in data.add){
                if (data.add.hasOwnProperty(i)){
                    let add = data.add[i];
                    this.add(add);
                }
            }
            if (data.update){
                this.add(data.update);
            }
        },
        add:function(data){
            let owner = Object.assign({}, data.owner);
            if (!this.reports[owner.id]){
                Vue.set(this.reports, owner.id, {
                    owner: owner,
                    reports:[]
                });
            }
            delete data.owner;
            this.update(this.reports[owner.id].reports, data);
        },
        update:function(list, data){
            let found = false;

            for (let i in list){
                if (list.hasOwnProperty(i)){
                    let item = list[i];
                    if (item.id === data.id){
                        found = true;
                        list.splice(i, 1, data);
                    }
                }
            }

            if (!found){
                list.push(data);
            }
            list.sort(function (a, b) {
                return new Date(b.leave) - new Date(a.leave);
            })
        }
    }
});