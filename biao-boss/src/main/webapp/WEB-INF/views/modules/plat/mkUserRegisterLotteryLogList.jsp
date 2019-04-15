<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>注册活动抽奖记录管理</title>
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
    <li class="active"><a href="${ctx}/plat/mkUserRegisterLotteryLog/">注册活动抽奖记录列表</a></li>
    <shiro:hasPermission name="plat:mkUserRegisterLotteryLog:edit">
        <li><a href="${ctx}/plat/mkUserRegisterLotteryLog/form">注册活动抽奖记录添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="mkUserRegisterLotteryLog" action="${ctx}/plat/mkUserRegisterLotteryLog/"
           method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>用户ID：</label>
            <form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
        </li>
        <li><label>邮箱：</label>
            <form:input path="mail" htmlEscape="false" maxlength="100" class="input-medium"/>
        </li>
        <li><label>手机号：</label>
            <form:input path="phone" htmlEscape="false" maxlength="50" class="input-medium"/>
        </li>
        <li><label>绑手机时间：</label>
            <input name="beginPhoneDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${mkUserRegisterLotteryLog.beginPhoneDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> -
            <input name="endPhoneDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${mkUserRegisterLotteryLog.endPhoneDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>用户id</th>
        <th>会员邮箱</th>
        <th>会员手机号</th>
        <th>会员绑定手机时间</th>
        <th>活动名称</th>
        <th>币种</th>
        <th>数量</th>
        <th>原因</th>
        <th>送币时间</th>
        <shiro:hasPermission name="plat:mkUserRegisterLotteryLog:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="mkUserRegisterLotteryLog">
        <tr>
            <td>
                    ${mkUserRegisterLotteryLog.userId}
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.mail}
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.phone}
            </td>
            <td>
                <fmt:formatDate value="${mkUserRegisterLotteryLog.phoneDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.lotteryName}
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.coinSymbol}
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.realVolume}
            </td>
            <td>
                    ${mkUserRegisterLotteryLog.reason}
            </td>

            <td>
                <fmt:formatDate value="${mkUserRegisterLotteryLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <shiro:hasPermission name="plat:mkUserRegisterLotteryLog:edit">
                <td>
                    <a href="${ctx}/plat/mkUserRegisterLotteryLog/form?id=${mkUserRegisterLotteryLog.id}">修改</a>
                    <a href="${ctx}/plat/mkUserRegisterLotteryLog/delete?id=${mkUserRegisterLotteryLog.id}"
                       onclick="return confirmx('确认要删除该注册活动抽奖记录吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>