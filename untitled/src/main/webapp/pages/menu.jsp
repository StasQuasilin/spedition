<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <div class="menu">
        <div class="menu-item">
            ${user.person.surname} ${user.person.forename}
        </div>
        <c:if test="${role ne 'user'}">
            <div class="menu-item" onclick="loadModal('${registration}')">
                <fmt:message key="registration"/>
            </div>
            <div class="menu-item" onclick="location.href = '${context}${monthReport}'">
                <fmt:message key="mount.report"/>
            </div>
        </c:if>
        <div class="menu-item" onclick="logout()">
            <fmt:message key="logout"/>
        </div>
    </div>
</html>