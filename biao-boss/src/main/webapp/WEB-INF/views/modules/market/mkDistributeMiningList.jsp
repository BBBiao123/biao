<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿规则管理</title>
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
		<li class="active"><a href="${ctx}/market/mkDistributeMining/">挖矿规则列表</a></li>
		<shiro:hasPermission name="market:mining:add"><li><a href="${ctx}/market/mkDistributeMining/form">挖矿规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkDistributeMining" action="${ctx}/market/mkDistributeMining/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>规则名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>规则名称</th>
				<th>状态</th>
				<th>百分比(手续费)</th>
				<th>币种代号</th>
				<th>币种总数量</th>
				<th>已发放数量</th>
				<th>说明</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mining:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDistributeMining">
			<tr>
				<td><a href="${ctx}/market/mkDistributeMining/form?id=${mkDistributeMining.id}">
					${mkDistributeMining.name}
				</a></td>
				<td>
					${fns:getDictLabel(mkDistributeMining.status, 'distributeRuleStatus', '')}
				</td>
				<td>
					<fmt:formatNumber value="${mkDistributeMining.percentage}" pattern="#0.00" />%
				</td>
				<td>
					${mkDistributeMining.coinSymbol}
				</td>
				<td>
					${mkDistributeMining.volume}
				</td>
				<td>
					${mkDistributeMining.grantVolume}
				</td>
				<td>
					${mkDistributeMining.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeMining.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeMining.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="market:mining:edit">
    					<a href="${ctx}/market/mkDistributeMining/form?id=${mkDistributeMining.id}">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="market:mining:delete">
						<c:if test="${mkDistributeMining.status eq 0}"><a href="${ctx}/market/mkDistributeMining/enable?id=${mkDistributeMining.id}" onclick="return confirmx('确认要启用该条规则吗？', this.href)">启用</a></c:if>
						<c:if test="${mkDistributeMining.status eq 1}"><a href="${ctx}/market/mkDistributeMining/forbidden?id=${mkDistributeMining.id}" onclick="return confirmx('确认要禁用该条规则吗？', this.href)">禁用</a></c:if>
						<a href="${ctx}/market/mkDistributeMining/delete?id=${mkDistributeMining.id}" onclick="return confirmx('确认要删除该挖矿规则吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>