app.controller('searchController',function($scope,$location,searchService){
	
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sortField':'','sort':''};//搜索对象
	
	//添加搜索项
	$scope.addSearchItem=function(key,value){
		if(key=='category' || key=='brand' || key=='price' ){//如果点击的是分类或者是品牌
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}	
		$scope.search();
	}

	//移除复合搜索条件
	$scope.removeSearchItem=function(key){
		if(key=="category" || key=='brand' || key=='price'){//如果点击的是分类或者是品牌
			$scope.searchMap[key]="";	
		}else {//否则是规格
			delete $scope.searchMap.spec[key];//移除此属性
		}
		$scope.search();//执行搜索 
	}
	
	//搜索
	$scope.search=function(){
		$scope.searchMap.pageNo= parseInt($scope.searchMap.pageNo) ;//在执行查询前，转换为int类型，否则提交到后端有可能变成字符串
		searchService.search( $scope.searchMap ).success(
			function(response){						
				$scope.resultMap=response;//搜索返回的结果
				
				buildPageLabel();//构建分页栏
			
			}
		);	
	}	
	
	//构建分页栏	
	buildPageLabel=function(){
		//构建分页栏
		$scope.pageLabel=[];
		var firstPage=1;//开始页码
		var lastPage=$scope.resultMap.totalPages;//截止页码
		$scope.firstDot=true;//前面有点
		$scope.lastDot=true;//后边有点		
		if($scope.resultMap.totalPages>5){  //如果页码数量大于5			
			if($scope.searchMap.pageNo<=3){//如果当前页码小于等于3 ，显示前5页
				lastPage=5;
				$scope.firstDot=false;//前面没点
			}else if( $scope.searchMap.pageNo>= $scope.resultMap.totalPages-2 ){//显示后5页
				firstPage=$scope.resultMap.totalPages-4;	
				$scope.lastDot=false;//后边没点
			}else{  //显示以当前页为中心的5页
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}			
		}else{
			$scope.firstDot=false;//前面无点
			$scope.lastDot=false;//后边无点
		}
		//构建页码
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
	}

	
	//根据页码查询
	$scope.queryByPage=function(pageNo){
		//页码验证
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return;
		}		
		$scope.searchMap.pageNo=pageNo;			
		$scope.search();
	}

	//判断当前页为第一页
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	
	//判断当前页是否未最后一页
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	
	//设置排序规则
	$scope.sortSearch=function(sortField,sort){
		$scope.searchMap.sortField=sortField;	
		$scope.searchMap.sort=sort;	
		$scope.search();
	}
	
	// 判断关键字是不是品牌
	$scope.keywordsIsBrand = function() {
		for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
			if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {// 如果包含
				return true;
			}
		}
		return false;
	}

	//加载查询字符串
	$scope.loadkeywords=function(){
		$scope.searchMap.keywords=  $location.search()['keywords'];
		$scope.search();
	}


	
	
});
