<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册送奖活动限制管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/mkUserRegisterLotteryLimit/">注册送奖活动限制列表</a></li>
		<shiro:hasPermission name="plat:mkUserRegisterLotteryLimit:edit"><li><a href="${ctx}/plat/mkUserRegisterLotteryLimit/form">注册送奖活动限制添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkUserRegisterLotteryLimit" action="${ctx}/plat/mkUserRegisterLotteryLimit/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>活动名称</th>
				<th>开始数量</th>
				<th>结束数量</th>
				<th>比例</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:mkUserRegisterLotteryLimit:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkUserRegisterLotteryLimit">
			<tr>
				<td>
					${mkUserRegisterLotteryLimit.lotteryName}
				</td>
				<td>
					${mkUserRegisterLotteryLimit.startCount}
				</td>
				<td>
					${mkUserRegisterLotteryLimit.endCount}
				</td>
				<td>
					${mkUserRegisterLotteryLimit.ratio}
				</td>
				<td>
					<fmt:formatDate value="${mkUserRegisterLotteryLimit.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:mkUserRegisterLotteryLimit:edit"><td>
    				<a href="${ctx}/plat/mkUserRegisterLotteryLimit/form?id=${mkUserRegisterLotteryLimit.id}">修改</a>
					<a href="${ctx}/plat/mkUserRegisterLotteryLimit/delete?id=${mkUserRegisterLotteryLimit.id}" onclick="return confirmx('确认要删除该注册送奖活动限制吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>