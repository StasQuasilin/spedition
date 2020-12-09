let modalLayer;
let modalTitle;
let modalContent;
let logoutApi = '';

$(document).ready(function(){
    modalLayer = document.getElementById('modalLayer');
    // modalTitle = document.getElementById('modalTitle');
    // modalContent = document.getElementById('modalContent');
    closeModal();
});
function closeModal(){
    modalLayer.style.display = 'none';
}
function showModal(content) {
    $(modalLayer).html(content);
    modalLayer.style.display = 'block';
}
function loadModal(url, params) {
    PostReq(url, params, function (ans) {
        showModal(ans)
    })
}
function logout() {
    PostReq(logoutApi);
}