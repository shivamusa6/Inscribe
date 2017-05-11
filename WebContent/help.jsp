<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inscription (Sharing Notes) | Dashboard</title>

<link rel="stylesheet" href="styles/dashboard.e9857f6468c6ff274984.css">

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
		if (!Clear.isProfileCompleted(session.getAttribute("uid")))
			response.sendRedirect("multisignup.jsp");
		else if (!Clear.validateSession(session.getAttribute("uid")))
			response.sendRedirect("login.jsp?sessionExpired");
	} else {
		response.sendRedirect("login.jsp?invalidAttempt");
	}
%>

</head>
<body>
	<div id="wrapper">
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="dashboard.jsp">Dashboard</a>
			</div>
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul id="active" class="nav navbar-nav side-nav">
					<li class="#"><a href="upload.jsp"><i class="fa fa-upload"></i>
							Upload</a></li>
					  <li><a href="discuss.jsp"><i class="glyphicon glyphicon-pushpin"></i> Discussion Forum</a></li> 
					<li><a href="mypublish.jsp"><i class="fa fa-globe"></i> My
							Publish</a></li>
					<li><a href="editor.jsp"><i class="glyphicon glyphicon-pencil"></i> My
						 Editor</a></li>		
				 <li><a href="myspace.jsp"><i class="glyphicon glyphicon-education"></i> My Space</a></li>
                    <li><a href="reqspace.jsp"><i class="glyphicon glyphicon-level-up"></i> Request Space</a></li>
                    <li><a href="changespace.jsp"><i class="glyphicon glyphicon-link"></i> Change Space</a></li>
                    <li><a href="invite.jsp"><i class="glyphicon glyphicon-comment"></i> Invite</a></li>
					<li><a href="help.jsp"><i
							class="glyphicon glyphicon-question-sign"></i> Help</a></li>

				</ul>
				<ul class="nav navbar-nav navbar-right navbar-user">
					<li class="dropdown messages-dropdown"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"><i
							class="glyphicon glyphicon-bell"></i> Notifications <span
							class="badge">2</span> <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li class="dropdown-header">2 New Messages</li>
							<li class="message-preview"><a href="#"> <span
									class="avatar"><i class="fa fa-bell"></i></span> <span
									class="message">Security alert</span>
							</a></li>
							<li class="divider"></li>
							<li class="message-preview"><a href="#"> <span
									class="avatar"><i class="fa fa-bell"></i></span> <span
									class="message">Security alert</span>
							</a></li>
							<li class="divider"></li>
							<li><a href="dashboard.jsp">Go to Dashboard <span class="badge">2</span></a></li>
						</ul></li>
					<li class="dropdown user-dropdown"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"><i
							class="fa fa-user"></i> <%= Clear.getSingleVariable(session.getAttribute("uid"), "initials") %>&nbsp;<b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
							<li><a href="#"><i class="fa fa-gear"></i> Settings</a></li>
							<li class="divider"></li>
							<li><a href="getLogout"><i class="fa fa-power-off"></i>
									Log Out</a></li>

						</ul></li>
					<li class="divider-vertical"></li>
					<li>
						<form class="navbar-search">
							<input type="text" placeholder="Search" class="form-control">
						</form>
					</li>
				</ul>
			</div>
		</nav>

		<div id="page-wrapper">

			<div class="panel-group" id="accordion">
				<div class="faqHeader" style="color: #FFFFFF;">General
					questions</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle" data-toggle="collapse"
								data-parent="#accordion" href="#collapseOne">Is account
								registration required?</a>
						</h4>
					</div>
					<div id="collapseOne" class="panel-collapse collapse in">
						<div class="panel-body">Yes, Registration is must for
							joining your peer network and material.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseTen">Can I add my
								institute if it is not there?</a>
						</h4>
					</div>
					<div id="collapseTen" class="panel-collapse collapse">
						<div class="panel-body">You can make a request to Admin by
							clicking "Request Institution". After verification it'll be
							automatically added and you will be notified on your mail.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseEleven">Is it
								necessary to Login to use Discussion Forum?</a>
						</h4>
					</div>
					<div id="collapseEleven" class="panel-collapse collapse">
						<div class="panel-body">Yes, You have to login separately
							for joining the discussion form.</div>
					</div>
				</div>

				<div class="faqHeader" style="color: #FFFFFF;">Students</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseTwo">What is the
								Dashboard page?</a>
						</h4>
					</div>
					<div id="collapseTwo" class="panel-collapse collapse">
						<div class="panel-body">This page provides tools and
							materials for using the materials that other users and teachers
							have posted. These materials include lesson plans, documents,
							presentations and professional development resources.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseThree">Where my
								posts will be shown?</a>
						</h4>
					</div>
					<div id="collapseThree" class="panel-collapse collapse">
						<div class="panel-body">They are shown in "My Publish""
							Section of your dashboard and will be shown to the dashboard of
							other users linked to that course and semester.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseFive">Can I
								Broadcast the message?</a>
						</h4>
					</div>
					<div id="collapseFive" class="panel-collapse collapse">
						<div class="panel-body">
							No, Only teachers have that right. But if you want then you can
							first give your message to teachers to let them broadcast for
							you. <br />
						</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseSix">Can I request a
								new Space?</a>
						</h4>
					</div>
					<div id="collapseSix" class="panel-collapse collapse">
						<div class="panel-body">Yes, you can anytime.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseEight">Can I change
								the Space?</a>
						</h4>
					</div>
					<div id="collapseEight" class="panel-collapse collapse">
						<div class="panel-body">Yes, But you will not be able to see
							the details of your previous space.</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseNine">Can I delete a
								post, once posted?</a>
						</h4>
					</div>
					<div id="collapseNine" class="panel-collapse collapse">
						<div class="panel-body">Yes, you can delete but only the
							posts that you have posted.</div>
					</div>
				</div>

				<div class="faqHeader" style="color: #FFFFFF;">Teachers</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseFour">Can I
								Broadcast the message?</a>
						</h4>
					</div>
					<div id="collapseFour" class="panel-collapse collapse">
						<div class="panel-body">Yes, you can even Spacecast.
							(Spacecast- Sending posts to particular Space.)</div>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<a class="accordion-toggle collapsed" data-toggle="collapse"
								data-parent="#accordion" href="#collapseSeven">Can I delete
								a post, once posted?</a>
						</h4>
					</div>
					<div id="collapseSeven" class="panel-collapse collapse">
						<div class="panel-body">Yes, You can !</div>
					</div>
				</div>
			</div>

			<style>
.faqHeader {
	font-size: 27px;
	margin: 20px;
}

.panel-heading [data-toggle="collapse"]:after {
	font-family: 'Glyphicons Halflings';
	content: "\e072"; /* "play" icon */
	float: right;
	color: #F58723;
	font-size: 18px;
	line-height: 22px;
	/* rotate "play" icon from > (right arrow) to down arrow */
	-webkit-transform: rotate(-90deg);
	-moz-transform: rotate(-90deg);
	-ms-transform: rotate(-90deg);
	-o-transform: rotate(-90deg);
	transform: rotate(-90deg);
}

.panel-heading [data-toggle="collapse"].collapsed:after {
	/* rotate "play" icon from > (right arrow) to ^ (up arrow) */
	-webkit-transform: rotate(90deg);
	-moz-transform: rotate(90deg);
	-ms-transform: rotate(90deg);
	-o-transform: rotate(90deg);
	transform: rotate(90deg);
	color: #454444;
}
</style>

			<!-- Bootstrap FAQ - END -->

		</div>
	</div>

	<script src="scripts/discuss.690e0a0938202b1bdc6f.js"></script>

</body>
</html>