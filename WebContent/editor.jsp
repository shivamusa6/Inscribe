<%@page import="com.inscript.Call.Clear"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script src="scripts/editor.js"></script>
		<script>
			$(document).ready(function() {
				$("#txtEditor").Editor();
			});
		</script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
		<link href="styles/editor.css" type="text/css" rel="stylesheet"/>
		 <link rel="stylesheet" type="text/css" href="styles/local.css" />
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
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
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
                    <li class="dropdown messages-dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-bell"></i> Notifications <span class="badge">2</span> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li class="dropdown-header">2 New Messages</li>
                            <li class="message-preview">
                                <a href="#">
                                    <span class="avatar"><i class="fa fa-bell"></i></span>
                                    <span class="message">Security alert</span>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li class="message-preview">
                                <a href="#">
                                    <span class="avatar"><i class="fa fa-bell"></i></span>
                                    <span class="message">Security alert</span>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li><a href="#">Go to Inbox <span class="badge">2</span></a></li>
                        </ul>
                    </li>
                    <li class="dropdown user-dropdown"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"><i
							class="fa fa-user"></i> <%=Clear.getSingleVariable(session.getAttribute("uid"), "initials")%>&nbsp;<b
							class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
                            <li><a href="#"><i class="fa fa-gear"></i> Settings</a></li>
                            <li class="divider"></li>
                            <li><a href="#"><i class="fa fa-power-off"></i> Log Out</a></li>

                        </ul>
                    </li>
                    <li class="divider-vertical"></li>
                    <li>
                        <form class="navbar-search">
                            <input type="text" placeholder="Search" class="form-control">
                        </form>
                    </li>
                </ul>
            </div>
        </nav>

        <div id="page-wrapper" >
            
            <div class="container-fluid" style="float: left;  ">
			<div class="row">
				<div class="container" style="width: 88%;">
					<div class="row">
						<div class="col-lg-12 nopadding">
						<form action="#">
						    <input type="text" placeholder="Type Topic Here..." name="tpname" class="form-control" style="color: black;">
							<textarea id="txtEditor" placeholder="Type Content Here..." name="tpcontent" ></textarea> 
						    <center><button type="submit" class="btn btn-default btn-lg" >Publish</button></center>
						    </form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
  <br>           
 

        </div>
    </div>

</body>
</html>