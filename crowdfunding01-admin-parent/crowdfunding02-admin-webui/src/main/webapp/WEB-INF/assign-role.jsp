<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    $(function () {

        // 给向右的按钮绑定单击响应函数
        $("#toRightBtn").click(function () {
            // 将把左边选中的角色给加入到右边去
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");

        });


        // 给向左的按钮绑定单击响应函数
        $("#toLeftBtn").click(function () {
            // 将右边选中的角色给加入到左边去
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        });


        // 给这个按钮绑定一个单击响应函数
        $("#submitBtn").click(function () {
            // 在表单提交的时候把现在在分配角色的这个框里的数据全部选中
            $("select:eq(1)>option").prop("selected","selected")

        });





    });

</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/assign.html" role="form" class="form-inline">
                        <!--隐藏域保存不会改变的adminId、pageNum、keyword，在提交时一起传给后端-->
                        <input type="hidden" value="${param.adminId}" name="adminId"/>
                        <input type="hidden" value="${param.pageNum}" name="pageNum"/>
                        <input type="hidden" value="${param.keyword}" name="keyword"/>
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button id="submitBtn" type="submit" style="width:100px;margin-top: 20px;margin-left: 230px;" class="btn btn-sm btn-success btn-block">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
