package com.inscript.Call;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

public class GDriveAPI {
	private static Drive driveService = null;
	private static JsonFactory JSON_FACTORY = null;
	private static HttpTransport httpTransport = null;
	public static boolean isServiceInitialised = false;
	//private static final String CLIENT_SECRET_FILE = "<path to CLIENT_SECRET_FILE>";
	private static final String APPLICATION_NAME = "Inscription | Sharing Notes is now Easy";
	private static final String DIR_FOR_DOWNLOADS = "/Downloads/Inscription";		//example: "D:/folder1/folder2"

	public static void initialise(InputStream is){

		if(is!=null){
			System.out.println("\n -->JSON_KEY_FILE file found");

			try {
				GoogleCredential credential = GoogleCredential.fromStream(is).createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

				JSON_FACTORY = JacksonFactory.getDefaultInstance();
				httpTransport = GoogleNetHttpTransport.newTrustedTransport();

				credential.refreshToken();
				System.out.println(" -->tokenURI : "+credential.getTokenServerEncodedUrl());
				System.out.println(" -->accessToken : "+credential.getAccessToken());
				System.out.println(" -->expiration : "+String.valueOf(credential.getExpirationTimeMilliseconds()));
				System.out.println(" -->refreshToken : "+credential.getRefreshToken());

				driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
						.setApplicationName(APPLICATION_NAME)
						.build();

				System.out.println(" -->service request success");

				/*System.out.println(" -->printing data");

				System.out.println("\n -->app name : "+driveService.getApplicationName()+
						"\n -->base URL : "+driveService.getBaseUrl()+
						"\n -->root URL : "+driveService.getRootUrl()+
						"\n -->service path : "+driveService.getServicePath());*/

				isServiceInitialised = true;
			} catch (IOException e) {
				System.out.println(" -->service request failed");
				e.printStackTrace();
			} catch(GeneralSecurityException e){
				System.out.println(" -->service request failed");
				e.printStackTrace();
			} catch(Exception e){
				System.out.println(" -->service request failed");
				e.printStackTrace();			
			}
		}else
			System.out.println("\n -->JSON_KEY_FILE file not found");		
	}

	public static void initialiseDWD(String p12KeyFilePath){		//DWD - Domain Wide Delegation
		try{
			JSON_FACTORY = JacksonFactory.getDefaultInstance();
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();

			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					//.setTokenServerEncodedUrl("https://accounts.google.com/o/oauth2/token")
					//.setClientSecrets("117945090407725632611", CLIENT_SECRET_FILE)
					.setServiceAccountId("accessinscript-252@inscription-152612.iam.gserviceaccount.com")
					.setServiceAccountPrivateKeyFromP12File(new java.io.File(p12KeyFilePath))
					//.setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
					.setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE))
					.setServiceAccountUser("divyansh.agicha@gmail.com")
					.build();

			/*System.out.println("\nrefreshing token... ");

			try{
				if(credential.refreshToken()){
					//credential = credential.setAccessToken(credential.getAccessToken());
					credential = credential.setRefreshToken(credential.getRefreshToken());
					System.out.println("refreshed token... "+credential.getAccessToken());
				}else
					System.out.println("refresh token failed... ");
			} catch(TokenResponseException e){
				System.out.println("TokenResponseException... 401 Unauthorised");
			}*/

			driveService = new Drive.Builder(httpTransport, JSON_FACTORY, null)
					.setHttpRequestInitializer(credential)
					.setApplicationName(APPLICATION_NAME)
					.build();
		} catch(Exception e){
			System.out.println(" -->service request failed");
			e.printStackTrace();
		}
	}


	/** ----------------- CREATE ----------------- */

	/** returns fileId : extension : fileSize */
	public static String createFile(boolean useDirectUpload, InputStream is, String UPLOAD_FILE_NAME, String UPLOAD_FILE_PATH, String UPLOAD_FILE_DESCRIPTION, String parentFolderPath){
		System.out.println("\ncreating file...");
		List<String> parentReference = createFoldersPath(parentFolderPath.split("/"));

		if(parentReference!=null){
			try{
				java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
				String mimeType = getMimeType(is, UPLOAD_FILE.getName());

				if(mimeType!=null){
					String[] parts = mimeType.split(":");

					File fileMetadata = new File();
					fileMetadata.setParents(parentReference);
					fileMetadata.setName(UPLOAD_FILE_NAME+parts[1]);
					fileMetadata.setMimeType(parts[0]);
					fileMetadata.setDescription(UPLOAD_FILE_DESCRIPTION);

					FileContent mediaContent = new FileContent(parts[0], UPLOAD_FILE);

					Drive.Files.Create create = driveService.files().create(fileMetadata, mediaContent).setFields("*");

					MediaHttpUploader uploader = create.getMediaHttpUploader();
					uploader.setDirectUploadEnabled(useDirectUpload);
					uploader.setProgressListener(new MediaHttpUploaderProgressListener() {

						@Override
						public void progressChanged(MediaHttpUploader arg0) throws IOException {
							// TODO Auto-generated method stub
							System.err.println(String.valueOf(arg0.getNumBytesUploaded())+" bytes uploaded");
						}
					});

					File file = create.execute();

					System.out.println("\nFile ID: " + file.getId());
					shareFile(file.getId());
					return file.getId()+":"+parts[1]+":"+refineSize(String.valueOf(file.getSize()));
				}
			} catch(IOException e){
				System.out.println("\nFile creation failed!!!");
				e.printStackTrace();
			} catch(Exception e){
				System.out.println("\nFile creation failed!!!");
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param folderName the folder's title
	 * @param parentReferenceList the list of parents references where you want the folder to be created, 
	 * if you have more than one parent references, then a folder will be created in each one of them  
	 * @return google drive file object   
	 * @throws IOException
	 */
	private static File createFolder(String folderName, List<String> parentReferenceList) throws IOException {
		File fileMetadata = new File();
		fileMetadata.setName(folderName);
		fileMetadata.setParents(parentReferenceList);
		fileMetadata.setMimeType("application/vnd.google-apps.folder");

		System.out.print("creating folder \""+folderName+"\"... ");

		File folder = null;
		try {
			folder = driveService.files().create(fileMetadata)
					.setFields("id")
					.execute();

			System.out.println("Folder ID: " + folder.getId());
			shareFile(folder.getId());
			return folder;
		} catch (IOException e) {
			System.out.println("Folder creation failed");
			throw new IOException(e);
		} catch (Exception e) {
			System.out.println("Folder creation failed");
			throw new IOException(e);
		}
	}

	/**
	 * 
	 * @param titles list of folders titles (for String...titles)
	 * i.e. if your path like this folder1/folder2/folder3 then pass them in this order createFoldersPath(service, folder1, folder2, folder3)
	 * @return parent reference of the last added folder in case you want to use it to create a file inside this folder.
	 * @throws IOException
	 */
	private static List<String> createFoldersPath(String[] titles) {
		System.out.print("\ncreating folder path: \"");
		for(String folder : titles){
			System.out.print("/"+folder);
		}
		System.out.println("\"... ");

		List<String> parentReferenceList = new ArrayList<String>();
		File file = null;

		try{
			for(int i=0;i<titles.length;i++) {
				file = searchFolder(titles[i], (file==null)?"root":file.getId());
				if (file == null) {
					file = createFolder(titles[i], parentReferenceList);
				}
				parentReferenceList.clear();
				parentReferenceList.add(file.getId());
			}

			System.out.println("\nPATH CREATION SUCCESSFUL");
			return parentReferenceList;				
		}catch(IOException e){
			System.out.println("\nPATH CREATION FAILED");
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("\nPATH CREATION FAILED");
			e.printStackTrace();
		}

		return null;
	}


	/** ----------------- READ ----------------- */

	public static void readAllFiles(boolean performComplexSearch, boolean deleteAllFiles){
		if(driveService!=null){
			System.out.print("\nreading files... ");
			FileList result = null;

			if(performComplexSearch){
				String pageToken = null;

				do {
					try{
						result = driveService.files().list()
								//.setQ("mimeType='image/jpeg'")
								.setSpaces("drive")
								.setFields("nextPageToken, files(id, name)")
								.setPageToken(pageToken)
								.execute();

						System.out.println(result.getFiles().size()+" file(s) found");

						for(File file: result.getFiles()) {
							System.out.printf("\nFound file: %s (id: %s)\n",
									file.getName(), file.getId());

							/*System.out.printf("\nFound file: %s (id: %s) (mimeType: %s) (size: %d)\n",
							file.getName(), file.getId(), file.getMimeType(), file.getSize());*/
							if(deleteAllFiles)
								deleteFileById(file.getId());
						}

						pageToken = result.getNextPageToken();
						System.out.println("\n---------next page token---------");
					} catch(IOException e){
						if(e.getMessage().equals("401 Unauthorized"))
							System.out.println("TokenResponseException... 401 Unauthorised");
						else
							e.printStackTrace();
					} catch(Exception e){
						System.out.println("Error occured!!!");
						e.printStackTrace();
					}
				} while (pageToken != null);
			}else{
				try {
					result = driveService.files().list()
							.setFields("files(*)")
							//.setFields("files(id, name)")
							.execute();

					System.out.println(result.getFiles().size()+" file(s) found");

					for(File file: result.getFiles()) {
						System.out.println("\nFound file: "+file.getName()+
								" (id: "+String.valueOf(file.getId())+
								")\n (mimeType: "+String.valueOf(file.getMimeType())+
								")\n (kind: "+String.valueOf(file.getKind())+
								/** ------- below this requires setting setFields("files(*)") ------ */
								")\n (description: "+String.valueOf(file.getDescription())+
								")\n (size: "+refineSize(String.valueOf(file.getSize()))+
								")\n (originalFilename: "+String.valueOf(file.getOriginalFilename())+
								")\n (fileExtension: "+String.valueOf(file.getFileExtension())+
								")\n (fullFileExtension: "+String.valueOf(file.getFullFileExtension())+
								")\n (isAppAuthorized: "+String.valueOf(file.getIsAppAuthorized())+
								")\n (webContentLink: "+String.valueOf(file.getWebContentLink())+
								")\n (webViewLink: "+String.valueOf(file.getWebViewLink())+
								")\n (createdTime: "+String.valueOf(file.getCreatedTime())+
								")\n (explicitlyTrashed: "+String.valueOf(file.getExplicitlyTrashed())+
								")\n (Md5Checksum: "+String.valueOf(file.getMd5Checksum())+
								")\n (modifiedByMe: "+String.valueOf(file.getModifiedByMe())+
								")\n (lastModifyingUser: "+String.valueOf(file.getLastModifyingUser())+")");

						if(deleteAllFiles)
							deleteFileById(file.getId());
					}
				} catch (IOException e) {
					if(e.getMessage().equals("401 Unauthorized"))
						System.out.println("TokenResponseException... 401 Unauthorised");
					else
						e.printStackTrace();
				} catch(Exception e){
					System.out.println("Error occured!!!");
					e.printStackTrace();
				}
			}

			System.out.println("\nreading files complete!!!");
		}else
			System.out.println("\nService not available!!!");
	}


	/** ----------------- UPDATE ----------------- */

	public static void updateFile(boolean isFolder, String fileID,  final String UPLOAD_FILE_PATH) {
		System.out.print("\nupdating file \""+fileID+"\"... ");

		File fileMetadata = new File();
		String newFileName = null;

		if(isFolder){
			newFileName = UPLOAD_FILE_PATH;
			fileMetadata.setName(newFileName+"_EDITED");
		}else{
			java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);
			newFileName = UPLOAD_FILE.getName();
			fileMetadata.setName(newFileName+"_EDITED");
		}

		File file = null;
		try {
			File original = searchFileById(fileID);

			Drive.Files.Update update = driveService.files().update(fileID, fileMetadata);
			file = update.execute();

			System.out.println("File Updation successful: \""+original.getName()+"\" -> \""+newFileName+"\"");
			shareFile(file.getId());
			//downloadFile(true, file);
		} catch (IOException e) {
			System.out.println("(File: "+fileID+") update error: IOException occured");
		} catch(Exception e){
			if(e.getMessage().equals("InvalidIdException"))
				System.out.println("(File: "+fileID+") update error: InvalidIdException occured");
			else{
				System.out.println("(File: "+fileID+") update error: some error occured");
				e.printStackTrace();
			}
		}
	}


	/** ----------------- DELETE ----------------- */

	/** returns mimeType of deleted file for DB manipulation */
	public static String deleteFileById(String fileID){
		File file = null;
		try {
			file = searchFileById(fileID);

			driveService.files().delete(fileID).execute();
			System.out.println("\nFile \""+file.getName()+"\"(id: "+fileID+") deleted successfully!!!");
			return file.getMimeType();			
		} catch (IOException e) {
			System.out.println("\nFile \""+file.getName()+"\"(id: "+fileID+") deletion failed!!!");
			e.printStackTrace();
		} catch(Exception e){
			if(e.getMessage().equals("InvalidIdException"))
				System.out.println("\nFile \"\"(id: "+fileID+") deletion failed!!!... InvalidIdException occured");
			else if(e.getMessage().contains("404 Not Found"))
				System.out.println("\nFile \"\"(id: "+fileID+") deletion failed!!!... FileNotFoundException occured");				
			else
				e.printStackTrace();
		}
		
		return null;
	}

	public static void deleteFileByName(boolean deleteRecursively, String fileName){
		FileList result = searchFileByName(fileName);

		if(result!=null && result.getFiles().size()>0){
			for(File file : result.getFiles()){	
				String fileID = file.getId();				
				try{
					driveService.files().delete(fileID).execute();
					System.out.println("\nFile \""+file.getName()+"\"(id: "+fileID+") deleted successfully!!!");
				}catch(IOException e){
					System.out.println("\nFile \""+file.getName()+"\"(id: "+fileID+") deletion failed!!!");
					e.printStackTrace();
				}catch(Exception e){
					System.out.println("\nFile \""+file.getName()+"\"(id: "+fileID+") deletion failed!!!");
					e.printStackTrace();
				}

				if(!deleteRecursively)
					break;
			}
		}
	}


	/** ----------------- SEARCH ----------------- */

	public static File searchFileById(String fileID) throws Exception{
		File file = null;
		try{
			file = driveService.files().get(fileID).setFields("*").execute();
		} catch(Exception e){
			if(e.getMessage().contains("404 Not Found"))
				throw new Exception("InvalidIdException");
			else
				e.printStackTrace();
		}

		if(file!=null && file.getName()!=null)
			return file;
		else
			throw new Exception("InvalidIdException");
	}

	public static FileList searchFileByName(String fileName){
		System.out.print("\nsearching file \""+fileName+"\"...");
		FileList result = null;

		try {
			result = driveService.files().list()
					.setQ("fullText contains '"+fileName+"'")
					.execute();

			if(result.getFiles().size()>0){
				System.out.print(" Result: ("+String.valueOf(result.getFiles().size())+" file(s) found)\n");
				for(File file: result.getFiles()) {
					System.out.println(" File Found \""+file.getName()+"\"(id: "+String.valueOf(file.getId())+")");
				}

				return result;
			}
		} catch (IOException e) {
			if(e.getMessage().equals("401 Unauthorized"))
				System.out.println("TokenResponseException... 401 Unauthorised");
			else
				e.printStackTrace();
		} catch (Exception e) {
			System.out.println("error occured");
			e.printStackTrace();
		}

		System.out.println(" File NOT Found");
		return null;
	}

	/**
	 * 
	 * @param folderName the title (name) of the folder (the one you search for)
	 * @param parentId the parent Id of this folder (use root) if the folder is in the main directory of google drive
	 * @return google drive file object 
	 * @throws IOException
	 */
	private static File searchFolder(String folderName, String parentId) throws IOException {
		System.out.print("\nsearching folder \""+folderName+"\"... ");

		Drive.Files.List request = null;

		try{
			request = driveService.files().list();

			String query = "mimeType='application/vnd.google-apps.folder' AND trashed=false AND fullText contains '" + folderName + "' AND '" + parentId + "' in parents";
			//Logger.info(TAG + ": isFolderExists(): Query= " + query);
			request = request.setQ(query);

			FileList files = request.execute();
			//Logger.info(TAG + ": isFolderExists(): List Size =" + files.getItems().size());
			if (files.getFiles().size() == 0) { //if the size is zero, then the folder doesn't exist
				System.out.println("folder does not exist");
				return null;
			}else {
				System.out.println("folder exist");

				//since google drive allows to have multiple folders with the same title (name)
				//we select the first file in the list to return
				return files.getFiles().get(0);
			}
		} catch(IOException e){
			System.out.println("IOException occured");
			throw new IOException(e);
		} catch(Exception e){
			System.out.println("Exception occured");
			throw new IOException(e);
		}
	}


	/** ----------------- SHARE ----------------- */

	private static void shareFile(String fileId){
		System.out.println("sharing file... "+fileId);

		JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
			@Override
			public void onFailure(GoogleJsonError e,
					HttpHeaders responseHeaders)
							throws IOException {
				// Handle error
				System.err.println(e.getMessage());
			}

			@Override
			public void onSuccess(Permission permission,
					HttpHeaders responseHeaders)
							throws IOException {
				System.out.println("Permission ID: " + permission.getId());
			}
		};

		BatchRequest batch = driveService.batch();
		Permission userPermission = new Permission()
				.setType("user")
				.setRole("writer")
				.setEmailAddress("divyansh.agicha@gmail.com");
		
		Permission publicPermission = new Permission()
		        .setType("anyone")
		        .setRole("reader");

		try {
			driveService.permissions().create(fileId, userPermission)
			.setFields("id")
			.queue(batch, callback);

			driveService.permissions().create(fileId, publicPermission)
			.setFields("id")
			.queue(batch, callback);

			batch.execute();
		} catch (IOException e) {
			System.out.println("exception occured while sharing file");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("exception occured while sharing file");
			e.printStackTrace();
		}
	}


	/** ----------------- DOWNLOAD ----------------- */
	
	public static String doownloadFile(String fileID){
		try{
			System.out.print("\ndownloading file ");
			File uploadedFile = searchFileById(fileID);
			System.out.print("'"+uploadedFile.getName()+"'... ");

			System.out.println("(download successfull)\ngetting file from link '"+String.valueOf(uploadedFile.getWebContentLink())+"'");
			return uploadedFile.getWebContentLink();
		} catch(Exception e){
			System.out.println("(download Error: Invalid File ID)");
		}
		
		return null;
	}

	public static boolean downloadFileInBackground(boolean useDirectDownload, String fileID) {
		try{
			System.out.print("\ndownloading file ");
			File uploadedFile = searchFileById(fileID);
			System.out.print("'"+uploadedFile.getName()+"'... ");

			java.io.File parentDir = new java.io.File(System.getProperty("user.home")+DIR_FOR_DOWNLOADS);

			if(parentDir.exists() || parentDir.mkdirs()){
				OutputStream outputStream = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getName()));

				try {
					driveService.files().get(fileID).executeMediaAndDownloadTo(outputStream);
					//driveService.files().export(fileId, uploadedFile.getMimeType()).executeMediaAndDownloadTo(outputStream);
					outputStream.flush();
					System.out.println("(download successfull)");
					return true;
				} catch (IOException e) {
					System.out.println("(download Error: IOException occured)");
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("(download Error: Exception occured)");
					e.printStackTrace();
				} finally {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				System.out.println("(download Error: Unable to create parent directory)");				
			}
		} catch (FileNotFoundException e) {
			System.out.println("(download Error: File not found and error creating it)");
		} catch(Exception e){
			System.out.println("(download Error: Invalid File ID)");
		}

		return false;

		/*MediaHttpDownloader downloader = new MediaHttpDownloader(httpTransport, driveService.getRequestFactory().getInitializer());
		downloader.setDirectDownloadEnabled(useDirectDownload);
		downloader.setProgressListener(new MediaHttpDownloaderProgressListener() {

			@Override
			public void progressChanged(MediaHttpDownloader arg0) throws IOException {
				// TODO Auto-generated method stub

			}
		});

		try {
			downloader.download(new GenericUrl(uploadedFile.getWebContentLink()), out);
		} catch (IOException e) {
			System.out.println("\n(File: "+uploadedFile.getName()+")download error...IOException occured");
		}*/

		/*String webContentLink = file.getWebContentLink();
		InputStream in = new URL(webContentLink).openStream();*/
	}


	/** ----------------- MIMETYPE ----------------- */

	private static String getMimeType(InputStream is, String fileName){		//returns mimeType:extension
		String[] parts = fileName.split("\\.");
		System.out.print("\n'"+fileName+"' ");

		int index = parts.length - 1;
		String fileExt = "."+parts[index];
		System.out.print("--> MimeType for file extension '"+fileExt+"'... ");
		Scanner input = null;

		try{
			if(is!=null){
				input = new Scanner(is);
				StringBuilder jsonString = new StringBuilder();  
				while(input.hasNextLine()) {  
					jsonString.append(input.nextLine());  
				}

				JSONArray json = (JSONArray) new JSONParser().parse(jsonString.toString());

				for(int i=0;i<json.size();i++){				
					JSONObject data = (JSONObject) json.get(i);
					if(data!=null){
						String extension = (String)data.get("extension");

						if(extension.contains(fileExt.toLowerCase())){
							System.out.println(data.get("mimeType").toString());
							return data.get("mimeType").toString()+":"+fileExt;
						}
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(input!=null)
				input.close();
			
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		System.out.println("null");
		return null;
	}


	/** ----------------- SIZE ----------------- */

	private static String refineSize(String sizeInBytes){
		if(sizeInBytes==null || sizeInBytes.equals("null") || sizeInBytes.isEmpty())
			return "0 B";

		Long size = Long.parseLong(sizeInBytes);

		if(size<1024)
			return sizeInBytes+" B";
		else if(size<1048576)
			return String.valueOf(size/1024)+" KB";
		else
			return String.valueOf(size/(1024*1024))+" MB";
	}
}
