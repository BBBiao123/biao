<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台链接管理</title>
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
		<li class="active"><a href="${ctx}/plat/platLink/">平台链接列表</a></li>
		<shiro:hasPermission name="plat:platLink:edit"><li><a href="${ctx}/plat/platLink/form">平台链接添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="platLink" action="${ctx}/plat/platLink/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="typeid" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('plat_link')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>主键id</th>
				<th>类型</th>
				<th>URL</th>
				<th>图片</th>
				<th>展示顺序</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:platLink:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="platLink">
			<tr>
				<td><a href="${ctx}/plat/platLink/form?id=${platLink.id}">
					${platLink.id}
				</a></td>
				<td>
					${fns:getDictLabel(platLink.typeid, 'plat_link', '')}
				</td>
				<td>
					${platLink.linkurl}
				</td>
				<td>
					${platLink.linkimage}
				</td>
				<td>
					${platLink.showOrder}
				</td>
				<td>
					<fmt:formatDate value="${platLink.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${platLink.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:platLink:edit"><td>
    				<a href="${ctx}/plat/platLink/form?id=${platLink.id}">修改</a>
					<a href="${ctx}/plat/platLink/delete?id=${platLink.id}" onclick="return confirmx('确认要删除该平台链接吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>