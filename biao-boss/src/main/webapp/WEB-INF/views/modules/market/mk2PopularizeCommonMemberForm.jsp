<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>普通用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();

			if ($("#messageBox")) {
                $("#messageBox").css({display: 'block'})
			}

			if ($('#id').val()) {
                $('#selectCoin').attr("disabled","disabled");
                $('#releaseCycle').attr("disabled","disabled");
                $('#type').attr("disabled","disabled");
                $('#lockVolume').attr("readonly", "true");
			} else {
			    $('#releaseForm').hide();
            }

            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });

            $("#releaseSubmit").click(function () {
                var releaseVolume = $("#manualReleaseVolume").val();
                if (!releaseVolume || releaseVolume == '') {
                    alertx("请输入释放数量！");
                    return;
                } else {
                    confirmx("确认手工释放["+releaseVolume+"]?", function () {
                        $("#releaseForm").submit();
                    });
                }
            });

            $("#userId").on('click', function () {
                if ($('#id').val()) {
                    return;
                }
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            $(userThis).val(user.val());
                            $("#mail").val(user.attr("userLabel"));
                            $("#mobile").val(user.attr("userMobile"));
                            $("#idCard").val(user.attr("userIdCard"));
                            $("#realName").val(user.attr("userRealName"));
                        }
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
            });

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
		<li><a href="${ctx}/market/mk2PopularizeCommonMember/">普通用户列表</a></li>
		<li class="active"><a href="${ctx}/market/mk2PopularizeCommonMember/form?id=${mk2PopularizeCommonMember.id}">普通用户<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit">${not empty mk2PopularizeCommonMember.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mk2PopularizeCommonMember:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeCommonMember" action="${ctx}/market/mk2PopularizeCommonMember/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">类型：</label>
			<div class="controls">
				<form:input path="type" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<label><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;1：充值冻结；2：广告商冻结；3：活动奖励冻结。</font></label>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">普通会员父ID：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="parentId" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">用户ID：</label>
			<div class="controls">
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-xlarge required" readonly="true"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱：</label>
			<div class="controls">
				<form:input path="mail" htmlEscape="false" maxlength="64" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号：</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="11" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证：</label>
			<div class="controls">
				<form:input path="idCard" htmlEscape="false" maxlength="18" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:input path="realName" htmlEscape="false" maxlength="45" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin" cssStyle="width: 176px;">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">是否冻结：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="lockStatus" htmlEscape="false" maxlength="1" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">冻结数量：</label>
			<div class="controls">
				<form:input path="lockVolume" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已释放数量：</label>
			<div class="controls">
				<form:input path="releaseVolume" htmlEscape="false" class="input-xlarge  number" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放开始时间：</label>
			<div class="controls">
				<input name="releaseBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${mk2PopularizeCommonMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false, disabledDates:[29, 30, 31]});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放周期：</label>
			<div class="controls">
                <form:select path="releaseCycle" class="input-xlarge " >
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('cycle')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">周期释放比例(百分比)：</label>
			<div class="controls">
				<form:input path="releaseCycleRatio" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">全部释放：</label>
			<div class="controls">
				<form:select path="releaseOver" class="input-xlarge " disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="location.href='${ctx}/market/mk2PopularizeCommonMember'"/>
		</div>
	</form:form>

	<br/>
	<form:form id="releaseForm" style="border: 1px solid #666; padding: 20px 10px;" action="${ctx}/market/mk2PopularizeCommonMember/release" method="post">
		<input type="hidden" name="commonMemberId" value="${mk2PopularizeCommonMember.id}"/>
		<div class="control-group">
			<div class="controls">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手工释放数量：<input type="text" id = "manualReleaseVolume" name = "manualReleaseVolume"/>
				<shiro:hasPermission name="market:mk2PopularizeCommonMember:edit"><input id="releaseSubmit" class="btn btn-primary" style="margin-top: -10px; margin-left: 20px" type="button" value="手工释放"/>&nbsp;</shiro:hasPermission>
			</div>
		</div>
	</form:form>
</body>
</html>