<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员推广管理</title>
	<meta name="decorator" content="default"/>
	<style>
		.detail-form{
			border: 1px solid #eee;
			padding: 6px 10px;

		}
		.form-item{
			display: inline-block;
			width: 30%;
		}
		.form-item label{

		}
		.form-btn{

		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

            <%--var child = "<div class=\"detail-line\"><form action=\"\" class=\"detail-form\"><span class=\"form-item\" style=\"display: none\"><input type=\"hidden\" name=\"id\" value=\"\"><input type=\"hidden\" name=\"promoteId\" value=\"${page.list[0].id}\"></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">层级：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input\"  name=\"level\" style=\"padding-left: 6px\" value=\"\"></div></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">奖励数量：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input-volume\" name=\"volume\" style=\"padding-left: 6px\"  value=\"\"></div></span><span class=\"form-btn\"><a style=\"position: relative; top: -6px;\" href=\"${ctx}/market/mkDistributePromote/deleteDetail?id=\">删除本行</a></span></form></div>";--%>
            var child = "<div class=\"detail-line\"><form action=\"\" class=\"detail-form\"><span class=\"form-item\" style=\"display: none;\"><input type=\"hidden\" name=\"id\" value=\"\"><input type=\"hidden\" name=\"promoteId\" value=\"${page.list[0].id}\"></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">层级：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><select class=\"select-form\" style=\"width: 200px\" name=\"level\"><option value =\"\"> </option><option value =\"1\"> 1</option><option value =\"2\"> 2</option><option value =\"3\"> 3</option><option value =\"4\"> 4</option><option value =\"5\"> 5</option><option value =\"6\"> 6</option><option value =\"7\"> 7</option><option value =\"8\"> 8</option><option value =\"9\"> 9</option><option value =\"10\">10</option></select></div></span><span class=\"form-item\"><label class=\"control-label\" style=\"float: left; margin-top: 6px\">奖励数量：</label><div class=\"controls\" style=\"float: left; margin-top: 4px\"><input class=\"input-volume\" name=\"volume\" style=\"padding-left: 6px\"  value=\"\"></div></span><span class=\"form-btn\"><a style=\"position: relative; top: -6px;\" href=\"${ctx}/market/mkDistributePromote/deleteDetail?id=\">删除本行</a></span></form></div>";
            $("body").on("click",".group-add", function(){
                $("#detail-save").before(child);
            });

            $("body").on("click",".form-btn a", function(){
                $.get($(this).attr('href'),function(data){
                });
                $(this).closest(".detail-line").remove();
                return false;
            });

            $("body").on("click",".group-save", function(){
                var details = [];
                var flag = true;
                $("form").each(function(){
                    var detail = "{";
                    var x = $(this).serializeArray();
                    $.each(x, function(i, field){

                        if(field.name == "level"){
                            if(field.value == ""){
                                alertx("层级不能为空！");
                                flag = false;
                                return ;
                            }
                            if(!checkLevel(field.value)){
                                flag = false;
                                return ;
                            }
                        }

                        if(field.name == "volume"){
                            if(field.value == ""){
                                alertx("奖励数量不能为空！");
                                flag = false;
                                return ;
                            }
                            if(!checkVolume(field.value)){
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
                    url:'${ctx}/market/mkDistributePromote/saveDetails',
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

        function checkLevel(level){
            var reg = new RegExp("^[0-9]*$");
            if(!reg.test(level)){
                alertx("请输入数字!");
                return false;
            }
            return true;
		}
        function checkVolume(volume){
            var reg = new RegExp("^[0-9]+(.[0-9]{1,8})?$");
            if (!reg.test(volume)) {
                alertx("请输入正确的奖励数量！")
                return false;
            }

            if(volume <= 0){
                alertx("奖励数量不能小于零！")
                return false;
            }

            return true;
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/market/mkDistributePromote/">会员推广列表</a></li>
		<shiro:hasPermission name="market:promote:add"><li><a href="${ctx}/market/mkDistributePromote/form">会员推广添加</a></li></shiro:hasPermission>
	</ul>
	<%--<form:form id="searchForm" modelAttribute="mkDistributePromote" action="${ctx}/market/mkDistributePromote/" method="post" class="breadcrumb form-search">--%>
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
		<%--<ul class="ul-form">--%>
			<%--<li><label>规则名称：</label>--%>
				<%--<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>状态：</label>--%>
				<%--<form:input path="status" htmlEscape="false" maxlength="1" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
	<%--</form:form>--%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>规则名称</th>
				<th>状态</th>
				<th>币种代号</th>
				<th>推广总量</th>
				<th>已发放数量</th>
				<th>更新时间</th>
				<shiro:hasPermission name="market:promote:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mkDistributePromote">
			<tr>
				<td><a href="${ctx}/market/mkDistributePromote/form?id=${mkDistributePromote.id}">
					${mkDistributePromote.name}
				</a></td>
				<td>
					${fns:getDictLabel(mkDistributePromote.status, 'distributeRuleStatus', '')}
				</td>
				<td>
					${mkDistributePromote.coinSymbol}
				</td>
				<td>
					${mkDistributePromote.volume}
				</td>
				<td>
					${mkDistributePromote.grantVolume}
				</td>
				<td>
					<fmt:formatDate value="${mkDistributePromote.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="market:promote:edit">
						<a href="${ctx}/market/mkDistributePromote/form?id=${mkDistributePromote.id}">修改</a>
						<span href="" class="group-add" style="cursor: pointer; color: #2fa4e7;">添加明细</span>
					</shiro:hasPermission>
					<shiro:hasPermission name="market:promote:delete">
						<a href="${ctx}/market/mkDistributePromote/delete?id=${mkDistributePromote.id}" onclick="return confirmx('确认要删除该会员推广吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>


	<c:forEach items="${details}" var="detail" varStatus="status">
		<div class="detail-line">
			<form action="" class="detail-form">
				<span class="form-item" style="display: none">
					<input type="hidden" name="id" value="${detail.id}">
					<input type="hidden" name="promoteId" value="${page.list[0].id}">
				</span>
				<span class="form-item">
					<label class="control-label" style="float: left; margin-top: 6px">层级：</label>
					<div class="controls" style="float: left; margin-top: 4px">
						<select class="select-form" style="width: 200px" name="level">
							<option value ="1" <c:if test="${ 1 eq detail.level}">selected</c:if> > 1</option>
							<option value ="2" <c:if test="${ 2 eq detail.level}">selected</c:if> > 2</option>
							<option value ="3" <c:if test="${ 3 eq detail.level}">selected</c:if> > 3</option>
							<option value ="4" <c:if test="${ 4 eq detail.level}">selected</c:if> > 4</option>
							<option value ="5" <c:if test="${ 5 eq detail.level}">selected</c:if> > 5</option>
							<option value ="6" <c:if test="${ 6 eq detail.level}">selected</c:if> > 6</option>
							<option value ="7" <c:if test="${ 7 eq detail.level}">selected</c:if> > 7</option>
							<option value ="8" <c:if test="${ 8 eq detail.level}">selected</c:if> > 8</option>
							<option value ="9" <c:if test="${ 9 eq detail.level}">selected</c:if> > 9</option>
							<option value ="10" <c:if test="${ 10 eq detail.level}">selected</c:if> >10</option>
						</select>
					</div>
				</span>
				<span class="form-item">
					<label class="control-label" style="float: left; margin-top: 6px">奖励数量：</label>
					<div class="controls" style="float: left; margin-top: 4px">
						<input class="input-volume" name="volume" style="padding-left: 6px"  value="${detail.volume}">
					</div>
				</span>
				<span class="form-btn">
						<a style="position: relative; top: -6px;" href="${ctx}/market/mkDistributePromote/deleteDetail?id=${detail.id}">删除本行</a>
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