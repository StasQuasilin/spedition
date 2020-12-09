<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 01.06.20
  Time: 16:58
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="${locale}">
<head>
    <title><fmt:message key="application.title"/></title>
    <link rel="stylesheet" href="${context}/css/main.css"/>
    <link rel="stylesheet" href="${context}/css/menu.css">
    <script>
        if(typeof context == "undefined"){
            context = '${context}';
        }
        if (typeof userId == "undefined"){
            userId = ${user.id};
        }
    </script>
    <script src="${context}/external/jquery.min.js"></script>
    <script src="${context}/js/utils.js"></script>
    <script src="${context}/external/vue.js"></script>
    <script src="${context}/js/socket.js"></script>
    <script src="${context}/js/modal.js"></script>
    <script>
        logoutApi = context + '${logout}';
    </script>
</head>
    <body style="background-color: #d3f9d3;">
        <div id="modalLayer" class="modal-layer">

        </div>
        <div style="padding: 8px">
            <div class="menu-holder">
                <jsp:include page="menu.jsp"/>
            </div>
            <jsp:include page="${content}"/>
        </div>
    </body>
</html>
