<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手续费按交易对汇总</title>
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
		<li class="active"><a href="${ctx}/report/reportTradeFeeRecord/count">手续费按交易对汇总</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTradeFeeRecord" action="${ctx}/report/reportTradeFeeRecord/count" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label>主币：</label>
				<form:input path="coinMain" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>被交易币：</label>
				<form:input path="coinOther" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label>开始时间：</label>
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFeeRecord.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label>结束时间：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportTradeFeeRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
			   	<th>主币</th>
				<th>被交易币</th>
				<th>主币手续费总量</th>
				<th>被交易币手续费总量</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${pageList}" var="reportTradeFeeRecord">
			<tr>
			    <td>
					${reportTradeFeeRecord.coinMain}
				</td>
				<td>
					${reportTradeFeeRecord.coinOther}
				</td>
				<td>
					${reportTradeFeeRecord.mainFree}
				</td>
				<td>
					${reportTradeFeeRecord.otherFree}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>