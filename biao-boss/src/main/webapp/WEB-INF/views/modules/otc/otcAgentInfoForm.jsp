<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商列表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {


            <c:if test="${not empty otcAgentInfo.id}">
				$(':input[name=sysUserName]').attr("readonly",true);
				$(':input[name=sysUserMail]').attr("readonly",true);
            </c:if>

			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    var check = true;
                    var userIds = [];
                    $(':input[name=userIds]').each(function (i) {
                        debugger;
                        if (!$(this).val()) {
                            alertx("会员ID不能为空！");
                            check = false;
                            return false;
                        }
                        if (userIds.length == 0) {
                            userIds.push($(this).val());
                        } else {
                            for (var i = 0; i < userIds.length; i ++) {
                                if (userIds[i] == $(this).val()) {
                                    alertx("会员ID不能重复！");
                                    check = false;
                                    return false;
                                }
                            }
                            userIds.push($(this).val());
                        }
                    });

                    if (userIds.length == 0) {
                        alertx("会员列表不能为空！");
                        check = false;
                    }

                    if (!check) {
                        return;
                    }

                    var discount = $(':input[name=discount]').val();
                    if(!checkAccount(discount)){
                        check = false;
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

            $("#addAgentUser").on('click', function () {
                var html = [];
                html.push("<tr>");
                html.push("<td><input name=\"userIds\" value=\"\" readonly=\"readonly\" onclick=\"javascript:addAgentUserInput(this)\"/></td>");
                html.push("<td></td>");
                html.push("<td></td>");
                html.push("<td><span style='cursor:pointer; color: #2fa4e7; text-decoration: underline' onclick=\"deleteAgentUser(this)\">删除</span></td>");
                html.push("</tr>");
                $("#contentTable").find('tbody').append(html.join());
            });
		});

        // 弹出用户框
        function addAgentUserInput(obj) {
            //var userThis = this;
            $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                    if (v == 1) {
                        var iframeName = h.children(0).attr("name");
                        var container = window.frames[iframeName].document;
                        var user = $(':radio[name="chooseUser"]:checked', container);
                        $(obj).val(user.val());
                        $(obj).parent().parent().attr('id', 'agentUser_'+user.val());
                        $(obj).parent().next().html(user.attr("userLabel"));
                        $(obj).parent().next().next().html(user.attr("userMobile"));
                    }
                    return true;
                }});
            $("#jbox-content").css({"overflow-y":"hidden"});
        }

        function deleteAgentUser(obj) {
            confirmx('确认要删除该会员吗？', function () {
                $(obj).parent().parent().remove();
                return false;
            });
            return false;
        }

        function checkAccount(discount){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
            if (!reg.test(discount)) {
                alertx("折扣必须为数字型！");
                return false;
            }

            if(parseFloat(discount) < 0.01){
                alertx("折扣不能小于0.01！");
                return false;
            }

            if(parseFloat(discount) > 1){
                alertx("折扣不能大于1！");
                return false;
            }
            return true;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/otc/otcAgentInfo/">银商列表列表</a></li>
		<li class="active"><a href="${ctx}/otc/otcAgentInfo/form?id=${otcAgentInfo.id}">银商列表<shiro:hasPermission name="otc:otcAgentInfo:edit">${not empty otcAgentInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="otc:otcAgentInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="otcAgentInfo" action="${ctx}/otc/otcAgentInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">银商名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('otcAgentStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">折扣：</label>
			<div class="controls">
				<form:input path="discount" htmlEscape="false" class="input-xlarge  number required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录账户：</label>
			<div class="controls">
				<form:input path="sysUserName" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">初始邮箱：</label>
			<div class="controls">
				<form:input path="sysUserMail" htmlEscape="false" class="input-xlarge required email"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinSymbol" id="coinSymbol"/>
				<form:select path="coinId" class="input-xlarge required" id="selectCoin">
					<form:option value="" label=""/>
					<form:options items="${offlineCoinList}" itemLabel="symbol" itemValue="coinId" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:input path="remark" htmlEscape="false" maxlength="500" class="input-xlarge "/>
			</div>
		</div>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<th style="width: 100px">会员ID</th>
			<th>邮箱</th>
			<th>手机</th>
			<c:if test="${empty otcAgentInfo.id}" >
				<shiro:hasPermission name="otc:otcAgentInfo:edit"><th>操作</th></shiro:hasPermission>
			</c:if>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${otcAgentUserList}" var="otcAgentUser">
				<tr id="agentUser_${otcAgentUser.userId}">
					<td>
						<input name ="userIds" value="${otcAgentUser.userId}" onclick="javascript:addAgentUserInput(this)" readonly="readonly"/>
					</td>
					<td>
							${otcAgentUser.mail}
					</td>
					<td>
							${otcAgentUser.mobile}
					</td>
					<c:if test="${not empty otcAgentInfo.id}" >
						<shiro:hasPermission name="otc:otcAgentInfo:edit">
							<td><span style="cursor:pointer; color: #2fa4e7; text-decoration: underline" onclick="deleteAgentUser(this)">删除</span></td>
						</shiro:hasPermission>
					</c:if>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="form-actions">
			<shiro:hasPermission name="otc:otcAgentInfo:edit"><input id="addAgentUser" class="btn btn-primary" type="button" value="添加会员"/>&nbsp;</shiro:hasPermission>
			<shiro:hasPermission name="otc:otcAgentInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>