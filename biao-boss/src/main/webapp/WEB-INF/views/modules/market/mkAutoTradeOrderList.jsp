<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易挂单管理</title>
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
		<li class="active"><a href="${ctx}/market/mkAutoTradeOrder/">自动交易挂单列表</a></li>
		<shiro:hasPermission name="market:mkAutoTradeOrder:edit"><li><a href="${ctx}/market/mkAutoTradeOrder/form">自动交易挂单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkAutoTradeOrder" action="${ctx}/market/mkAutoTradeOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<form:hidden path="monitorId" htmlEscape="false" maxlength="11" class="input-medium"/>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>买卖类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>挂单状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoOrderStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>主区币：</label>
				<form:input path="coinMainSymbol" htmlEscape="false" maxlength="12" class="input-medium"/>
			</li>
			<li><label>被交易币：</label>
				<form:input path="coinOtherSymbol" htmlEscape="false" maxlength="12" class="input-medium"/>
			</li>
			<li><label>创建账户：</label>
				<form:input path="createByName" htmlEscape="false" maxlength="12" class="input-medium"/>
			</li>
			<li><label>下单时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkAutoTradeOrder.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mkAutoTradeOrder.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>邮箱</th>
				<th>手机</th>
				<th>买卖类型</th>
				<th>挂单状态</th>
				<th>主区币</th>
				<th>被交易币</th>
				<th>单价</th>
				<th>数量</th>
				<th>备注</th>
				<th>下单时间</th>
				<th>创建账户</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkAutoTradeOrder">
			<tr>
				<%--<td><a href="${ctx}/market/mkAutoTradeOrder/form?id=${mkAutoTradeOrder.id}">--%>
					<%--${mkAutoTradeOrder.id}--%>
				<%--</a></td>--%>
				<td>
					${mkAutoTradeOrder.mail}
				</td>
				<td>
					${mkAutoTradeOrder.mobile}
				</td>
				<td>
					${fns:getDictLabel(mkAutoTradeOrder.type, 'mkAutoTradeType', '')}
				</td>
				<td>
					${fns:getDictLabel(mkAutoTradeOrder.status, 'mkAutoOrderStatus', '')}
				</td>
				<td>
					${mkAutoTradeOrder.coinMainSymbol}
				</td>
				<td>
					${mkAutoTradeOrder.coinOtherSymbol}
				</td>
				<td>
					${mkAutoTradeOrder.price}
				</td>
				<td>
					${mkAutoTradeOrder.volume}
				</td>
				<td>
					${mkAutoTradeOrder.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkAutoTradeOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${mkAutoTradeOrder.createByName}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>