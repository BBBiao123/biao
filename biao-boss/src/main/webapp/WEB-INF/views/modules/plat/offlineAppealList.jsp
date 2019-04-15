<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申诉管理</title>
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
		<li class="active"><a href="${ctx}/plat/offlineAppeal/">申诉列表</a></li>
		<shiro:hasPermission name="plat:offlineAppeal:edit"><li><a href="${ctx}/plat/offlineAppeal/form">申诉添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="offlineAppeal" action="${ctx}/plat/offlineAppeal/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="subOrderId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>姓名：</label>
				<form:input path="appealRealName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="appealMail" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>手机：</label>
				<form:input path="appealMobile" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('c2c_appeal')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>申诉时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${offlineAppeal.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${offlineAppeal.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>审核时间：</label>
				<input name="beginExamineDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${offlineAppeal.beginExamineDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endExamineDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${offlineAppeal.endExamineDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>订单号</th>
				<th>申诉人姓名</th>
				<th>申诉人邮件</th>
				<th>申诉人手机号</th>
                <th>原因类型</th>
                <th>申诉原因</th>
				<th>卖币方姓名</th>
				<th>卖币方手机</th>
				<th>卖币方邮箱</th>
				<th>买币方姓名</th>
				<th>买币方手机</th>
				<th>买币方邮箱</th>
                <th>状态</th>
				<th>审核人</th>
                <th>判诉胜方</th>
				<th>创建时间</th>
				<%--<th>审核时间</th>--%>
				<%--<shiro:hasPermission name="plat:offlineAppeal:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="offlineAppeal">
			<tr>
				<td><a href="${ctx}/plat/offlineAppeal/form?id=${offlineAppeal.id}">
						${offlineAppeal.subOrderId}
				</a></td>
				<td>
					${offlineAppeal.appealRealName}
				</td>
				<td>
						${offlineAppeal.appealMail}
				</td>
				<td>
						${offlineAppeal.appealMobile}
				</td>
                <td>
                    ${offlineAppeal.appealType}
                </td>
                <td>
                    ${offlineAppeal.reason}
                </td>
				<td>
					${offlineAppeal.sellRealName}
				</td>
				<td>
						${offlineAppeal.sellMobile}
				</td>
				<td>
						${offlineAppeal.sellMail}
				</td>
				<td>
					${offlineAppeal.buyRealName}
				</td>
				<td>
						${offlineAppeal.buyMobile}
				</td>
				<td>
						${offlineAppeal.buyMail}
				</td>
                <td>
                    ${fns:getDictLabel(offlineAppeal.status, 'c2c_appeal', '')}
                </td>
                <td>
                    ${offlineAppeal.examineUserId}
                </td>
                <td>
                    ${offlineAppeal.examineResultUserName}
                </td>
				<td>
					<fmt:formatDate value="${offlineAppeal.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${offlineAppeal.examineDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<%--<shiro:hasPermission name="plat:offlineAppeal:edit"><td>--%>
    				<%--<a href="${ctx}/plat/offlineAppeal/form?id=${offlineAppeal.id}">修改</a>--%>
					<%--<a href="${ctx}/plat/offlineAppeal/delete?id=${offlineAppeal.id}" onclick="return confirmx('确认要删除该申诉吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>