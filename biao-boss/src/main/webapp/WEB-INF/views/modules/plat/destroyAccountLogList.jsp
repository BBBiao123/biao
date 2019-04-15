<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>销毁账户流水管理</title>
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
		<li class="active"><a href="${ctx}/plat/destroyAccountLog/">销毁账户流水列表</a></li>
		<shiro:hasPermission name="plat:destroyAccountLog:edit"><li><a href="${ctx}/plat/destroyAccountLog/form">销毁账户流水添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="destroyAccountLog" action="${ctx}/plat/destroyAccountLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${destroyAccountLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${destroyAccountLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
				<th>币种代号</th>
				<th>手机</th>
				<th>邮箱</th>
				<th>数量</th>
				<th>类型</th>
				<th>创建时间</th>
				<shiro:hasPermission name="plat:destroyAccountLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="destroyAccountLog">
			<tr>
				<td><a href="${ctx}/plat/destroyAccountLog/form?id=${destroyAccountLog.id}">
					${destroyAccountLog.id}
				</a></td>
				<td>
					${destroyAccountLog.userId}
				</td>
				<td>
					${destroyAccountLog.coinSymbol}
				</td>
				<td>
					${destroyAccountLog.mobile}
				</td>
				<td>
					${destroyAccountLog.mail}
				</td>
				<td>
					${destroyAccountLog.volume}
				</td>
				<td>
						${fns:getDictLabel(destroyAccountLog.type, 'destroyAccountLogType', '')}
				</td>
				<td>
					<fmt:formatDate value="${destroyAccountLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:destroyAccountLog:edit"><td>
    				<a href="${ctx}/plat/destroyAccountLog/form?id=${destroyAccountLog.id}">修改</a>
					<a href="${ctx}/plat/destroyAccountLog/delete?id=${destroyAccountLog.id}" onclick="return confirmx('确认要删除该销毁账户流水吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>