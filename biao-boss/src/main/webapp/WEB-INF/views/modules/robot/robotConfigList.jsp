<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机器人配置管理</title>
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
		<li class="active"><a href="${ctx}/robot/robotConfig/">机器人配置列表</a></li>
		<shiro:hasPermission name="robot:robotConfig:edit"><li><a href="${ctx}/robot/robotConfig/form">机器人配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="robotConfig" action="${ctx}/robot/robotConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>type：</label>
				<form:input path="type" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>coin_main：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label>coin_other：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>type</th>
				<th>coin_main</th>
				<th>coin_other</th>
				<th>user_id</th>
				<th>status</th>
				<th>volume_range</th>
				<th>price_range</th>
				<shiro:hasPermission name="robot:robotConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="robotConfig">
			<tr>
				<td><a href="${ctx}/robot/robotConfig/form?id=${robotConfig.id}">
					${robotConfig.type}
					<c:choose>
				        <c:when test="${robotConfig.type eq '1'}">买入</c:when>
				        <c:otherwise>卖出</c:otherwise>
				    </c:choose>
				</a></td>
				<td>
					${robotConfig.coinMain}
				</td>
				<td>
					${robotConfig.coinOther}
				</td>
				<td>
					${robotConfig.user.name}
				</td>
				<td>
				    <c:choose>
				        <c:when test="${robotConfig.status==1}">禁用</c:when>
				        <c:otherwise>启用</c:otherwise>
				    </c:choose>
				</td>
				<td>
					${robotConfig.volumeRange}
				</td>
				<td>
					${robotConfig.priceRange}
				</td>
				<shiro:hasPermission name="robot:robotConfig:edit"><td>
    				<a href="${ctx}/robot/robotConfig/form?id=${robotConfig.id}">修改</a>
					<a href="${ctx}/robot/robotConfig/delete?id=${robotConfig.id}" onclick="return confirmx('确认要删除该机器人配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>