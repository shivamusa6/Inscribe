package com.inscript;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inscript.Call.Clear;
import com.inscript.Call.UserSessionProfile;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void createCookiesForUser(HttpServletRequest request, HttpServletResponse response, HttpSession s, String email, String date){
		if(request.getParameter("rememberMe")!=null && request.getParameter("rememberMe").equals("true")){
			System.out.print("\ncreating cookieData... ");
			String encodedEmail = Base64.encode(email.getBytes());
			String encodedDate = Base64.encode(date.getBytes());

			if(Clear.alterCookieData(email, encodedEmail+":"+encodedDate, 0)){
				Cookie c1 = new Cookie("email", encodedEmail);
				Cookie c2 = new Cookie("date", encodedDate);
				c1.setMaxAge(24*60*60);
				c2.setMaxAge(24*60*60);
				response.addCookie(c1);
				response.addCookie(c2);
				s.setMaxInactiveInterval(24*60*60);
				System.out.println("CREATED");
			}else{
				s.setMaxInactiveInterval(-1);
				System.out.println("FAILED");
			}
		}else{
			s.setMaxInactiveInterval(-1);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd=request.getRequestDispatcher("/login.jsp");

		/*if(request.getParameter("redirect")!=null && request.getParameter("redirect").equals("signupSuccess"))
			request.setAttribute("msg", "Signup successful. Now Log in to get access to Inscript Notes!");*/
		if(request.getParameter("error")!=null && request.getParameter("error").equals("confirmMail"))
			request.setAttribute("error", "Please confirm the email sent to your Email id to proceed.");
		else if(request.getParameter("error")!=null && request.getParameter("error").equals("dataTampered"))
			request.setAttribute("error", "We've encountered some problems on your system. Please try again after sometime!");
		else if(request.getParameter("error")!=null && request.getParameter("error").equals("bandwidthErr"))
			request.setAttribute("error", "We've encountered some problems on your internet. Please try again after sometime!");
		else
			request.setAttribute("error", "Entered credentials are not valid. Please try again!");

		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email").toLowerCase();
		String pass = request.getParameter("password");

		try {
			if(Clear.authenticate(email, pass)){				
				String[] getValuesOf = {"id", "verified", "multiSignupStatus"};			
				String userData = Clear.getVariables(email, getValuesOf);

				if(userData!=null){
					String[] data = userData.split(":");
					userData = null;

					if(data[1].equals("no")){
						data = null;
						response.sendRedirect("getLogin?error=confirmEmail");
					}else if(data[1].equals("yes")){						
						if(data[2].equals("done")){
							HttpSession s = request.getSession(true);
				
							UserSessionProfile usp = new UserSessionProfile(email);
							s.setAttribute("uid", usp);
							s.setAttribute("s2", email);
							createCookiesForUser(request, response, s, email, usp.getDate());

							response.sendRedirect("dashboard.jsp");					// TODO redirect only to multisignup.jsp	
						}else if(data[2].equals("waiting")){
							HttpSession s = request.getSession(true);
							UserSessionProfile usp = new UserSessionProfile(email);
							s.setAttribute("uid", usp);

							createCookiesForUser(request, response, s, email, usp.getDate());

							response.sendRedirect("multisignup.jsp");								
						}else
							response.sendRedirect("getLogin?error=dataTampered");
					}else
						response.sendRedirect("getLogin?error=dataTampered");

				}else{
					response.sendRedirect("getLogin?error=bandwidthErr");
				}
			}else{
				doGet(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			doGet(request, response);
		}
	}

}
