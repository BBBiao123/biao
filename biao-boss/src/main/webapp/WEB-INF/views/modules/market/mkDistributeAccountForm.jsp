<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>营销账户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    var lockVolume = $("input[name='lockVolume']").val();
                    var releaseVolume = $("input[name='releaseVolume']").val();
                    if(!checkVolume(lockVolume,releaseVolume)){
                        return ;
                    }
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

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });

            // $("#selectUser").change(function(){
            //     $("#username").val($("#selectUser").find("option:selected").text());
            // });
            $("body").on("click","#realName", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
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

        function checkVolume(lockVolume,releaseVolume){

            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(lockVolume)) {
                alertx("请输入正确的冻结资产！")
                return false;
            }

            if(parseFloat(lockVolume) <= 0){
                alertx("冻结资产不能小于零！")
                return false;
            }

            if(parseFloat(lockVolume) <= parseFloat(releaseVolume)){
                alertx("冻结资产不能小于已释放数量！")
                return false;
            }

            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkDistributeAccount/">挖矿冻结账户列表</a></li>
		<li class="active"><a href="${ctx}/market/mkDistributeAccount/form?id=${mkDistributeAccount.id}">营销账户<shiro:hasPermission name="market:account:edit">${not empty mkDistributeAccount.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:account:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkDistributeAccount" action="${ctx}/market/mkDistributeAccount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<%--<div class="control-group">--%>
			<%--<label class="control-label">账户类型：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:select path="type" class="input-xlarge required">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${fns:getDictList('mkAccountType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
				<%--</form:select>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">账户名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('distributeAccountStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户（真实姓名）：</label>
			<div class="controls">
					<form:hidden path="userId" id="userId"/>
					<form:input path="realName" id="realName" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls">
				<form:input path="mail" id="mail" readonly="true" htmlEscape="false" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机：</label>
			<div class="controls">
				<form:input path="mobile" id="mobile" readonly="true" htmlEscape="false" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证：</label>
			<div class="controls">
				<form:input path="idCard" id="idCard" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge " id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结资产：</label>
			<div class="controls">
				<form:input path="lockVolume" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label">已释放资产：</label>
            <div class="controls">
                <form:input path="releaseVolume" readonly="true" htmlEscape="false" class="input-xlarge"/>
            </div>
        </div>
		<div class="form-actions">
			<shiro:hasPermission name="market:account:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>