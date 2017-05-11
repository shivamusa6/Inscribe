package com.inscript;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.inscript.Call.Clear;
import com.inscript.Call.DBOperation;
import com.inscript.Call.MultiUser;
import com.inscript.Call.UserSessionProfile;

/**
 * Servlet implementation class MultiSignup
 */
public class MultiSignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MultiSignup() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String getID(String name, int type){		//'0' for institute and '1' for course
		Scanner input = null;
		
    	try{
			String JSON_FILE = null;
			
			if(type==0)
				JSON_FILE = getInitParameter("INSTITUTES_JSON");
			else
				JSON_FILE = getInitParameter("COURSES_JSON");	

			input = new Scanner(getServletContext().getResourceAsStream(JSON_FILE)); 
			StringBuilder jsonString = new StringBuilder();  
			while(input.hasNextLine()) {  
				jsonString.append(input.nextLine());  
			}
			
			JSONArray json = (JSONArray) new JSONParser().parse(jsonString.toString());
			
			for(int i=0;i<json.size();i++){				
				JSONObject data = (JSONObject) json.get(i);
				if(data!=null){
					String storedName = (String)data.get("name");
					
					if(storedName.equalsIgnoreCase(name)){
						return data.get("id").toString();
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(input!=null)
				input.close();
		}
    	
    	return "10000";
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher rd=request.getRequestDispatcher("/multisignup.jsp");
		request.setAttribute("error", "Some error has been encountered. Please try again!");		
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String dob = request.getParameter("dob");
		String mobile = request.getParameter("mobile");
		String profile = request.getParameter("profile");
		String institute = getID(request.getParameter("institute"), 0);
		String course = getID(request.getParameter("course"), 1);
		//String branch = request.getParameter("branch");
		String sem = request.getParameter("sem");
		String memberType = request.getParameter("memberType");
		
		HttpSession s = request.getSession(false);
		
		if(s==null)
			response.sendRedirect("login.jsp");
		else if(institute.equals("10000") || course.equals("10000"))
			doGet(request, response);
		else{
			try{
				UserSessionProfile usp = (UserSessionProfile) s.getAttribute("uid");
				String email = usp.getEmail();		//email
				String[] getValuesOf = {"id"};
				String userID = Clear.getVariables(email, getValuesOf);

				MultiUser u = new MultiUser(Integer.parseInt(userID),dob,mobile,profile,Integer.parseInt(institute),Integer.parseInt(course),Integer.parseInt(sem),memberType);

				if(DBOperation.addMultiUser(u)){
					int result = DBOperation.insertTicket(email, null, 3);
					if(result!=5050)
						response.sendRedirect("dashboard.jsp?success");
					else
						response.sendRedirect("dashboard.jsp?check");		// TODO again attempt to call insertTicket() on dashboard.jsp loadup 
				}else
					doGet(request, response);
			}catch(Exception e){
				e.printStackTrace();
				doGet(request, response);
			}
		}
	}

}
