package com.inscript;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

/**
 * Servlet implementation class Data
 */
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Data() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String JSON_FILE = null, term = null;
		Scanner input = null;
		
		if(request.getParameter("inst")!=null){
			JSON_FILE = getInitParameter("INSTITUTES_JSON");
			term = "inst";
		}else if(request.getParameter("cour")!=null){
			JSON_FILE = getInitParameter("COURSES_JSON");
			term = "cour";
		}
		
		try {
			input = new Scanner(getServletContext().getResourceAsStream(JSON_FILE)); 
			StringBuilder jsonString = new StringBuilder();  
			while(input.hasNextLine()) {  
				jsonString.append(input.nextLine());  
			}  
			//System.out.println(jsonString.toString());
			
			System.out.println("\nserving data for "+term+"='"+request.getParameter(term)+"'...");
			JSONArray json = (JSONArray) new JSONParser().parse(jsonString.toString());
			System.out.println("\nfile loaded...");

			ArrayList<String> list = new ArrayList<String>();
			
			for(int i=0;i<json.size();i++){
				if(!list.isEmpty() && list.size()==10)
					break;
				
				JSONObject data = (JSONObject) json.get(i);
				if(data!=null){
					String name = (String)data.get("name");
					String lower = name.toLowerCase();
					
					if(lower.contains(request.getParameter(term).toLowerCase()))
						list.add(name);
				}
			}
			
			if(list!=null && !list.isEmpty()){
				System.out.println("\nsearchlist generated...");
                response.setContentType("application/json");
				response.getWriter().write(new Gson().toJson(list));
			}else{
				list.add("");
				System.out.println("\nsearchlist generated...");
                response.setContentType("application/json");
				response.getWriter().write(new Gson().toJson(list));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(FileNotFoundException e){
			System.out.println("\nFILE NOT FOUND!!!");
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(input!=null)
				input.close();
		}
	}

}
