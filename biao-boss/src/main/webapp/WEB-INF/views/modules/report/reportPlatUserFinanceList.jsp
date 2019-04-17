<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人资产对帐</title>
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
		<li class="active"><a href="${ctx}/report/reportPlatUserReconciliation/">金额盈收对帐报表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportPlatUserFinance" action="${ctx}/report/financeList/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户ID</th>
				<th>真实姓名</th>
				<th>手机号</th>
				<th>邮箱</th>
				<th>币种</th>
				<th>充币总额</th>
				<th>提币总额</th>
				<th>盈亏</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportPlatUserReconciliation">
			<tr>
				<td>
						${reportPlatUserReconciliation.userId}
				</td>
				<td>
						${reportPlatUserReconciliation.realName}
				</td>
				<td>
						${reportPlatUserReconciliation.mobile}
				</td>
				<td>
						${reportPlatUserReconciliation.mail}
				</td>
				<td>
					${reportPlatUserReconciliation.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.cbvolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.tbvolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliation.yingkui}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
    <div>
		<form    class="breadcrumb form-search">

			<p style="margin: 0 0 0 auto;font-size: 16px;font-weight: bold">合计</p>
			<ul class="ul-form" style="margin-left: 50px">

				<li><label>充币总额：</label>
					<label style="text-align: left"   >${platUserFinanceTotal.cbvolume} </label>
				</li>
				<li><label>提币总额：</label>
					<label    style="text-align: left">${platUserFinanceTotal.tbvolume} </label>
				</li>
				<li><label>盈亏：</label>
					<label    style="text-align: left" >${platUserFinanceTotal.yingkui} </label>
				</li>

				<li class="clearfix"></li>
			</ul>
		</form>
	</div>
</body>
</html>