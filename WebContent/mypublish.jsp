<%@page import="com.inscript.Call.Clear"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="true"%>
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
.tooltip-inner {
    max-width: 100%;
    /* If max-width does not work, try using width instead */
    width: auto;
    color: black;
    background-color: white; 
     
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
							class="fa fa-user"></i> <%=Clear.getSingleVariable(session.getAttribute("uid"), "initials")%>&nbsp;<b
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

		<sql:query dataSource="jdbc/development" var="result">
		SELECT date, fileName, fileDescription, fileSize, fileID FROM filesuploaded WHERE uid='<%=Clear.getSingleVariable(session.getAttribute("uid"), "id")%>' AND folderPath='<%=Clear.getParentFolderPath(session.getAttribute("uid"))%>' AND status='success' ORDER BY id DESC;
		</sql:query>

		<div id="page-wrapper">
			<br>
			
<div class="col-lg-6">
          	<div class="panel-body">
<table class="table table-condensed" style="border-collapse:collapse;">

   <thead>
					<tr>
						<th>File Name</th>
						<th>Time</th>
						<th>Size</th>
						<th>Download</th>
						<th>Discuss</th>
						<th>Delete</th>
					</tr>
	</thead>
				<tbody>
    <c:forEach var="row" items="${result.rows}">
        <tr>
             <td>   <a   data-toggle="tooltip"  title="<c:out value="${row.fileDescription}" />"><c:out value="${row.fileName}" /></a></td>
						
							<td><c:out value="${row.date}" /></td>
							<td><c:out value="${row.fileSize}" /></td>
							<td><button type="button" style="color: blue"
									class="requestButton"
									id="Download_<c:out value="${row.fileID}"/>">
									<i class="fa fa-download fa-lg" aria-hidden="true"></i>&nbsp;Download
								</button></td>
								<td><button class="btn btn-default btn-xs"><span class="glyphicon glyphicon-eye-open"></span></button></td>
							<td><button type="button" style="color: red"
									class="requestButton"
									id="Delete_<c:out value="${row.fileID}"/>">
									<i class="fa fa-trash-o fa-lg" aria-hidden="true"></i>&nbsp;Delete
								</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<script src="scripts/dashboard.6e4e1e57922942204540.js"></script>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>     
       
</body>
</html>