<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册送币规则管理</title>
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
		<li class="active"><a href="${ctx}/plat/mk2PopularizeRegisterConf/">注册送币规则列表</a></li>
		<shiro:hasPermission name="plat:mk2PopularizeRegisterConf:edit"><li><a href="${ctx}/plat/mk2PopularizeRegisterConf/form">注册送币规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeRegisterConf" action="${ctx}/plat/mk2PopularizeRegisterConf/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>推广名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>币名称：</label>
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>创建人：</label>
				<form:input path="createBy.id" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeRegisterConf.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeRegisterConf.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>推广名称</th>
				<th>币名称</th>
				<th>状态</th>
				<th>注册送币数量</th>
				<th>推荐送币数量</th>
				<th>总送币量</th>
				<th>已送币量</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<shiro:hasPermission name="plat:mk2PopularizeRegisterConf:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeRegisterConf">
			<tr>
				<td><a href="${ctx}/plat/mk2PopularizeRegisterConf/form?id=${mk2PopularizeRegisterConf.id}">
					${mk2PopularizeRegisterConf.name}
				</a></td>
				<td>
					${mk2PopularizeRegisterConf.coinSymbol}
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeRegisterConf.status, 'mk2_register_conf_status', '')}
				</td>
				<td>
					${mk2PopularizeRegisterConf.registerVolume}
				</td>
				<td>
					${mk2PopularizeRegisterConf.referVolume}
				</td>
				<td>
					${mk2PopularizeRegisterConf.totalVolume}
				</td>
				<td>
					${mk2PopularizeRegisterConf.giveVolume}
				</td>
				<td>
					${mk2PopularizeRegisterConf.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeRegisterConf.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeRegisterConf.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="plat:mk2PopularizeRegisterConf:edit"><td>
    				<a href="${ctx}/plat/mk2PopularizeRegisterConf/form?id=${mk2PopularizeRegisterConf.id}">修改</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>