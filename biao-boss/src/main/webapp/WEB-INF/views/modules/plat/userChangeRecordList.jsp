<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户挖矿记录</title>
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
		<li class="active"><a href="${ctx}/plat/userChangeRecord/">用户挖矿记录</a></li>
		<shiro:hasPermission name="plat:userCoinVolume:edit"><li><a href="${ctx}/plat/userCoinVolume/form">币币资产添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userChangeRecordVolume" action="${ctx}/plat/userChangeRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>user_id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>user_id</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>币种</th>
				<th>数量</th>
				<th>类型</th>
				<th>操作时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userCoinVolume">
			<tr>
				<td>${userCoinVolume.userId}</td>
				<td>${userCoinVolume.mail}</td>
				<td>
					${userCoinVolume.mobile}
				</td>
				<td>
					${userCoinVolume.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${userCoinVolume.coinNum}" pattern="#0.00000000" />
				</td>
				<td>
						${userCoinVolume.flag}
				</td>

				<td>
					<fmt:formatDate value="${userCoinVolume.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>