<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 12.01.21
  Time: 09:35
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <div>
        ${user.person.value}?
    </div>
    <div style="width: 100%; text-align: center">
        <button onclick="closeModal()">
            No
        </button>
        <button onclick="PostApi('${delete}', {id:${user.id}}, function(a) {
                if (a.status === 'success'){
                    closeModal();
                }
                })">
            Yes
        </button>
    </div>
</html>
