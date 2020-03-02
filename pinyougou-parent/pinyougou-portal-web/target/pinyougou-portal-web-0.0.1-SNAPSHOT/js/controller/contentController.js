//广告控制层（运营商后台）
app.controller("contentController",function($scope,contentService){
	
	$scope.contentList=[];//广告列表
	$scope.findByCategoryId=function(categorId){
		contentService.findByCategoryId(categorId).success(
			function(response){
				$scope.contentList[categorId]=response;
			}	
		)
	}
	
	//搜素跳转
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}
	
})