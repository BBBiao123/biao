<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>节点人管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var refreshLocalUrl = "${ctx}/market/mk2PopularizeNodalMember/form?id=${mk2PopularizeNodalMember.id}&returnMsg=";
		$(document).ready(function() {
            $("#selectCoin").change(function(){
                $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            });
            if ($('#id').val()) {
                $('#selectCoin').attr("disabled","disabled");
			}
            if (!$('#sucMsg').html().trim()) {
                $('#sucButton').click();
            }
            if (!$('#errorMsg').html().trim()) {
                $('#errorButton').click();
            }
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
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
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

        /**
		 *  添加一条TR 节点人锁定和解锁信息
         */
		function createNodalTr() {
			var html = [];
			html.push("<tr>");
			html.push("<td><input name =\"lockVolume\" value=\"0\" /></td>");
            html.push("<td>0</td>");
            html.push("<td><input name=\"releaseBeginDate\" type=\"text\" readonly=\"readonly\" maxlength=\"20\" class=\"input-medium Wdate required\" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,disabledDates:[29, 30, 31]});\"/></td>");
            html.push("<td>月</td>");
            html.push("<td><input name =\"releaseCycleRatio\" value=\"0\" /></td>");
            html.push("<td>否</td>");
            html.push("<td>否</td>");
            html.push("<td><span style=\"cursor:pointer; color: #2fa4e7; text-decoration: underline\" onclick=\"saveNodalRule(this, '0');\">保存</span>&nbsp;&nbsp;&nbsp;" +
				"<span style=\"cursor:pointer; color: #2fa4e7; text-decoration: underline\" onclick=\"saveNodalRule(this, '1');\">冻结</span>&nbsp;&nbsp;&nbsp;" +
				"<span style=\"cursor:pointer; color: #2fa4e7; text-decoration: underline\" onclick=\"deleteNodalRule(this);\">删除</span></td>");
            html.push("</tr>");
            $("#contentTable").find('tbody').append(html.join());
        }
        // 获取节点人的用户信息和冻结币种信息
        function getUserCoinInfoObj() {
			var member = {};
			member.parentId = $("#id").val();
            member.userId = $("#userId").val();
            member.mail = $("#mail").val();
            member.mobile = $("#mobile").val();
            member.idCard = $("#idCard").val();
            member.realName = $("#realName").val();
            member.coinId = $("#selectCoin").val();
            member.coinSymbol = $("#coinSymbol").val();
            return member;
        }

        /**
         * 保存节点人锁定和解锁信息
         */
        function saveNodalRule(obj, lockStatus) {
            var trObj = $(obj).parent().parent();
            var member = getUserCoinInfoObj();
            member.id = $(trObj).find("input[name='mk2NodalLockReleaseInfoId']").val();
            member.lockVolume = $(trObj).find("input[name='lockVolume']").val();
            member.releaseBeginDate = $(trObj).find("input[name='releaseBeginDate']").val();
            member.releaseCycleRatio = $(trObj).find("input[name='releaseCycleRatio']").val();
            member.lockStatus = lockStatus;// 0 未冻结 1 冻结
            $.ajax({
                type: "POST",
                async: false,
                url: "${ctx}/market/mk2PopularizeNodalMember/saveLockRelease",
                beforeSend: function() { loading('正在提交，请稍等...'); },
                data: member,// 要提交的表单
                success: function (msg) {
                    closeLoading();
                    var resultObj = $.parseJSON(msg);
					if (resultObj && resultObj.success == 0) { // 保存失败提示信息
                        $.jBox.info(resultObj.msg);
					}
                    if (resultObj && resultObj.success == 1) {// 保存成功刷新页面
                        refreshLocalPage(resultObj.msg);
                    }
                },
                error: function (error) {
                    closeLoading();
                    $.jBox.error("输入参数错误");
                    console.log(error.responseText); // 日志输出控制台
                }
            });
        }

        /**
         * 保存节点人锁定和解锁信息
         */
        function deleteNodalRule(obj) {
            var trObj = $(obj).parent().parent();
            if ($(trObj).find("input[name='mk2NodalLockReleaseInfoId']") && $(trObj).find("input[name='mk2NodalLockReleaseInfoId']").val()) {// 之前已保存过，删除需请求后台
                var member = getUserCoinInfoObj();
                member.id = $(trObj).find("input[name='mk2NodalLockReleaseInfoId']").val();
                $.ajax({
                    type: "POST",
                    async: false,
                    url: "${ctx}/market/mk2PopularizeNodalMember/deleteLockRelease",
                    beforeSend: function() { loading('正在提交，请稍等...'); },
                    data: member,// 要提交的表单
                    success: function (msg) {
                        closeLoading();
                        var resultObj = $.parseJSON(msg);
                        if (resultObj && resultObj.success == 0) { //删除失败提示信息
                            $.jBox.info(resultObj.msg);
                        }
                        if (resultObj && resultObj.success == 1) {// 删除成功刷新页面
                            refreshLocalPage(resultObj.msg);
                        }
                    },
                    error: function (error) {
                        closeLoading();
                        $.jBox.error("输入参数错误");
                        console.log(error.responseText); // 日志输出控制台
                    }
                });
            } else {// 之前未保存过，删除TR即可
                $(trObj).remove();
            }

        }

        function refreshLocalPage(msg) {
            location.href = refreshLocalUrl + msg;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mk2PopularizeNodalMember/">节点人列表</a></li>
		<li class="active"><a href="${ctx}/market/mk2PopularizeNodalMember/form?id=${mk2PopularizeNodalMember.id}">节点人<shiro:hasPermission name="market:mk2PopularizeNodalMember:edit">${not empty mk2PopularizeNodalMember.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mk2PopularizeNodalMember:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeNodalMember" action="${ctx}/market/mk2PopularizeNodalMember/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<%--<sys:message content="${message}"/>		--%>
        <div class="alert alert-success "><button id="sucButton" data-dismiss="alert" class="close">×</button><span id="sucMsg">${sucMessage}</span></div>
        <div class="alert alert-error "><button id="errorButton" data-dismiss="alert" class="close">×</button><span id="errorMsg">${errorMessage}</span></div>
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
			<label class="control-label">身份证号：</label>
			<div class="controls">
				<form:input path="idCard" htmlEscape="false" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">姓名：</label>
			<div class="controls">
				<form:input path="realName" htmlEscape="false" class="input-xlarge " readonly="true"/>
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
			<%--<label class="control-label">冻结数量：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="lockVolume" htmlEscape="false" class="input-xlarge required number"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">已释放数量：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="releaseVolume" htmlEscape="false" class="input-xlarge" readonly="true"/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">释放开始时间：</label>--%>
			<%--<div class="controls">--%>
				<%--<input name="releaseBeginDate" id="releaseBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"--%>
					<%--value="<fmt:formatDate value="${mk2PopularizeNodalMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>"--%>
					<%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">释放周期：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:hidden path="releaseCycle" htmlEscape="false" class="input-xlarge required" /><label>月</label>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">周期释放数量：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="releaseCycleRatio" htmlEscape="false" class="input-xlarge required number"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">全部释放：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:select path="releaseOver" class="input-xlarge required" disabled="true">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />--%>
				<%--</form:select>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="form-actions">
			<c:if test="${not empty mk2PopularizeNodalMember.id}">
				<input class="btn btn-primary" type="button" value="添加锁定" onclick="createNodalTr();"/>
			</c:if>
			<c:if test="${empty mk2PopularizeNodalMember.id}">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="location.href='${ctx}/market/mk2PopularizeNodalMember'"/>
		</div>
	</form:form>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<th>冻结数量</th>
		<th>已释放数量</th>
		<th>释放开始时间</th>
		<th>释放周期</th>
		<th>周期释放比例(百分比)</th>
		<th>资金是否已冻结</th>
		<th>是否已全部释放</th>
		<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${mk2NodalLockReleaseInfoList}" var="mk2NodalLockReleaseInfo">
			<tr>
				<td>
					<input name ="mk2NodalLockReleaseInfoId" type="hidden"  value="${mk2NodalLockReleaseInfo.id}"/>
					<c:if test="${ '1' eq mk2NodalLockReleaseInfo.lockStatus}">
						<input name ="lockVolume" value="${mk2NodalLockReleaseInfo.lockVolume}" readonly="true"/>
					</c:if>
					<c:if test="${ '1' ne mk2NodalLockReleaseInfo.lockStatus}">
						<input name ="lockVolume" value="${mk2NodalLockReleaseInfo.lockVolume}"/>
					</c:if>

				</td>
				<td>
						${mk2NodalLockReleaseInfo.releaseVolume}
				</td>
				<td>
					<input name="releaseBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
						   value="<fmt:formatDate value="${mk2NodalLockReleaseInfo.releaseBeginDate}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false, disabledDates:[29, 30, 31]});"/>
				</td>
				<td>
					月
                </td>
				<td>
					<input name ="releaseCycleRatio" value="${mk2NodalLockReleaseInfo.releaseCycleRatio}" />
                </td>
				<td>
					<c:if test="${ '1' eq mk2NodalLockReleaseInfo.lockStatus}">是</c:if><c:if test="${ '0' eq mk2NodalLockReleaseInfo.lockStatus}">否</c:if>
				</td>
                <td>
					 <c:if test="${ '1' eq mk2NodalLockReleaseInfo.releaseOver}">是</c:if><c:if test="${ '0' eq mk2NodalLockReleaseInfo.releaseOver}">否</c:if>
                </td>
				<td>
					<span style="cursor:pointer; color: #2fa4e7; text-decoration: underline" onclick="saveNodalRule(this, '0');">保存</span>
					<c:if test="${ '1' ne mk2NodalLockReleaseInfo.lockStatus}">
						&nbsp;&nbsp;&nbsp;<span style="cursor:pointer; color: #2fa4e7; text-decoration: underline" onclick="saveNodalRule(this, '1');">冻结</span>
						&nbsp;&nbsp;&nbsp;<span style="cursor:pointer; color: #2fa4e7; text-decoration: underline" onclick="deleteNodalRule(this);">删除</span>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>