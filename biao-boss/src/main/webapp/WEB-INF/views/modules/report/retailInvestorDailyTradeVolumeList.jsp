<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>统计散户每天交易量</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#selectExPair").change(function(){
                var exPair = $("#selectExPair").find("option:selected").text();
                $("#exPairSymbol").val(exPair);

                var coins = exPair.split('/');
                $("input[name='coinSymbol']").val(coins[1]);
                $("input[name='toCoinSymbol']").val(coins[0]);
            });
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
		<li class="active"><a href="${ctx}/report/retailInvestorDailyTradeVolume/">操盘手资产快照列表</a></li>
		<shiro:hasPermission name="report:retailInvestorDailyTradeVolume:edit"><li><a href="${ctx}/report/retailInvestorDailyTradeVolume/form">操盘手资产快照添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="retailInvestorDailyTradeVolume" action="${ctx}/report/retailInvestorDailyTradeVolume/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易日期：</label>
				<input name="tradeDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${retailInvestorDailyTradeVolume.tradeDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>类型：</label>
				<form:select path="userTag" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:option value="UES-RETAIL">散户</form:option>
					<form:options items="${fns:getDictList('sourceUser')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>币币对：</label>
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:hidden path="toCoinSymbol" id="toCoinSymbol"/>
				<form:select path="exPairId" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${exPairList}" itemLabel="pairSymbol" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>

			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>交易日期</th>
				<th>用户类型</th>
				<th>操作类型</th>
				<th>买入币种</th>
				<th>成交数量</th>
				<th>被交易币种</th>
				<th>得到的币种数量</th>
				<th>手续费币种</th>
				<th>手续费数量</th>
				<shiro:hasPermission name="report:retailInvestorDailyTradeVolume:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="retailInvestorDailyTradeVolume">
			<tr>
				<td>
					<fmt:formatDate value="${retailInvestorDailyTradeVolume.tradeDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${retailInvestorDailyTradeVolume.userTag}
				</td>
				<td>
					${retailInvestorDailyTradeVolume.exType}
				</td>
				<td>
						${retailInvestorDailyTradeVolume.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${retailInvestorDailyTradeVolume.spentVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
						${retailInvestorDailyTradeVolume.toCoinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${retailInvestorDailyTradeVolume.earnVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
						${retailInvestorDailyTradeVolume.feeCoinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${retailInvestorDailyTradeVolume.feeVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<shiro:hasPermission name="report:retailInvestorDailyTradeVolume:edit"><td>
    				<a href="${ctx}/report/retailInvestorDailyTradeVolume/form?id=${retailInvestorDailyTradeVolume.id}">修改</a>
					<a href="${ctx}/report/retailInvestorDailyTradeVolume/delete?id=${retailInvestorDailyTradeVolume.id}" onclick="return confirmx('确认要删除该操盘手资产快照吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>