package com.inscript;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inscript.Call.Clear;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
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
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession s=request.getSession();
		
		if(s!=null){
			try{
				s.removeAttribute("uid");
				s.removeAttribute("init");
				s.invalidate();
			}catch(Exception e){
				e.printStackTrace();
				s.invalidate();
			}
		}
		
		clearCookiesData(request, response);

		if(request.getParameter("id")!=null && request.getParameter("step")!=null)
			response.sendRedirect("login.jsp?id="+request.getParameter("id")+"&step="+request.getParameter("step"));
		else if(request.getParameter("redirect")!=null && request.getParameter("redirect").equals("fpass"))
			response.sendRedirect("forgotpassword.jsp?pathFollowed");
		else
			response.sendRedirect("login.jsp?logout=success");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
