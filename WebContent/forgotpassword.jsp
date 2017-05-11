<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forgot Password</title>

<link rel="stylesheet" href="styles/forgotpassword.48978bb9eeadaa1dcd8f.css">

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
		response.sendRedirect("getLogout?redirect=fpass");
	}
%>

</head>
<body>
	<div class="container"
		style="margin-top: 5%; margin-left: 5%; margin-right: 5%;">
		<div class="row">
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="text-center">
								<h3>
									<i class="fa fa-lock fa-4x"></i>
								</h3>
								<h2 class="text-center">Forgot Password?</h2>
								<p>You can reset your password here.</p>
								<div class="panel-body">
									<%
										String email = request.getParameter("user");
										String ticket = request.getParameter("ticket");
										if (email != null && ticket != null && Clear.checkTicket(email, ticket)) {
									%>
									<form action="getFPass" method="post">
										<fieldset>
											<input type="hidden" name="email"
												value=<%=request.getParameter("user")%>>
											<div class="form-group">
												<input type="password" name="npass"
													placeholder="New Password" class="form-control" id="pass1"
													required="required">
											</div>
											<div class="form-group">
												<input type="password" name="cpass"
													placeholder="Confirm Password" class="form-control"
													id="pass2" required="required">
											</div>
											<div class="form-group">
												<input class="btn btn-lg btn-primary btn-block"
													value="Reset Password" type="submit">
											</div>
										</fieldset>
									</form>
									<%
										} else {
									%>
									<form class="form" action="getFPass" method="post">
										<fieldset>
											<div class="form-group">
												<div class="input-group">
													<span class="input-group-addon"><i
														class="glyphicon glyphicon-envelope color-blue"></i></span> <input
														id="emailInput" placeholder="email address" name="email"
														class="form-control" type="email"
														oninvalid="setCustomValidity('Please enter a valid email address!')"
														onchange="try{setCustomValidity('')}catch(e){}"
														required="">
												</div>
											</div>
											<div class="form-group">
												<input class="btn btn-lg btn-primary btn-block"
													value="Reset Password" type="submit">
											</div>
										</fieldset>
									</form>
									<%
										}
									%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="scripts/forgotpassword.dd86a37dcd365ed6eaaf.js"></script>

</body>
</html>