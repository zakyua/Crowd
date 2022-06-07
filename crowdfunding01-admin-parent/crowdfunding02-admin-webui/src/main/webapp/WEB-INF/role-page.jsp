<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css" />
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-role.js"></script>
<script type="text/javascript">
    $(function () {
        // 1.为分页操作提供初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        // 根据关键字,分页数据查询角色信息
        generatePage();

        // 给查询按钮绑定单击响应函数
        $("#searchBtn").click(function (){
            // 从文本框中取出关键字更新全局变量
            window.keyword=$("#keywordInput").val();
            // 在根据关键字查询到角色数据,刷新页面
            generatePage();
        });

        // 给新增按钮绑定打开模态框的单击响应函数
        $("#showAddModalBtn").click(function () {
            // 打开模态框
            $('#addModal').modal('show');
        });

        // 给新增角色的模态框的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function (){

            // 获取文本框中的信息
            // #addModal表示找到整个模态框
            // 空格表示在后代元素中继续查找
            // [name=roleName]表示匹配name属性等于roleName的元素
            // $.trim表示去除前后空格
            var roleName = $.trim($("#addModal [name=roleName]").val());

            // 发送Ajax请求去保存角色数据
            $.ajax({
                "url":"role/save.json",
                "type":"post",
                "data":{
                    "name":roleName
                },
                "dataType": "json",
                "success":function (response){
                    // 如果成功，服务器发来的数据就只有result
                    var result = response.result;
                    //
                    if(result == "SUCCESS"){
                        layer.msg("操作成功");
                        // 跳转页码的最后一页数据
                        window.pageNum = 99999999;
                        // 重新加载分页数据
                        generatePage();
                    }
                    if(result == "FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response){
                    // console.log(response)
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框
            $("#addModal [name=roleName]").val("");


        });




        // 给更新的按钮绑定单击响应函数
        // ①首先找到所有“动态生成”的元素所附着的“静态”元素
        // ②on()函数的第一个参数是事件类型
        // ③on()函数的第二个参数是找到真正要绑定事件的元素的选择器
        // ③on()函数的第三个参数是事件的响应函数
        $("#rolePageBody").on("click",".pencilBtn",function(){
            // 打开模态框
            $('#editModal').modal('show');
            // 回显数据 找到铅笔按钮的父元素，父元素的兄弟就是那个文本框,然后就可以找到文本框中的数据
            var roleName = $(this).parent().prev().text();
            // 获取id,保存为全局变量
            window.roleId= this.id;
            // 将数据显示在模态框上
            $("editModal" [name=roleName]).val(roleName);
        });

        // 给更新的模态框上的更新按钮绑定响应函数
        $("#updateRoleBtn").click(function () {
            // 从文本中获取新的名字
            var roleName = $("#editModal [name=roleName]").val();

            // 发送请求给服务器
            $.ajax({
                "url":"role/update.json",
                "type":"post",
                "data":{
                    "id":window.roleId,
                    "name":roleName
                },
                "dataType":"json",
                "success":function (response){
                    var result = response.result;
                    if(result == "SUCCESS"){
                         layer.msg("操作成功");
                         // 重新加载数据,还是在当前的页面
                        generatePage();
                    }
                    if(result == "FAILED"){
                        layer.msg("操作失败"+result.message);
                    }
                },
                "error":function (response){
                    // console.log(response)
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            // 关闭模态框
            $("#editModal").modal("hide");
        });


        // 给总的多选框summaryBox绑定事件
        $("#summaryBox").click(function(){

            // ①获取当前多选框自身的状态
            var currentStatus = this.checked;

            // ②用当前多选框的状态设置其他多选框
            $(".itemBox").prop("checked", currentStatus);

        });




        // 全选和全不选 如果所以的复选框都选择了，summaryBox的状态也必须是选中的
        $("#rolePageBody").on("click",".itemBox",function(){

            // 获取当前复选框的个数
            var totalCount = $(".itemBox").length;

            // 获取当前复选框的被选中的个数
            var checkedBoxCount = $(".itemBox:checked").length;

            // 使用二者比较的结果来决定summaryBox是否被选中
            $("#summaryBox").prop("checked",totalCount == checkedBoxCount);
        });


        // 给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 准备好需要删除的数据
            var roleArray = [];
            // 把复选框的状态是这个已选中的需要保存在数组中
            // 遍历当前的多选框
            $(".itemBox:checked").each(function () {
                // 根据Dom操作找到名称的td
                var roleName = $(this).parent().next().text();
                roleArray.push({
                    "roleId":this.id,
                    "roleName":roleName
                });
            });
            // 判断是否选中了元素
            if(roleArray.length == 0){
                  layer.msg("请至少选择一个执行删除！")
                return;
            }
            // 打开模态框
            showConfirmModal(roleArray);

        });

        // 给单条删除的按钮绑定单击响应函数
        $("#rolePageBody").on("click",".removeBtn",function(){
            // 准备好需要删除的数据
            var roleName = $(this).parent().prev().text();
            var roleArray = [{
                "roleId":this.id,
                "roleName":roleName
            }];
           // 打开模态框
            showConfirmModal(roleArray);

        });


        // 给删除模态框中的删除按钮绑定单击响应函数，访问服务器
        $("#removeRoleBtn").click(function () {
            // 准备要发送的数据
            var requestBody = JSON.stringify(window.roleArray);

            // 发送ajax请求访问服务器
            $.ajax({
                "url":"role/remove/by/role/id/array.json",
                "type":"post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8", // 告诉服务器发送的请求是JSON
                "dataType":"json",
                "success":function (response){
                    // 如果成功，服务器发来的数据就只有result
                    var result = response.result;
                    //
                    if(result == "SUCCESS"){
                        layer.msg("操作成功");
                        // 跳转页码的最后一页数据
                        // window.pageNum = 99999999;
                        // 重新加载分页数据
                        generatePage();
                    }
                    if(result == "FAILED"){
                        layer.msg("操作失败");
                    }
                },
                "error":function (response){
                    // console.log(response)
                    layer.msg(response.status+" "+response.statusText);
                }

            });

            // 关闭模态框
            $("#confirmModal").modal("hide")

            // 将summaryBox的状态设置为不勾选
            $("#summaryBox").prop("checked",false);



        });



        // 给分配权限的按钮当定单击响应函数，打开模态框
        $("#rolePageBody").on("click",".checkBtn",function () {
            // 将当前按钮的id放入全局变量
            window.roleId = this.id;
            // 打开模态框
            $("#assignModal").modal("show");
            // 加载属性结构
            fillAuthTree();
        });




        // 给分配权限的模态框中的报村按钮绑定单击响应函数
        $("#assignBtn").click(function () {

            // 收集被选中的节点
            var authIdArray = [];
            // 获取zTreeObj的对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // 获取所有选中的节点
            var checkedNodes = zTreeObj.getCheckedNodes();

            // 准备我们需要发送的数据
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode =   checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            var requestBody = {
                // 这里发给服务器的形式是这样的{authIdArray:[],roleId[]}
                "authIdArray":authIdArray,
                // 这里的这个roleId我们之后会发送一个值，但是为了我们后端方便接收，将他封装成一个数组来发送
                "roleId":[window.roleId]
            };
            requestBody = JSON.stringify(requestBody);
            // 发送数据到服务器保存
            $.ajax({
                "url":"assign/do/role/assign/auth.json",
                "type":"post",
                "data":requestBody,
                "contentType":"application/json;charset=UTF-8",
                "dataType":"json",
                "success":function (response) {
                    var result = response.result;
                    if(result == "SUCCESS") {
                        layer.msg("操作成功！");
                    }
                    if(result == "FAILED") {
                        layer.msg("操作失败！"+response.message);
                    }
                },
                "error":function (response) {
                    layer.msg(response.status+" "+response.statusText);
                }
            });

            // 关闭模态框
            $("#assignModal").modal("hide");

        });





    });
</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;" ><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 引入新增的模态框-->
<%@include file="/WEB-INF/modal-role-add.jsp" %>
<!-- 引入修改的模态框-->
<%@include file="/WEB-INF/modal-role-edit.jsp" %>
<!--引入批量删除删除的模态框-->
<%@include file="/WEB-INF/modal-role-confirm.jsp" %>
<!-- 引入分配权限的模态框-->
<%@include file="/WEB-INF/model-role-assign-auth.jsp" %>
</body>
</html>
