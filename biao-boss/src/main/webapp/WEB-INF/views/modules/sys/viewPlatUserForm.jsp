<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>前台用户信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
<ul class="nav nav-tabs">
    <li ><a href="${ctx}/sys/user/list">用户列表</a></li>
    <li class="active"><a href="${ctx}/sys/user/formPlatUser?id=${platUser.id}">前台用户列表</a></li>
</ul>
	<form:form id="inputForm" modelAttribute="platUser" class="form-horizontal" title="前台用户信息">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:input path="realName" htmlEscape="false" maxlength="45" disabled="true" class="input-xlarge "/>
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
				<form:input path="mail" htmlEscape="false" maxlength="45" readonly="true" disabled="true" class="input-xlarge "/>
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



		<div class="form-actions">
		   <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>