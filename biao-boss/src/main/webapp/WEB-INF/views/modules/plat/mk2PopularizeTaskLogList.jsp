<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>mk2营销任务执行结果管理</title>
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
		<li class="active"><a href="${ctx}/plat/mk2PopularizeTaskLog/">注册送币执行结果列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeTaskLog" action="${ctx}/plat/mk2PopularizeTaskLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>活动名称：</label>
				<form:input path="typeName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>送币时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeTaskLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeTaskLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>执行结果：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_task_log_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<%--<th>任务类型</th>--%>
				<th>活动名称</th>
				<%--<th>用户审核时间</th>--%>
					<th>任务执行时间</th>
				<th>送币量</th>
				<th>送币时间</th>
				<%--<th>更新时间</th>--%>
				<th>执行结果</th>
				<shiro:hasPermission name="plat:mk2PopularizeTaskLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeTaskLog">
			<tr>
				<%--<td><a href="${ctx}/plat/mk2PopularizeTaskLog/form?id=${mk2PopularizeTaskLog.id}">--%>
					<%--${mk2PopularizeTaskLog.type}--%>
				<%--</a></td>--%>
				<td>
					${mk2PopularizeTaskLog.typeName}
				</td>
				<%--<td>--%>
					<%--${mk2PopularizeTaskLog.paramTaskDate}--%>
				<%--</td>--%>
					<td>
						<fmt:formatDate value="${mk2PopularizeTaskLog.executeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				<td>
					${mk2PopularizeTaskLog.dayGiveColume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeTaskLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>
					<fmt:formatDate value="${mk2PopularizeTaskLog.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>--%>
				<td>
					${fns:getDictLabel(mk2PopularizeTaskLog.status, 'mk2_popularize_task_log_status', '')}
				</td>
				<shiro:hasPermission name="plat:mk2PopularizeTaskLog:edit"><td>
    				<a href="${ctx}/plat/mk2PopularizeTaskLog/form?id=${mk2PopularizeTaskLog.id}">修改</a>
					<a href="${ctx}/plat/mk2PopularizeTaskLog/delete?id=${mk2PopularizeTaskLog.id}" onclick="return confirmx('确认要删除该mk2营销任务执行结果吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>