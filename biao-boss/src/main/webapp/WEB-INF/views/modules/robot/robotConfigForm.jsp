<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机器人配置管理</title>
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
		<li><a href="${ctx}/robot/robotConfig/">机器人配置列表</a></li>
		<li class="active"><a href="${ctx}/robot/robotConfig/form?id=${robotConfig.id}">机器人配置<shiro:hasPermission name="robot:robotConfig:edit">${not empty robotConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="robot:robotConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="robotConfig" action="${ctx}/robot/robotConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">type：</label>
			<div class="controls">
			    <form:select path="type" class="input-xlarge ">
				     <form:option value="0" label="卖出"/>
				     <form:option value="1" label="买入"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">coin_main：</label>
			<div class="controls">
				<form:input path="coinMain" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">coin_other：</label>
			<div class="controls">
				<form:input path="coinOther" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_id：</label>
			<div class="controls">
			     <form:select path="user.id" class="input-xlarge">
				     <form:option value="1" label="userId为1"/>
				     <form:option value="2" label="userId为2"/>
				</form:select>
				<%-- <sys:treeselect id="user" name="user.id" value="${robotConfig.user.id}" labelName="user.name" labelValue="${robotConfig.user.name}"
					title="用户" url="/sys/office/robitUsers?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">status：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
				     <form:option value="0" label="启用"/>
				     <form:option value="1" label="禁用"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">volume_range：</label>
			<div class="controls">
				<form:input path="volumeRange" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">price_range：</label>
			<div class="controls">
				<form:input path="priceRange" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="robot:robotConfig:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>