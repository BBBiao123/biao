<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿任务日志管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeMiningTaskLog/">挖矿任务日志列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeMiningTaskLog:edit"><li><a href="${ctx}/market/mk2PopularizeMiningTaskLog/form">挖矿任务日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeMiningTaskLog" action="${ctx}/market/mk2PopularizeMiningTaskLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_mining_conf_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_task_log_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeMiningTaskLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeMiningTaskLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>类型</th>
				<th>币种名称</th>
				<th>本次总量</th>
				<th>送出挖矿总量</th>
				<th>挖矿时间</th>
				<th>状态</th>
				<th>创建时间</th>
				<th>结果</th>
				<shiro:hasPermission name="market:mk2PopularizeMiningTaskLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeMiningTaskLog">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeMiningTaskLog/form?id=${mk2PopularizeMiningTaskLog.id}">
					${fns:getDictLabel(mk2PopularizeMiningTaskLog.type, 'mk2_popularize_mining_conf_type', '')}
				</a></td>
				<td>
					${mk2PopularizeMiningTaskLog.coinSymbol}
				</td>
				<td>
					${mk2PopularizeMiningTaskLog.miningVolume}
				</td>
				<td>
					${mk2PopularizeMiningTaskLog.grantVolume}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMiningTaskLog.countDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeMiningTaskLog.status, 'mk2_popularize_task_log_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMiningTaskLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mk2PopularizeMiningTaskLog.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeMiningTaskLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeMiningTaskLog/form?id=${mk2PopularizeMiningTaskLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeMiningTaskLog/delete?id=${mk2PopularizeMiningTaskLog.id}" onclick="return confirmx('确认要删除该挖矿任务日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>