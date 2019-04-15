<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币种销毁记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mkCoinDestroyRecord/">币种销毁记录列表</a></li>
		<shiro:hasPermission name="market:mkCoinDestroyRecord:edit"><li><a href="${ctx}/market/mkCoinDestroyRecord/form">币种销毁记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkCoinDestroyRecord" action="${ctx}/market/mkCoinDestroyRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键id</th>
				<th>币种标识</th>
				<th>销毁数量</th>
				<th>销毁日期</th>
				<th>备注</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:mkCoinDestroyRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkCoinDestroyRecord">
			<tr>
				<td><a href="${ctx}/market/mkCoinDestroyRecord/form?id=${mkCoinDestroyRecord.id}">
					${mkCoinDestroyRecord.id}
				</a></td>
				<td>
					${mkCoinDestroyRecord.symbol}
				</td>
				<td>
					${mkCoinDestroyRecord.volume}
				</td>
				<td>
					<fmt:formatDate value="${mkCoinDestroyRecord.destroyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkCoinDestroyRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkCoinDestroyRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkCoinDestroyRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkCoinDestroyRecord:edit"><td>
    				<a href="${ctx}/market/mkCoinDestroyRecord/form?id=${mkCoinDestroyRecord.id}">修改</a>
					<a href="${ctx}/market/mkCoinDestroyRecord/delete?id=${mkCoinDestroyRecord.id}" onclick="return confirmx('确认要删除该币种销毁记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>