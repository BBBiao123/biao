<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>前台用户管理</title>
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
	<%--<form:form id="searchForm" modelAttribute="platUser" action="${ctx}/plat/market/mkAutoTradeSetting/PlatUserDialog" method="post" class="breadcrumb form-search">--%>
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="10"/>--%>
		<%--<ul class="ul-form">--%>
			<%--<li><label>用户名：</label>--%>
				<%--<form:input path="username" htmlEscape="false" maxlength="45" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>邮箱：</label>--%>
				<%--<form:input path="mail" htmlEscape="false" maxlength="11" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>手机号：</label>--%>
				<%--<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>真实姓名：</label>--%>
				<%--<form:input path="realName" htmlEscape="false" maxlength="45" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>开始时间：</label>--%>
				<%--<input name="startTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"--%>
					<%--value="<fmt:formatDate value="${platUser.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
			<%--</li>--%>
			<%--<li><label>结束时间：</label>--%>
					<%--<input name="endTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"--%>
					<%--value="<fmt:formatDate value="${platUser.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>--%>
			<%--</li>--%>
			<%--<li><label>审核状态：</label>--%>
				<%--<form:select path="cardStatus" class="input-medium">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${fns:getDictList('UserCardStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
			<%--</li>--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
	<%--</form:form>--%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>真实名称</th>
				<th>是否锁定</th>
				<th>手机号</th>
				<th>邮箱</th>
				<th>实名状态</th>
				<th>谷歌验证</th>
				<th>性别</th>
				<th>年龄</th>
				<th>邀请码</th>
				<th>身份证地址</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="platUser">
			<tr>
				<td>
					<input name="chooseUser" type="radio" value="${platUser.id}" userLabel = "${platUser.mail}" userMobile="${platUser.mobile}" userIdCard="${platUser.idCard}" userRealName="${platUser.realName}"/>
				</td>
				<td>
					${platUser.realName}
				</td>
				<td>
				   ${fns:getDictLabel(platUser.status, 'UserStatus', '')}
				</td>
				<td>
					${platUser.mobile}
				</td>
				<td>
					${platUser.mail}
				</td>
				<td>
				    ${fns:getDictLabel(platUser.cardStatus, 'UserCardStatus', '')}
				</td>
				<td>
				    <c:choose>
				       <c:when test="${empty platUser.googleAuth}">no</c:when>
				       <c:otherwise>yes</c:otherwise>
				    </c:choose>
				</td>
				<td>
				    ${fns:getDictLabel(platUser.sex, 'sex', '')}
				</td>
				<td>
					${platUser.age}
				</td>
				<td>
					${platUser.inviteCode}
				</td>
				<td>
					${platUser.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>