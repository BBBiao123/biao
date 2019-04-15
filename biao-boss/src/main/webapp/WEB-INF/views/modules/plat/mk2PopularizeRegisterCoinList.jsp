<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册用户送币管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var userDilogPath = "iframe:${ctx}/plat/platUser/userDialog?";
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
        
        function showUserDialog(id) {
            var url = userDilogPath + "userId=" + id;
            $.jBox.open(url, "用户信息", 1100, 500, { buttons: { '关闭': true}});
            $("#jbox-content").css({"overflow-y":"hidden"});
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/plat/mk2PopularizeRegisterCoin/">注册用户送币列表</a></li>
		<shiro:hasPermission name="plat:mk2PopularizeRegisterCoin:edit"><li><a href="${ctx}/plat/mk2PopularizeRegisterCoin/form">注册用户送币添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mk2PopularizeRegisterCoin" action="${ctx}/plat/mk2PopularizeRegisterCoin/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<%--<li><label>活动名称：</label>--%>
				<%--<form:input path="confName" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>用户姓名：</label>--%>
				<%--<form:input path="userName" htmlEscape="false" maxlength="45" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>用户ID：</label>
				<form:input path="userId" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>邮箱：</label>
				<form:input path="mail" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>手机号码：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeRegisterCoin.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mk2PopularizeRegisterCoin.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th>活动名称</th>
				<th>用户ID</th>
				<th>用户邮箱</th>
				<th>用户手机</th>
				<th>送币数量</th>
				<th>送币名称</th>
                <th>送币原因</th>
				<th>被推荐用户ID</th>
				<th>状态</th>
				<th>创建时间</th>
				<%--<shiro:hasPermission name="plat:mk2PopularizeRegisterCoin:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mk2PopularizeRegisterCoin">
			<tr>
				<td>
					${mk2PopularizeRegisterCoin.confName}
				</td>
				<td>
					<a style="cursor:pointer;text-decoration:none;" onclick="showUserDialog('${mk2PopularizeRegisterCoin.userId}')" >
						${mk2PopularizeRegisterCoin.userId}
					</a>
				</td>
				<td>
					${mk2PopularizeRegisterCoin.mail}
				</td>
				<td>
					${mk2PopularizeRegisterCoin.mobile}
				</td>
				<td>
					${mk2PopularizeRegisterCoin.volume}
				</td>
				<td>
					${mk2PopularizeRegisterCoin.coinSymbol}
				</td>
                <td>
                    <c:if test="${not empty mk2PopularizeRegisterCoin.forUserId}">推荐</c:if>
                    <c:if test="${empty mk2PopularizeRegisterCoin.forUserId}">注册</c:if>
                </td>
				<td>
                    <a style="cursor:pointer;text-decoration:none;" onclick="showUserDialog('${mk2PopularizeRegisterCoin.forUserId}')" >
					    ${mk2PopularizeRegisterCoin.forUserId}
                    </a>
				</td>
				<td>
					${fns:getDictLabel(mk2PopularizeRegisterCoin.status, 'mk2_register_coin_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${mk2PopularizeRegisterCoin.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<shiro:hasPermission name="plat:mk2PopularizeRegisterCoin:edit"><td>--%>
    				<%--<a href="${ctx}/plat/mk2PopularizeRegisterCoin/form?id=${mk2PopularizeRegisterCoin.id}">修改</a>--%>
					<%--<a href="${ctx}/plat/mk2PopularizeRegisterCoin/delete?id=${mk2PopularizeRegisterCoin.id}" onclick="return confirmx('确认要删除该注册用户送币吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>