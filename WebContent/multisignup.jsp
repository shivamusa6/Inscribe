<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>

<link rel="stylesheet" href="styles/multisignup.57c695c6e1b8d3bf4f9a.css">


<!-- Favicon and touch icons -->


<%
	if (session.getAttribute("uid") != null) {
		if (Clear.isProfileCompleted(session.getAttribute("uid")))
			response.sendRedirect("dashboard.jsp");
		else if (!Clear.validateSession(session.getAttribute("uid")))
			response.sendRedirect("login.jsp?sessionExpired");
	} else {
		response.sendRedirect("login.jsp?invalidAttempt");
	}
%>

</head>

<body>

	<!-- Top menu -->


	<!-- Description -->


	<!-- Multi Step Form -->
	<div class="msf-container">
		<div class="container">
			<div class="row">
				<div class="col-sm-12 msf-title">
					<h3>Fill In The Form</h3>
					<p>Please complete the form below to get instant access to our
						application and all its features:</p>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 msf-form">

					<form role="form" action="getMultiSignup" method="post"
						class="form-inline">

						<fieldset>
							<h4>
								Personal Details <span class="step">(Step 1 / 3)</span>
							</h4>
							<div class="form-group">
								<label for="dob">Date of Birth:</label><br> <input
									type="date" name="dob" class="first-name form-control"
									id="first-name" required="required">
							</div>
							<div class="form-group">
								<label for="mobile">Mobile Number:</label><br> <input
									type="text" name="mobile" class="last-name form-control"
									onkeyup="mobilefunc(); return false;" id="last_name"
									required="required" maxlength="10">
							</div>
							<div class="form-group">
								<label for="profile">Linkedin/Facebook Profile:</label><br>
								<input type="text" name="profile" class="height form-control"
									id="height" required="required">
							</div>

							<br>
							<button type="button" class="btn btn-next">
								Next <i class="fa fa-angle-right"></i>
							</button>
						</fieldset>

						<fieldset>
							<h4>
								Institute Details <span class="step">(Step 2 / 3)</span>
							</h4>
							<div class="form-group">
								<label for="institute">Name of Institute:</label><br> <input
									type="text" name="institute" id="institute"
									class="form-control" required="required"
									placeholder="Start typing...">
								<!-- <select class="form-control" name="institute" required="required">
				                    	<option>Start typing...</option>
				                    	<option value="1">college1</option>
				                    	<option value="2">college2</option>
					                </select> -->
							</div>
							<div class="form-group">
								<label for="course">Course:</label><br> <input type="text"
									name="course" id="course" class="form-control"
									required="required" placeholder="Start typing...">
								<!-- <select class="form-control" name="course" required="required">
					                	<option value="1">BTech</option>
					                	<option value="2">BCA</option>
					                	<option value="3">BBA</option>
					                	<option value="4">MTech</option>
					                	<option value="5">MCA</option>
					                	<option value="6">MBA</option>
					                	<option value="7">LLB</option>
					                </select> -->
							</div>
							<!-- <div class="form-group">
				                    <label for="branch">Branch:</label><br>
				                      <select class="form-control" name="branch" required="required">
					                	<option value="1">Computer Science and Engineering</option>
					                	<option value="2">Electronics Engineering</option>
					                	<option value="3">Electrical Engineering</option>
					                	<option value="4">Mechanical Engineering</option>
					                </select>
				                </div> -->
							<div class="form-group">
								<label for="sem">Year/Semester:</label><br> <select
									class="form-control" name="sem" id="sem" required="required">
									<option>Select Year/Semester</option>
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9</option>
									<option value="10">10</option>
									<option value="11">11</option>
									<option value="12">12</option>
									<option value="13">13</option>
									<option value="14">14</option>
								</select>
							</div>
							<br>
							<button type="button" class="btn btn-previous">
								<i class="fa fa-angle-left"></i> Previous
							</button>
							<button type="button" class="btn btn-next">
								Next <i class="fa fa-angle-right"></i>
							</button>
						</fieldset>

						<fieldset>
							<h4>
								Member Type <span class="step">(Step 3 / 3)</span>
							</h4>
							<div class="radio-buttons-1">

								<label class="radio-inline"> <input type="radio"
									name="memberType" value="student" required="required">
									Student
								</label> <label class="radio-inline"> <input type="radio"
									name="memberType" value="teacher" required="required">
									Teacher
								</label>


							</div>

							<br>

							<button type="button" class="btn btn-previous">
								<i class="fa fa-angle-left"></i> Previous
							</button>
							<button type="submit" class="btn">Submit</button>
						</fieldset>
						<span id="confirmMessage" class="confirmMessage"></span>
					</form>

				</div>
			</div>
		</div>
	</div>
	<center>
		<div class="copyrights" style="margin-top: 15%;">
			<p>
				<font color="#FFFFFF">&copy; 2016 Inscription. All Rights Reserved
					| Design by <u><a href="https://in.linkedin.com/in/shivamusa6"
						style="color: #FFFFFF;">Shivam Agrawal</a></u>, <u><a
						href="https://in.linkedin.com/in/anshkamra"
						style="color: #FFFFFF;">Ansh Kamra</a></u>, <u><a
						href="https://in.linkedin.com/in/juhi-869272116"
						style="color: #FFFFFF;">Juhi Sharma</a></u>, <u><a
						href="https://in.linkedin.com/in/divyansh-agicha-78ba07116"
						style="color: #FFFFFF;">Divyansh Agicha</a></u>.
				</font>
			</p>
		</div>
	</center>

	<script src="scripts/multisignup.078b6aa08c465cb4dccf.js"></script>

</body>

</html>
