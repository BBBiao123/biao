<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>挖矿规则送币流水管理</title>
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
		<li class="active"><a href="${ctx}/market/mk2PopularizeMiningGiveCoinLog/">挖矿规则送币流水列表</a></li>
		<shiro:hasPermission name="market:mk2PopularizeMiningGiveCoinLog:edit"><li><a href="${ctx}/market/mk2PopularizeMiningGiveCoinLog/form">挖矿规则送币流水添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeMiningGiveCoinLog" action="${ctx}/market/mk2PopularizeMiningGiveCoinLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mk2_popularize_mining_conf_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>挖矿时间：</label>
				<input name="beginCountDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeMiningGiveCoinLog.beginCountDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCountDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeMiningGiveCoinLog.endCountDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>用户ID</th>
				<th>邮箱</th>
				<th>手机号</th>
				<th>身份证</th>
				<th>真实姓名</th>
				<th>币种名称</th>
				<th>送币数量</th>
				<th>挖矿总量</th>
				<th>排名</th>
				<th>参与挖矿总量</th>
				<th>大区总量</th>
				<th>团队持币量</th>
				<th>挖矿时间</th>
				<shiro:hasPermission name="market:mk2PopularizeMiningGiveCoinLog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeMiningGiveCoinLog">
			<tr>
				<td>
					${fns:getDictLabel(mk2PopularizeMiningGiveCoinLog.type, 'mk2_popularize_mining_conf_type', '')}
				</td>
				<td>
					<a href="${ctx}/market/mk2PopularizeMiningGiveCoinLog/form?id=${mk2PopularizeMiningGiveCoinLog.id}">
					${mk2PopularizeMiningGiveCoinLog.userId}
					</a>
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.mail}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.mobile}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.idCard}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.realName}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.coinSymbol}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.volume}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.totalVolume}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.orderNo}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.joinVolume}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.maxSubVolume}
				</td>
				<td>
					${mk2PopularizeMiningGiveCoinLog.teamHoldTotal}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeMiningGiveCoinLog.countDate}" pattern="yyyy-MM-dd"/>
				</td>
				<shiro:hasPermission name="market:mk2PopularizeMiningGiveCoinLog:edit"><td>
    				<a href="${ctx}/market/mk2PopularizeMiningGiveCoinLog/form?id=${mk2PopularizeMiningGiveCoinLog.id}">修改</a>
					<a href="${ctx}/market/mk2PopularizeMiningGiveCoinLog/delete?id=${mk2PopularizeMiningGiveCoinLog.id}" onclick="return confirmx('确认要删除该挖矿规则送币流水吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>