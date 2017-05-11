package com.inscript.Call;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Clear {

	/** ----------------- PRIVATE ----------------- */

	private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private static String getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException
	{
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if(paddingLength > 0)
		{
			return String.format("%0"  +paddingLength + "d", 0) + hex;
		}else{
			return hex;
		}
	}

	private static boolean validatePassword(String enteredPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(enteredPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for(int i = 0; i < hash.length && i < testHash.length; i++)
		{
			diff |= hash[i] ^ testHash[i];
		}
		//System.out.println("\nvalidatePassword: "+diff);
		return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
	{
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i<bytes.length ;i++)
		{
			bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}


	/** ----------------- PUBLIC ----------------- */

	public static String hash(String args) throws NoSuchAlgorithmException, InvalidKeySpecException {	//to generate hash from entered password
		String originalPassword = args;
		String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
		return generatedSecuredPasswordHash;
	}

	public static String init(String name) {	//to generate initials from the UserName	    	
		int cnt=0;
		char l=0;

		name = name.trim();

		for(int i=0;i<name.length();i++)
		{
			if(name.charAt(i)==' ')
			{
				cnt++;
				if(cnt==1)
					l = name.charAt(i+1);
			}
		}

		char f = name.charAt(0);
		String initials;
		if(l!=0)
			initials = ""+f+l;
		else
			initials = ""+f;

		return initials.toUpperCase();
	}

	public static boolean authenticate(String src,String enteredPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {	//to match passwords during login
		String[] getValuesOf = {"password"};
		String DBPassword = DBOperation.getVariables(src, getValuesOf);

		boolean matched = validatePassword(enteredPassword, DBPassword);
		//System.out.println(String.valueOf(matched));
		return matched;
	}

	public static boolean send(String email,int val) {	//'0' for contactus,'1' for fpass and '2' for confirmationMail
		int contact = 0;
		String to=null, msg=null, subj=null;
		String[] det = null;
		String[] parts = null;

		if(email.indexOf(":+91")>6 && val==0){
			det = email.split(":");
			contact=1;
		}else if(val==1){
			String ans = DBOperation.fetch(email, 0);			//returns name:ticket

			if(ans==null){ return false; }

			parts = ans.split(":");

			if(parts[1]=="5050"){ return false; }
		}else if(val==2){
			parts = email.split(":");		// email = name:email:ticket
		}else{
			return false;
		}

		if(contact==1){
			to = "div.agicha@gmail.com";
			subj = "SeekFresh User Query";
			msg = "Name -- " + det[0] +
					"<br>Email -- <a href='mailto:" + det[1] + "'>" + det[1] +
					"</a><br>Mobile -- " + det[2] +
					"<br>Query -- " + det[3];
		}else if(val==1){
			to = email;
			subj = "Reset Inscription account password";
			msg = "Dear " + parts[0].toUpperCase() + ",<br/><br/>" +
					"Follow the link below to reset your <strong>account password</strong> :<br>" + 
					"<a href='http://localhost:9999/Inscript/forgotpassword.jsp?id=fpass&user="+email+"&ticket="+parts[1]+"' target='_blank'>Reset Password</a>";
		}else if(val==2){
			to = parts[1];
			subj = "Confirm Inscription Account";

			msg = "Dear " + parts[0].toUpperCase() + ",<br/><br/>" +
					"Follow the link below to verify your Email id --<br/>" +
					"<a href='http://localhost:8080/Inscript/getVerification?id=signup&user=" + parts[1] + "&ticket=" + parts[2] + "'>Confirm your Email</a>";
		}

		String from = "no-reply@inscription.com";

		Properties props = System.getProperties();

		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp.port", "25"); 
		//props.put("mail.debug", "true"); 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.starttls.enable","true"); 
		props.put("mail.smtp.EnableSSL.enable","true");

		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
		props.setProperty("mail.smtp.socketFactory.fallback", "false");   
		props.setProperty("mail.smtp.port", "465");   
		props.setProperty("mail.smtp.socketFactory.port", "465");

		Session session = Session.getDefaultInstance(props);

		try{
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from, "Inscription Support Team"));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject(subj);

			message.setContent(msg, "text/html" );

			Transport.send(message, "shivamusa666@gmail.com", "9430305069");
			return true;
		}catch (MessagingException mex) {
			//mex.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			return false;
		}
	}

	public static String getSingleVariable(Object sessionVariable, String get){
		try{
			if(sessionVariable!=null){
				UserSessionProfile usp = (UserSessionProfile) sessionVariable;
				String[] getValuesOf = {get};
				return getVariables(usp.getEmail(), getValuesOf);
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public static String getVariables(String src, String[] get) {	//returns requested data of the logged in User
		return DBOperation.getVariables(src, get);
	}
	
	public static String getParentFolderPath(Object sessionVariable){
		try{
			if(sessionVariable!=null){
				UserSessionProfile usp = (UserSessionProfile) sessionVariable;
				String path = DBOperation.getParentFolderPath(usp.getEmail());
				System.out.println("\nparent folder path for (user: "+usp.getEmail()+"): '"+path+"'");
				return path;
			}
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public static int insertTicket(String src,String pass,int type){
		return DBOperation.insertTicket(src, pass, type);
	}

	public static boolean checkTicket(String src, String ticket){
		String[] getValuesOf = {"passwordTicket"};
		String userData = getVariables(src, getValuesOf);

		if(userData!=null && userData.equals(ticket))
			return true;
		else
			return false;
	}

	public static boolean isProfileCompleted(Object sessionVariable){
		UserSessionProfile usp = null;
		try{
			usp = (UserSessionProfile) sessionVariable;
		}catch(Exception e){
			return false;
		}

		String email = usp.getEmail();
		String[] getValuesOf = {"multiSignupStatus"};
		String userData = getVariables(email, getValuesOf);

		if(userData.equals("done"))
			return true;
		else
			return false;
	}

	public static boolean validateSession(Object sessionVariable){
		UserSessionProfile usp = null;
		try{
			usp = (UserSessionProfile) sessionVariable;

			String id = usp.getId();
			String email = usp.getEmail();
			String date = usp.getDate();
			String newId = null;

			String[] getValuesOf = {"id", "initials"};
			String userData = Clear.getVariables(email, getValuesOf);

			String[] parts = date.split("/");
			double today = Double.parseDouble(parts[0]);
			today = Math.atan(today);
			newId = String.valueOf(today)+								//date
					userData.split(":")[0]+
					parts[1]+											//month
					userData.split(":")[1]+
					parts[2];											//year

			System.out.println("\nid: '"+id+"'");
			System.out.println("newId: '"+newId+"'");

			return validatePassword(newId, "1000:"+id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean validateSession(Object sessionVariable, String cookieEmail, String cookieDate){
		UserSessionProfile usp = null;
		try{
			usp = (UserSessionProfile) sessionVariable;

			String id = usp.getId();
			String email = cookieEmail;
			String date = cookieDate;
			String newId = null;

			String[] getValuesOf = {"id", "initials"};
			String userData = Clear.getVariables(email, getValuesOf);

			String[] parts = date.split("/");
			double today = Double.parseDouble(parts[0]);
			today = Math.atan(today);
			newId = String.valueOf(today)+								//date
					userData.split(":")[0]+
					parts[1]+											//month
					userData.split(":")[1]+
					parts[2];											//year

			System.out.println("\nid: '"+id+"'");
			System.out.println("newId: '"+newId+"'");

			return validatePassword(newId, "1000:"+id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean alterCookieData(String src, String encodedData, int type){	//'0' for addition and '1' for removal
		return DBOperation.alterCookieData(src, encodedData, type);
	}

	public static void insertFileDetails(String tableWithCols, String[] values){
		DBOperation.insertFileDetails(tableWithCols, values);
	}

	public static void changeStatusToDeleted(String fileID, String date){
		DBOperation.changeStatusToDeleted(fileID, date);
	}

	public static String toCamelCase(String input) {
		try{
			StringBuilder camelCase = new StringBuilder();
			boolean nextTitleCase = true;

			for (char c : input.toCharArray()) {
				if (Character.isSpaceChar(c)) {
					nextTitleCase = true;
				} else if (nextTitleCase) {
					c = Character.toTitleCase(c);
					nextTitleCase = false;
				} else{
					c = Character.toLowerCase(c);
				}

				camelCase.append(c);
			}

			return camelCase.toString();
		} catch(Exception e){
			e.printStackTrace();
			return input;
		}
	}
}