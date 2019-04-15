<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分红任务运行记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeBonusTaskLog/">分红任务运行记录列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeBonusTaskLog:edit"><li><a href="${ctx}/market/mk2PopularizeBonusTaskLog/form">分红任务运行记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeBonusTaskLog" action="${ctx}/market/mk2PopularizeBonusTaskLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>状态：</label>
				<form:input path="status" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusTaskLog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeBonusTaskLog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>状态</th>
				<th>分红总量</th>
				<th>分红币种ID</th>
				<th>分红币种名称</th>
				<th>统计开始时间</th>
				<th>统计结束时间</th>
				<th>创建时间</th>
				<th>结果描述</th>
				<shiro:hasPermission name="market:mk2PopularizeBonusTaskLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeBonusTaskLog">
			<tr>
				<td><a href="${ctx}/market/mk2PopularizeBonusTaskLog/form?id=${mk2PopularizeBonusTaskLog.id}">
					${mk2PopularizeBonusTaskLog.id}
				</a></td>
				<td>
					${fns:getDictLabel(mk2PopularizeBonusTaskLog.status, 'mkTaskStatus', '')}
				</td>
				<td>
					${mk2PopularizeBonusTaskLog.bonusVolume}
				</td>
				<td>
					${mk2PopularizeBonusTaskLog.coinId}
				</td>
				<td>
					${mk2PopularizeBonusTaskLog.coinSymbol}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusTaskLog.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusTaskLog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeBonusTaskLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mk2PopularizeBonusTaskLog.remark}
				</td>
				<shiro:hasPermission name="market:mk2PopularizeBonusTaskLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeBonusTaskLog/form?id=${mk2PopularizeBonusTaskLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeBonusTaskLog/delete?id=${mk2PopularizeBonusTaskLog.id}" onclick="return confirmx('确认要删除该分红任务运行记录吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>