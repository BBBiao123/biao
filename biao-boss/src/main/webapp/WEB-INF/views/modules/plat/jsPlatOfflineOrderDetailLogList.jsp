<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c流水统计表管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatOfflineOrderDetailLog/">c2c流水统计表列表</a></li>
		<shiro:hasPermission name="plat:jsPlatOfflineOrderDetailLog:view"><li><a href="${ctx}/plat/jsPlatOfflineOrderDetailLog/form">c2c流水统计表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatOfflineOrderDetailLog" action="${ctx}/plat/jsPlatOfflineOrderDetailLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
			     <form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>统计时间：</label>
				<input name="countDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${jsPlatOfflineOrderDetailLog.countDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主键</th>
				<th>用户id</th>
				<th>币种id</th>
				<th>币种符号</th>
				<th>买入统计值</th>
				<th>卖出统计值</th>
				<th>结余统计值</th>
				<th>统计时间</th>
				<shiro:hasPermission name="plat:jsPlatOfflineOrderDetailLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatOfflineOrderDetailLog">
			<tr>
				<td><a href="${ctx}/plat/jsPlatOfflineOrderDetailLog/form?id=${jsPlatOfflineOrderDetailLog.id}">
					${jsPlatOfflineOrderDetailLog.id}
				</a></td>
				<td>
					${jsPlatOfflineOrderDetailLog.userId}
				</td>
				<td>
					${jsPlatOfflineOrderDetailLog.coinId}
				</td>
				<td>
					${jsPlatOfflineOrderDetailLog.symbol}
				</td>
				<td>
					${jsPlatOfflineOrderDetailLog.buyTotal}
				</td>
				<td>
					${jsPlatOfflineOrderDetailLog.sellTotal}
				</td>
				<td>
					${jsPlatOfflineOrderDetailLog.surplusTotal}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatOfflineOrderDetailLog.countDate}" pattern="yyyy-MM-dd"/>
				</td>
				<shiro:hasPermission name="plat:jsPlatOfflineOrderDetailLog:edit"><td>
    				<a href="${ctx}/plat/jsPlatOfflineOrderDetailLog/form?id=${jsPlatOfflineOrderDetailLog.id}">修改</a>
					<a href="${ctx}/plat/jsPlatOfflineOrderDetailLog/delete?id=${jsPlatOfflineOrderDetailLog.id}" onclick="return confirmx('确认要删除该c2c流水统计表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>