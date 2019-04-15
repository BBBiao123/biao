<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>注册抽奖活动管理</title>
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
    <li class="active"><a href="${ctx}/plat/mkUserRegisterLottery/">注册抽奖活动列表</a></li>
    <shiro:hasPermission name="plat:mkUserRegisterLottery:edit">
        <li><a href="${ctx}/plat/mkUserRegisterLottery/form">注册抽奖活动添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="mkUserRegisterLottery" action="${ctx}/plat/mkUserRegisterLottery/"
           method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>活动名称：</label>
            <form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li><label>币种：</label>
            <form:input path="coinSymbol" htmlEscape="false" maxlength="32" class="input-medium"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>活动名称</th>
        <th>活动开始时间</th>
        <th>币种</th>
        <th>奖金总量</th>
        <th>推荐人最少持币量</th>
        <th>推荐人奖金比例</th>
        <th>推荐人每天奖励数量</th>
        <th>推荐人奖励总数量</th>
        <th>推荐人数限制</th>
        <th>更新时间</th>
        <th>状态</th>
        <shiro:hasPermission name="plat:mkUserRegisterLottery:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="mkUserRegisterLottery">
        <tr>
            <td>
                    ${mkUserRegisterLottery.name}
            </td>
            <td>
                <fmt:formatDate value="${mkUserRegisterLottery.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${mkUserRegisterLottery.coinSymbol}
            </td>
            <td>
                    ${mkUserRegisterLottery.totalPrize}
            </td>
            <td>
                    ${mkUserRegisterLottery.recommendMinVolume}
            </td>
            <td>
                    ${mkUserRegisterLottery.recommendRatio}
            </td>
            <td>
                    ${mkUserRegisterLottery.recommendDayCount}
            </td>
            <td>
                    ${mkUserRegisterLottery.recommendTotalCount}
            </td>

            <td>
                    ${mkUserRegisterLottery.recommendCountLimit}
            </td>

            <td>
                <fmt:formatDate value="${mkUserRegisterLottery.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <shiro:hasPermission name="plat:mkUserRegisterLottery:enable">
                <td>
                    <c:if test="${mkUserRegisterLottery.status=='0'}">
                        <a href="${ctx}/plat/mkUserRegisterLottery/enable/1?id=${mkUserRegisterLottery.id}"
                           onclick="return confirmx('确认要开启此活动吗？', this.href)">停用</a>
                    </c:if>
                    <c:if test="${mkUserRegisterLottery.status=='1'}">
                        <a href="${ctx}/plat/mkUserRegisterLottery/enable/0?id=${mkUserRegisterLottery.id}"
                           onclick="return confirmx('确认要停用此活动吗？', this.href)">启用</a>
                    </c:if>
                </td>
            </shiro:hasPermission>
            <shiro:hasPermission name="plat:mkUserRegisterLottery:edit">
                <td>
                    <a href="${ctx}/plat/mkUserRegisterLottery/form?id=${mkUserRegisterLottery.id}">修改</a>
                    <a href="${ctx}/plat/mkUserRegisterLottery/delete?id=${mkUserRegisterLottery.id}"
                       onclick="return confirmx('确认要删除该注册抽奖活动吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>