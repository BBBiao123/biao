<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户提现管理管理</title>
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
		<li><a href="${ctx}/plat/userWithdrawLog/">用户提现管理列表</a></li>
		<li class="active"><a href="${ctx}/plat/userWithdrawLog/form?id=${userWithdrawLog.id}">用户提现管理<shiro:hasPermission name="plat:userWithdrawLog:edit">${not empty userWithdrawLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:userWithdrawLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="userWithdrawLog" action="${ctx}/plat/userWithdrawLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">用户id：</label>
			<div class="controls">
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币id：</label>
			<div class="controls">
				<form:input path="coinId" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提现地址：</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种符号：</label>
			<div class="controls">
				<form:input path="coinSymbol" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易hash：</label>
			<div class="controls">
				<form:input path="txId" htmlEscape="false" maxlength="128" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge " disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('withdrawStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">提币数量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="form-actions">
		<!--	<shiro:hasPermission name="plat:userWithdrawLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission> -->
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>