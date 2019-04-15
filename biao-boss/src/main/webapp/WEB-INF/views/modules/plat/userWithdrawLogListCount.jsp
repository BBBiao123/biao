<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户充值提币汇总</title>
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
		<li class="active"><a href="${ctx}/plat/userWithdrawLog/count">用户充值提币汇总</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="userWithdrawLog" action="${ctx}/plat/userWithdrawLog/count" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
			     <form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>开始时间：</label>
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${userWithdrawLog.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${userWithdrawLog.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>币id</th>
				<th>币种符号</th>
				<th>提币数量</th>
				<th>充币数量</th> 
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageList}" var="userWithdrawLog">
			<tr>
				<td>
					${userWithdrawLog.coinId}
				</td>
				<td>
					${userWithdrawLog.coinSymbol}
				</td>
				<td>
					${userWithdrawLog.withdrawVolume}
				</td>
				<td>
					${userWithdrawLog.depositVolume}
				</td>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>