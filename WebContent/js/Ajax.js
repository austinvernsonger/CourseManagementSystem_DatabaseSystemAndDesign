/**
 * JQuery Javascript Library - http://jquery.com/download/
 */

$(document).ready(function(){
	$("#loginForm").submit(function(){
        var name = $("input#username").val();
        var password = $("input#password").val();
		
		$.ajax({
			url:"StudentLogin",
			type:"POST",
			dataType:"json",
			data: {
				username_name: name,
				password_name: password
			},
			success: function(data){

				
				if(data.message.match("LoginSuccess")){
					window.location.href = "main.html";
			
				}else{
					$("#serverMessage").html("Login Message: " + data.message + " for --- user: " + data.username);
					$(displayName).slideDown(500);
				}
				 
			}
		})
		return false;
	});
	
	
	$("#logout").click(function(){ 
		$.ajax({
		       url: "StudentLogout",
		       type: "POST",//type of posting the data
		       dataType:"json",
		       data: {
		    	   "dummyKey": "dummyValue"
		       },
		       success: function (data) {
		         //what to do in success
		    	   window.location.href = "index.html";
		       }
		  })
		  
		  return false;
	});
	
	$("#registrationForm_id").submit(function(){
		var courseID = $("input#courseCode").val();
		$.ajax({
			url:"StudentRegister",
			type:"POST",
			dataType:"json",
			//data:$("#registrationForm_id").serialize(),
			data: {
				course_name: courseID
			},
			success: function(data){
				$("#registrationMessage_id").html("Registration Message: " + data.message);
				$(displayName).slideDown(500);
			}
		})
		return false;
	});
});