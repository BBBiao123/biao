<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c资产管理</title>
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
		<li class="active"><a href="${ctx}/plat/offlineCoinVolume/">c2c资产列表</a></li>
		<shiro:hasPermission name="plat:offlineCoinVolume:edit"><li><a href="${ctx}/plat/offlineCoinVolume/form">c2c资产添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="offlineCoinVolume" action="${ctx}/plat/offlineCoinVolume/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>user_id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-medium"/>
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
				<th>币种id</th>
				<th>币种代号</th>
				<th>币种数量</th>
				<th>保证金数量</th>
				<th>广告冻结资产</th>
				<th>交易冻结资产</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:offlineCoinVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="offlineCoinVolume">
			<tr>
				<td><a href="${ctx}/plat/offlineCoinVolume/form?id=${offlineCoinVolume.id}">
					${offlineCoinVolume.userId}
				</a></td>
				<td>
					${offlineCoinVolume.coinId}
				</td>
				<td>
					${offlineCoinVolume.coinSymbol}
				</td>
				<td>
					${offlineCoinVolume.volume}
				</td>
				<td>
					${offlineCoinVolume.bailVolume}
				</td>
				<td>
					${offlineCoinVolume.advertVolume}
				</td>
				<td>
					${offlineCoinVolume.lockVolume}
				</td>
				<td>
					<fmt:formatDate value="${offlineCoinVolume.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${offlineCoinVolume.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:offlineCoinVolume:edit"><td>
					<%--<a href="${ctx}/plat/offlineCoinVolume/form?id=${offlineCoinVolume.id}">修改</a>--%>
					<%--<a href="${ctx}/plat/offlineCoinVolume/delete?id=${offlineCoinVolume.id}" onclick="return confirmx('确认要删除该c2c资产吗？', this.href)">删除</a>--%>
					<a href="${ctx}/plat/offlineCoinVolume/transfer?id=${offlineCoinVolume.id}" onclick="return confirmx('确认要划转该c2c资产吗？', this.href)">划转</a>

				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>