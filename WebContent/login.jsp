<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Inscription (Sharing Notes)</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Sharing Notes, Signup Students, Signup Teachers, Signup Institutes, Creating new Networks" />

<link rel="stylesheet" href="styles/login.37710003883c7dee1399.css" media="all">

<!--Google Fonts-->
<link href='//fonts.googleapis.com/css?family=Carrois+Gothic' rel='stylesheet' type='text/css'>
<link href='//fonts.googleapis.com/css?family=Work+Sans:400,500,600' rel='stylesheet' type='text/css'>

<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>

<!--static chart-->
<style type="text/css">
body {
	background: url(images/back.jpg) no-repeat center center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}
</style>

<%
	if (session.getAttribute("uid") != null) {
		if (Clear.validateSession(session.getAttribute("uid")))
			response.sendRedirect("multisignup.jsp");
	}else{
		Cookie[] cookies = request.getCookies();
		String email = "", date = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("email")) {
					email = cookie.getValue();
				}
				if (cookie.getName().equals("date")) {
					date = cookie.getValue();
				}
			}

			if (!email.isEmpty() && !date.isEmpty())
				response.sendRedirect("getVerification?email=" + email + "&date=" + date);
		}
	}
%>

</head>
<body>
	<div class="login-page">
		<div class="login-main">
			<div class="login-head">
				<h1>Login</h1>
			</div>
			<div class="login-block">
				<form action="getLogin" method="post">
					<span id="confirmMessage" class="confirmMessage"></span> <input
						type="text" name="email" id="email_id" placeholder="Email"
						onkeyup="email_function(); return false;"> <input
						type="password" name="password" class="lock"
						placeholder="Password">
					<div class="forgot-top-grids">
						<div class="forgot-grid">
							<ul>
								<li><input type="checkbox" id="brand1" name="rememberMe"
									value="true"> <label for="brand1"><span></span>Remember
										me</label></li>
							</ul>
						</div>
						<div class="forgot">
							<a href="forgotpassword.jsp">Forgot password?</a>
						</div>
						<div class="clearfix"></div>
					</div>
					<input type="submit" name="Sign In" value="Login">
					<h3>
						Not a member?<a href="signup.jsp" style="color: #68ae00;">
							Sign up now</a>
					</h3>


				</form>

			</div>
		</div>
	</div>
	<!--inner block end here-->
	<!--copy rights start here-->
	<div class="copyrights">
		<p>
			<font color="#FFFFFF">&copy; 2016 Inscription. All Rights Reserved
				| Design by <a href="about.jsp" style="color: #FFFFFF;">Shivam
					Agrawal, Ansh Kamra, Juhi Sharma, Divyansh Agicha</a>
			</font>
		</p>
	</div>
	<!--COPY rights end here-->

	<script src="scripts/login.9c717c1f07a7462b1e4f.js"></script>

</body>
</html>




