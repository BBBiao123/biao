<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户空头币种管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatUserAirdrop/">用户空头币种列表</a></li>
		<shiro:hasPermission name="plat:jsPlatUserAirdrop:edit"><li><a href="${ctx}/plat/jsPlatUserAirdrop/form">用户空头币种添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatUserAirdrop" action="${ctx}/plat/jsPlatUserAirdrop/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>币种代号：</label>
			     <form:select path="coinId" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>用户类型：</label>
				<form:select path="userType" class="input-medium">
					<form:option value="" label=""/>
			        <form:option value="1" label="实名用户"/>
					<form:option value="0" label="所有用户"/>
			    </form:select>
			</li>
			<li><label>记录状态  ：</label>
			    <form:select path="status" class="input-medium">
			        <form:option value="" label=""/>
			        <form:option value="1" label="空头中"/>
					<form:option value="0" label="开始空头"/>
					<form:option value="2" label="空头完成"/>
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
				<th>币种符号</th>
				<th>币种id</th>
				<th>用户类型</th>
				<th>数量</th>
				<th>空头时间</th>
				<th>备注</th>
				<th>用户开始时间</th>
				<th>用户截止时间</th>
				<th>记录状态</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatUserAirdrop">
			<tr>
				<td><a href="${ctx}/plat/jsPlatUserAirdrop/form?id=${jsPlatUserAirdrop.id}">
					${jsPlatUserAirdrop.coinSymbol}
				</a></td>
				<td>
					${jsPlatUserAirdrop.coinId}
				</td>
				<td>
				   <c:choose>
	                    <c:when test="${jsPlatUserAirdrop.userType==0}">所有用户</c:when>
	                    <c:when test="${jsPlatUserAirdrop.userType==1}">实名用户</c:when>
	                    <c:otherwise></c:otherwise>
	                </c:choose>
				</td>
				<td>
					${jsPlatUserAirdrop.number}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserAirdrop.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatUserAirdrop.mark}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserAirdrop.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserAirdrop.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
				   <c:choose>
	                    <c:when test="${jsPlatUserAirdrop.status==2}">空头完成</c:when>
	                    <c:when test="${jsPlatUserAirdrop.status==1}">空头中</c:when>
	                    <c:otherwise>开始空头</c:otherwise>
	                </c:choose>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>