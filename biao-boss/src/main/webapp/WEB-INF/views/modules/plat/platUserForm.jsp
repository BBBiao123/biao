<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>前台用户管理</title>
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
		<li><a href="${ctx}/plat/platUser/">前台用户列表</a></li>
		<li class="active"><a href="${ctx}/plat/platUser/form?id=${platUser.id}">前台用户<shiro:hasPermission name="plat:platUser:edit">${not empty platUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:platUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="platUser" action="${ctx}/plat/platUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				<form:input path="username" htmlEscape="false" maxlength="45" readonly="true" disabled="true" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户类型：</label>
			<div class="controls">
				<form:input path="userType" htmlEscape="false" maxlength="1" readonly="true" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否锁定：</label>
			<div class="controls">
				<form:input path="status" htmlEscape="false" maxlength="1" readonly="true" disabled="true" class="input-xlarge "/>
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
			<label class="control-label">性别：</label>
			<div class="controls">
				 ${fns:getDictLabel(platUser.sex, 'sex', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年龄：</label>
			<div class="controls">
				<form:input path="age" htmlEscape="false" maxlength="2" disabled="true" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:input path="realName" htmlEscape="false" maxlength="45" disabled="true" class="input-xlarge "/>
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
		<%--<div class="control-group">--%>
			<%--<label class="control-label">身份证 正面图案id：</label>--%>
			<%--<div class="controls">--%>
			    <%--<img src="${imageUrl}/${platUser.cardUpId}" height="200" width="300">--%>
				<%--&lt;%&ndash; <form:input path="cardUpId" htmlEscape="false" maxlength="45" class="input-xlarge "/> &ndash;%&gt;--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">身份证反面id：</label>--%>
			<%--<div class="controls">--%>
			     <%--<img src="${imageUrl}/${platUser.cardDownId}" height="200" width="300">--%>
				<%--&lt;%&ndash; <form:input path="cardDownId" htmlEscape="false" maxlength="45" class="input-xlarge "/> &ndash;%&gt;--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">手持身份id：</label>--%>
			<%--<div class="controls">--%>
			    <%--<img src="${imageUrl}/${platUser.cardFaceId}" height="200" width="300">--%>
				<%--&lt;%&ndash; <form:input path="cardFaceId" htmlEscape="false" maxlength="45" class="input-xlarge "/> &ndash;%&gt;--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">审核状态 ：</label>
			<div class="controls">
			    通过<form:radiobutton path="cardStatus" value="11" htmlEscape="false" maxlength="1" class="input-xlarge "/>&nbsp;&nbsp;
			  不通过<form:radiobutton path="cardStatus" value="19" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核原因：</label>
			<div class="controls">
				<form:input path="cardStatusReason" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>

		<%--
		<div class="control-group">
			<label class="control-label">是否开启平台币抵扣手续费：</label>
			<div class="controls">
				<form:input path="openDiscount" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div> --%>
		<%-- <div class="control-group">
			<label class="control-label">country_id：</label>
			<div class="controls">
				<form:input path="countryId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">country_code：</label>
			<div class="controls">
				<form:input path="countryCode" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div> --%>
		<%-- <div class="control-group">
			<label class="control-label">微信号：</label>
			<div class="controls">
				<form:input path="wechatNo" htmlEscape="false" maxlength="24" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">微信收款码：</label>
			<div class="controls">
				<form:input path="wechatQrcodeId" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付宝账号：</label>
			<div class="controls">
				<form:input path="alipayNo" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付宝收款码：</label>
			<div class="controls">
				<form:input path="alipayQrcodeId" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">推荐人：</label>
			<div class="controls">
				<form:input path="referId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div> --%>
		<div class="form-actions">
			<shiro:hasPermission name="plat:platUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>