<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>币币订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            $("#btnExport").click(function(){
                var newUrl = '${ctx}/plat/exOrder/exportFile/';    //设置新提交地址
                $("#searchForm").attr('action',newUrl);    //通过jquery为action属性赋值
                $("#searchForm").submit();    //提交ID为searchForm的表单
            })

            $("#btnSubmit").click(function(){
                var newUrl = '${ctx}/plat/exOrder/';    //设置新提交地址
                $("#searchForm").attr('action',newUrl);    //通过jquery为action属性赋值
                $("#searchForm").submit();    //提交ID为searchForm的表单
            })

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
		<li class="active"><a href="${ctx}/plat/exOrder/">币币订单列表</a></li>
		<shiro:hasPermission name="plat:exOrder:edit"><li><a href="${ctx}/plat/exOrder/form">币币订单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="exOrder" action="${ctx}/plat/exOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>挂单币种标识</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>状态</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('OrderStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>挂单类型</label>
				<form:select path="exType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('exType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>挂单时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${exOrder.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${exOrder.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<shiro:hasPermission name="plat:exOrder:export">
				<li class="btns"><input id="btnExport" class="btn btn-primary" type="submit" value="导出报表"/></li>
			</shiro:hasPermission>

			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>用户id</th>
				<th>挂单数量</th>
				<th>挂单价格</th>
				<th>消费数量</th>
				<th>挂单币种标识</th>
				<th>手续费</th>
				<th>成交状态</th>
				<th>换取的币种符号</th>
				<th>交易得到的币的数量</th>
				<th>挂单类型</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:exOrder:details"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="exOrder">
			<tr>
				<td><a href="${ctx}/plat/exOrder/form?id=${exOrder.id}">
					${exOrder.userId}
				</a></td>
				<td>
					${exOrder.askVolume}
				</td>
				<td>
				     ${exOrder.price}
				</td>
				<td>
					${exOrder.successVolume}
				</td>
				<td>
					${exOrder.coinSymbol}
				</td>
				<td>
					${exOrder.exFee}
				</td>
				<td>
					${fns:getDictLabel(exOrder.status, 'OrderStatus', '')}
				</td>
				<td>
					${exOrder.toCoinSymbol}
				</td>
				<td>
					${exOrder.toCoinVolume}
				</td>
				<td>
					${fns:getDictLabel(exOrder.exType, 'exType', '')}
				</td>
				<td>
					<fmt:formatDate value="${exOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${exOrder.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:exOrder:details"><td>
    				<a href="${ctx}/plat/exOrder/details?id=${exOrder.id}">详情</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>