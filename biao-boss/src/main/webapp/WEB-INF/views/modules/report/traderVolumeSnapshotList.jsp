<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操盘手资产快照管理</title>
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
		<li class="active"><a href="${ctx}/report/traderVolumeSnapshot/">操盘手资产快照列表</a></li>
		<shiro:hasPermission name="report:traderVolumeSnapshot:edit"><li><a href="${ctx}/report/traderVolumeSnapshot/form">操盘手资产快照添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="traderVolumeSnapshot" action="${ctx}/report/traderVolumeSnapshot/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>快照日期：</label>
				<input name="snapDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${traderVolumeSnapshot.snapDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</li>
			<li><label>类型：</label>
				<form:select path="userTag">
					<form:option value="" label="全部"/>
					<form:option value="UES-TRADER">UES-TRADER</form:option>
					<form:option value="AUTO-TRADER">AUTO-TRADER</form:option>
					<form:option value="ALL">ALL</form:option>
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
				<th>主键id</th>
				<th>快照日期</th>
				<th>user_tag</th>
				<th>币种代号</th>
				<th>币币</th>
				<th>c2c资产</th>
				<th>冻结资产</th>
				<th>总资产</th>
				<th>流水汇总</th>
				<th>差额</th>
				<th>拨币资产</th>
				<th>创建日期</th>
				<shiro:hasPermission name="report:traderVolumeSnapshot:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="traderVolumeSnapshot">
			<tr>
				<td><a href="${ctx}/report/traderVolumeSnapshot/form?id=${traderVolumeSnapshot.id}">
					${traderVolumeSnapshot.id}
				</a></td>
				<td>
					<fmt:formatDate value="${traderVolumeSnapshot.snapDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${traderVolumeSnapshot.userTag}
				</td>
				<td>
					${traderVolumeSnapshot.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.tradeVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.offlineVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.lockVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.totalVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.billSumVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.balance}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatNumber value="${traderVolumeSnapshot.bobiVolume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<td>
					<fmt:formatDate value="${traderVolumeSnapshot.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="report:traderVolumeSnapshot:edit"><td>
    				<a href="${ctx}/report/traderVolumeSnapshot/form?id=${traderVolumeSnapshot.id}">修改</a>
					<a href="${ctx}/report/traderVolumeSnapshot/delete?id=${traderVolumeSnapshot.id}" onclick="return confirmx('确认要删除该操盘手资产快照吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>