<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>主区币兑人民币汇率管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatMainCnb/">主区币兑人民币汇率列表</a></li>
		<shiro:hasPermission name="plat:jsPlatMainCnb:edit"><li><a href="${ctx}/plat/jsPlatMainCnb/form">主区币兑人民币汇率添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatMainCnb" action="${ctx}/plat/jsPlatMainCnb/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主区币：</label>
				<%--<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
				<form:select path="coinId" htmlEscape="false" maxlength="64" class="input-medium">
					<form:option value="" label="无"/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
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
				<th>主区币ID</th>
				<th>主区币名称</th>
				<th>兑人民币汇率</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:jsPlatMainCnb:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatMainCnb">
			<tr>
				<td><a href="${ctx}/plat/jsPlatMainCnb/form?id=${jsPlatMainCnb.id}">
					${jsPlatMainCnb.coinId}
				</a></td>
				<td>
					${jsPlatMainCnb.coinSymbol}
				</td>
				<td>
					${jsPlatMainCnb.cnbRate}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatMainCnb.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatMainCnb:edit"><td>
    				<a href="${ctx}/plat/jsPlatMainCnb/form?id=${jsPlatMainCnb.id}">修改</a>
					<a href="${ctx}/plat/jsPlatMainCnb/delete?id=${jsPlatMainCnb.id}" onclick="return confirmx('确认要删除该主区币兑人民币汇率吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>