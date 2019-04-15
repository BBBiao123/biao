<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员资产明细核对表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            $("#btnExport").click(function(){
                var newUrl = '${ctx}/report/reportPlatUserReconciliationDetail/exportFile/';    //设置新提交地址
                $("#searchForm").attr('action',newUrl);    //通过jquery为action属性赋值
                $("#searchForm").submit();    //提交ID为searchForm的表单
            });

            $("#btnSubmit").click(function(){
                var newUrl = '${ctx}/report/reportPlatUserReconciliationDetail/';    //设置新提交地址
                $("#searchForm").attr('action',newUrl);    //通过jquery为action属性赋值
                $("#searchForm").submit();    //提交ID为searchForm的表单dia
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
		<li class="active"><a href="${ctx}/report/reportPlatUserReconciliationDetail/">会员资产明细核对表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportPlatUserReconciliationDetail" action="${ctx}/report/reportPlatUserReconciliationDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="45" class="input-medium"/>
			</li>
			<li><label>币种：</label>

				<form:select path="coinSymbol" class="input-xlarge">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false" />
				</form:select>

				<%--<c:if test="${ not empty reportPlatUserReconciliationDetail.coinSymbol}">--%>
					<%--<form:input path="coinSymbol" htmlEscape="false" maxlength="32" class="input-medium"/>--%>
				<%--</c:if>--%>

				<%--<c:if test="${ empty reportPlatUserReconciliationDetail.coinSymbol}">--%>
					<%--<form:input path="coinSymbol" value="UES" htmlEscape="false" maxlength="32" class="input-medium"/>--%>
				<%--</c:if>--%>

			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="submit" value="导出报表"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>时间</th>
				<th>资产获得类型</th>
				<th>币种</th>
				<th>数量</th>
				<shiro:hasPermission name="report:reportPlatUserReconciliationDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportPlatUserReconciliationDetail">
			<tr>
				<td>
					<fmt:formatDate value="${reportPlatUserReconciliationDetail.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${reportPlatUserReconciliationDetail.type}
				</td>
				<td>
					${reportPlatUserReconciliationDetail.coinSymbol}
				</td>
				<td>
					<fmt:formatNumber value="${reportPlatUserReconciliationDetail.volume}" pattern="#.##" minFractionDigits="2" > </fmt:formatNumber>
				</td>
				<shiro:hasPermission name="report:reportPlatUserReconciliationDetail:edit"><td>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>