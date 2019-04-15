<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>接力撞奖配置管理</title>
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

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
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
                            $("input[name='realName']").val(user.attr("userRealName"));
                            if(user.attr("userLabel") == null || user.attr("userLabel") == ""){
                                $("input[name='username']").val(user.attr("userMobile"));
							}else{
                                $("input[name='username']").val(user.attr("userLabel"));
							}
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
		<li><a href="${ctx}/market/mkRelayPrizeConfig/">接力撞奖配置列表</a></li>
		<li class="active"><a href="${ctx}/market/mkRelayPrizeConfig/form?id=${mkRelayPrizeConfig.id}">接力撞奖配置<shiro:hasPermission name="market:mkRelayPrizeConfig:edit">${not empty mkRelayPrizeConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mkRelayPrizeConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkRelayPrizeConfig" action="${ctx}/market/mkRelayPrizeConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">规则名称：</label>
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
					<form:options items="${fns:getDictList('mkRelayConfigStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">奖金总量：</label>
			<div class="controls">
				<form:input path="volume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">起步奖金：</label>
			<div class="controls">
				<form:input path="startVolume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">新增奖金：</label>
			<div class="controls">
				<form:input path="stepAddVolume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每天开始时间：</label>
			<div class="controls">
				<fmt:parseDate value="${mkRelayPrizeConfig.beginTime}" pattern="HH:mm:ss" var="beginTime"/>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   value="<fmt:formatDate value="${beginTime}" pattern="HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每天结束时间：</label>
			<div class="controls">
				<fmt:parseDate value="${mkRelayPrizeConfig.endTime}" pattern="HH:mm:ss" var="endTime"/>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					   value="<fmt:formatDate value="${endTime}" pattern="HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">归集账户：</label>
			<div class="controls">
				<form:hidden path="userId" id="userId"/>
				<form:hidden path="mail" id="mail"/>
				<form:hidden path="mobile" id="mobile"/>
				<form:hidden path="realName" id="realName"/>
				<form:input path="username" id="username" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否打款：</label>
			<div class="controls">
				<form:select path="isRemit" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('mkRelayIsRemit')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最低参与数量：</label>
			<div class="controls">
				<form:input path="minVolume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*包含</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已发放数量：</label>
			<div class="controls">
				<form:input path="grantVolume" readonly="true" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前奖池总量：</label>
			<div class="controls">
				<form:input path="curPoolVolume" readonly="true" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mkRelayPrizeConfig:edit">
				<c:if test="${empty mkRelayPrizeConfig.status || mkRelayPrizeConfig.status == '0'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>