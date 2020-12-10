<%--
  Created by IntelliJ IDEA.
  User: szpt-user045
  Date: 23.09.20
  Time: 10:12
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>

</head>

<div id="report">
    <div style="width: 100%; text-align: center">
        <span v-if="loading">
            <fmt:message key="loading"/>
        </span>
        <template v-else>
            <span v-on:click="dateOffset(-1)" style="font-size: 16pt">&#9666;</span>
            <span>{{date.toLocaleDateString().substring(3)}}{{idx}}</span>
            <span v-on:click="dateOffset(1)" style="font-size: 16pt">&#9656;</span>
        </template>
    </div>
    <div>
        <div v-for="(val, key) in reports">
            <span style="font-weight: bold">
                {{key}} ( {{val.length}} )
            </span>
            <div style="padding-left: 16px">
                <div v-for="r in val">
                    <span>
                        {{new Date(r.leave).toLocaleDateString()}}
                    </span>
                    <span>
                        {{r.route}}
                        <template v-if="r.product">
                            {{r.product.name}}
                        </template>
                    </span>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${context}/vue/templates/cell.vue"></script>
<script src="${context}/vue/templates/userCell.vue"></script>
<script src="${context}/vue/monthReport.vue"></script>
<script>
    monthReport.api.getReports = '${getReports}';
    monthReport.getReports();
</script>
</html>
