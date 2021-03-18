<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <div class="menu">
<%--        <div class="menu-item">--%>
<%--            ${user.person.surname} ${user.person.forename}--%>
<%--        </div>--%>
        <c:if test="${role ne 'user'}">
            <div class="menu-item" onclick="loadModal('${userList}')">
                <fmt:message key="users"/>
            </div>
            <c:if test="${not empty monthReport}">
                <div class="menu-item" onclick="location.href = '${context}${monthReport}'">
                    <fmt:message key="mount.report"/>
                </div>
            </c:if>
            <c:if test="${not empty reports}">
                <div class="menu-item" onclick="location.href = '${context}${reports}'">
                    <fmt:message key="application.title"/>
                </div>
            </c:if>
        </c:if>
        <div class="menu-item" onclick="logout()">
            <fmt:message key="logout"/>
        </div>
    </div>
</html>