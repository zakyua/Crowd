// 加载分配权限的树结构
function fillAuthTree() {

	// 发送ajax请求去服务器将所有权限的信息查询出来然后在树形结构上展示
	var ajaxReturn = $.ajax({
		"url":"assgin/get/all/auth.json",
		"type":"post",
		"dataType":"json",
		"async":false
	});


	//
	if(ajaxReturn.status != 200){
		layer.msg("请求出错！响应状态码是："+ajaxReturn.status+"说明是："+ajaxReturn.statusText)
		return;
	}
	// 送响应结果中获取的json数据
	var authList = ajaxReturn.responseJSON.data;

	// 准备对zTree进行设置
	var setting ={
		"data":{
			"simpleData":{
				// 开启简单的json功能
				"enable":true,
				// 使用categoryId来表示父子结点的关系
				"pIdKey":"categoryId"

			},
			"key":{
				// 使用title属性显示节点名称，不用默认的name
				"name":"title"
			}
		},
		"check":{
			"enable":true,
		}
	};

	// 生成树结构  <ul id="authTreeDemo" class="ztree"></ul>
	$.fn.zTree.init($("#authTreeDemo"), setting, authList);

	// 获取zTreeOBJ的对象
	var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

	// 调用zTreeObj的方法，展开节点
	zTreeObj.expandAll(true);

	// 查询当前角色已经分配的Auth的id组成的数组(就是当前角色已经拥有的权限)
	ajaxReturn = $.ajax({
		"url":"assign/get/assigned/auth/id/by/role/id.json",
		"type":"post",
		"data":{
			"roleId":window.roleId
		},
		"dataType":"json",
		"async":false
	});

	if(ajaxReturn.status != 200){
		layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： "+ajaxReturn.status+" 说 明 是 ："+ajaxReturn.statusText);
		return ;
	}
	
	// 从响应体中获取到这个角色已经分配的权限的全部信息
	var authIdArray = ajaxReturn.responseJSON.data;

	// 根据这个authIdArray去将属性结构上的对应的节点给选上
	// 遍历authIdArray
	for (var i = 0; i < authIdArray.length; i++) {
		var authId = authIdArray[i];

		// 根据这个id就可以查询到对应的节点
		var treeNode = zTreeObj.getNodeByParam("id",authId);
        // 将这个节点勾中

		// checked 设置为 true 表示节点勾选
		var checked = true;
        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
		var checkTypeFlag = false;
		zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
	}
}








// 打开删除的模态框
function showConfirmModal(roleArray) {
	// 打开模态框
	// 打开模态框
	$("#confirmModal").modal("show");
	// 清楚roleNameDiv中的数据
	$("#roleNameDiv").empty();

	// 将来这个数据是需要发给服务器的，所以准备一个全局变量来保存数组
	window.roleArray =[];
	// 模态框中需要回显数据
	for (var i = 0; i < roleArray.length; i++) {
		// 取出数组中的数据
		var role = roleArray[i];

		//取出role的数据
		var roleId = role.roleId;
		var roleName = role.roleName;

		// 将数据追加到模态框的roleNameDiv
		$("#roleNameDiv").append(roleName+"</br>");

		// 将数据保存到全局的roleArray,这个数据将来给服务器只需要带上id就可以了，服务器根据id进行删除角色
		window.roleArray.push(roleId);

	}



}

// 执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
	
	// 1.获取分页数据
	var pageInfo = getPageInfoRemote();
	
	// 2.填充表格
	fillTableBody(pageInfo);
	
}


// 填充表格
function fillTableBody(pageInfo){

	// 清除tbody中的旧的内容
	$("#rolePageBody").empty();
	// 这里清空的逻辑是,现在这里还没有生成导航条，如果查询到的pageInfo有误就不会在生成导航条了(因为已经清除了)
	$("#Pagination").empty();

	// 判断pageInfo是否有误
	if(pageInfo == null || pageInfo ==undefined || pageInfo.list.length == 0){
		$("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉，没有您需要的数据!</td>></tr>>");
          return;
	}

	// 如果pageInfo是正确的,就是用list来填充tbody
	for (var i = 0; i < pageInfo.list.length; i++) {
		// 取出role
		var role = pageInfo.list[i];
		// 取出role的数据
		var roleID = role.id;
		var roleName = role.name;

        // 显示序号的td标签   <td>1</td>
		var numberTd = "<td>"+(i+1)+"</td>";
		// 显示复选框的标签      <td><input type="checkbox"></td>
		var checkbox = "<td><input id='"+roleID+"' class='itemBox' type='checkbox'></td>";

		// 显示角色的名称       <td>PM - 项目经理</td>
		var roleNameTd = "<td>"+roleName+"</td>";

		// 显示的3个按钮
		/*
		 <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
         <button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
          <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
		 */
		// 页面操作中的check按钮
		var checkBtn = "<button id='"+roleID+"' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
		// 页面操作中的铅笔按钮  利用id给铅笔按钮绑定单机响应函数，
		var pencilBtn = "<button id='"+roleID+"' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
		// 页面操作中的删除按钮
		var removeBtn = "<button id='"+roleID+"' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";

		// 将三个按钮放入td中
		var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+" "+"</td>>"

		// 将td填充到tr中
		var tr = "<tr>"+numberTd+checkbox+roleNameTd+buttonTd+"</tr>>";

		// 将tr填充到tbody中
		$("#rolePageBody").append(tr);

		// 生成分页导航条
		generateNavigator(pageInfo);
	}

}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {

	// 调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
	var ajaxResult = $.ajax({
		"url": "role/get/page/info.json",
		"type":"post",
		"data": {
			"pageNum": window.pageNum,
			"pageSize": window.pageSize,
			"keyword": window.keyword
		},
		"async":false,
		"dataType":"json"
	});

	console.log(ajaxResult);

	// 判断当前响应状态码是否为200
	var statusCode = ajaxResult.status;

	// 如果当前响应状态码不是200，说明发生了错误或其他意外情况，显示提示消息，让当前函数停止执行
	if(statusCode != 200) {
		layer.msg("失败！响应状态码="+statusCode+" 说明信息="+ajaxResult.statusText);
		return null;
	}
	// 如果响应状态码是200，请求成功 (这是浏览器方面的)
	var resultEntity = ajaxResult.responseJSON;
	// 获取响应的状态 (这个响应的状态是从服务器发来的)
	var result = resultEntity.result;
	// 判断服务器是否成功返回数据
	if(result == "FAILED"){
		layer.msg(resultEntity.message);
		return null;
	}
	// 接收服务器发来的数据 (请求服务器成功后，服务器会把pageInfo放在data中)
	var pageInfo = resultEntity.data;
	// 返回数据
	return pageInfo;
}
// 生成分页页码导航条
function generateNavigator(pageInfo) {
	
	// 获取总记录数
	var totalRecord = pageInfo.total;
	
	// 声明相关属性
	var properties = {
		"num_edge_entries": 3,
		"num_display_entries": 5,
		"callback": paginationCallBack,
		"items_per_page": pageInfo.pageSize,
		"current_page": pageInfo.pageNum - 1,
		"prev_text": "上一页",
		"next_text": "下一页"
	}
	
	// 调用pagination()函数
	$("#Pagination").pagination(totalRecord, properties);
}
// 翻页时的回调函数
function paginationCallBack(pageIndex, jQuery) {
	
	// 修改window对象的pageNum属性
	window.pageNum = pageIndex + 1;
	
	// 调用分页函数
	generatePage();
	
	// 取消页码超链接的默认行为
	return false;
	
}