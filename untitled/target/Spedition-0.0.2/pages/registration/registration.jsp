
<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 22.06.20
  Time: 08:29
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setBundle basename="messages"/>
<fmt:setLocale value="${locale}"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <script src="${context}/vue/registration.vue"></script>
        <script>
            registration.api.registration = '${registration}';
            <c:forEach items="${roles}" var="r">
            registration.roles.push({
                id:'${r}',
                name:'<fmt:message key="role.${r}"/>'
            });
            </c:forEach>
            <c:forEach items="${supervisors}" var="supervisor">
            registration.supervisors.push(
               ${supervisor.toJson()}
            );
            </c:forEach>
            if (registration.supervisors.length > 0) {
                registration.user.supervisor = registration.supervisors[0].id;
            }
            registration.user.role = 'user'
        </script>
        <title></title>
    </head>
    <body>
        <table id="reg">
            <tr>
                <td>
                    <label for="surname">
                        <fmt:message key="person.surname"/>
                    </label>
                </td>
                <td>
                    <input id="surname" v-model="user.surname" :class="{error : errors.surname}"
                           v-on:focus="errors.surname=false" autocomplete="off" >
                </td>
            </tr>
            <tr>
                <td>
                    <label for="forename">
                        <fmt:message key="person.forename"/>
                    </label>
                </td>
                <td>
                    <input id="forename" v-model="user.forename" :class="{error : errors.forename}"
                           v-on:focus="errors.forename=false" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="patronymic">
                        <fmt:message key="person.patronymic"/>
                    </label>
                </td>
                <td>
                    <input id="patronymic" v-model="user.patronymic" :class="{error : errors.patronymic}"
                           v-on:focus="errors.patronymic=false" autocomplete="off">
                </td>
            </tr>
            <c:if test="${role eq 'admin'}">
                <tr>
                    <td>
                        <label for="role">
                            <fmt:message key="user.role"/>
                        </label>
                    </td>
                    <td>
                        <select id="role" style="width: 100%" v-model="user.role">
                            <option v-for="role in roles" :value="role.id">
                                {{role.name}}
                            </option>
                        </select>
                    </td>
                </tr>
            </c:if>
            <tr v-if="user.role === 'user'">
                <td>
                    <fmt:message key="role.supervisor"/>
                </td>
                <td>
                    <select v-model="user.supervisor" style="width: 100%">
                        <option v-for="supervisor in supervisors" :value="supervisor.id">
                            {{supervisor.person.surname}} {{supervisor.person.forename}}
                        </option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="phone">
                        <fmt:message key="person.phone"/>
                    </label>
                </td>
                <td>
                    <input id="phone" type="tel" v-model="user.phone" :class="{error : errors.phone}"
                           v-on:focus="errors.phone = false" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td>
                    <label for="password">
                        <fmt:message key="password"/>
                    </label>
                </td>
                <td>
                    <input id="password" v-model="user.password" :class="{error : errors.password}"
                           v-on:focus="errors.password = false" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center">
                    <button onclick="closeModal()">
                        <fmt:message key="button.cancel"/>
                    </button>
                    <button v-on:click="registration">
                        <fmt:message key="button.registration"/>
                    </button>
                </td>
            </tr>
            <tr v-if="this.answer">
                <td colspan="2" style="color: orangered; text-align: center">
                    {{answer}}
                </td>
            </tr>
        </table>
    </body>
</html>
