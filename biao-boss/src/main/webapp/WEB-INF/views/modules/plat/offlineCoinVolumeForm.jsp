<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>c2c资产管理</title>
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
		<li><a href="${ctx}/plat/offlineCoinVolume/">c2c资产列表</a></li>
		<li class="active"><a href="${ctx}/plat/offlineCoinVolume/form?id=${offlineCoinVolume.id}">c2c资产<shiro:hasPermission name="plat:offlineCoinVolume:edit">${not empty offlineCoinVolume.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:offlineCoinVolume:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="offlineCoinVolume" action="${ctx}/plat/offlineCoinVolume/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">user_id：</label>
			<div class="controls">
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种id：</label>
			<div class="controls">
				<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种代号：</label>
			<div class="controls">
				<form:input path="coinSymbol" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种数量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告冻结资产：</label>
			<div class="controls">
				<form:input path="advertVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易冻结资产：</label>
			<div class="controls">
				<form:input path="lockVolume" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:offlineCoinVolume:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>