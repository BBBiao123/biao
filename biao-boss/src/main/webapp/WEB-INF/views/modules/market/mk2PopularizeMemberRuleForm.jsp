<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员规则设置管理</title>
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
		<li><a href="${ctx}/market/mk2PopularizeMemberRule/">会员规则设置列表</a></li>
		<li class="active"><a href="${ctx}/market/mk2PopularizeMemberRule/form?id=${mk2PopularizeMemberRule.id}">会员规则设置<shiro:hasPermission name="market:mk2PopularizeMemberRule:edit">${not empty mk2PopularizeMemberRule.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mk2PopularizeMemberRule:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeMemberRule" action="${ctx}/market/mk2PopularizeMemberRule/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">会员类型：</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span><label><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;1：普通会员；2：节点人；3：合伙人</font></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放开关：</label>
			<div class="controls">
				<form:input path="releaseOpen" htmlEscape="false" maxlength="1" class="input-xlarge "/><label><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;0：关；1：开；为空表示关闭</font></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">天周期开关：</label>
			<div class="controls">
				<form:input path="releaseDay" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">周周期开关：</label>
			<div class="controls">
				<form:input path="releaseWeek" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">月周期开关：</label>
			<div class="controls">
				<form:input path="releaseMonth" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年周期开关：</label>
			<div class="controls">
				<form:input path="releaseYear" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放冻结类型：</label>
			<div class="controls">
				<form:input path="releaseType" htmlEscape="false" maxlength="50" class="input-xlarge "/>
				<label><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;1：充值冻结；2：广告商冻结；3：活动奖励冻结。多个冻结类型用英文逗号分隔（如：1,2）</font></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">会员总量：</label>
			<div class="controls">
				<form:input path="totalMember" htmlEscape="false" maxlength="10" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已售出会员总量：</label>
			<div class="controls">
				<form:input path="soldMember" htmlEscape="false" maxlength="10" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">固定分红比例百分比：</label>
			<div class="controls">
				<form:input path="bonusRatio" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">号码归属地分红：</label>
			<div class="controls">
				<form:input path="phoneBonusRatio" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐分红：</label>
			<div class="controls">
				<form:input path="referBonusRatio" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mk2PopularizeMemberRule:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>