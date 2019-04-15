<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>自动交易用户管理</title>
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
            $("body").on("click","#realName", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 900, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            $(userThis).prev("input[name='userId']").val(user.val());
                            $("input[name='mail']").val(user.attr("userLabel"));
                            $("input[name='mobile']").val(user.attr("userMobile"));
                            $("input[name='idCard']").val(user.attr("userIdCard"));
                            $("input[name='realName']").val(user.attr("userRealName"));
                        }
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
            });

		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkAutoTradeUser/">自动交易用户列表</a></li>
		<li class="active"><a href="${ctx}/market/mkAutoTradeUser/form?id=${mkAutoTradeUser.id}">自动交易用户<shiro:hasPermission name="market:mkAutoTradeUser:edit">${not empty mkAutoTradeUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkAutoTradeUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkAutoTradeUser" action="${ctx}/market/mkAutoTradeUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:hidden path="userId" id="userId"/>
				<form:input path="realName" id="realName" readonly="true" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机：</label>
			<div class="controls">
				<form:input path="mobile"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls">
				<form:input path="mail"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证：</label>
			<div class="controls">
				<form:input path="idCard"  readonly="true" htmlEscape="false" maxlength="18" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkAutoTradeUser:edit">
				<c:if test="${mkAutoTradeUser.source eq 'add'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
				<c:if test="${empty mkAutoTradeUser.source}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>