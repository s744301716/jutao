app.controller("baseController", function($scope) {
	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1, // 当前页
		totalItems : 10, // 总记录数
		itemsPerPage : 10, // 每页记录数
		perPageOptions : [ 10, 20, 30, 40, 50 ], // 分页选项
		// 当页码变更后自动触发的方法
		onChange : function() {
			$scope.reloadList();// 刷新列表
		}
	};

	// 刷新列表
	$scope.reloadList = function() {
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}

	$scope.selectIds = []; // 用户勾选的ID集合
	// 用户勾选复选框
	$scope.updateSelection = function($event, id) {
		if ($event.target.checked) {
			$scope.selectIds.push(id); // push向集合添加元素
		} else {
			var index = $scope.selectIds.indexOf(id);// 查找值的位置
			$scope.selectIds.splice(index, 1); // 参数1：移除的位置；参数2：移除的个数
		}

	}
	
	//提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.jsonToString=function(jsonString,key){
		var json= JSON.parse(jsonString);
		var value="";
		for(var i=0;i<json.length;i++){
			if(i>0){
				value+=", ";
			}
			value+=json[i][key];
		}
		
		return value;
	}

	
	
	// 全选复选框
	// 全选和全不全
	/*$scope.box1 = function() {
		var aa = $scope.checkAll;
		alert($scope.entity.length);
		if (aa == true) {
			for (var i = 0; i < $scope.entity.length; i++) {
				
				$scope.entity[i].ckx = true;
			}
		} else {
			for (var i = 0; i < $scope.entity.length; i++) {
				$scope.entity[i].ckx = false;
			}
		}
	}*/
	
	//在list集合中根据某key的值查询对象
	$scope.searchObjectByKey=function(list,key,keyValue){
		for(var i=0;i<list.length;i++){
			if(list[i][key]==keyValue){
				return list[i];
			}
		}
		return null;
	}
	

})