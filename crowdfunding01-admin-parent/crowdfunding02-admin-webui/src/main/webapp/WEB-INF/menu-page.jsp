<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-menu.js"></script>
<script type="text/javascript">
   $(function () {

       // 加载树结构
       generateTree();


       $("#treeDemo").on("click",".addBtn",function(){

           // 将当前节点的id，作为新节点的pid保存到全局变量
           window.pid = this.id;

           // 打开模态框
           $("#menuAddModal").modal("show");

           return false;
       });

       // 给添加子节点的模态框的绑定单击响应函数
       $("#menuSaveBtn").click(function () {
           // 收集表单上的数据
           var name = $.trim($("#menuAddModal [name=name]").val());
           var url = $.trim($("#menuAddModal [name=url]").val());
           // 单选按钮要定位到“被选中”的那一个
           var icon = $.trim($("#menuAddModal [name=icon]:checked").val());

           // 发送ajax请求
           $.ajax({
               "url":"menu/save.json",
               "type":"post",
               "data":{
                   "pid":window.pid,                  // 添加的这个元素的父节点是谁要指明
                   "name":name,
                   "url":url,
                   "icon":icon
               },
               "dataType":"json",
               "success":function (response) {
                   var result = response.result;
                   if(result == "SUCCESS"){
                       layer.msg("操作成功！");
                       // 重新加载树结构
                       generateTree();
                   }
                   if(result == "FAILED") {
                       layer.msg("操作失败！"+response.message);
                   }
               },
               "error":function(response){
                   layer.msg(response.status+" "+response.statusText);
               }
           });
           // 关闭模态框
           $("#menuAddModal").modal("hide");

           // 清楚模态框中数据
           $("#menuAddModal").click();
       });


       // 给更新字节点绑定单击响应函数
       $("#treeDemo").on("click",".editBtn",function () {
           // 打开模态框
           $("#menuEditModal").modal("show");

           // alert(this.id);
           window.id =  this.id;

           //在模态框回显数据
           // 1.获取zTreeObj对象
           var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
           // 2.调用zTreeObj的getNodeByParam(key,value)
           //  2.1准备key，和value
           var key = "id";
           var value = window.id ;
           var currentNode = zTreeObj.getNodeByParam(key,value);

           // 回显数据
           $("#menuEditModal [name=name]").val(currentNode.name);
           $("#menuEditModal [name=url]").val(currentNode.url);

           // 回显radio可以这样理解：被选中的radio的value属性可以组成一个数组，
           // 然后再用这个数组设置回radio，就能够把对应的值选中
           $("#menuEditModal [name=icon]").val([currentNode.icon]);

           return false;
       });

       // 给更新字节点的模态框的更新按钮绑定单击响应函数
       $("#menuEditModal").click(function () {

           // 收集模态框的数据
           // 收集表单数据
           var name = $("#menuEditModal [name=name]").val();
           var url = $("#menuEditModal [name=url]").val();
           var icon = $("#menuEditModal [name=icon]:checked").val();

           // 发送ajax请求
           $.ajax({
               "url":"menu/update.json",
               "type":"post",
               "data":{
                   "id":window.id,
                   "name":name,
                   "url":url,
                   "icon":icon
               },
               "dataType":"json",
               "success":function (response) {
                   var result = response.result;
                   if(result == "SUCCESS"){
                       layer.msg("操作成功！");
                       // 重新加载树结构
                       generateTree();
                   }
                   if(result == "FAILED") {
                       layer.msg("操作失败！"+response.message);
                   }
               },
               "error":function(response){
                   layer.msg(response.status+" "+response.statusText);
               }
           });

           // 关闭模态框
           $("#menuAddModal").modal("hide");
       });



       // 给删除按钮绑定单击响应函数
       $("#treeDemo").on("click",".removeBtn",function () {

           // 准备发发送的数据
           window.id = this.id;
           // 打开模态框
           $("#menuConfirmModal").modal("show");
           // 回显数据
           var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
           // 根据id来回显数据
           var key = "id"
           var value = window.id;
           // 获取到当前的节点
           var nodeByParam = zTreeObj.getNodeByParam(key,value);
           $("#removeNodeSpan").html("【<i class='"+nodeByParam.icon+"'></i>"+nodeByParam.name+"】")

           return false;

       });

       // 给删除模态框上的ok按钮绑定响应函数
       $("#confirmBtn").click(function (){

           // 发送ajax请求
           $.ajax({
               "url":"menu/remove.json",
               "type":"post",
               "data":{
                   "id":window.id
               },
               "dataType":"json",
               "success":function(response){
                   var result = response.result;

                   if(result == "SUCCESS") {
                       layer.msg("操作成功！");

                       // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                       // 否则有可能刷新不到最新的数据，因为这里是异步的
                       generateTree();
                   }

                   if(result == "FAILED") {
                       layer.msg("操作失败！"+response.message);
                   }
               },
               "error":function(response){
                   layer.msg(response.status+" "+response.statusText);
               }
           });

           // 关闭模态框
           $("#menuConfirmModal").modal("hide");
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
                    <i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float: right; cursor: pointer;" data-toggle="modal"
                         data-target="#myModal">
                        <i class="glyphicon glyphicon-question-sign"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <!-- 这个ul标签是zTree动态生成的节点所依附的静态节点 -->
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 引入添加的模态框-->
<%@include file="/WEB-INF/modal-menu-add.jsp" %>
<!-- 引入删除的模态框-->
<%@include file="/WEB-INF/modal-menu-confirm.jsp" %>
<!-- 引入更新的模态框-->
<%@include file="/WEB-INF/modal-menu-edit.jsp" %>
</body>
</html>
