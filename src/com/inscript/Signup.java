package com.inscript;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.inscript.Call.Clear;
import com.inscript.Call.User;
import com.inscript.Call.DBOperation;

/**
 * Servlet implementation class Signup
 */
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd=request.getRequestDispatcher("/signup.jsp");
		
		if(request.getParameter("error")!=null && request.getParameter("error").equals("mismatchPass"))
			request.setAttribute("error", "Passwords mismatch error");
		else
			request.setAttribute("error", "Signup failed. Please try again!");
		
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name").toLowerCase();
		String email = request.getParameter("email").toLowerCase();
		String pass1 = request.getParameter("password");
		String pass2 = request.getParameter("confirmpassword");
		String gender = ""+request.getParameter("gender").charAt(0);
		
		if(!pass1.equals(pass2))
			response.sendRedirect("/getSignup?error=mismatchPass");
		else{
			String initials = Clear.init(name.trim());
			User u = new User(Clear.toCamelCase(name.trim()), email.trim(), gender, initials);
			
			try{
				String passwordHash = Clear.hash(pass1.trim());
				u.setPassword(passwordHash);
				
				if(DBOperation.addUser(u))
					response.sendRedirect("getLogout?id=signupSuccess&step=mailVerification");
				else
					doGet(request, response);
			}catch(Exception e){
				e.printStackTrace();
				doGet(request, response);
			}
		}
	}

}
