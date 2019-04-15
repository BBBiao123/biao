<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>前台用户管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var lockPath = "iframe:${ctx}/plat/platUser/lockForm/";
        var lockMap = new Map();
        lockFactor = new Object();
        lockFactor.formUrl = lockPath + "2/?id=";
        lockFactor.updateUrl = "${ctx}/plat/platUser/update/lock/9";
        lockFactor.title = "锁定会员";
        lockMap.set("2", lockFactor);

        unLockFactor = new Object();
        unLockFactor.formUrl = lockPath + "2/?id=";
        unLockFactor.updateUrl = "${ctx}/plat/platUser/update/lock/0";
        unLockFactor.title = "解锁会员";
        lockMap.set("3", unLockFactor);

        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function lockPlatUser(id, type) {

            var factor = lockMap.get(type);
            var url = factor.formUrl + id;
            $.jBox.open(url, factor.title, 720, 360, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                    if (v == 1) {
                        var iframeName = h.children(0).attr("name");
                        var container = window.frames[iframeName].document;
                        var ruleObj = $('#inputForm', container).serialize();
                        var reasonStr = ruleObj.substring(ruleObj.indexOf("reason"));
                        debugger;
                        if(reasonStr.indexOf("=") >= reasonStr.length - 1){
                            alertx("原因不能为空");
                            return false;
                        }
                        var success = false;
                        $.ajax({
                            type: "POST",
                            async: false,
                            url: factor.updateUrl,
                            data: ruleObj,// 要提交的表单
                            success: function (msg) {
                                debugger;
                                var resultObj = $.parseJSON(msg);
                                $.jBox.alert(resultObj.msg);
                                if (resultObj && resultObj.success == 1) {// 保存成功
                                    success = true;
                                    $('#searchForm').submit();
                                } else { // 保存失败
                                    success = false;
                                }
                            },
                            error: function (error) {
                                $.jBox.alert("输入参数错误");
                                console.log(error.responseText); // 日志输出控制台
                                success = false;
                            }
                        });
                        return success;
                    } else {
                        return true;
                    }
                }});
            $("#jbox-content").css({"overflow-y":"hidden"});
        };

    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/plat/platUser/">前台用户列表</a></li>
    <%-- <shiro:hasPermission name="plat:platUser:edit"><li><a href="${ctx}/plat/platUser/form">前台用户添加</a></li></shiro:hasPermission> --%>
</ul>
<form:form id="searchForm" modelAttribute="platUser" action="${ctx}/plat/platUser/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>用户id：</label>
            <form:input path="id" htmlEscape="false" maxlength="45" class="input-medium"/>
        </li>
        <li><label>用户名：</label>
            <form:input path="username" htmlEscape="false" maxlength="45" class="input-medium"/>
        </li>
        <li><label>手机号：</label>
            <form:input path="mobile" htmlEscape="false" maxlength="11" class="input-medium"/>
        </li>
        <li><label>邮箱：</label>
            <form:input path="mail" htmlEscape="false" maxlength="45" class="input-medium"/>
        </li>
        <li><label>真实姓名：</label>
            <form:input path="realName" htmlEscape="false" maxlength="45" class="input-medium"/>
        </li>
        <li><label style="width: 105px;">注册开始时间：</label>
            <input name="startTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"
                   value="<fmt:formatDate value="${platUser.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
        </li>
        <li><label style="width: 105px;">注册结束时间：</label>
            <input name="endTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"
                   value="<fmt:formatDate value="${platUser.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
        </li>
        <li><label>审核状态：</label>
            <form:select path="cardStatus" class="input-medium">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('UserCardStatus')}" itemLabel="label" itemValue="value"
                              htmlEscape="false"/>
            </form:select>
        </li>
        <li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>用户id</th>
        <th>真实姓名</th>
        <th>是否锁定</th>
        <th>手机号</th>
        <th>邮箱</th>
        <th>实名状态</th>
        <th>谷歌验证</th>
        <th>性别</th>
        <th>年龄</th>
        <th>国籍</th>
        <th>注册时间</th>
        <th>最后更新时间</th>
        <th>邀请码</th>
        <th>推荐人</th>
        <th>身份证地址</th>
        <shiro:hasPermission name="plat:platUser:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="pu">
        <tr>
            <td><a href="${ctx}/plat/platUser/form?id=${pu.id}">
                    ${pu.id}
            </a></td>
            <td>
                    ${pu.realName}
            </td>
            <td>
                    ${fns:getDictLabel(pu.status, 'UserStatus', '')}
            </td>
            <td>
                    ${pu.mobile}
            </td>
            <td>
                    ${pu.mail}
            </td>
            <td>
                    ${fns:getDictLabel(pu.cardStatus, 'UserCardStatus', '')}
            </td>
            <td>
                <c:choose>
                    <c:when test="${empty pu.googleAuth}">no</c:when>
                    <c:otherwise>yes</c:otherwise>
                </c:choose>
            </td>
            <td>
                    ${fns:getDictLabel(pu.sex, 'sex', '')}
            </td>
            <td>
                    ${pu.age}
            </td>
            <td>
                <c:choose>
                	<c:when test="${empty pu.countryCode}">未知</c:when>
                    <c:when test="${pu.countryCode=='00'}">境内</c:when>
                    <c:otherwise>境外</c:otherwise>
                </c:choose>
            </td>
            <td>
                <fmt:formatDate value="${pu.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                <fmt:formatDate value="${pu.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td>
                    ${pu.inviteCode}
            </td>
            <td>
                <a href="${ctx}/plat/platUser/form?id=${pu.referId}">${pu.referInviteCode}</a>
            </td>
            <td>
                    ${pu.remarks}
            </td>
            <shiro:hasPermission name="plat:platUser:edit">
                <td>
                    <c:if test="${pu.cardStatus=='12'}">
                        <a href="${ctx}/plat/platUser/form?id=${pu.id}">审核</a>
                    </c:if>
                    <c:if test="${pu.status=='9'}">
                        <%--<a href="${ctx}/plat/platUser/lock/0?id=${pu.id}" onclick="return confirmx('确认要解锁该前台用户吗？', this.href)">解锁</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}','3')">解锁</a>
                    </c:if>
                    <c:if test="${pu.status=='0'}">
                        <a href="#" onclick="lockPlatUser('${pu.id}','2')">锁定</a>
                    </c:if>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>