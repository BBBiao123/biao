<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商拨币申请管理</title>
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
		<li class="active"><a href="${ctx}/otc/otcAgentRemitApply/">银商拨币申请列表</a></li>
		<%--<shiro:hasPermission name="otc:otcAgentRemitApply:edit"><li><a href="${ctx}/otc/otcAgentRemitAudit/form">银商拨币申请添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="otcAgentRemitApply" action="${ctx}/otc/otcAgentRemitAudit/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>主键id：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种代号：</label>
				<form:select path="coinSymbol" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${offlineCoinList}" itemLabel="symbol" itemValue="symbol" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建日期：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${otcAgentRemitApply.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${otcAgentRemitApply.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>主键id</th>
				<th>银商名称</th>
				<th>状态</th>
				<th>币种代号</th>
				<th>折扣</th>
				<th>支付币种</th>
				<th>USDT汇率</th>
				<th>交易币种单价</th>
				<th>拨币数量</th>
				<th>财务审核意见</th>
				<th>运营审核意见</th>
				<th>创建人</th>
				<th>创建日期</th>
				<shiro:hasPermission name="otc:otcAgentRemitApply:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="otcAgentRemitApply">
			<tr>
				<td><a href="${ctx}/otc/otcAgentRemitAudit/form?id=${otcAgentRemitApply.id}">
					${otcAgentRemitApply.id}
				</a></td>
				<td>
					${otcAgentRemitApply.agentName}
				</td>
				<td>
					${fns:getDictLabel(otcAgentRemitApply.status, 'otcRemitApplyStatus', '')}
				</td>
				<td>
					${otcAgentRemitApply.coinSymbol}
				</td>
				<td>
					${otcAgentRemitApply.discountPercentage}
				</td>
				<td>
					${fns:getDictLabel(otcAgentRemitApply.payCoinType, 'otcPayCoinType', '')}
				</td>
				<td>
					${otcAgentRemitApply.usdtRate}
				</td>
				<td>
					${otcAgentRemitApply.tradeCoinRate}
				</td>
				<td>
					${otcAgentRemitApply.remitVolume}
				</td>
				<td>
					${otcAgentRemitApply.financeAuditComment}
				</td>
				<td>
					${otcAgentRemitApply.marketAuditComment}
				</td>
				<td>
					${otcAgentRemitApply.createByName}
				</td>
				<td>
					<fmt:formatDate value="${otcAgentRemitApply.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>

					<c:if test="${otcAgentRemitApply.status == '0'}">
						<shiro:hasPermission name="otc:otcAgentRemitAudit:edit">
						<a href="${ctx}/otc/otcAgentRemitAudit/form?id=${otcAgentRemitApply.id}">财务审核</a>
						</shiro:hasPermission>
					</c:if>

					<c:if test="${otcAgentRemitApply.status == '1'}">
						<shiro:hasPermission name="otc:otcAgentRemitCoin:edit">
							<a href="${ctx}/otc/otcAgentRemitAudit/form?id=${otcAgentRemitApply.id}">运营审核</a>
						</shiro:hasPermission>
					</c:if>

				</td>
			</tr>

		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>