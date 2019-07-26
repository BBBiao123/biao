<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>前台用户管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        var userVolumePath = "iframe:${ctx}/plat/platUser/userVolumeList?id=";
        var changeMobilePath = "iframe:${ctx}/plat/platUser/mobileForm?id=";
        var tagMobilePath = "iframe:${ctx}/plat/platUser/tagForm?id=";
        var lockDatePath = "iframe:${ctx}/plat/platUser/lockDateForm/25?id=";

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

        freeC2cInFactor = new Object();
        freeC2cInFactor.formUrl = lockPath + "4/?id=";
        freeC2cInFactor.updateUrl = "${ctx}/plat/platUser/c2cIn/1";
        freeC2cInFactor.title = "冻结转入C2C";
        lockMap.set("4", freeC2cInFactor);

        unFreeC2cInFactor = new Object();
        unFreeC2cInFactor.formUrl = lockPath + "4/?id=";
        unFreeC2cInFactor.updateUrl = "${ctx}/plat/platUser/c2cIn/0";
        unFreeC2cInFactor.title = "解冻转入C2C";
        lockMap.set("5", unFreeC2cInFactor);

        freeC2cOutFactor = new Object();
        freeC2cOutFactor.formUrl = lockPath + "6/?id=";
        freeC2cOutFactor.updateUrl = "${ctx}/plat/platUser/c2cOut/1";
        freeC2cOutFactor.title = "冻结转出C2C";
        lockMap.set("6", freeC2cOutFactor);

        unFreeC2cOutFactor = new Object();
        unFreeC2cOutFactor.formUrl = lockPath + "6/?id=";
        unFreeC2cOutFactor.updateUrl = "${ctx}/plat/platUser/c2cOut/0";
        unFreeC2cOutFactor.title = "解冻转出C2C";
        lockMap.set("7", unFreeC2cOutFactor);

        freePublishFactor = new Object();
        freePublishFactor.formUrl = lockPath + "8/?id="
        freePublishFactor.updateUrl = "${ctx}/plat/platUser/c2cPublish/1";
        freePublishFactor.title = "冻结发广告";
        lockMap.set("8", freePublishFactor);

        unFreePublishFactor = new Object();
        unFreePublishFactor.formUrl = lockPath + "8/?id=";
        unFreePublishFactor.updateUrl = "${ctx}/plat/platUser/c2cPublish/0";
        unFreePublishFactor.title = "解冻发广告";
        lockMap.set("9", unFreePublishFactor);

        freeCoinOutFactor = new Object();
        freeCoinOutFactor.formUrl = lockPath + "10/?id=";
        freeCoinOutFactor.updateUrl = "${ctx}/plat/platUser/coinOut/1";
        freeCoinOutFactor.title = "冻结提现";
        lockMap.set("10", freeCoinOutFactor);

        unFreeCoinOutFactor = new Object();
        unFreeCoinOutFactor.formUrl = lockPath + "10/?id=";
        unFreeCoinOutFactor.updateUrl = "${ctx}/plat/platUser/coinOut/0";
        unFreeCoinOutFactor.title = "解冻提现";
        lockMap.set("11", unFreeCoinOutFactor);

        freeC2cChangeFactor = new Object();
        freeC2cChangeFactor.formUrl = lockPath + "12/?id=";
        freeC2cChangeFactor.updateUrl = "${ctx}/plat/platUser/c2cChange/1";
        freeC2cChangeFactor.title = "冻结C2C转账";
        lockMap.set("12", freeC2cChangeFactor);

        unFreeC2cChangeFactor = new Object();
        unFreeC2cChangeFactor.formUrl = lockPath + "12/?id=";
        unFreeC2cChangeFactor.updateUrl = "${ctx}/plat/platUser/c2cChange/0";
        unFreeC2cChangeFactor.title = "解冻C2C转账";
        lockMap.set("13", unFreeC2cChangeFactor);

        freeC2cSwitchFactor = new Object();
        freeC2cSwitchFactor.formUrl = lockPath + "14/?id=";
        freeC2cSwitchFactor.updateUrl = "${ctx}/plat/platUser/c2cSwitch/1";
        freeC2cSwitchFactor.title = "冻结C2C交易";
        lockMap.set("14", freeC2cSwitchFactor);

        unFreeC2cSwitchFactor = new Object();
        unFreeC2cSwitchFactor.formUrl = lockPath + "14/?id=";
        unFreeC2cSwitchFactor.updateUrl = "${ctx}/plat/platUser/c2cSwitch/0";
        unFreeC2cSwitchFactor.title = "解冻C2C交易";
        lockMap.set("15", unFreeC2cSwitchFactor);

        freeTradeSwitchFactor = new Object();
        freeTradeSwitchFactor.formUrl = lockPath + "24/?id=";
        freeTradeSwitchFactor.updateUrl = "${ctx}/plat/platUser/tradeSwitch/1";
        freeTradeSwitchFactor.title = "冻结币币交易";
        lockMap.set("24", freeTradeSwitchFactor);

        unFreeTradeSwitchFactor = new Object();
        unFreeTradeSwitchFactor.formUrl = lockPath + "24/?id=";
        unFreeTradeSwitchFactor.updateUrl = "${ctx}/plat/platUser/tradeSwitch/0";
        unFreeTradeSwitchFactor.title = "解冻币币交易";
        lockMap.set("25", unFreeTradeSwitchFactor);


        $(document).ready(function () {

            $("body").on("click",".button", function(){
                var userId = $(this).attr('id');
                $.jBox.open("iframe:${ctx}/plat/jsPlatUserOplog/list?userId="+userId, "操作日志", 1200, 500, { buttons: { '确定': 1}, submit: function (v, h, f){
                        return true;
                    }});
                $("#jbox-content").css({"overflow-y":"hidden"});
            });
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function showUserVolumePath(id) {
            var url = userVolumePath + id;
            $.jBox.open(url, "用户资产信息", 800, 400, { buttons: { '关闭': 0}});
        };

        function lockDate(id,value) {

            var url = lockDatePath + id;

            var title = "锁定资产";
            if('0' == value){
                title = "锁定资产";
            }
            $.jBox.open(url, title, 720, 360, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
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
                            url: "${ctx}/plat/platUser/lockDate/" + value,
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

        function changeMobile(id) {

            var url = changeMobilePath + id;
            $.jBox.open(url, "修改手机", 720, 360, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
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
                            url: "${ctx}/plat/platUser/update/changeMobile",
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

        function changeTag(id) {

            var url = tagMobilePath + id;
            $.jBox.open(url, "修改用户标识", 600, 400, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
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
                            url: "${ctx}/plat/platUser/update/changeTag",
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

        function lockPlatUser(id, type) {

            var factor = lockMap.get(type);
            var url = factor.formUrl + id;
            $.jBox.open(url, factor.title, 720, 360, { buttons: { '关闭': 0, '确定': 1}, submit: function (v, h, f){
                    if (v == 1) {
                        var iframeName = h.children(0).attr("name");
                        var container = window.frames[iframeName].document;
                        var ruleObj = $('#inputForm', container).serialize();
                        var reasonStr = ruleObj.substring(ruleObj.indexOf("reason"));
                        var reasonContent = reasonStr.substring(reasonStr.indexOf("="));
                        debugger;
                        if($.trim(reasonContent) == '='){
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
<form:form id="searchForm" modelAttribute="platUser" action="${ctx}/plat/platUser/manList" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label>用户id：</label>
            <form:input path="id" htmlEscape="false" maxlength="45" class="input-medium"/>
        </li>
        <li><label>推荐人id：</label>
            <form:input path="referId" htmlEscape="false" maxlength="45" class="input-medium"/>
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
            <input name="platUser.startTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"
                   value="<fmt:formatDate value="${platUser.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
        </li>
        <li><label style="width: 105px;">注册结束时间：</label>
            <input name="platUser.endTime" type="text" readonly="readonly" maxlength="45" class="input-medium Wdate"
                   value="<fmt:formatDate value="${platUser.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
        <th>推荐人id</th>
        <th>真实姓名</th>
        <th>是否锁定</th>
        <shiro:hasPermission name="plat:platUser:keyinfo">
            <th>会员类型</th>
        </shiro:hasPermission>
        <th>手机号</th>
        <th>邮箱</th>
        <th>实名状态</th>
        <th>解锁时间</th>
        <shiro:hasPermission name="plat:platUser:c2cIn">
            <th>转入C2C</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:c2cOut">
            <th>转出C2C</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:c2cPublish">
            <th>发广告</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:coinOut">
            <th>提现</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:c2cChange">
            <th>C2C转账</th>
        </shiro:hasPermission>
        <%--<shiro:hasPermission name="plat:platUser:c2cSwitch">--%>
            <%--<th>C2C交易</th>--%>
        <%--</shiro:hasPermission>--%>
        <%--<shiro:hasPermission name="plat:platUser:tradeSwitch">--%>
            <%--<th>币币交易</th>--%>
        <%--</shiro:hasPermission>--%>
        <shiro:hasPermission name="plat:platUser:edit">
            <th>会员登录</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:lockDate">
            <th>资产锁定</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:keyinfo">
            <th>关键信息</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:platUser:operate">
            <th >操作</th>
        </shiro:hasPermission>
        <shiro:hasPermission name="plat:jsPlatUserOplog:view">
            <th>操作日志</th>
        </shiro:hasPermission>


    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="pu">
        <tr>
            <td>
                <a href="#" onclick="showUserVolumePath('${pu.id}')">
                        ${pu.id}
                </a>
            </td>
            <td>
                    ${pu.referId}
            </td>
            <td>
                    ${pu.realName}
            </td>
            <td>
                ${fns:getDictLabel(pu.status, 'UserStatus', '')}
            </td>
            <shiro:hasPermission name="plat:platUser:keyinfo">
                <td>
                    ${fns:getDictLabel(pu.tag, 'sourceUser', '')}
                </td>
            </shiro:hasPermission>
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
                <fmt:formatDate value="${pu.lockDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <shiro:hasPermission name="plat:platUser:c2cIn">
                <td>
                    <c:if test="${pu.c2cIn=='0' || pu.c2cIn==null  || pu.c2cIn=='' || pu.c2cIn=='null' }">
                        <%--<a href="${ctx}/plat/platUser/c2cIn/1?id=${pu.id}"onclick="return confirmx('确认要冻结该前台用户C2C转入吗？', this.href)">冻结</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '4')">冻结</a>
                    </c:if>
                    <c:if test="${pu.c2cIn=='1'}">
                        <%--<a href="${ctx}/plat/platUser/c2cIn/0?id=${pu.id}" onclick="return confirmx('确认要解冻该前台用户C2C转入吗？', this.href)">解冻</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '5')">解冻</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:platUser:c2cOut">
                <td>
                    <c:if test="${pu.c2cOut=='0' || pu.c2cOut==null  || pu.c2cOut=='' || pu.c2cOut=='null' }">
                        <%--<a href="${ctx}/plat/platUser/c2cOut/1?id=${pu.id}" onclick="return confirmx('确认要冻结该前台用户C2C转出吗？', this.href)">冻结</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '6')">冻结</a>
                    </c:if>
                    <c:if test="${pu.c2cOut=='1'}">
                        <%--<a href="${ctx}/plat/platUser/c2cOut/0?id=${pu.id}" onclick="return confirmx('确认要解冻该前台用户C2C转出吗？', this.href)">解冻</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '7')">解冻</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:platUser:c2cPublish">
                <td>
                    <c:if test="${pu.c2cPublish=='0' || pu.c2cPublish==null  || pu.c2cPublish=='' || pu.c2cPublish=='null' }">
                        <%--<a href="${ctx}/plat/platUser/c2cPublish/1?id=${pu.id}" onclick="return confirmx('确认要冻结该前台用户c2cPublish吗？', this.href)">冻结</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '8')">冻结</a>
                    </c:if>
                    <c:if test="${pu.c2cPublish=='1'}">
                        <%--<a href="${ctx}/plat/platUser/c2cPublish/0?id=${pu.id}" onclick="return confirmx('确认要解冻该前台用户c2cPublish吗？', this.href)">解冻</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '9')">解冻</a>
                    </c:if>
                </td>
            </shiro:hasPermission>


            <shiro:hasPermission name="plat:platUser:coinOut">
                <td>
                    <c:if test="${pu.coinOut=='0' || pu.coinOut==null  || pu.coinOut=='' || pu.coinOut=='null' }">
                        <%--<a href="${ctx}/plat/platUser/coinOut/1?id=${pu.id}" onclick="return confirmx('确认要禁止前台用户提现吗？', this.href)">禁止</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '10')">冻结</a>
                    </c:if>
                    <c:if test="${pu.coinOut=='1'}">
                        <%--<a href="${ctx}/plat/platUser/coinOut/0?id=${pu.id}" onclick="return confirmx('确认要解禁前台用户提现吗？？', this.href)">解禁</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '11')">解冻</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:platUser:c2cChange">
                <td>
                    <c:if test="${pu.c2cChange=='0' || pu.c2cChange==null  || pu.c2cChange=='' || pu.c2cChange=='null' }">
                    <%--<a href="${ctx}/plat/platUser/c2cChange/1?id=${pu.id}" onclick="return confirmx('确认要禁用该前台用户C2C转账吗？', this.href)"> 禁用转账 </a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '12')">冻结</a>
                    </c:if>
                    <c:if test="${pu.c2cChange=='1'}">
                    <%--<a href="${ctx}/plat/platUser/c2cChange/0?id=${pu.id}" onclick="return confirmx('确认要恢复该前台用户C2C转账吗？', this.href)">恢复转账</a>--%>
                        <a href="#" onclick="lockPlatUser('${pu.id}', '13')">解冻</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <%--<shiro:hasPermission name="plat:platUser:c2cSwitch">--%>
                <%--<td>--%>
                    <%--<c:if test="${pu.c2cSwitch=='0' || pu.c2cSwitch==null  || pu.c2cSwitch=='' || pu.c2cSwitch=='null' }">--%>
                        <%--<a href="#" onclick="lockPlatUser('${pu.id}', '14')">冻结</a>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${pu.c2cSwitch=='1'}">--%>
                        <%--<a href="#" onclick="lockPlatUser('${pu.id}', '15')">解冻</a>--%>
                    <%--</c:if>--%>
                <%--</td>--%>
            <%--</shiro:hasPermission>--%>
            <%--<shiro:hasPermission name="plat:platUser:tradeSwitch">--%>
                <%--<td>--%>
                    <%--<c:if test="${pu.tradeSwitch=='0' || pu.tradeSwitch==null  || pu.tradeSwitch=='' || pu.tradeSwitch=='null' }">--%>
                        <%--<a href="#" onclick="lockPlatUser('${pu.id}', '24')">冻结</a>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${pu.tradeSwitch=='1'}">--%>
                        <%--<a href="#" onclick="lockPlatUser('${pu.id}', '25')">解冻</a>--%>
                    <%--</c:if>--%>
                <%--</td>--%>
            <%--</shiro:hasPermission>--%>

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

            <shiro:hasPermission name="plat:platUser:lockDate">
                <td>
                    <c:if test="${pu.isLockTrade=='0' || pu.isLockTrade==null  || pu.isLockTrade=='' || pu.isLockTrade=='null' }">
                        <a href="#" onclick="lockDate('${pu.id}', '0')">锁定</a>
                    </c:if>
                    <c:if test="${pu.isLockTrade=='1'}">
                        <a href="#" onclick="lockDate('${pu.id}', '1')">锁定</a>
                    </c:if>
                </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:platUser:keyinfo">
            <td>
                <a href="#" onclick="changeMobile('${pu.id}')">修改手机号&nbsp;<font color="black"></font></a>
            <c:if test="${empty pu.tag}">
                <a href="#" onclick="changeTag('${pu.id}')">修改用户标识&nbsp;<font color="black"></font></a>
            </c:if>
            </td>
            </shiro:hasPermission>

            <shiro:hasPermission name="plat:platUser:operate">
                <td >
                    <c:if test="${not empty pu.mobile && not empty pu.mail}">
                        <a href="${ctx}/plat/platUser/update/cleanMail?id=${pu.id}"
                           onclick="return confirmx('确认要清空用户邮箱吗？', this.href)">清空邮箱&nbsp;<font color="black">|</font></a>
                    </c:if>
                    <c:if test="${not empty pu.googleAuth}">
                        <a href="${ctx}/plat/platUser/update/google?id=${pu.id}"
                           onclick="return confirmx('确认要清空用户谷歌验证吗？', this.href)">清空谷歌&nbsp;<font color="black">|</font></a>
                    </c:if>
                    <a href="${ctx}/plat/platUser/update/clearPass?id=${pu.id}"
                       onclick="return confirmx('确认要清空用户登录次数吗？', this.href)">清空登录次数&nbsp;<font color="black">|</font></a>
                    <a href="${ctx}/plat/platUser/update/clearExPass?id=${pu.id}"
                       onclick="return confirmx('确认要清空用户交易次数吗？', this.href)">清空交易次数&nbsp;<font color="black">|</font></a>
                    <a href="${ctx}/plat/platUser/update/clearCardCheckTime?id=${pu.id}"
                       onclick="return confirmx('确认要清空用户身份验证次数吗？', this.href)">清空身份验证次数&nbsp;<font color="black">|</font></a>
                    <a href="${ctx}/plat/platUser/update/cleanOfflineCancel?id=${pu.id}"
                       onclick="return confirmx('确认要清空发广告次数吗？', this.href)">清空发广告次数</a>
                </td>
            </shiro:hasPermission>
            <shiro:hasPermission name="plat:jsPlatUserOplog:view">
                <td>
                    <a href="#" class="button" id="${pu.id}"> 查看</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>