<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分红统计管理</title>
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
		<li class="active"><a href="${ctx}/market/mkDividendStat/">分红统计列表</a></li>
		<shiro:hasPermission name="market:mkDividendStat:edit"><li><a href="${ctx}/market/mkDividendStat/form">分红统计添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkDividendStat" action="${ctx}/market/mkDividendStat/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>统计日期：</label>
				<input name="beginTaskDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkDividendStat.beginTaskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endTaskDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkDividendStat.endTaskDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<%--<li><label>平台币符号：</label>--%>
				<%--<form:select path="coinSymbol" class="input-medium">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			<%--</li>--%>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>统计日期</th>
				<th>平台币符号</th>
				<th>平台币总量</th>
				<th>USDT手续费</th>
				<th>BTC手续费</th>
				<th>ETH手续费</th>
				<th>USDT实际分红</th>
				<th>BTC实际分红</th>
				<th>ETH实际分红</th>
				<th>USDT/(1000平台币)</th>
				<th>BTC/(1000平台币)</th>
				<th>ETH/(1000平台币)</th>
				<th>备注</th>
				<th>创建时间</th>
				<shiro:hasPermission name="market:mkDividendStat:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDividendStat">
			<tr>
				<td>
					<fmt:formatDate value="${mkDividendStat.statDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
						${mkDividendStat.coinSymbol}
				</td>
				<td>
					${mkDividendStat.volume}
				</td>
				<td>
					${mkDividendStat.usdtVolume}
				</td>
				<td>
					${mkDividendStat.btcVolume}
				</td>
				<td>
					${mkDividendStat.ethVolume}
				</td>
				<td>
					${mkDividendStat.usdtRealVolume}
				</td>
				<td>
					${mkDividendStat.btcRealVolume}
				</td>
				<td>
					${mkDividendStat.ethRealVolume}
				</td>
				<td>
					${mkDividendStat.usdtPerVolume}
				</td>
				<td>
					${mkDividendStat.btcPerVolume}
				</td>
				<td>
					${mkDividendStat.ethPerVolume}
				</td>
				<td>
					${mkDividendStat.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkDividendStat.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:mkDividendStat:edit"><td>
    				<a href="${ctx}/market/mkDividendStat/form?id=${mkDividendStat.id}">修改</a>
					<a href="${ctx}/market/mkDividendStat/delete?id=${mkDividendStat.id}" onclick="return confirmx('确认要删除该分红统计吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>