// 加载树结构的函数
function generateTree() {
    // 发送ajax请求请求ztree
    $.ajax({
        "url":"menu/get/whole/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function (response) {
            // 从服务器获取到响应的状态
            var result = response.result;
            // 判断数据状态
            if(result == "SUCCESS"){
                var setting = {
                  "view":{
                      // 用于在节点上固定显示用户自定义控件
                      "addDiyDom":myAddDiyDom,
                      // 用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
                      "addHoverDom":myAddHoverDom,

                      "removeHoverDom":myRemoveHoverDom
                  },
                    "data":{
                      "key":{
                          // zTree 节点数据保存节点链接的目标 URL 的属性名称。
                          // 特殊用途：当后台数据只能生成 url 属性，又不想实现点击节点跳转的功能时，
                          // 可以直接修改此属性为其他不存在的属性名称
                          url: "maomi"
                      }
                    }
                };
                // 从服务器获取json数据
                var zNodes = response.data;

                // 初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }

            if(result == "FAILED") {
                layer.msg(response.message);
            }
        }
    });



}


// 设置自定义的组件
function myAddDiyDom(treeId, treeNode) {
    //  treeId是整个树形结构附着的ul标签的id
    // console.log("treeId="+treeId);
    //  当前树形节点的全部的数据，包括从后端查询得到的Menu对象的全部属性
    // console.log("treeNode="+treeNode);
    // 定位到当前的spend
    var spanId = treeNode.tId + "_ico"
    // 删除组件之前的样式,添加新的样式
    $("#"+spanId).removeClass().addClass(treeNode.icon);


}

// 用于当鼠标移动到节点上时，显示用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
function myAddHoverDom(treeId, treeNode) {
     // 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>
    // 按钮组出现的位置：节点中treeDemo_n_a超链接的后面
    // 为了在需要移除按钮组的时候能够精确定位到按钮组所在span，需要给span设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";

    // 判断一下当前的超链接是不是添加已经添加了按钮组
    if($("#"+btnGroupId).length > 0){
        return;
    }


    // 准备各个按钮的HTML标签(这个id是从数据库取出来的id)
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 将准备好的按钮给连接在一起
    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";
    // 获取当前节点的级别
    var level = treeNode.level;
    // 如果是根节点
    if(level == 0){
        // 只添加增加的按钮(中能添加子节点)
        btnHTML = addBtn;
    }
    // 如果是中间节点
    if(level == 1){
        // 可以添加，可以删除，可以更新
        // 级别为1时是分支节点，可以添加子节点、修改
        btnHTML = addBtn + " " + editBtn;
        // 删除需要判断当前节点是不是有子节点,没有子节点可以删除
        var length = treeNode.children.length;
        if(length ==0){
        btnHTML =  addBtn + " " + editBtn + " " + removeBtn;
        }
    }
    // 如果是叶子节点，可以删除,修改
    if(level == 2){
        btnHTML = editBtn + " " + removeBtn;
    }
    // 所有的按钮凭借完成后，添加在名字为名字为 treeDemo_n_a的超链接的后面
    var anchorId = treeNode.tId + "_a";
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");
}


// 在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode) {

    // 移除按钮组
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 移除对应的元素
    $("#"+btnGroupId).remove();

}