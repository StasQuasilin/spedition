let login = new Vue({
    el:'#signIn',
    data:{
        api:{},
        login:'',
        password:''
    },
    methods:{
        signIn:function(){
            let data = {
                phone:this.login,
                password:btoa(this.password),
                redirect:true
            };
            PostApi(this.api.login, data, function (ans) {
                console.log(ans);
                if (ans.status === 'success'){
                    window.location = ans.redirect;
                }
            })
        }
    }
});