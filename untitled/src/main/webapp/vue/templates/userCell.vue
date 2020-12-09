let userCell = {
    props:{
        user:Object
    },
    template:'<div style="padding: 0 8px">' +
            '<div class="middle">' +
                '{{user.person.surname}}' +
            '</div>' +
            '<div class="middle">' +
                '{{user.person.forename}}' +
            '</div>' +
            '<div class="middle">' +
                '{{user.person.patronymic}}' +
            '</div>' +
            '<div class="middle" v-for="phone in user.person.phones" style="font-size: 12px">' +
                '( {{phone}} )' +
            '</div>' +
        '</div>'
};