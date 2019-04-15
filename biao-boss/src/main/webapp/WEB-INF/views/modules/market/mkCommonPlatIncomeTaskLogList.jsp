<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台收入任务日志管理</title>
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
		<li class="active"><a href="${ctx}/market/mkCommonPlatIncomeTaskLog/">平台收入任务日志列表</a></li>
		<shiro:hasPermission name="market:mkCommonPlatIncomeTaskLog:edit"><li><a href="${ctx}/market/mkCommonPlatIncomeTaskLog/form">平台收入任务日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkCommonPlatIncomeTaskLog" action="${ctx}/market/mkCommonPlatIncomeTaskLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>开始时间：</label>
				<input name="beginBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.beginBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.endBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="beginEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.beginEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.endEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkTaskStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>开始时间</th>
				<th>结束时间</th>
				<th>状态</th>
				<th>创建时间</th>
				<th>结果</th>
				<shiro:hasPermission name="market:mkCommonPlatIncomeTaskLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkCommonPlatIncomeTaskLog">
			<tr>
				<td><a href="${ctx}/market/mkCommonPlatIncomeTaskLog/form?id=${mkCommonPlatIncomeTaskLog.id}">
					${mkCommonPlatIncomeTaskLog.id}
				</a></td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(mkCommonPlatIncomeTaskLog.status, 'mkTaskStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${mkCommonPlatIncomeTaskLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkCommonPlatIncomeTaskLog.remark}
				</td>
				<shiro:hasPermission name="market:mkCommonPlatIncomeTaskLog:edit"><td>
    				<a href="${ctx}/market/mkCommonPlatIncomeTaskLog/form?id=${mkCommonPlatIncomeTaskLog.id}">修改</a>
					<a href="${ctx}/market/mkCommonPlatIncomeTaskLog/delete?id=${mkCommonPlatIncomeTaskLog.id}" onclick="return confirmx('确认要删除该平台收入任务日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>