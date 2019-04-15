<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员操作日志管理</title>
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
		<li class="active"><a href="${ctx}/plat/jsPlatUserOplog/">会员操作日志列表</a></li>
		<shiro:hasPermission name="plat:jsPlatUserOplog:edit"><li><a href="${ctx}/plat/jsPlatUserOplog/form">会员操作日志添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="jsPlatUserOplog" action="${ctx}/plat/jsPlatUserOplog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('PlatUserOplogType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>userId：</label>
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mobile：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>mail：</label>
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>操作时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${jsPlatUserOplog.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> -
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${jsPlatUserOplog.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<%--<li><label>邮箱：</label>--%>
				<%--<form:input path="mail" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>手机：</label>--%>
				<%--<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>--%>
			<%--</li>--%>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型</th>
				<th>内容</th>
				<th>原因</th>
				<th>会员ID</th>
				<th>真实姓名</th>
				<th>邮箱</th>
				<th>手机</th>
				<th>操作时间</th>
				<th>操作人</th>
				<th>是否发送短信</th>
				<th>短信模板</th>
				<shiro:hasPermission name="plat:jsPlatUserOplog:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="jsPlatUserOplog">
			<tr>
				<td>
					${fns:getDictLabel(jsPlatUserOplog.type, 'PlatUserOplogType', '')}
				</td>
				<td>
						${jsPlatUserOplog.content}
				</td>
				<td>
						${jsPlatUserOplog.reason}
				</td>
				<td>
					${jsPlatUserOplog.userId}
				</td>
				<td>
					${jsPlatUserOplog.realName}
				</td>
				<td>
					${jsPlatUserOplog.mail}
				</td>
				<td>
					${jsPlatUserOplog.mobile}
				</td>
				<td>
					<fmt:formatDate value="${jsPlatUserOplog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${jsPlatUserOplog.createBy.loginName}
				</td>
				<td>
						${fns:getDictLabel(jsPlatUserOplog.sendStatus, 'PlatUserOplogSengStatus', '')}
				</td>
				<td>
						${jsPlatUserOplog.templateCode}
				</td>
				<shiro:hasPermission name="plat:jsPlatUserOplog:edit"><td>
    				<a href="${ctx}/plat/jsPlatUserOplog/form?id=${jsPlatUserOplog.id}">修改</a>
					<a href="${ctx}/plat/jsPlatUserOplog/delete?id=${jsPlatUserOplog.id}" onclick="return confirmx('确认要删除该会员操作日志吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>