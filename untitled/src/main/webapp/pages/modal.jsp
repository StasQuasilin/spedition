<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 04.06.20
  Time: 09:43
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="messages"/>
<fmt:setLocale value="${locale}"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<link rel="stylesheet" href="${context}/css/modalLayer.css">
    <body>
        <div class="modal-body">
            <div class="model-content">
                <div class="modal-header">
                    <div id="modalTitle" class="modal-title">
                        &nbsp;
                        <c:if test="${not empty title}">
                            <fmt:message key="${title}"/>
                        </c:if>
                    </div>
                    <div class="modal-close-button" onclick="closeModal()">
                        &times;
                    </div>
                </div>
                <div id="modalContent" class="modal-data">
                    <jsp:include page="${content}"/>
                </div>
            </div>
        </div>
    </body>
</html>
