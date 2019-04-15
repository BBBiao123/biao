<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>平台链接管理</title>
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
		<li><a href="${ctx}/plat/platLink/">平台链接列表</a></li>
		<li class="active"><a href="${ctx}/plat/platLink/form?id=${platLink.id}">平台链接<shiro:hasPermission name="plat:platLink:edit">${not empty platLink.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:platLink:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="platLink" action="${ctx}/plat/platLink/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">类型：</label>
			<div class="controls">
				<form:select path="typeid" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('plat_link')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">URL：</label>
			<div class="controls">
				<form:textarea path="linkurl" htmlEscape="false" rows="4" maxlength="1500" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">图片：</label>
			<div class="controls">
				<form:hidden id="linkimage" path="linkimage" htmlEscape="false" maxlength="1000" class="input-xlarge"/>
				<sys:ckfinder input="linkimage" type="files" uploadPath="/plat/platLink" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">展示顺序：</label>
			<div class="controls">
				<form:input path="showOrder" htmlEscape="false" maxlength="3" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:platLink:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>