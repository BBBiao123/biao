<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>超级钱包资产管理</title>
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
		<li class="active"><a href="${ctx}/plat/superCoinVolume/">超级钱包资产列表</a></li>
		<shiro:hasPermission name="plat:superCoinVolume:edit"><li><a href="${ctx}/plat/superCoinVolume/form">超级钱包资产添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="superCoinVolume" action="${ctx}/plat/superCoinVolume/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户ID：</label>
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
				<th>主键id</th>
				<th>用户ID</th>
				<th>币种id</th>
				<th>币种代号</th>
				<th>币种数量</th>
				<th>钱包创建时间</th>
				<th>锁定周期（天）</th>
				<th>剩余天数</th>
				<th>资产更新时间</th>
				<shiro:hasPermission name="plat:superCoinVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="superCoinVolume">
			<tr>
				<td><a href="${ctx}/plat/superCoinVolume/form?id=${superCoinVolume.id}">
					${superCoinVolume.id}
				</a></td>
				<td>
					${superCoinVolume.userId}
				</td>
				<td>
					${superCoinVolume.coinId}
				</td>
				<td>
					${superCoinVolume.coinSymbol}
				</td>
				<td>
					${superCoinVolume.volume}
				</td>
				<td>
					<fmt:formatDate value="${superCoinVolume.depositBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						${superCoinVolume.lockCycle}
				</td>
				<td>
						${superCoinVolume.remainingDays}
				</td>
				<td>
					<fmt:formatDate value="${superCoinVolume.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:superCoinVolume:edit"><td>
    				<a href="${ctx}/plat/superCoinVolume/form?id=${superCoinVolume.id}">修改</a>
					<a href="${ctx}/plat/superCoinVolume/delete?id=${superCoinVolume.id}" onclick="return confirmx('确认要删除该超级钱包资产吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>