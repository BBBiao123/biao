<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域销售管理</title>
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
		<li class="active"><a href="${ctx}/plat/areaSell/">区域销售列表</a></li>
		<shiro:hasPermission name="plat:areaSell:edit"><li><a href="${ctx}/plat/areaSell/form">区域销售添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="areaSell" action="${ctx}/plat/areaSell/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>城市：</label>
				<form:input path="areaName"  htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="width: 105px">省份：</label>
				<form:input path="areaParaentName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>售出：</label>
				<form:select path="sold" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('area_sold')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>城市</th>
				<th>省份</th>
				<th>卖价</th>
				<th>售出</th>
				<th>备注信息</th>
				<shiro:hasPermission name="plat:areaSell:sold"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="areaSell">
			<tr>
				<td>
					${areaSell.areaName}
				</td>
				<td>
					${areaSell.areaParaentName}
				</td>
				<td>
					${areaSell.sellPrice}
				</td>
				<td>
					${fns:getDictLabel(areaSell.sold, 'area_sold', '')}
				</td>
				<td>
					${areaSell.remarks}
				</td>
				<shiro:hasPermission name="plat:areaSell:sold"><td>
					<c:if test="${areaSell.sold eq 0}"><a href="${ctx}/plat/areaSell/sold?id=${areaSell.id}" onclick="return confirmx('确认要卖出${areaSell.areaName}对吗？', this.href)">卖出</a></c:if>
					<c:if test="${areaSell.sold eq 1}"><a href="${ctx}/plat/areaSell/sale?id=${areaSell.id}" onclick="return confirmx('确认取消${areaSell.areaName}对吗？', this.href)">取消</a></c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>