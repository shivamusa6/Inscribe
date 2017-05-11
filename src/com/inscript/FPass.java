package com.inscript;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inscript.Call.Clear;

/**
 * Servlet implementation class FPass
 */
public class FPass extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FPass() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd = request.getRequestDispatcher("forgotpassword.jsp");

		if(request.getAttribute("info")!=null)
			System.out.println("\nFPASS INFO: '"+request.getAttribute("info")+"'");

		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
			if(request.getParameter("email")!=null){
				if(request.getParameter("npass")!=null && request.getParameter("cpass")!=null){
					String email = request.getParameter("email").toLowerCase();
					String pass1 = request.getParameter("npass");
					String pass2 = request.getParameter("cpass");
					
					if(!pass1.equals(pass2))
						request.setAttribute("info", "Passwords did not match");
					else{
						if(Clear.insertTicket(email, pass1, 1)!=5050)
							request.setAttribute("info", "Password changed successfully. Please login with your new password now");
						else
							request.setAttribute("info", "Password Reset failed.");
					}
				}else{
					String email = request.getParameter("email").toLowerCase();
					String[] getValuesOf = {"verified"};
					String userData = Clear.getVariables(email, getValuesOf);

					if(userData!=null){
						if(userData.equals("yes")){
							if(Clear.send(email, 1)){
								request.setAttribute("info", "Please check your mail to proceed with the Password Reset process");
							}else{
								request.setAttribute("info", "Our system encountered some error. Please try again after some time");					
							}
						}else{
							request.setAttribute("info", "You are not verified yet to access this functionality");
						}
					}else{
						request.setAttribute("info", "User not registered");
					}
				}
			}
		}catch(Exception e){
			request.setAttribute("info", "Unauthorised Access Detected");
			e.printStackTrace();
		}

		doGet(request, response);
	}

}
