<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分红规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({

				submitHandler: function(form){
                    var percentage = $("input[name='percentage']").val();
                    if(!checkPercentage(percentage)){
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

            // $("#selectCoin").change(function(){
            //     $("#coinSymbol").val($("#selectCoin").find("option:selected").text());
            // });

            $("#selectPlatCoin").change(function(){
                $("#platCoinSymbol").val($("#selectPlatCoin").find("option:selected").text());
            });

            $("body").on("click","#usename", function(){
                var userThis = this;
                $.jBox.open("iframe:${ctx}/plat/platUser/dialog/", "用户列表", 1100, 500, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                        if (v == 1) {
                            var iframeName = h.children(0).attr("name");
                            var container = window.frames[iframeName].document;
                            var user = $(':radio[name="chooseUser"]:checked', container);
                            var userLabel = user.attr("userLabel");
                            if(userLabel == null || userLabel == ""){
                                userLabel = user.attr("userMobile");
							}
                            $(userThis).val(userLabel);
                            $(userThis).prev("input[name='userId']").val(user.val());
                        }
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
            });
		});

        function checkPercentage(percentage){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
            if (!reg.test(percentage)) {
                alertx("请输入正确百分比！")
                return false;
            }

            if(parseFloat(percentage) <= 0){
                alertx("百分比不能小于等于零！")
                return false;
            }

            if(parseFloat(percentage) > 100){
                alertx("百分比不能超过100！")
                return false;
            }

            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/market/mkDistributeDividend/">分红规则列表</a></li>
		<li class="active"><a href="${ctx}/market/mkDistributeDividend/form?id=${mkDistributeDividend.id}">分红规则<shiro:hasPermission name="market:dividend:edit">${not empty mkDistributeDividend.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="market:dividend:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mkDistributeDividend" action="${ctx}/market/mkDistributeDividend/save" method="post" class="form-horizontal">
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
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('distributeRuleStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">百分比（手续费）：</label>
			<div class="controls">
				<form:input path="percentage" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*例如：100.00%,输入: 100.00</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">收纳手续费账户：</label>
			<div class="controls">
				<form:hidden path="userId" id="userId"/>
				<form:input path="username" id="usename" readonly="true" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">发放币种：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:hidden path="coinSymbol" id="coinSymbol"/>--%>
				<%--<form:select path="coinId" class="input-xlarge required" id="selectCoin">--%>
					<%--<form:option value="" label=""/>--%>
					<%--<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />--%>
				<%--</form:select>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">用户持有币种：</label>
			<div class="controls">
				<form:hidden path="platCoinSymbol" id="platCoinSymbol"/>
				<form:select path="platCoinId" class="input-xlarge required" id="selectPlatCoin">
					<form:option value="" label=""/>
					<form:options items="${coinList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">说明：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="market:dividend:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>