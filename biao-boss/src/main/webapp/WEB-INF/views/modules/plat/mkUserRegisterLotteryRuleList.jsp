<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册活动规则管理</title>
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
		<li class="active"><a href="${ctx}/plat/mkUserRegisterLotteryRule/">注册活动规则列表</a></li>
		<shiro:hasPermission name="plat:mkUserRegisterLotteryRule:edit"><li><a href="${ctx}/plat/mkUserRegisterLotteryRule/form">注册活动规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkUserRegisterLotteryRule" action="${ctx}/plat/mkUserRegisterLotteryRule/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>规则名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
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
				<th>规则名称</th>
				<th>最小数量</th>
				<th>最大数量</th>
				<th>中奖比例</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:mkUserRegisterLotteryRule:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkUserRegisterLotteryRule">
			<tr>
				<td>
					${mkUserRegisterLotteryRule.lotteryName}
				</td>
				<td>
					${mkUserRegisterLotteryRule.name}
				</td>
				<td>
					${mkUserRegisterLotteryRule.minCount}
				</td>
				<td>
					${mkUserRegisterLotteryRule.maxCount}
				</td>
				<td>
					${mkUserRegisterLotteryRule.ratio}
				</td>

				<td>
					<fmt:formatDate value="${mkUserRegisterLotteryRule.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:mkUserRegisterLotteryRule:edit"><td>
    				<a href="${ctx}/plat/mkUserRegisterLotteryRule/form?id=${mkUserRegisterLotteryRule.id}">修改</a>
					<a href="${ctx}/plat/mkUserRegisterLotteryRule/delete?id=${mkUserRegisterLotteryRule.id}" onclick="return confirmx('确认要删除该注册活动规则吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>