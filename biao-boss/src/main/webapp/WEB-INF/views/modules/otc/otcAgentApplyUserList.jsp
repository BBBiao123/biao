<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拨币申请会员列表管理</title>
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
		<li class="active"><a href="${ctx}/otc/otcAgentApplyUser/">拨币申请会员列表列表</a></li>
		<shiro:hasPermission name="otc:otcAgentApplyUser:edit"><li><a href="${ctx}/otc/otcAgentApplyUser/form">拨币申请会员列表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="otcAgentApplyUser" action="${ctx}/otc/otcAgentApplyUser/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>银商名称：</label>
				<form:input path="agentName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>拨币申请ID：</label>
				<form:input path="applyId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建日期：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${otcAgentApplyUser.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${otcAgentApplyUser.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>银商名称</th>
				<th>拨币申请ID</th>
				<th>拨币占比(%)</th>
				<th>币种符号</th>
				<th>拨币数量</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>说明</th>
				<th>创建日期</th>
				<shiro:hasPermission name="otc:otcAgentApplyUser:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="otcAgentApplyUser">
			<tr>
				<td><a href="${ctx}/otc/otcAgentApplyUser/form?id=${otcAgentApplyUser.id}">
					${otcAgentApplyUser.id}
				</a></td>
				<td>
					${otcAgentApplyUser.agentName}
				</td>
				<td>
					${otcAgentApplyUser.applyId}
				</td>
				<td>
					${otcAgentApplyUser.percentage}
				</td>
				<td>
					${otcAgentApplyUser.coinSymbol}
				</td>
				<td>
					${otcAgentApplyUser.volume}
				</td>
				<td>
					${otcAgentApplyUser.mail}
				</td>
				<td>
					${otcAgentApplyUser.mobile}
				</td>
				<td>
					${otcAgentApplyUser.remark}
				</td>
				<td>
					<fmt:formatDate value="${otcAgentApplyUser.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="otc:otcAgentApplyUser:edit"><td>
    				<a href="${ctx}/otc/otcAgentApplyUser/form?id=${otcAgentApplyUser.id}">修改</a>
					<a href="${ctx}/otc/otcAgentApplyUser/delete?id=${otcAgentApplyUser.id}" onclick="return confirmx('确认要删除该拨币申请会员列表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>