<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种归集管理</title>
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
		<li class="active"><a href="${ctx}/plat/coinCollection/">币种归集列表</a></li>
		<shiro:hasPermission name="plat:coinCollection:edit"><li><a href="${ctx}/plat/coinCollection/form">币种归集添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="coinCollection" action="${ctx}/plat/coinCollection/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>symbol：</label>
				<form:input path="symbol" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>user_id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>address：</label>
				<form:input path="address" htmlEscape="false" maxlength="256" class="input-medium"/>
			</li>
			<li><label>归集状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:option value="0" label="未完成"/>
					<form:option value="1" label="归集完成"/>
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
			    <th>币种</th>
		        <th>用户id</th>
		        <th>地址</th>
		        <th>总量</th>
		        <th>状态</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:coinCollection:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="coinCollection">
			<tr>
				<td>
                    ${coinCollection.symbol}
	            </td>
	            <td>
	                    ${coinCollection.userId}
	            </td>
	            <td>
	                    ${coinCollection.address}
	            </td>
	            <td>
	                    ${coinCollection.volume}
	            </td>
	            <td>
	                <c:choose>
	                   <c:when test="${coinCollection.status==0}">未完成</c:when>
	                   <c:when test="${coinCollection.status==1}">归集完成</c:when>
	                   <c:otherwise></c:otherwise>
	                </c:choose>
	            </td>
	            <td>
	                <fmt:formatDate value="${coinCollection.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
	            </td>
				<shiro:hasPermission name="plat:coinCollection:edit"><td>
    				<a href="${ctx}/plat/coinCollection/form?id=${coinCollection.id}">修改</a>
					<a href="${ctx}/plat/coinCollection/delete?id=${coinCollection.id}" onclick="return confirmx('确认要删除该币种归集吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>