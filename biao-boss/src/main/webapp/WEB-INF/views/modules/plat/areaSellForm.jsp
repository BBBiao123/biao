<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域销售管理</title>
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
		<li><a href="${ctx}/plat/areaSell/">区域销售列表</a></li>
		<li class="active"><a href="${ctx}/plat/areaSell/form?id=${areaSell.id}">区域销售<shiro:hasPermission name="plat:areaSell:edit">${not empty areaSell.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:areaSell:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="areaSell" action="${ctx}/plat/areaSell/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">城市：</label>
			<div class="controls">
				<form:input path="areaName" htmlEscape="false" maxlength="64" class="input-xlarge required" disabled="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">省份：</label>
			<div class="controls">
				<form:input path="areaParaentName" htmlEscape="false" maxlength="64" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">卖价：</label>
			<div class="controls">
				<form:input path="sellPrice" htmlEscape="false" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">售出：</label>
			<div class="controls">
				<form:select path="sold" class="input-xlarge required" disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('area_sold')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge " disabled="true"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:areaSell:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>