<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>营销任务执行记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mkTaskRecord/">营销任务执行记录列表</a></li>
		<shiro:hasPermission name="market:mkTaskRecord:edit"><li><a href="${ctx}/market/mkTaskRecord/form">营销任务执行记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkTaskRecord" action="${ctx}/market/mkTaskRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>营销类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('distributeRuleType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>任务日期：</label>
				<input name="beginTaskDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkTaskRecord.beginTaskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endTaskDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkTaskRecord.endTaskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>营销类型</th>
				<th>任务日期</th>
				<th>执行时间</th>
				<th>币种符号</th>
				<th>币种数量</th>
				<th>状态</th>
				<th>说明</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkTaskRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkTaskRecord">
			<tr>
				<%--<td><a href="${ctx}/market/mkTaskRecord/form?id=${mkTaskRecord.id}">--%>
					<%--${mkTaskRecord.id}--%>
				<%--</a></td>--%>
				<td>
					${fns:getDictLabel(mkTaskRecord.type, 'distributeRuleType', '')}
				</td>
				<td>
					<fmt:formatDate value="${mkTaskRecord.taskDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${mkTaskRecord.executeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkTaskRecord.coinSymbol}
				</td>
				<td>
					${mkTaskRecord.volume}
				</td>
				<td>
					${fns:getDictLabel(mkTaskRecord.status, 'mkTaskStatus', '')}
				</td>
				<td>
						${mkTaskRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkTaskRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkTaskRecord:edit"><td>
    				<a href="${ctx}/market/mkTaskRecord/form?id=${mkTaskRecord.id}">修改</a>
					<a href="${ctx}/market/mkTaskRecord/delete?id=${mkTaskRecord.id}" onclick="return confirmx('确认要删除该营销任务执行记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>