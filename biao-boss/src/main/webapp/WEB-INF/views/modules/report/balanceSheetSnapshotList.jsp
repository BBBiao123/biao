<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资产负债表管理</title>
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
		<li class="active"><a href="${ctx}/report/balanceSheetSnapshot/">资产负债表列表</a></li>
		<shiro:hasPermission name="report:balanceSheetSnapshot:edit"><li><a href="${ctx}/report/balanceSheetSnapshot/form">资产负债表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="balanceSheetSnapshot" action="${ctx}/report/balanceSheetSnapshot/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>快照日期：</label>
				<input name="beginSnapDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${balanceSheetSnapshot.beginSnapDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input name="endSnapDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${balanceSheetSnapshot.endSnapDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>币种代号：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>币种代号</th>
				<th>快照日期</th>
				<th>总资产</th>
				<th>收入累计</th>
				<th>币币交易手续费</th>
				<th>充值</th>
				<th>c2c手续费</th>
				<th>手动回扣币</th>
				<th>提现手续费</th>
				<th>支出累计</th>
				<th>提币</th>
				<th>提币区块链手续费</th>
				<th>充币归集手续费</th>
				<th>手动拨币</th>
				<th>挖矿</th>
				<th>注册奖励</th>
				<th>接力撞奖</th>
				<th>备注</th>
				<th>创建日期</th>
				<shiro:hasPermission name="report:balanceSheetSnapshot:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="balanceSheetSnapshot">
			<tr>
				<td>
						${balanceSheetSnapshot.coinSymbol}
				</td>
				<td>
					<fmt:formatDate value="${balanceSheetSnapshot.snapDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${balanceSheetSnapshot.totalVolume}
				</td>
				<td>
					${balanceSheetSnapshot.income}
				</td>
				<td>
					${balanceSheetSnapshot.tradeFee}
				</td>
				<td>
					${balanceSheetSnapshot.depositVolume}
				</td>
				<td>
					${balanceSheetSnapshot.offlineFee}
				</td>
				<td>
					${balanceSheetSnapshot.deductVolume}
				</td>
				<td>
					${balanceSheetSnapshot.withdrawFee}
				</td>
				<td>
					${balanceSheetSnapshot.expense}
				</td>
				<td>
					${balanceSheetSnapshot.withdrawVolume}
				</td>
				<td>
					${balanceSheetSnapshot.withdrawBlockFee}
				</td>
				<td>
					${balanceSheetSnapshot.depositAllocationFee}
				</td>
				<td>
					${balanceSheetSnapshot.remitVolume}
				</td>
				<td>
					${balanceSheetSnapshot.miningVolume}
				</td>
				<td>
					${balanceSheetSnapshot.registerVolume}
				</td>
				<td>
					${balanceSheetSnapshot.relayVolume}
				</td>
				<td>
					${balanceSheetSnapshot.remark}
				</td>
				<td>
					<fmt:formatDate value="${balanceSheetSnapshot.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="report:balanceSheetSnapshot:edit"><td>
    				<a href="${ctx}/report/balanceSheetSnapshot/form?id=${balanceSheetSnapshot.id}">修改</a>
					<a href="${ctx}/report/balanceSheetSnapshot/delete?id=${balanceSheetSnapshot.id}" onclick="return confirmx('确认要删除该资产负债表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>