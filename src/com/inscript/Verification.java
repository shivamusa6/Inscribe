package com.inscript;

import java.io.IOException;

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
 * Servlet implementation class Verification
 */
public class Verification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Verification() {
		super();
		// TODO Auto-generated constructor stub
	}

	private void clearCookiesData(HttpServletRequest request, HttpServletResponse response){

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("email")) {
					
					try{
					if(Clear.alterCookieData(getDecodedString(cookie.getValue()), null, 1))
						System.out.println("\ncookieData cleared!!!");
					else
						System.out.println("\ncookieData not cleared!!!");
					}catch(Exception e){
						System.out.println("\nerror decoding...cookieData not cleared!!!");
					}
					
					cookie.setMaxAge(0);
					cookie.setValue(null);
					response.addCookie(cookie);
				}
				if (cookie.getName().equals("date")) {
					cookie.setMaxAge(0);
					cookie.setValue(null);
					response.addCookie(cookie);
				}
			}
		}
		System.out.println("\nCookies Cleared!!!");
	}

	private String getDecodedString(String encodedData) throws Exception{
		return new String(Base64.decode(encodedData));
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		if(request.getParameter("id")!=null){
			if(request.getParameter("id").equals("signup")){
				String email = request.getParameter("user").toLowerCase();
				String ticket = request.getParameter("ticket");

				String[] getValuesOf = {"mailTicket"};
				String userData = Clear.getVariables(email, getValuesOf);

				if(userData!=null && userData.equals(ticket)){
					if(Clear.insertTicket(email, null, 2)!=5050)
						response.sendRedirect("login.jsp?userVerification=success");
					else
						response.sendRedirect("login.jsp?userVerification=error");
				}else{
					response.sendRedirect("login.jsp?userVerification=failed");
				}
			} /*else if(request.getParameter("id").equals("fpass")){
				String email = request.getParameter("user").toLowerCase();
				String ticket = request.getParameter("ticket");

				String[] getValuesOf = {"passwordTicket"};
				String userData = Clear.getVariables(email, getValuesOf);

				if(userData!=null && userData.equals(ticket)){
					response.setHeader("referer", "getVerification");
					response.sendRedirect("forgotpassword.jsp?userVerification=success&user="+email);
				}else{
					response.sendRedirect("login.jsp?userVerification=failed");
				}
			}*/
		}else if(request.getParameter("email")!=null && request.getParameter("date")!=null){

			try {
				String email = getDecodedString(request.getParameter("email"));
				String date = getDecodedString(request.getParameter("date"));
				System.out.println("\nparams: email='"+email+"' & date='"+date+"'");

				String[] getValuesOf = {"cookieData"};
				String userData = Clear.getVariables(email, getValuesOf);
				String storedEmail = getDecodedString(userData.split(":")[0]);
				String storedDate = getDecodedString(userData.split(":")[1]);

				if(email.equals(storedEmail) && date.equals(storedDate)){
					HttpSession s = request.getSession(true);
					UserSessionProfile usp = new UserSessionProfile(email, date);
					s.setAttribute("uid", usp);
					response.sendRedirect("multisignup.jsp");
				}else{
					System.out.println("\nparams data tampered...");
					clearCookiesData(request, response);
					response.sendRedirect("login.jsp");
				}
			} catch (Exception e) {
				System.out.println("\nparams decoding error...");
				clearCookiesData(request, response);
				//e.printStackTrace();
				response.sendRedirect("login.jsp");
			}

		}else{
			response.sendRedirect("login.jsp?invalidAttempt");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
