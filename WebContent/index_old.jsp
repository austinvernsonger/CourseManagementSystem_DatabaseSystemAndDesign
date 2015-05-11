<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP AJAX Form</title>
<script src="javascript_old/jquery-2.1.1.js"></script>
<script src="javascript_old/basic.js"></script>
<link rel="stylesheet" href="css_old/basic.css" type="text/css" media="screen" />
</head>

<body>
	<p class="large">Course Prerequisite Management System</p>
	
	<form id="userLoginForm_id">
		<label for="username_id">Enter username:</label>
		<input type="text" id="username_id" name="username_name" />
		<br>
	 	<label for="password_id">Enter passowrd:</label>
		<input type="password" id="password_id" name="password_name" />
		<br> 
		<input type="submit" />
	</form>
	<p id="loginMessage_id" />
	
	<hr>
	
	<form id="registrationForm_id">
		<label for="course_id">Course to enroll: </label>
		<input type="text" id="course_id" name="course_name"/>
		<br>
		<input type="submit" />
	</form>
	<p id="registrationMessage_id" />
	
	<hr>
</body>

</html>