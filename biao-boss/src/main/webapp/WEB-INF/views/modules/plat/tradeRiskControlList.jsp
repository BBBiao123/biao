<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易风险控制管理</title>
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
		<li class="active"><a href="${ctx}/plat/tradeRiskControl/">交易风险控制列表</a></li>
		<shiro:hasPermission name="plat:tradeRiskControl:edit"><li><a href="${ctx}/plat/tradeRiskControl/form">交易风险控制添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tradeRiskControl" action="${ctx}/plat/tradeRiskControl/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主区：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>被交易币种：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>主区</th>
				<th>被交易币种</th>
				<th>操盘手标识</th>
				<th>用户id集合</th>
				<th>风险比例</th>
				<th>护盘资金</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:tradeRiskControl:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tradeRiskControl">
			<tr>
				<td>
					${tradeRiskControl.coinMain}
				</td>
				<td>
					${tradeRiskControl.coinOther}
				</td>
				<td>
					${tradeRiskControl.sourceUser}
				</td>
				<td>
					${tradeRiskControl.userIds}
				</td>
				<td>
					${tradeRiskControl.riskRatio}
				</td>
				<td>
					${tradeRiskControl.fixedVolume}
				</td>
				<td>
					<fmt:formatDate value="${tradeRiskControl.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:tradeRiskControl:edit"><td>
    				<a href="${ctx}/plat/tradeRiskControl/form?id=${tradeRiskControl.id}">修改</a>
					<a href="${ctx}/plat/tradeRiskControl/delete?id=${tradeRiskControl.id}" onclick="return confirmx('确认要删除该交易风险控制吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>