<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>前台用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	</script>
</head>
<body>
	<br/>
	<form:form id="inputForm" modelAttribute="platUser" action="${ctx}/plat/platUser/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				<form:input path="username" htmlEscape="false" maxlength="45" readonly="true" disabled="true" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户类型：</label>
			<div class="controls">
				<form:input path="userType" htmlEscape="false" maxlength="1" readonly="true" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否锁定：</label>
			<div class="controls">
				<form:input path="status" htmlEscape="false" maxlength="1" readonly="true" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号：</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="11" readonly="true" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls">
				<form:input path="mail" htmlEscape="false" maxlength="20" readonly="true" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">性别：</label>
			<div class="controls">
				 ${fns:getDictLabel(platUser.sex, 'sex', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年龄：</label>
			<div class="controls">
				<form:input path="age" htmlEscape="false" maxlength="2" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:input path="realName" htmlEscape="false" maxlength="45" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证：</label>
			<div class="controls">
				<form:input path="idCard" htmlEscape="false" maxlength="18" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证地址:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" disabled="true" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证 正面图案id：</label>
			<div class="controls">
			    <img src="${imageUrl}/card/view/${platUser.cardUpId}" height="200" width="300">
				<%-- <form:input path="cardUpId" htmlEscape="false" maxlength="45" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证反面id：</label>
			<div class="controls">
			     <img src="${imageUrl}/card/view/${platUser.cardDownId}" height="200" width="300">
				<%-- <form:input path="cardDownId" htmlEscape="false" maxlength="45" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手持身份id：</label>
			<div class="controls">
			    <img src="${imageUrl}/card/view/${platUser.cardFaceId}" height="200" width="300">
				<%-- <form:input path="cardFaceId" htmlEscape="false" maxlength="45" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核状态 ：</label>
			<div class="controls">
			    通过<form:radiobutton path="cardStatus" value="1" htmlEscape="false" maxlength="1" class="input-xlarge "/>&nbsp;&nbsp;
			  不通过<form:radiobutton path="cardStatus" value="9" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核原因：</label>
			<div class="controls">
				<form:input path="cardStatusReason" htmlEscape="false" maxlength="45" class="input-xlarge " disabled="true"/>
			</div>
		</div>
	</form:form>
</body>
</html>