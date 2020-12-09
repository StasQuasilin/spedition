registration = new Vue({
    el:'#reg',
    data:{
        api:{},
        user:{
            surname:'',
            forename:'',
            patronymic:'',
            role:'',
            phone:'',
            password:''
        },
        errors:{
            surname:false,
            forename:false,
            patronymic:false,
            phone:false,
            password:false
        },
        answer:'',
        roles:[],
        supervisors:[]
    },
    methods:{
        registration:function () {
            if (!this.checkErrors()){
                const self = this;
                PostApi(this.api.registration, this.user, function(answer){
                    if (answer.answer === 'success'){
                        closeModal();
                    } else {
                        self.answer = answer.message;
                    }
                })
            }
        },
        checkErrors:function(){
            let errors = this.errors;
            errors.surname = this.user.surname === '';
            errors.forename = this.user.forename === '';
            errors.patronymic = this.user.patronymic === '';
            errors.phone = this.user.phone === '' || this.checkPhone();
            errors.password = this.user.password === '';
            return errors.surname || errors.forename || errors.patronymic || errors.phone || errors.password;
        },

        checkPhone() {
            let phone = this.user.phone;
            phone = this.user.phone = phone.split(' ').join('');
            phone = this.user.phone = phone.split('-').join('');
            if (phone.indexOf('+38') !== -1){
                phone = phone.replace('+380', '');
            }
            return /\D/.test(phone) && (phone.length !== 10 || phone.length !== 13);
        }
    }
});