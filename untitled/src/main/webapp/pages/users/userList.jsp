<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 12.01.21
  Time: 09:04
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<html>
    <div>
        <c:forEach items="${users}" var="usr">
            <div>
                <span class="modal-close-button" onclick="loadModal('${delete}', {id:${usr.id}})">
                    &times;
                </span>
                <span>
                    ${usr.person.value}
                </span>
            </div>
        </c:forEach>
    </div>
    <div style="width: 100%; text-align: center">
        <button onclick="loadModal('${registration}')">
            <fmt:message key="registration"/>
        </button>
        <button onclick="closeModal()">
            <fmt:message key="button.close"/>
        </button>
    </div>
</html>
