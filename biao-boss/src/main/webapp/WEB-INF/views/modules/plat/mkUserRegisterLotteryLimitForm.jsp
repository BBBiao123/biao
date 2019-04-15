<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册送奖活动限制管理</title>
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
		<li><a href="${ctx}/plat/mkUserRegisterLotteryLimit/">注册送奖活动限制列表</a></li>
		<li class="active"><a href="${ctx}/plat/mkUserRegisterLotteryLimit/form?id=${mkUserRegisterLotteryLimit.id}">注册送奖活动限制<shiro:hasPermission name="plat:mkUserRegisterLotteryLimit:edit">${not empty mkUserRegisterLotteryLimit.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:mkUserRegisterLotteryLimit:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkUserRegisterLotteryLimit" action="${ctx}/plat/mkUserRegisterLotteryLimit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">注册抽奖活动：</label>
			<div class="controls">
				<form:select path="lotteryId" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${lotteryList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">开始数量：</label>
			<div class="controls">
				<form:input path="startCount" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束数量：</label>
			<div class="controls">
				<form:input path="endCount" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">比例：</label>
			<div class="controls">
				<form:input path="ratio" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:mkUserRegisterLotteryLimit:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>