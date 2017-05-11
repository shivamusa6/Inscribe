<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Inscription (Sharing Notes)</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Sharing Notes, Signup Students, Signup Teachers, Signup Institutes, Creating new Networks" />

<link rel="stylesheet" href="styles/login.37710003883c7dee1399.css" media="all">

<link href='//fonts.googleapis.com/css?family=Carrois+Gothic' rel='stylesheet' type='text/css'>
<link href='//fonts.googleapis.com/css?family=Work+Sans:400,500,600' rel='stylesheet' type='text/css'>

<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>

<style type="text/css">
body {
	background: url(images/back.jpg) no-repeat center center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}

#signupm {
	margin-top: -40px;
}

select {
	width: 100%;
	height: 40px;
	background-color: #F5F5F5;
	color: #696969;
	border-radius: 5px;
	border-color: #d3d3d3;
	font-family: inherit;
	padding-left: 18px;
	font-size: 14.5px;
}
</style>
</head>
<body>
	<!--inner block start here-->
	<div class="signup-page-main">
		<div class="signup-main" id="signupm">
			<div class="signup-head">
				<h1>Sign Up</h1>
			</div>
			<div class="signup-block">
				<span id="confirmMessage" class="confirmMessage"></span>
				<form action="getSignup" method="post">
					<input type="text" name="name" placeholder="Full Name"
						required="required"> <input type="text" name="email"
						id="email_id" placeholder="Email"
						onkeyup="email_function(); return false;" required="required">
					<input type="password" name="password" id="pass1" class="lock"
						placeholder="Password" required="required"> <input
						type="password" name="confirmpassword" id="pass2"
						onkeyup="checkPass(); return false;" class="lock"
						placeholder="Confirm Password" required="required"> <select
						class="lock" name="gender" required="required">
						<option>Gender</option>
						<option value="M">Male</option>
						<option value="F">Female</option>
						<option value="O">Other</option>
					</select>
					<div class="forgot-top-grids">
						<div class="forgot-grid">
							<ul>
								<li><input type="checkbox" id="brand1" value=""> <label
									for="brand1"><span></span>I agree to the terms</label></li>
							</ul>
						</div>

						<div class="clearfix"></div>
					</div>
					<input type="submit" name="Sign In" value="Sign Up">
				</form>
				<div class="sign-down">
					<h4>
						Already have an account? <a href="login.jsp"
							style="color: #68ae00;"> Login here</a>
					</h4>

				</div>
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

	<!-- mother grid end here-->
</body>
</html>




