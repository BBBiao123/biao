<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分红规则管理</title>
	<meta name="decorator" content="default"/>
	<style>
		.detail-form{
			border: 1px solid #eee;
			padding: 6px 10px;

		}
		.form-item{
			display: inline-block;
            width: 20%;
		}
		.form-item label{

		}
		.form-btn{

		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

            <%--var child = "<div class=\"detail-line\"><form action=\"\" class=\"detail-form\"><span class=\"form-item\" style=\"display: none\"><input type=\"hidden\" name=\"id\" value=\"\"><input type=\"hidden\" name=\"dividendId\" value=\"${page.list[0].id}\"></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">百分比：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input\"  name=\"percentage\" style=\"padding-left: 6px\" value=\"\"><span class=\"help-inline\"><font color=\"red\">*例如：80%,输入: 80</font> </span></div></span><span class=\"form-item\"><label style=\"float: left; margin-top: 10px\" class=\"control-label\">分红对象：</label><div class=\"controls\" style=\"float: left; margin-top: 6px; margin-left: 10px\"><select class=\"select-form\" style=\"width: 200px\" name=\"accountType\"><option value =\"1\"> 普通会员</option><option value =\"9\">平台</option></select></div></span><span class=\"form-item\" style=\"display: none\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">用户：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input type=\"hidden\" name=\"userId\" value=\"\"/><input class=\"input-user\" name=\"username\" style=\"padding-left: 6px\" readonly = readonly value=\"\"></div></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">说明：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input\"  name=\"remark\" style=\"padding-left: 6px\" value=\"\"></div></span><span class=\"form-btn\"><a style=\"position: relative; top: -6px;\" href=\"${ctx}/market/mkDistributeDividend/deleteDetail?id=\">删除本行</a></span></form></div>";--%>
            var child = "<div class=\"detail-line\"><form action=\"\" class=\"detail-form\"><span class=\"form-item\" style=\"display: none\"><input type=\"hidden\" name=\"id\" value=\"\"><input type=\"hidden\" name=\"dividendId\" value=\"${page.list[0].id}\"></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">百分比：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input\" name=\"percentage\" style=\"padding-left: 6px; width: 120px\" value=\"\"><span class=\"help-inline\"><font color=\"red\">*例如：80%,输入: 80</font> </span></div></span><span class=\"form-item\"><label style=\"float: left; margin-top: 10px\" class=\"control-label\">分红对象：</label><div class=\"controls\" style=\"float: left; margin-top: 6px; margin-left: 10px\"><select class=\"select-form\" style=\"width: 200px\" name=\"accountType\"><option value =\"1\" selected> 普通会员</option><option value =\"9\">平台</option></select></div></span><span class=\"form-item\" style=\"display: none\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">用户：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input type=\"hidden\" name=\"userId\" value=\"\"/><input class=\"input-user\" name=\"username\" style=\"padding-left: 6px\" readonly = readonly value=\"\"></div></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">说明：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input\"  name=\"remark\" style=\"padding-left: 6px\" value=\"\"></div></span><span class=\"form-btn\"><a style=\"position: relative; top: -6px;\" href=\"${ctx}/market/mkDistributeDividend/deleteDetail?id=\">删除本行</a></span></form></div>";
            $("body").on("click",".group-add", function(){
                $("#detail-save").before(child);
            });

            // $("body").on("click",".group-btn", function(){
            //     $(this).closest("tr").remove();
            // });

            $("body").on("click",".form-btn a", function(){
                $.get($(this).attr('href'),function(data){
                });
                $(this).closest(".detail-line").remove();
                return false;
            });

            $("body").on("change",".select-form", function () {
                if(this.value == 1){
                    $(this).parent().parent().next().hide();
                }else{
                    $(this).parent().parent().next().show();
                }
            })

            $("body").on("click",".group-save", function(){
                var details = [];
                var flag = true;
                $("form").each(function(){
                    var detail = "{";
                    var x = $(this).serializeArray();
                    $.each(x, function(i, field){

                        if(field.name == "percentage"){
                            if(field.value == ""){
                                alertx("百分比不能为空！");
                                flag = false;
                                return ;
                            }
                            if(!checkPercentage(field.value)){
                                flag = false;
                                return ;
                            }
                        }

                        detail = detail + "\"" + field.name + "\"" + ":" + "\"" +field.value + "\"";
                        if(getJsonLength(x) == (i + 1)){
                            detail = detail + "}";
						}else{
                            detail = detail + ',';
						}
                    });
                    details.push(JSON.parse(detail));
                });

                if(!flag) return ;

                $.ajax({
                    type:'POST',
                    url:'${ctx}/market/mkDistributeDividend/saveDetails',
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(details),
                    success:function(data){
                        if(data.code == "0000"){
                            alertx("保存成功！");
                            window.location.reload(true);
                        }else{
                            alertx(data.message);
                        }
                    }
                });

            });

            $("body").on("click",".input-user", function(){
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

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

        function getJsonLength(jsonData){
            var jsonLength = 0;
            for(var item in jsonData){
                jsonLength++;
            }
            return jsonLength;
        }

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
		<li class="active"><a href="${ctx}/market/mkDistributeDividend/">分红规则列表</a></li>
		<shiro:hasPermission name="market:dividend:add"><li><a href="${ctx}/market/mkDistributeDividend/form">分红规则添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>规则名称</th>
				<th>状态</th>
				<th>百分比（手续费）</th>
				<th>收纳手续费账户</th>
				<%--<th>发放币种</th>--%>
				<th>用户持有币种</th>
				<th>USDT已发放数量</th>
				<th>BTC已发放数量</th>
				<th>ETH已发放数量</th>
				<th>说明</th>
				<th>创建日期</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:dividend:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDistributeDividend">
			<tr>
				<td><a href="${ctx}/market/mkDistributeDividend/form?id=${mkDistributeDividend.id}">
					${mkDistributeDividend.name}
				</a></td>
				<td>
					${fns:getDictLabel(mkDistributeDividend.status, 'distributeRuleStatus', '')}
				</td>
				<td>
					<fmt:formatNumber value="${mkDistributeDividend.percentage}" pattern="#0.00" />%
				</td>
				<td>
					${mkDistributeDividend.username}
				</td>
				<%--<td>--%>
					<%--${mkDistributeDividend.coinSymbol}--%>
				<%--</td>--%>
				<td>
					${mkDistributeDividend.platCoinSymbol}
				</td>
				<td>
					${mkDistributeDividend.usdtGrantVolume}
				</td>
				<td>
					${mkDistributeDividend.btcGrantVolume}
				</td>
				<td>
					${mkDistributeDividend.ethGrantVolume}
				</td>
				<td>
					${mkDistributeDividend.remark}
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeDividend.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${mkDistributeDividend.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="market:dividend:edit"><td>
    				<a href="${ctx}/market/mkDistributeDividend/form?id=${mkDistributeDividend.id}">修改</a>
					<span href="" class="group-add" style="cursor: pointer; color: #2fa4e7;">添加明细</span>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<c:forEach items="${details}" var="detail" varStatus="status">
			<div class="detail-line">
				<form action="" class="detail-form">
					<span class="form-item" style="display: none">
						<input type="hidden" name="id" value="${detail.id}">
						<input type="hidden" name="dividendId" value="${page.list[0].id}">
					</span>
					<span class="form-item">
						<label class="control-label" style="float: left; margin-top: 6px">百分比：</label>
						<div class="controls" style="float: left; margin-top: 4px">
							<input class="input" name="percentage" style="padding-left: 6px; width: 120px" value="${detail.percentage}">
                            <span class="help-inline"><font color="red">*例如：80%,输入: 80</font> </span>
						</div>
					</span>
					<span class="form-item">
						<label style="float: left; margin-top: 10px" class="control-label">分红对象：</label>
						<div class="controls" style="float: left; margin-top: 6px; margin-left: 10px">
							<select class="select-form" style="width: 200px" name="accountType">
								<option value ="1" <c:if test="${'1' eq detail.accountType}">selected</c:if> > 普通会员</option>
								<option value ="9" <c:if test="${'9' eq detail.accountType}">selected</c:if>  >平台</option>
							</select>
						</div>
					</span>
					<span class="form-item" <c:if test="${'1' eq detail.accountType}">style="display: none"</c:if>>
						<label class="control-label" style="float: left; margin-top: 6px">用户：</label>
						<div class="controls" style="float: left; margin-top: 4px">
							<input type="hidden" name="userId" value="${detail.userId}"/>
							<input class="input-user" name="username" style="padding-left: 6px" readonly = readonly value="${detail.username}">
						</div>
					</span>

					<span class="form-item">
						<label class="control-label" style="float: left; margin-top: 6px">说明：</label>
						<div class="controls" style="float: left; margin-top: 4px">
							<input class="input"  name="remark" style="padding-left: 6px" value="${detail.remark}">
						</div>
					</span>

					<span class="form-btn">
						<a style="position: relative; top: -6px;" href="${ctx}/market/mkDistributeDividend/deleteDetail?id=${detail.id}">删除本行</a>
					</span>
				</form>
			</div>
	</c:forEach>

	<table id="detail-save" class="table table-striped table-bordered table-condensed">
		<tbody>
		<tr>
			<td colspan="5" style="text-align:center;">
				<span class="group-save" style="cursor: pointer; color: #2fa4e7;">保存明细</span>
			</td>
		</tr>
		</tbody>
	</table>
	<%--<div class="pagination">${page}</div>--%>
</body>
</html>