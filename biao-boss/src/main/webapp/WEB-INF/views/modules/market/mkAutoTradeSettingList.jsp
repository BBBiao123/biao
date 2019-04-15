<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易管理</title>
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
		<li class="active"><a href="${ctx}/market/mkAutoTradeSetting/">自动交易列表</a></li>
		<shiro:hasPermission name="market:mkAutoTradeSetting:edit"><li><a href="${ctx}/market/mkAutoTradeSetting/form">自动交易添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkAutoTradeSetting" action="${ctx}/market/mkAutoTradeSetting/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>买卖类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>主区币：</label>
				<form:input path="coinMainSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>被交易币：</label>
				<form:input path="coinOtherSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建账户：</label>
				<form:input path="createByName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>买卖类型</th>
				<th>状态</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>真实姓名</th>
				<th>主区币</th>
				<th>被交易币</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>最小成交量</th>
				<th>最大成交量</th>
				<th>最低价</th>
				<th>最高价</th>
				<th>频率</th>
				<th>时间单位</th>
				<th>交易最小成交量</th>
				<th>交易最大成交量</th>
				<th>交易价格精度</th>
				<th>交易数量精度</th>
				<th>创建账户</th>
				<shiro:hasPermission name="market:mkAutoTradeSetting:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkAutoTradeSetting">
			<tr>
				<td><a href="${ctx}/market/mkAutoTradeSetting/form?id=${mkAutoTradeSetting.id}">
					${fns:getDictLabel(mkAutoTradeSetting.type, 'mkAutoTradeType', '')}
				</a></td>
				<td>
					${fns:getDictLabel(mkAutoTradeSetting.status, 'mkAutoTradeStatus', '')}
				</td>
				<td>
					${mkAutoTradeSetting.mail}
				</td>
				<td>
					${mkAutoTradeSetting.mobile}
				</td>
				<td>
					${mkAutoTradeSetting.realName}
				</td>
				<td>
					${mkAutoTradeSetting.coinMainSymbol}
				</td>
				<td>
					${mkAutoTradeSetting.coinOtherSymbol}
				</td>
				<td>
					<fmt:formatDate value="${mkAutoTradeSetting.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkAutoTradeSetting.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

				<td>
					${mkAutoTradeSetting.minVolume}
				</td>
				<td>
					${mkAutoTradeSetting.maxVolume}
				</td>
				<td>
					${mkAutoTradeSetting.minPrice}
				</td>
				<td>
					${mkAutoTradeSetting.maxPrice}
				</td>
				<td>
					${mkAutoTradeSetting.frequency}
				</td>
				<td>
					${fns:getDictLabel(mkAutoTradeSetting.timeUnit, 'mkTimeUnit', '')}
				</td>
				<td>
						${mkAutoTradeSetting.exMinVolume}
				</td>
				<td>
						${mkAutoTradeSetting.exMaxVolume}
				</td>
				<td>
						${mkAutoTradeSetting.pricePrecision}
				</td>
				<td>
						${mkAutoTradeSetting.volumePrecision}
				</td>
				<td>
						${mkAutoTradeSetting.createByName}
				</td>
				<shiro:hasPermission name="market:mkAutoTradeSetting:edit"><td>
    				<a href="${ctx}/market/mkAutoTradeSetting/form?id=${mkAutoTradeSetting.id}">修改</a>
					<a href="${ctx}/market/mkAutoTradeSetting/delete?id=${mkAutoTradeSetting.id}" onclick="return confirmx('确认要删除该自动交易吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>