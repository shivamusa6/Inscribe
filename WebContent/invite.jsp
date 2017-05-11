<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
							class="fa fa-user"></i> <%=Clear.getSingleVariable(session.getAttribute("uid"), "initials")%>&nbsp;<b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
							<li><a href="#"><i class="fa fa-gear"></i> Settings</a></li>
							<li class="divider"></li>
							<li><a href="#"><i class="fa fa-power-off"></i> Log Out</a></li>

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

		<div id="page-wrapper"></div>
	</div>

	<script src="scripts/discuss.690e0a0938202b1bdc6f.js"></script>

</body>
</html>