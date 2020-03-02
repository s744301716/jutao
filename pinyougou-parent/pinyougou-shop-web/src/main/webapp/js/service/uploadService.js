app.service('uploadService',function($http){
	
	//上传文件
	this.uploadFile=function(){
		var formdata=new FormData();
		formdata.append('file',file.files[0]); //file 文件上传框的name
		return $http({
			url:'../upload.do',
			method:'post',
			data:formdata,
			headers:{'Content-Type':undefined}, //指定上传类型
			transformRequest: angular.identity
		});
		
	}
	
	
});