<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易监控管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("body").on("click",".button", function(){
                var monitorId = $(this).attr('id');
                $.jBox.open("iframe:${ctx}/market/mkAutoTradeOrder/list?monitorId="+monitorId, "挂单列表", 1200, 500, { buttons: { '确定': 1}, submit: function (v, h, f){
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
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
		<li class="active"><a href="${ctx}/market/mkAutoTradeRecord/">自动交易记录列表</a></li>
		<shiro:hasPermission name="market:mkAutoTradeMonitor:edit"><li><a href="${ctx}/market/mkAutoTradeRecord/form">自动交易记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mkAutoTradeMonitor" action="${ctx}/market/mkAutoTradeRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>买卖类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkAutoTradeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
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
				<th>邮箱</th>
				<th>手机</th>
				<th>主区币</th>
				<th>被交易币</th>
				<th>下单开始时间</th>
				<th>下单结束时间</th>
				<th>下单笔数</th>
				<th>下单数量</th>
				<th>下单金额</th>
				<th>频率</th>
				<th>时间单位</th>
				<th>说明</th>
				<th>创建账户</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkAutoTradeMonitor">
			<tr>
				<td><a href="${ctx}/market/mkAutoTradeMonitor/form?id=${mkAutoTradeMonitor.id}">
						${fns:getDictLabel(mkAutoTradeMonitor.type, 'mkAutoTradeType', '')}
				</a></td>
				<td>
					${mkAutoTradeMonitor.mail}
				</td>
				<td>
					${mkAutoTradeMonitor.mobile}
				</td>
				<td>
					${mkAutoTradeMonitor.coinMainSymbol}
				</td>
				<td>
					${mkAutoTradeMonitor.coinOtherSymbol}
				</td>
				<td>
					<fmt:formatDate value="${mkAutoTradeMonitor.orderBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkAutoTradeMonitor.orderEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<a href="#" class="button" id="${mkAutoTradeMonitor.id}">
							${mkAutoTradeMonitor.orderNumber}
					</a>

				</td>
				<td>
					${mkAutoTradeMonitor.orderVolume}
				</td>
				<td>
					${mkAutoTradeMonitor.orderPrice}
				</td>
				<td>
						${mkAutoTradeMonitor.frequency}
				</td>
				<td>
						${fns:getDictLabel(mkAutoTradeMonitor.timeUnit, 'mkTimeUnit', '')}
				</td>
				<td>
					${mkAutoTradeMonitor.remark}
				</td>
				<td>
					${mkAutoTradeMonitor.createByName}
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${mkAutoTradeMonitor.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>