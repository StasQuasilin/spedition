
<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 04.06.20
  Time: 09:44
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="ft" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <body>
        <div style="display: flex">
            <table border="1">
                <tr>
                    <td colspan="3">
                        <fmt:message key="forwarder"/>: ${report.owner.person.value}
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <fmt:message key="route"/>: ${fn:toUpperCase(report.buildRoute())}
                    </td>
                </tr>
                <tr>
                    <td>
                        <fmt:message key="product"/>
                    </td>
                    <td colspan="2">
                        ${report.product.name}
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <fmt:message key="driver"/>:<br>
                    </td>
                </tr>
                <c:forEach items="${report.details}" var="detail">
                    <tr>
                        <td>
                            <div style="padding-left: 16px">
                                <div>
                                        ${detail.driver.person.value}
                                </div>
                            </div>
                        </td>
                        <td colspan="2">
                            <c:if test="${detail.weight.gross-detail.weight.tare ne 0}">
                                <fmt:formatNumber value="${detail.weight.gross}"/>
                                -
                                <fmt:formatNumber value="${detail.weight.tare}"/>
                                =
                                <fmt:formatNumber value="${detail.weight.gross-detail.weight.tare}"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td>
                        <fmt:message key="date.leave"/>
                    </td>
                    <td colspan="2">
                        <fmt:formatDate value="${report.leaveTime}" pattern="dd.MM.yyyy hh:mm"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="vertical-align: top">
                        <table>
                            <tr>
                                <td colspan="2" style="max-height: 200pt; padding-left: 8pt">
                                    <div>
                                        <table>
                                            <c:forEach items="${fields}" var="field" varStatus="status">
                                                <tr>
                                                    <td colspan="2">
                                                        <b>
                                                            <fmt:message key="report.point"/> # ${status.index + 1}
                                                        </b>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        ${field.counterparty.name}
                                                    </td>
                                                </tr>
                                                <c:if test="${not empty field.arriveTime}">
                                                    <tr>
                                                        <td>
                                                            <fmt:message key="date.arrive"/>
                                                        </td>
                                                        <td>
                                                            <fmt:formatDate value="${field.arriveTime}" pattern="dd.MM.yy HH:mm"/>
                                                        </td>
                                                    </tr>
                                                    <c:if test="${not empty field.leaveTime}">
                                                        <tr>
                                                            <td>
                                                                <fmt:message key="date.leave"/>
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate value="${field.leaveTime}" pattern="dd.MM.yy HH:mm"/>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${field.money != 0}">
                                                    <tr>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${field.money > 0}">
                                                                    <fmt:message key="money.get"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <fmt:message key="money"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <fmt:formatNumber value="${field.money < 0 ? -field.money : field.money}"/>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                                <c:set var="haveCounterpartyWeight" value="false"/>
                                                <c:forEach items="${report.details}" var="detail">
                                                    <c:if test="${detail.haveWeight(field.uuid)}">
                                                        <c:set var="haveCounterpartyWeight" value="true"/>
                                                    </c:if>
                                                </c:forEach>

                                                <c:if test="${haveCounterpartyWeight}">
                                                    <tr>
                                                        <td style="vertical-align: top">
                                                            <fmt:message key="weight.point"/>
                                                        </td>
                                                    </tr>
                                                    <c:forEach items="${report.details}" var="detail">
                                                        <c:if test="${detail.haveWeight(field.uuid)}">
                                                            <tr>
                                                                <td>
                                                                        ${detail.driver.person.value}
                                                                </td>
                                                                <td>
                                                                    <c:set var="counterpartyWeight" value="${detail.counterpartyWeight[field.uuid]}"/>
                                                                    <fmt:formatNumber value="${counterpartyWeight.gross}"/>
                                                                    -
                                                                    <fmt:formatNumber value="${counterpartyWeight.tare}"/>
                                                                    =
                                                                    <fmt:formatNumber value="${counterpartyWeight.gross-counterpartyWeight.tare}"/>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:if>
                                                <tr>
                                                    <td colspan="2" style="border-bottom: solid gray 1pt; "></td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty report.done}">
                                <tr>
                                    <td colspan="2">
                                        <fmt:message key="date.done"/>:
                                        <fmt:formatDate value="${report.leaveTime}" pattern="dd.MM"/> -
                                        <fmt:formatDate value="${report.done}" pattern="dd.MM"/>
                                        <c:set var="length" value="${report.length()}"/>
                                        ( ${length}
                                        <c:choose>
                                            <c:when test="${length == 1}">
                                                <fmt:message key="day.1"/>
                                            </c:when>
                                            <c:when test="${length < 5}">
                                                <fmt:message key="day.2"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="day.5"/>
                                            </c:otherwise>
                                        </c:choose>
                                        )
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td colspan="2">
                                    <fmt:message key="money.spending"/>:
                                </td>
                            </tr>
                            <tr>
                                <td style="vertical-align: top">
                                    <fmt:message key="fare"/>
                                </td>
                                <td>
                                    <c:set var="totalFare" value="0"/>
                                    <c:forEach items="${report.fares}" var="fare" varStatus="fareStatus">
                                        <div>
                                                ${fareStatus.index + 1}. ${fn:toUpperCase(fare.description)}: ${fare.amount}
                                        </div>
                                        <c:set var="totalFare" value="${totalFare + fare.amount}"/>
                                    </c:forEach>
                                    <b>
                                        <c:if test="${totalFare > 0}">
                                            <fmt:message key="total"/>:
                                        </c:if>
                                        <fmt:formatNumber value="${totalFare}"/>
                                    </b>
                                </td>
                            </tr>
                            <tr>
                                <td style="vertical-align: top">
                                    <fmt:message key="expenses"/>
                                </td>
                                <td colspan="3">
                                    <c:set var="totalExpenses" value="0"/>
                                    <c:forEach items="${report.expenses}" var="expense" varStatus="exStatus">
                                        <div>
                                                ${exStatus.index + 1}. ${fn:toUpperCase(expense.description)}: ${expense.amount}
                                        </div>
                                        <c:set var="totalExpenses" value="${totalExpenses + expense.amount}"/>
                                    </c:forEach>
                                    <b>
                                        <c:if test="${totalExpenses > 0}">
                                            <fmt:message key="total"/>:
                                        </c:if>
                                        <fmt:formatNumber value="${totalExpenses}"/>
                                    </b>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td style="vertical-align: top;">
                        <c:if test="${fn:length(notes) > 0}">
                            <div style="height: 100%; display: block;">
                                <div style="width: 100%; display: block; text-align: center">
                                    <fmt:message key="notes"/>
                                </div>
                                <div style="padding: 0 4px; max-width: 300px;">
                                    <c:forEach items="${notes}" var="note">
                                        <div style="border-bottom: solid gray 1pt">
                                            <b>
                                                <fmt:formatDate value="${note.time}" pattern="HH:mm dd.MM"/>
                                            </b>
                                            <span style="word-wrap: break-spaces">
                                                    ${note.note}
                                            </span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" style="text-align: center">
                        <button onclick="closeModal()">
                            <fmt:message key="button.close"/>
                        </button>
                    </td>
                </tr>
            </table>
            <div>
                <script src="https://unpkg.com/@googlemaps/js-api-loader@1.0.0/dist/index.min.js"></script>
                <script src="${context}/vue/gm.vue?v=${now}"></script>
                <script src="${context}/vue/gmView.vue?v=${now}"></script>
                <div id="gm" >
                    <google-map :apikey="apiKey" :container="container" :config="mapConfig" ></google-map>
                    <div style="width: 400pt; height: 300pt; background-color: lightgreen" ref="googleMap"></div>
                </div>
            </div>
        </div>
    </body>
</html>
