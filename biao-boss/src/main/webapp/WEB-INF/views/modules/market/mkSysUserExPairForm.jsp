<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>营销用户币币对管理</title>
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

            $("#selectExPair").change(function(){
                var exPair = $("#selectExPair").find("option:selected").text();
                $("#exPairSymbol").val(exPair);

                var coins = exPair.split('/');
                $("input[name='coinOtherSymbol']").val(coins[0]);
                $("input[name='coinMainSymbol']").val(coins[1]);
            });

            $("body").on("click","#username", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            $("input[name='userId']").val(user.val());
                            $("input[name='mail']").val(user.attr("userLabel"));
                            $("input[name='mobile']").val(user.attr("userMobile"));
                            $("input[name='idCard']").val(user.attr("userIdCard"));
                            $("input[name='realName']").val(user.attr("userRealName"));

                            var userLabel = user.attr("userLabel");
                            if(userLabel == null || userLabel == ""){
                                userLabel = user.attr("userMobile");
                            }
                            $("input[name='username']").val(userLabel);
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
		<li><a href="${ctx}/market/mkSysUserExPair/">营销用户币币对列表</a></li>
		<li class="active"><a href="${ctx}/market/mkSysUserExPair/form?id=${mkSysUserExPair.id}">营销用户币币对<shiro:hasPermission name="market:mkSysUserExPair:edit">${not empty mkSysUserExPair.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkSysUserExPair:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkSysUserExPair" action="${ctx}/market/mkSysUserExPair/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">授权用户：</label>
			<div class="controls">
				<sys:treeselect id="sysUserId" name="sysUserId" value="${mkSysUserExPair.sysUserId}" labelName="sysUserName" labelValue="${mkSysUserExPair.sysUserName}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易账户：</label>
			<div class="controls">
				<form:hidden path="userId" id="userId"/>
				<form:hidden path="mail" id="mail"/>
				<form:hidden path="mobile" id="mobile"/>
				<form:hidden path="idCard" id="idCard"/>
				<form:input path="username" id="username" readonly="true" htmlEscape="false" maxlength="45" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易账户真实姓名：</label>
			<div class="controls">
				<form:input path="realName"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币币对：</label>
			<div class="controls">
				<form:hidden path="exPairSymbol" id="exPairSymbol"/>
				<form:select path="exPairId" class="input-xlarge required" id="selectExPair">
					<form:option value="" label=""/>
					<form:options items="${exPairList}" itemLabel="pairSymbol" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主交易区：</label>
			<div class="controls">
				<form:input path="coinMainSymbol" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">被交易币种：</label>
			<div class="controls">
				<form:input path="coinOtherSymbol" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkSysUserExPair:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>