package com.inscript;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inscript.Call.Clear;
import com.inscript.Call.GDriveAPI;

public class Drive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static final String P12_KEY_FILE = "<path to P12_KEY_FILE>";

	public Drive() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String execute = "";

		if(request.getAttribute("execute")!=null){
			if(!GDriveAPI.isServiceInitialised){
				InputStream is = getServletContext().getResourceAsStream(getInitParameter("JSON_KEY_FILE"));
				GDriveAPI.initialise(is);
			}

			if(request.getAttribute("execute").equals("upload")){
				execute = "?upload";
				String fileData = GDriveAPI.createFile(false, getServletContext().getResourceAsStream(getInitParameter("MIMETYPE_JSON")), (String) request.getAttribute("fname"), (String) request.getAttribute("fpath"), (String) request.getAttribute("desc"), (String) request.getAttribute("parents"));
				if(fileData!=null){
					System.out.println("\nUPLOAD SUCCESS");
					String[] values = {
							(String) request.getAttribute("uid"),
							(String) request.getAttribute("date"),
							((String) request.getAttribute("fname"))+fileData.split(":")[1],
							(String) request.getAttribute("fpath"),
							(String) request.getAttribute("desc"),
							(String) request.getAttribute("parents"),
							fileData.split(":")[0],
							fileData.split(":")[2]
					};
					Clear.insertFileDetails("`filesuploaded` (`uid`, `date`, `fileName`, `filePath`, `fileDescription`, `folderPath`, `fileID`, `fileSize`)", values);
				}else{
					System.out.println("\nUPLOAD FAILED");
					String[] values = {
							(String) request.getAttribute("uid"),
							(String) request.getAttribute("date"),
							(String) request.getAttribute("fname"),
							(String) request.getAttribute("fpath"),
							(String) request.getAttribute("desc"),
							(String) request.getAttribute("parents"),
							"failed"
					};
					Clear.insertFileDetails("`filesuploaded` (`uid`, `date`, `fileName`, `filePath`, `fileDescription`, `folderPath`, `status`)", values);
				}
			}else if(request.getAttribute("execute").equals("download")){
				String webLink = GDriveAPI.doownloadFile(String.valueOf(request.getAttribute("fileID")));
				//GDriveAPI.downloadFileInBackground(false, String.valueOf(request.getAttribute("fileID")))
				if(webLink!=null){
					String[] values = {
							(String) request.getAttribute("uid"),
							(String) request.getAttribute("date"),
							(String) request.getAttribute("fileID")
					};
					Clear.insertFileDetails("`filesdownloaded` (`uid`, `date`, `fileID`)", values);
					response.setHeader("Redirect", webLink);
				}else{
					String[] values = {
							(String) request.getAttribute("uid"),
							(String) request.getAttribute("date"),
							(String) request.getAttribute("fileID"),
							"failed"
					};
					Clear.insertFileDetails("`filesdownloaded` (`uid`, `date`, `fileID`, `status`)", values);
					//TODO add code to send apt info to JS ajax() function
				}
			}else if(request.getAttribute("execute").equals("delete")){
				String mimeType = GDriveAPI.deleteFileById(String.valueOf(request.getAttribute("fileID")));
				if(mimeType!=null && !mimeType.equals("application/vnd.google-apps.folder")){
					Clear.changeStatusToDeleted(String.valueOf(request.getAttribute("fileID")), String.valueOf(request.getAttribute("date")));
					response.setHeader("Redirect", "delete");
				}
			}
		}else{
			if(request.getAttribute("info")!=null)
				System.out.println("\nINFO: "+request.getAttribute("info"));
			else{
				if(!GDriveAPI.isServiceInitialised){
					InputStream is = getServletContext().getResourceAsStream(getInitParameter("JSON_KEY_FILE"));
					GDriveAPI.initialise(is);
				}

				//GDriveAPI.initialiseDWD(P12_KEY_FILE);

				//GDriveAPI.createFile(false, UPLOAD_FILE_PATH, UPLOAD_FILE_MIMETYPE, UPLOAD_FILE_PARENT_FOLDER_PATH);

				//GDriveAPI.deleteFileById("0B6mY5et8RxcQanFJSjBLYXlnT0U");
				//GDriveAPI.deleteFileByName(false, "inscription-83f4af3440cc");

				//System.out.println("\n'mimeType.json' MIMETYPE: "+GDriveAPI.getMimeType("mimeType.json"));

				//GDriveAPI.updateFile(true, "0B6mY5et8RxcQY2FyVF9KRHVjUU0", "personal");

				GDriveAPI.readAllFiles(false, false);

				//GDriveAPI.downloadFile(true, "0B6mY5et8RxcQNGs1Wk9jQ09GSUE");

				//GDriveAPI.searchFileByName("personal");
			}
		}

		if(request.getAttribute("reload")==null)
			response.sendRedirect("dashboard.jsp"+execute);
		else
			request.setAttribute("reload", null);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			if(request.getParameter("fname")!=null && request.getParameter("fpath")!=null && request.getParameter("desc")!=null){
				String file_name = request.getParameter("fname");
				String file_path = request.getParameter("fpath").replace("\\", "\\/");
				String desc = request.getParameter("desc");

				HttpSession s = request.getSession(false);
				if(s!=null){
					if(Clear.validateSession(s.getAttribute("uid"))){
						String userData = Clear.getParentFolderPath(s.getAttribute("uid"));

						if(userData!=null){
							request.setAttribute("execute", "upload");
							request.setAttribute("uid", Clear.getSingleVariable(s.getAttribute("uid"), "id"));
							request.setAttribute("date", String.valueOf(new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date())));
							request.setAttribute("fname", file_name);
							request.setAttribute("fpath", file_path);
							request.setAttribute("desc", desc);
							request.setAttribute("parents", userData);
						}else{
							request.setAttribute("info", "Incomplete User Profile");							
						}
					}else{
						request.setAttribute("info", "Invalid Attempt");						
					}
				}else{
					request.setAttribute("info", "Session Expired");
				}
			}else if(request.getParameter("buttonData")!=null){
				String buttonData = request.getParameter("buttonData");
				System.out.println("\nBUTTON CLICKED: "+buttonData);
				String[] parts = buttonData.split("_", 2);

				HttpSession s = request.getSession(false);
				if(s!=null){
					if(Clear.validateSession(s.getAttribute("uid"))){

						request.setAttribute("execute", parts[0].toLowerCase());
						request.setAttribute("uid", Clear.getSingleVariable(s.getAttribute("uid"), "id"));
						request.setAttribute("date", String.valueOf(new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date())));
						request.setAttribute("fileID", parts[1]);
						request.setAttribute("reload", "false");
					}else{
						request.setAttribute("info", "Invalid Attempt");						
					}
				}else{
					request.setAttribute("info", "Session Expired");
				}
			}else{
				request.setAttribute("info", "Data Missing");				
			}
		} catch(Exception e){
			request.setAttribute("info", "Unauthorised Access Detected");
			e.printStackTrace();
		}

		doGet(request, response);
	}

}
