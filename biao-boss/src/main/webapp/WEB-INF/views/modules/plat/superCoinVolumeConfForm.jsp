<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>超级钱包配置管理</title>
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

            $("body").on("click","#destroyUserId", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            $("input[name='destroyUserId']").val(user.val());
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
		<li><a href="${ctx}/plat/superCoinVolumeConf/">超级钱包配置列表</a></li>
		<li class="active"><a href="${ctx}/plat/superCoinVolumeConf/form?id=${superCoinVolumeConf.id}">超级钱包配置<shiro:hasPermission name="plat:superCoinVolumeConf:edit">${not empty superCoinVolumeConf.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="plat:superCoinVolumeConf:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="superCoinVolumeConf" action="${ctx}/plat/superCoinVolumeConf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">币种代号：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最小转入数量：</label>
			<div class="controls">
				<form:input path="inMinVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最小转出数量：</label>
			<div class="controls">
				<form:input path="outMinVolume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">放大倍数(超级钱包)：</label>
			<div class="controls">
				<form:input path="multiple" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">放大倍数(冻结)：</label>
			<div class="controls">
				<form:input path="memberLockMultiple" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">锁定周期（天）：</label>
			<div class="controls">
				<form:input path="lockCycle" htmlEscape="false" maxlength="10" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结天数：</label>
			<div class="controls">
				<form:input path="frozenDay" htmlEscape="false" maxlength="10" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">违约金比例：</label>
			<div class="controls">
				<form:input path="breakRatio" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">销毁总账户：</label>
			<div class="controls">
				<form:input path="destroyUserId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">划转状态：</label>
			<div class="controls">
				<form:select path="transferStatus" class="input-xlarge required">
					<form:options items="${fns:getDictList('superTransferStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:options items="${fns:getDictList('superConfStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="plat:superCoinVolumeConf:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>