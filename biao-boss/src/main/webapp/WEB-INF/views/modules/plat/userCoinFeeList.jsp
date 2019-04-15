<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户交易对手续费设置管理</title>
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
		<li class="active"><a href="${ctx}/plat/userCoinFee/">用户交易对手续费设置列表</a></li>
		<shiro:hasPermission name="plat:userCoinFee:edit"><li><a href="${ctx}/plat/userCoinFee/form">用户交易对手续费设置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userCoinFee" action="${ctx}/plat/userCoinFee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主区：</label>
				<form:input path="pairOne" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>交易币种：</label>
				<form:input path="pairOther" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>用户id</th>
				<th>交易账户</th>
				<th>真实姓名</th>
				<th>手机号</th>
				<th>邮箱</th>
				<th>主区</th>
				<th>被交易币种</th>
				<th>手续费</th>
				<th>状态</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:userCoinFee:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userCoinFee">
			<tr>
				<td>
						${userCoinFee.userId}
				</td>
				<td>
						${userCoinFee.userName}
				</td>
				<td>
						${userCoinFee.realName}
				</td>
				<td>
						${userCoinFee.mobile}
				</td>
				<td>
						${userCoinFee.mail}
				</td>
				<td>
						${userCoinFee.pairOne}
				</td>
				<td>
						${userCoinFee.pairOther}
				</td>

				<td>
						${fns:getDictLabel(userCoinFee.fee, 'feeRate', '')}
				</td>
				<td>
						${fns:getDictLabel(userCoinFee.status, 'mkAutoTradeStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${userCoinFee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:userCoinFee:edit"><td>
    				<a href="${ctx}/plat/userCoinFee/form?id=${userCoinFee.id}">修改</a>
					<a href="${ctx}/plat/userCoinFee/delete?id=${userCoinFee.id}" onclick="return confirmx('确认要删除该用户交易对手续费设置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>