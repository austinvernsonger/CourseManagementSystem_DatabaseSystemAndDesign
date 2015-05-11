/**
 * JQuery Javascript Library - http://jquery.com/download/
 */

$(document).ready(function(){
	$("#userLoginForm_id").submit(function(){
		$.ajax({
			url:"StudentLogin",
			type:"POST",
			dataType:"json",
			data: $("#userLoginForm_id").serialize(), 
			success: function(data){
				$("#loginMessage_id").html("Login Message: " + data.message);
				$(displayName).slideDown(500);
			}
		})
		return false;
	});
	
	$("#registrationForm_id").submit(function(){
		$.ajax({
			url:"StudentRegister",
			type:"POST",
			dataType:"json",
			//data: "username_name="+$('#username_id').val()+"&course_name="+$(course_id).val(),
			//data: "course_name="+$(course_id).val()
			data:$("#registrationForm_id").serialize(),
			success: function(data){
				$("#registrationMessage_id").html("Registration Message: " + data.message);
				$(displayName).slideDown(500);
			}
		})
		return false;
	});
});