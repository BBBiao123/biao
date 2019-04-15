<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>币币交易对管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/plat/exPair/">币币交易对列表</a></li>
    <shiro:hasPermission name="plat:exPair:edit">
        <li><a href="${ctx}/plat/exPair/form">币币交易对添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="exPair" action="${ctx}/plat/exPair/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>主交易区：</label>
            <form:input path="pairOne" htmlEscape="false" maxlength="11" class="input-medium"/>
        </li>

        <li><label>被交易币种：</label>
            <form:input path="pairOther" htmlEscape="false" maxlength="11" class="input-medium"/>
        </li>
        <li><label>状态：</label>
            <form:select path="status" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('exPairStatus')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>主交易区</th>
        <th>被交易币种</th>
        <th>状态</th>
        <th>手续费</th>
        <th>最小成交量</th>
        <th>最大成交量</th>
        <th>价格精度</th>
        <th>数量精度</th>
        <th>百分比数量</th>
        <th>备注</th>
        <th>排序</th>
        <shiro:hasPermission name="plat:exPair:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="exPair">
        <tr>
                <%--<td><a href="${ctx}/plat/exPair/form?id=${exPair.id}">
                    <fmt:formatDate value="${exPair.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </a></td>--%>
            <td>
                    ${exPair.pairOne}
            </td>
            <td>
                    ${exPair.pairOther}
            </td>
            <td>
                    ${fns:getDictLabel(exPair.status, 'exPairStatus', '')}
            </td>
            <td>
                    ${fns:getDictLabel(exPair.fee, 'feeRate', '')}
            </td>
            <td>
                    ${exPair.minVolume}
            </td>
            <td>
                    ${exPair.maxVolume}
            </td>
            <td>
                    ${exPair.pricePrecision}
            </td>
            <td>
                    ${exPair.volumePrecision}
            </td>
            <td>
                    ${exPair.volumePercent}

            </td>

            <td>
                    ${exPair.remarks}
            </td>
            <td>
                    ${exPair.sort}
            </td>
            <shiro:hasPermission name="plat:exPair:edit">
                <td>
                    <a href="${ctx}/plat/exPair/form?id=${exPair.id}">修改</a>
                    <a href="${ctx}/plat/exPair/delete?id=${exPair.id}"
                       onclick="return confirmx('确认要删除该币币交易对吗？', this.href)">删除</a>
                    <c:if test="${exPair.status eq 0}"><a href="${ctx}/plat/exPair/release?id=${exPair.id}"
                                                          onclick="return confirmx('确认要发布该币币交易对吗？', this.href)">发布</a></c:if>
                    <c:if test="${exPair.status eq 1}"><a href="${ctx}/plat/exPair/lock?id=${exPair.id}"
                                                          onclick="return confirmx('确认要锁定该币币交易对吗？', this.href)">锁定</a></c:if>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>