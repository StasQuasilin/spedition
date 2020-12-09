let column = {
    props:{
        date:Date
    },
    components:{
        cell:cell
    },
    template:
        '<div class="report-column">' +
            '<div>' +
                '{{date.toLocaleDateString()}}' +
            '</div>' +
        '</div>'
};