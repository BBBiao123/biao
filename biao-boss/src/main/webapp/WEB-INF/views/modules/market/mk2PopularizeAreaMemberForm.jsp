<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域合伙人售卖规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function NumberCheck(num) {
            var re=/^\d*\.{0,1}\d*$/;
            return re.exec(num) != null;
        };
		$(document).ready(function() {

            if (!$('#sucMsg').html().trim()) {
                $('#sucButton').click();
            }
            if (!$('#errorMsg').html().trim()) {
                $('#errorButton').click();
            }
            if ("1" == $('#soldStatus').val()) {
				$('#selectCoin').attr("disabled","disabled");
                $('#status').attr("disabled","disabled");
                $('#lockVolume').attr("disabled","disabled");
                // $('#releaseBeginDate').attr("disabled","disabled");
			}

			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					var check = true;
					var userIds = [];
					$(':input[name=shareholderUserId]').each(function (i) {
					    debugger;
						if (!$(this).val()) {
							alertx("股东ID不能为空！");
                            check = false;
							return false;
						}
						if (userIds.length == 0) {
                            userIds.push($(this).val());
						} else {
                            for (var i = 0; i < userIds.length; i ++) {
                                if (userIds[i] == $(this).val()) {
                                    alertx("股东ID不能重复！");
                                    check = false;
                                    return false;
                                }
                            }
                            userIds.push($(this).val());
						}
                    });
					if (!check) {
						return;
					}
					var ratioValue = 0;
                    $(':input[name=shareholderRatio]').each(function (i) {
                        if (!$(this).val()) {
                            alertx("股份比例不能为空！");
                            check = false;
                            return false;
                        }
                        $(this).val($(this).val().trim());
                        if (!NumberCheck($(this).val()) || $(this).val() < 0 || $(this).val() > 100) {
                            alertx("股份比例必须为(0,100)之间的数字！");
                            check = false;
                            return false;
						}
                        ratioValue = Number(ratioValue) + Number($(this).val());
                        if (ratioValue > 100) {
                            alertx("所有股份比例总和不能大于100！");
                            check = false;
                            return false;
						}
                    });
                    if ($('#status').val() == '1') {
                        if (!$('#userId').val()) {
                            alertx("请选择区域合伙人用户ID！");
                            return;
						}
                    }
                    if (check) {
                        form.submit();
					}
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
            
            $("#userId").on('click', function () {
                if ("1" == $('#soldStatus').val()) {
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

            function addShareholderInput(obj) {
                //var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            $(obj).val(user.val());
                        }
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
			};

            $("#addShareholder").on('click', function () {
				var html = [];
				html.push("<tr>");
                html.push("<td><input name=\"shareholderUserId\" value=\"\" readonly=\"readonly\" onclick=\"javascript:addShareholderInput(this)\"/></td>");
                html.push("<td></td>");
                html.push("<td></td>");
                html.push("<td></td>");
                html.push("<td></td>");
                html.push("<td><input name=\"shareholderRatio\" value=\"\" /></td>");
                html.push("<td><span style='cursor:pointer; color: #2fa4e7; text-decoration: underline' onclick=\"deleteShareholder(this)\">删除</span></td>");
                html.push("</tr>");
                $("#contentTable").find('tbody').append(html.join());
            });
		});
		// 弹出用户框
        function addShareholderInput(obj) {
            //var userThis = this;
            $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                    if (v == 1) {
                        var iframeName = h.children(0).attr("name");
                        var container = window.frames[iframeName].document;
                        var user = $(':radio[name="chooseUser"]:checked', container);
                        $(obj).val(user.val());
                        $(obj).parent().parent().attr('id', 'shareholder_'+user.val());
                        $(obj).parent().next().html(user.attr("userLabel"));
                        $(obj).parent().next().next().html(user.attr("userMobile"));
                        $(obj).parent().next().next().next().html(user.attr("userIdCard"));
                        $(obj).parent().next().next().next().next().html(user.attr("userRealName"));
                    }
                    return true;
                }});
            $("#jbox-content").css({"overflow-y":"hidden"});
        };
        function deleteShareholder(obj) {
            confirmx('确认要该股东吗？', function () {
                $(obj).parent().parent().remove();
                return false;
            });
			return false;
        };
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mk2PopularizeAreaMember/">区域合伙人售卖规则列表</a></li>
		<li class="active"><a href="${ctx}/market/mk2PopularizeAreaMember/form?id=${mk2PopularizeAreaMember.id}">区域合伙人售卖规则<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit">${not empty mk2PopularizeAreaMember.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:mk2PopularizeAreaMember:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<%--<sys:message content="${message}"/>--%>
	<div class="alert alert-success "><button id="sucButton" data-dismiss="alert" class="close">×</button><span id="sucMsg">${sucMessage}</span></div>
	<div class="alert alert-error "><button id="errorButton" data-dismiss="alert" class="close">×</button><span id="errorMsg">${errorMessage}</span></div>
	<input id="soldStatus" value="${mk2PopularizeAreaMember.status}" hidden="hidden"/>
	<form:form id="inputForm" modelAttribute="mk2PopularizeAreaMember" action="${ctx}/market/mk2PopularizeAreaMember/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<div class="control-group">
			<label class="control-label">城市：</label>
			<div class="controls">
				<form:input path="areaName" htmlEscape="false" maxlength="64" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">省份：</label>
			<div class="controls">
				<form:input path="areaParaentName" htmlEscape="false" maxlength="64" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">售出：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">冻结数量：</label>
			<div class="controls">
				<form:input path="lockVolume" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已释放数量：</label>
			<div class="controls">
				<form:input path="releaseVolume" htmlEscape="false" class="input-xlarge required number" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放开始时间：</label>
			<div class="controls">
				<input id="releaseBeginDate" name="releaseBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					   value="<fmt:formatDate value="${mk2PopularizeAreaMember.releaseBeginDate}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false, disabledDates:[29, 30, 31]});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">释放周期：</label>
			<div class="controls">
				<form:hidden path="releaseCycle" htmlEscape="false" class="input-xlarge required" /><label>月</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">周期释放比例(百分比)：</label>
			<div class="controls">
				<form:input path="releaseCycleRatio" htmlEscape="false" class="input-xlarge required number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户ID：</label>
			<div class="controls">
				<form:input path="userId" htmlEscape="false" maxlength="64" class="input-xlarge " readonly="true"/>
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
			<label class="control-label">全部释放：</label>
			<div class="controls">
				<form:select path="releaseOver" class="input-xlarge required" disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<th style="width: 100px">股东ID</th>
			<th>邮箱</th>
			<th>手机</th>
			<th>身份证</th>
			<th>姓名</th>
			<th style="width: 100px">股份比例</th>
			<%--<c:if test="${empty mk2PopularizeAreaMember.id}" >--%>
			<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><th>操作</th></shiro:hasPermission>
			<%--</c:if>--%>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${shareholderMemberList}" var="shareholderMember">
			<tr id="shareholder_${shareholderMember.userId}">
					<%--<td><a href="${ctx}/market/mk2PopularizeAreaMember/form?id=${mk2PopularizeAreaMember.id}">--%>
					<%--${mk2PopularizeAreaMember.coinId}--%>
					<%--</a></td>--%>
				<td>
					<input name ="shareholderId" type="hidden"  value="${shareholderMember.id}"/>
					<input name ="shareholderUserId" value="${shareholderMember.userId}" onclick="javascript:addShareholderInput(this)" readonly="readonly"/>
				</td>
				<td>
						${shareholderMember.mail}
				</td>
				<td>
						${shareholderMember.mobile}
				</td>
				<td>
						${shareholderMember.idCard}
				</td>
				<td>
						${shareholderMember.realName}
				</td>
				<td>
						<input name="shareholderRatio" value="${shareholderMember.ratio}"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${shareholderMember.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<%--<c:if test="${empty mk2PopularizeAreaMember.id}" >--%>
					<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit">
						<td><span style="cursor:pointer; color: #2fa4e7; text-decoration: underline" onclick="deleteShareholder(this)">删除</span></td>
					</shiro:hasPermission>
				<%--</c:if>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
		<div class="form-actions">
			<%--<c:if test="${empty mk2PopularizeAreaMember.id}" >--%>
			<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><input id="addShareholder" class="btn btn-primary" type="button" value="添加股东"/>&nbsp;</shiro:hasPermission>
			<%--</c:if>--%>
			<shiro:hasPermission name="market:mk2PopularizeAreaMember:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="location.href='${ctx}/market/mk2PopularizeAreaMember'"/>
		</div>
	</form:form>
</body>
</html>