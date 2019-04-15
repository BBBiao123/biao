<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>Plat系统配置管理</title>
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
		<li class="active"><a href="${ctx}/sys/jsSysConf/">Plat系统配置列表</a></li>
		<shiro:hasPermission name="sys:jsSysConf:edit"><li><a href="${ctx}/sys/jsSysConf/form">Plat系统配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsSysConf" action="${ctx}/sys/jsSysConf/" method="post" class="breadcrumb form-search">
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
				<th>c2c开关</th>
				<th>备注</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="sys:jsSysConf:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsSysConf">
			<tr>
				<td><a href="${ctx}/sys/jsSysConf/form?id=${jsSysConf.id}">
					${jsSysConf.id}
				</a></td>
				<td>
					${fns:getDictLabel(jsSysConf.offlineOnOff, 'jsSysConfOnOff', '')}
				</td>
				<td>
					${jsSysConf.remarks}
				</td>
				<td>
					<fmt:formatDate value="${jsSysConf.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsSysConf.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="sys:jsSysConf:edit"><td>
    				<a href="${ctx}/sys/jsSysConf/form?id=${jsSysConf.id}">修改</a>
					<a href="${ctx}/sys/jsSysConf/delete?id=${jsSysConf.id}" onclick="return confirmx('确认要删除该Plat系统配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>