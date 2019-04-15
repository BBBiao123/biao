<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户资产账单管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatUserCoinVolumeBill/">用户资产账单列表</a></li>
		<shiro:hasPermission name="plat:jsPlatUserCoinVolumeBill:edit"><li><a href="${ctx}/plat/jsPlatUserCoinVolumeBill/form">用户资产账单添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatUserCoinVolumeBill" action="${ctx}/plat/jsPlatUserCoinVolumeBill/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户id：</label>
				<form:input path="user.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>关联KEY：</label>
				<form:input path="refKey" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>币种：</label>
				<form:select path="coinSymbol" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false"/>
				</form:select>
			</li>
			</li>
			<li><label>执行状态：</label>
				<form:select path="status" maxlength="64" class="input-medium">
				  	<form:option value="" label=""/>
					<form:options items="${staticList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
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
				<th>用户id</th>
				<th>币种信息</th>
				<th>优先级 默认5</th>
				<th>关联KEY</th>
				<th>操作符号</th>
				<th>操作lock_volume的数量</th>
				<th>操作volume的数量</th>
				<th>来源</th>
				<th>备注信息</th>
				<th>执行状态</th>
				<th>create_date</th>
				<th>update_date</th>
				<th>hash</th>
				<shiro:hasPermission name="plat:jsPlatUserCoinVolumeBill:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatUserCoinVolumeBill">
			<tr>
				<td><a href="${ctx}/plat/jsPlatUserCoinVolumeBill/form?id=${jsPlatUserCoinVolumeBill.id}">
					${jsPlatUserCoinVolumeBill.user.id}
				</a></td>
				<td>
					${jsPlatUserCoinVolumeBill.coinSymbol}
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.priority}
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.refKey}
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.opSignText}
				</td>
				<td style="color:#660033">
					${jsPlatUserCoinVolumeBill.opLockVolume}
				</td>
				<td style="color:#ff0033">
					${jsPlatUserCoinVolumeBill.opVolume}
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.source}
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.mark}
				</td>
				<td>
				  	<c:choose>
					    <c:when test="${jsPlatUserCoinVolumeBill.status==0}"><span style="color:#ccc222">未处理</span></c:when>
					    <c:when test="${jsPlatUserCoinVolumeBill.status==1}"><span style="color:greed">处理中</span></c:when>
					    <c:when test="${jsPlatUserCoinVolumeBill.status==2}"><span>成功处理</span></c:when>
					    <c:when test="${jsPlatUserCoinVolumeBill.status==3}"><span style="color:red">处理失败</span></c:when>
					    <c:when test="${jsPlatUserCoinVolumeBill.status==4}"><span style="color:red">需要重试</span></c:when>
					    <c:otherwise></c:otherwise>
					</c:choose>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserCoinVolumeBill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserCoinVolumeBill.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatUserCoinVolumeBill.hash}
				</td>
				<shiro:hasPermission name="plat:jsPlatUserCoinVolumeBill:edit"><td>
    				<a href="${ctx}/plat/jsPlatUserCoinVolumeBill/form?id=${jsPlatUserCoinVolumeBill.id}">修改</a>
					<a href="${ctx}/plat/jsPlatUserCoinVolumeBill/delete?id=${jsPlatUserCoinVolumeBill.id}" onclick="return confirmx('确认要删除该用户资产账单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>