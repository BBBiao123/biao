<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手机模板管理</title>
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
		<li><a href="${ctx}/sys/mobileTemplate/">手机模板列表</a></li>
		<li class="active"><a href="${ctx}/sys/mobileTemplate/form?id=${mobileTemplate.id}">手机模板<shiro:hasPermission name="sys:mobileTemplate:edit">${not empty mobileTemplate.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:mobileTemplate:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mobileTemplate" action="${ctx}/sys/mobileTemplate/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">业务类型：</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">短信key：</label>
			<div class="controls">
				<form:input path="accessKey" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">短信secret：</label>
			<div class="controls">
				<form:input path="accessSecret" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">短信签名：</label>
			<div class="controls">
				<form:input path="signName" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			</div>
		</div>
		<%-- 
		<div class="control-group">
			<label class="control-label">模板参数：</label>
			<div class="controls">
				<form:input path="templateParam" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> 
		--%>
		<div class="control-group">
			<label class="control-label">模板id：</label>
			<div class="controls">
				<form:input path="templateCode" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">业务标识：</label>
			<div class="controls">
				<form:input path="workSign" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注描述：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">扩展字段：</label>
			<div class="controls">
				<form:input path="expandJson" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">超时时间：</label>
			<div class="controls">
				<form:input path="timeOut" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:mobileTemplate:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>