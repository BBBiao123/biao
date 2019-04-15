<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币安自动化管理管理</title>
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
		<li class="active"><a href="${ctx}/plat/robotConfigUnsafe/">币安自动化管理列表</a></li>
		<shiro:hasPermission name="plat:robotConfigUnsafe:edit"><li><a href="${ctx}/plat/robotConfigUnsafe/form">币安自动化管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="robotConfigUnsafe" action="${ctx}/plat/robotConfigUnsafe/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>type：</label>
				<form:input path="type" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>coin_main：</label>
			     <form:select path="coinMain" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>coin_other：</label>
				<form:select path="coinOther" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>user_id：</label>
			    <form:input id="user" path="user.id" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label>状态  ：</label>
				<form:select path="status">
				   <form:option value="">---</form:option>
				   <form:option value="0">启用</form:option>
				   <form:option value="1">禁用</form:option>
				</form:select>
			</li>
			<li><label>is_init：</label>
				<form:select path="isInit">
				   <form:option value="">---</form:option>
				   <form:option value="0">不初始化</form:option>
				   <form:option value="1">初始化</form:option>
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
				<th>type</th>
				<th>coin_main</th>
				<th>coin_other</th>
				<th>user_id</th>
				<th>user_name</th>
				<th>password</th>
				<th>交易量范围</th>
				<th>价格范围</th>
				<th>状态</th>
				<th>is_init</th>
				<shiro:hasPermission name="plat:robotConfigUnsafe:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="robotConfigUnsafe">
			<tr>
				<td><a href="${ctx}/plat/robotConfigUnsafe/form?id=${robotConfigUnsafe.id}">
					<c:choose>
					    <c:when test="${robotConfigUnsafe.type=='0'}">买入</c:when>
					    <c:when test="${robotConfigUnsafe.type=='1'}">卖出</c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</a></td>
				<td>
					${robotConfigUnsafe.coinMain}
				</td>
				<td>
					${robotConfigUnsafe.coinOther}
				</td>
				<td>
					${robotConfigUnsafe.user.id}
				</td>
				<td>
					${robotConfigUnsafe.userName}
				</td>
				<td>
					***************
				</td>
				<td>
					${robotConfigUnsafe.volumeRange}
				</td>
				<td>
					${robotConfigUnsafe.priceRange}
				</td>
				<td>
					<c:choose>
					    <c:when test="${robotConfigUnsafe.status=='0'}"><span style="color:#00CC00">启用</span></c:when>
					    <c:when test="${robotConfigUnsafe.status=='1'}"><span style="color:red">禁用</span></c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
					    <c:when test="${robotConfigUnsafe.isInit=='0'}">不初始化</c:when>
					    <c:when test="${robotConfigUnsafe.isInit=='1'}">初始化</c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<shiro:hasPermission name="plat:robotConfigUnsafe:edit"><td>
    				<a href="${ctx}/plat/robotConfigUnsafe/form?id=${robotConfigUnsafe.id}">修改</a>
					<a href="${ctx}/plat/robotConfigUnsafe/delete?id=${robotConfigUnsafe.id}" onclick="return confirmx('确认要删除该币安自动化管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>