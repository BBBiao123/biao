<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银商拨币审批管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

            <c:if test="${otcAgentRemitApply.payCoinType == '0'}">
				$(':input[name=transferTo]').attr("readonly",true);
				$(':input[name=transferOut]').attr("readonly",true);
				$(':input[name=usdtRate]').attr("readonly",true);
            </c:if>

            <c:if test="${otcAgentRemitApply.status == '1' || otcAgentRemitApply.status == '2'}">
				$('#financeAuditComment').attr("readonly",true);
            	$(':input[name=usdtRate]').attr("readonly",true);
			</c:if>

            <c:if test="${otcAgentRemitApply.status == '2'}">
				$('#marketAuditComment').attr("readonly",true);
            </c:if>

			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form, options){
					loading('正在提交，请稍等...');
					console.log(JSON.stringify(options));
                    form.attr("action","${ctx}/otc/otcAgentRemitAudit/form?id=${otcAgentRemitApply.id}").submit();
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

			$("#btnSubmit").click(function () {

                var coinType = $("#selectPayCoinType").find("option:selected").val();
                if(coinType == 1){
                    var usdtRate =  $(':input[name=usdtRate]').val();
                    if(usdtRate == null || usdtRate == ""){
                        alertx("USDT汇率不能为空！");
                        $(':input[name=volume]').val("");
                        return;
                    }
                    if(!checkUsdtRate(usdtRate)){
                        return ;
                    }
                }

                $("#inputForm").attr("action", "${ctx}/otc/otcAgentRemitAudit/audit");
            });

            $("#btnCancel").click(function () {
                $("#inputForm").attr("action", "${ctx}/otc/otcAgentRemitAudit/cancel");
            });

            $("#btnRemit").click(function () {
                $("#inputForm").attr("action", "${ctx}/otc/otcAgentRemitAudit/remit");
            });

            $("#selectPayCoinType").change(function(){
                var coinType = $("#selectPayCoinType").find("option:selected").val();
                if(coinType == 0){
                    $(':input[name=transferTo]').attr("readonly",true);
                    $(':input[name=transferOut]').attr("readonly",true);
                    $(':input[name=usdtRate]').attr("readonly",true);
                    $(':input[name=transferTo]').val("");
                    $(':input[name=transferOut]').val("");
                    $(':input[name=usdtRate]').val("");
				}else{
                    $(':input[name=transferTo]').attr("readonly",false);
                    $(':input[name=transferOut]').attr("readonly",false);
                    $(':input[name=usdtRate]').attr("readonly",false);
				}

                $(':input[name=volume]').val("");
                $(':input[name=applyVolume]').val("");
            });


            $("#usdtRate").on("input",function(e){
                //获取input输入的值
                var usdtRate = e.delegateTarget.value;
                var tradeCoinRate =  $(':input[name=tradeCoinRate]').val();
                var coinType = $("#selectPayCoinType").find("option:selected").val();
                var volume = $(':input[name=volume]').val();
                var discount = $(':input[name=discount]').val();

                var applyVolume ;
                if(coinType == 0){
                    applyVolume = parseFloat(volume) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=remitVolume]').val(applyVolume.toFixed(6));
                }else{
                    if(usdtRate == null || usdtRate == ""){
                        alertx("USDT汇率不能为空！");
                        return;
                    }

                    // if(!checkUsdtRate(usdtRate)){
                     //    return ;
					// }
                    applyVolume = (parseFloat(volume) * parseFloat(usdtRate)) / (parseFloat(tradeCoinRate) * parseFloat(discount));
                    $(':input[name=remitVolume]').val(applyVolume.toFixed(6));
                }

                $(':input[name=userIds]').each(function (i) {
                    debugger;
                    var percentage = parseFloat($("#percentage_" + $(this).val()).val()) / parseFloat(100);
                    $("#remitVolume_" + $(this).val()).val((applyVolume * percentage).toFixed(6));
                });
            });
		});

        function checkUsdtRate(usdtRate){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,2})?$");
            if (!reg.test(usdtRate)) {
                return false;
            }

            if(parseFloat(usdtRate) < 5){
                alertx("usdt汇率小于等于5！");
                return false;
            }

            if(parseFloat(usdtRate) > 10){
                alertx("usdt汇率不能超过10！");
                return false;
            }
            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/otc/otcAgentRemitApply/">银商拨币申请列表</a></li>
		<li class="active"><a href="${ctx}/otc/otcAgentRemitApply/form?id=${otcAgentRemitApply.id}">银商拨币申请<shiro:hasPermission name="otc:otcAgentRemitApply:edit">${not empty otcAgentRemitApply.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="otc:otcAgentRemitApply:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="otcAgentRemitApply" action="${ctx}/otc/otcAgentRemitApply/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">银商名称：</label>
			<div class="controls">
				<form:hidden path="agentId"/>
				<form:input path="agentName" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">币种：</label>
			<div class="controls">
				<form:hidden path="coinId"/>
				<form:input path="coinSymbol" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">折扣：</label>
			<div class="controls">
				<form:hidden path="discount"/>
				<form:input path="discountPercentage" readonly="true" htmlEscape="false" class="input-xlarge "/>			</div>
		</div>
		<div class="control-group">
			<label class="control-label">支付币种：</label>
			<div class="controls">
				<form:select path="payCoinType" disabled="true" class="input-xlarge " id="selectPayCoinType">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('otcPayCoinType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">usdt转入地址：</label>
			<div class="controls">
				<form:input path="transferTo" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">usdt转出地址：</label>
			<div class="controls">
				<form:input path="transferOut" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量(金额)：</label>
			<div class="controls">
				<form:input path="volume" readonly="true" id="volume" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易币种单价：</label>
			<div class="controls">
				<form:input path="tradeCoinRate" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">USDT汇率：</label>
			<div class="controls">
				<form:input path="usdtRate" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">${otcAgentRemitApply.coinSymbol}数量：</label>
			<div class="controls">
				<form:input path="remitVolume" id="remitVolume" readonly="true" htmlEscape="false" class="input-xlarge  number"/>
			</div>
		</div>

		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<th style="width: 100px">会员ID</th>
			<th>邮箱</th>
			<th>手机</th>
			<th>拨币占比(%)</th>
			<th>预估可拨币数量</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${otcAgentApplyUserList}" var="otcAgentApplyUser">
				<tr>
					<td>
						<input name ="userIds" value="${otcAgentApplyUser.userId}" readonly="readonly"/>
					</td>
					<td>
							${otcAgentApplyUser.mail}
					</td>
					<td>
							${otcAgentApplyUser.mobile}
					</td>
					<td>
						<input name ="percentages" id="percentage_${otcAgentApplyUser.userId}" readonly value="${otcAgentApplyUser.percentage}"/>
					</td>
					<td>
						<input name ="remitVolumes" id="remitVolume_${otcAgentApplyUser.userId}" readonly value="${otcAgentApplyUser.volume}"/>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

		<div class="control-group">
			<label class="control-label">财务审核意见：</label>
			<div class="controls">
				<form:textarea path="financeAuditComment" id="financeAuditComment" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>

		<c:if test="${otcAgentRemitApply.status == '1' || otcAgentRemitApply.status == '2'}">
			<div class="control-group">
				<label class="control-label">运营审核意见：</label>
				<div class="controls">
					<form:textarea path="marketAuditComment" id="marketAuditComment" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
				</div>
			</div>
		</c:if>

		<div class="form-actions">
			<c:if test="${otcAgentRemitApply.status == '0'}">
				<shiro:hasPermission name="otc:otcAgentRemitAudit:edit">
					<input id="btnSubmit"  class="btn btn-primary" type="submit" value="财务审核"/>&nbsp;
					<input id="btnCancel" class="btn btn-primary" type="submit" value="终止"/>&nbsp;
				</shiro:hasPermission>
			</c:if>
			<c:if test="${otcAgentRemitApply.status == '1'}">
				<shiro:hasPermission name="otc:otcAgentRemitCoin:edit">
					<input id="btnRemit"  class="btn btn-primary" type="submit" value="拨币"/>&nbsp;
				</shiro:hasPermission>
			</c:if>
			<input id="btnBack" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>