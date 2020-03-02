app.controller('brandController',function($scope,$controller,brandService){
    		
    		$controller("baseController",{$scope:$scope});
    		
    		//查询品牌列表
    		$scope.findAll=function(){
    			brandService.findAll().success(
    				function(response){
    					$scope.list=response;
    				}	
    			);
    		}
    		
    		
        	
        	
        	//分页
        	$scope.findPage=function(page,size){
        		brandService.findPage(page,size).success(
        				function(data){
        					$scope.list=data.rows;//显示当前页数据
        					$scope.paginationConf.totalItems=data.total;//更新总记录数
        			
        			})
        	};
        	
        	//新增和修改
        	$scope.save=function(){
        		var object=null;//方法名
        		if($scope.entity.id!=null){
        			object=brandService.update($scope.entity);
        		}else{
        			object=brandService.add($scope.entity);
        		}
        		
        		object.success(
        				function(data){
        					if(data.success){
        						$scope.reloadList();//刷新列表
        					}else{
        						alert(data.message);
        					}
        					
        				}
        		)
        	};
        	
        	//查询实体
        	$scope.findOne=function(id){
        		brandService.findOne(id).success(
        			function(data){
        				$scope.entity=data;
        			}	
        		)
        	};
        	
        	
        	
        	//删除
        	$scope.dele=function(){
        		brandService.dele($scope.selectIds).success(
        			function(data){
        				if(data.success){
        					$scope.reloadList();//刷新
        				}else{
        					alert(data.message);
        				}
        			}		
        		);
        	}
        	
        	$scope.searchEntity={};
        	//条件查询
        	$scope.search=function(page,size){
        		brandService.search(page,size,$scope.searchEntity).success(
        				function(data){
        					$scope.list=data.rows;//显示当前页数据
        					$scope.paginationConf.totalItems=data.total;//更新总记录数
        			
        			});
        	}
        	
    	});