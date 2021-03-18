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
<link rel="stylesheet" href="${context}/css/monthReports.css?v=${now}">
<html>
<div id="report" style="height: calc(100% - 40pt); padding: 8pt">
    <div style="width: 100%; text-align: center">
        <span v-if="loading">
            <fmt:message key="loading"/>
        </span>
        <template v-else>
            <span v-on:click="dateOffset(-1)" style="font-size: 16pt">&#9666;</span>
            <span>{{date.toLocaleDateString().substring(3)}}</span>
            <span v-on:click="dateOffset(1)" style="font-size: 16pt">&#9656;</span>
        </template>
    </div>
    <div style="overflow-y: scroll; height: calc(100% - 26pt)">
        <div v-for="(val, key) in reports">
            <span style="font-weight: bold">
                {{key}} ( {{val.length}} )
            </span>
            <div style="padding-left: 16px">
                <div v-for="r in val" class="report" v-on:click="show(r.id)">
                    <div>
                        {{new Date(r.leave).toLocaleDateString()}} - {{new Date(r.done).toLocaleDateString()}}
                    </div>
                    <div>
                        <div>
                            {{r.route.replaceAll(',', ' - ')}}
                        </div>
                        <div v-if="r.product">
                            {{r.product.name}}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${context}/vue/templates/cell.vue?v=${now}"></script>
<script src="${context}/vue/templates/userCell.vue?v=${now}"></script>
<script src="${context}/vue/monthReport.vue?v=${now}"></script>
<script>
    monthReport.api.getReports = '${getReports}';
    monthReport.api.show = '${show}';
    monthReport.getReports();
</script>
</html>
