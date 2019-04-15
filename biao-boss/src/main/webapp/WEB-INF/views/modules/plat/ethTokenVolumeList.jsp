<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>eth代币余额管理</title>
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
		<li class="active"><a href="${ctx}/plat/ethTokenVolume/">eth代币余额列表</a></li>
		<shiro:hasPermission name="plat:ethTokenVolume:edit"><li><a href="${ctx}/plat/ethTokenVolume/form">eth代币余额添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ethTokenVolume" action="${ctx}/plat/ethTokenVolume/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label>address：</label>
				<form:input path="address" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>name</th>
				<th>volume</th>
				<th>address</th>
				<th>创建者</th>
				<th>创建时间</th>
				<th>更新者</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:ethTokenVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ethTokenVolume">
			<tr>
				<td><a href="${ctx}/plat/ethTokenVolume/form?id=${ethTokenVolume.id}">
					${ethTokenVolume.name}
				</a></td>
				<td>
					${ethTokenVolume.volume}
				</td>
				<td>
					${ethTokenVolume.address}
				</td>
				<td>
					${ethTokenVolume.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${ethTokenVolume.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${ethTokenVolume.updateBy.id}
				</td>
				<td>
					<fmt:formatDate value="${ethTokenVolume.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:ethTokenVolume:edit"><td>
    				<a href="${ctx}/plat/ethTokenVolume/form?id=${ethTokenVolume.id}">修改</a>
					<a href="${ctx}/plat/ethTokenVolume/delete?id=${ethTokenVolume.id}" onclick="return confirmx('确认要删除该dd吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>