<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力自动撞奖记录管理</title>
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
		<li class="active"><a href="${ctx}/market/mkRelayAutoRecord/">接力自动撞奖记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="mkRelayAutoRecord" action="${ctx}/market/mkRelayAutoRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayRecordStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>执行时间：</label>
				<input name="beginBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayAutoRecord.beginBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkRelayAutoRecord.endBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键ID</th>
				<th>状态</th>
				<th>任务执行时间</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>预参与时间</th>
				<th>用户资产</th>
				<th>币符号</th>
				<th>备注</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkRelayAutoRecord:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkRelayAutoRecord">
			<tr>
				<td><a href="${ctx}/market/mkRelayAutoRecord/form?id=${mkRelayAutoRecord.id}">
					${mkRelayAutoRecord.id}
				</a></td>
				<td>
					${fns:getDictLabel(mkRelayAutoRecord.status, 'mkRelayRecordStatus', '')}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayAutoRecord.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkRelayAutoRecord.mail}
				</td>
				<td>
					${mkRelayAutoRecord.mobile}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayAutoRecord.reachDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkRelayAutoRecord.volume}
				</td>
				<td>
					${mkRelayAutoRecord.coinSymbol}
				</td>
				<td>
					${mkRelayAutoRecord.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkRelayAutoRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkRelayAutoRecord:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>