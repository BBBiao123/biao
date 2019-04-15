<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>注册抽奖活动管理</title>
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
		<li><a href="${ctx}/plat/mkUserRegisterLottery/">注册抽奖活动列表</a></li>
		<li class="active"><a href="${ctx}/plat/mkUserRegisterLottery/form?id=${mkUserRegisterLottery.id}">注册抽奖活动<shiro:hasPermission name="plat:mkUserRegisterLottery:edit">${not empty mkUserRegisterLottery.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:mkUserRegisterLottery:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkUserRegisterLottery" action="${ctx}/plat/mkUserRegisterLottery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">活动名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">活动开始时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   value="<fmt:formatDate value="${mkUserRegisterLottery.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:select path="coinSymbol" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="name" htmlEscape="false" />
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">奖金总量：</label>
			<div class="controls">
				<form:input path="totalPrize" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐人获得奖金最小持币量：</label>
			<div class="controls">
				<form:input path="recommendMinVolume" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐人获得奖金比例：</label>
			<div class="controls">
				<form:input path="recommendRatio" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐人每天奖励数量：</label>
			<div class="controls">
				<form:input path="recommendDayCount" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐人奖励总数量：</label>
			<div class="controls">
				<form:input path="recommendTotalCount" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">推荐人数限制：</label>
			<div class="controls">
				<form:input path="recommendCountLimit" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>


		<div class="form-actions">
			<shiro:hasPermission name="plat:mkUserRegisterLottery:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>