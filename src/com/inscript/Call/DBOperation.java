package com.inscript.Call;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import com.inscript.Call.Clear;

public class DBOperation {
	private static Connection con = null;

	public static boolean addUser(User u){
		try{
			if(con==null)
				con=Database.getConnection();

			String auto="ALTER TABLE `user` AUTO_INCREMENT = 1";
			PreparedStatement pst=con.prepareStatement(auto);
			pst.executeUpdate();
			//System.out.println("alteration successfull");
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(5001) + 1000;			//range: 1000-6000

			if(Clear.send(u.getName()+":"+u.getEmail()+":"+String.valueOf(randomInt), 2)){			
				String q="INSERT INTO `user` (`name`, `email`, `password`, `gender`, `initials`, `mailTicket`) " +
						"VALUES ('"+u.getName()+
						"', '"+u.getEmail()+
						"', '"+u.getPassword()+
						"', '"+u.getGender()+
						"', '"+u.getInitials()+
						"', "+randomInt+")";
				pst=con.prepareStatement(q);
				pst.executeUpdate();

				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return false;
	}

	public static boolean addMultiUser(MultiUser u){
		try{
			if(con==null)
				con=Database.getConnection();

			String auto="ALTER TABLE `userDetails` AUTO_INCREMENT = 1";
			PreparedStatement pst=con.prepareStatement(auto);
			pst.executeUpdate();
			//System.out.println("alteration successfull");
			String q="INSERT INTO `userDetails` (`uid`, `dob`, `mobile`, `profileURL`, `instituteID`, `courseID`, `semesterID`, `memberType`) "
					+ "VALUES ("+u.getUid()+
					", '"+u.getDob()+
					"', '"+u.getMobile()+
					"', '"+u.getProfileURL()+
					"', "+u.getInstituteID()+
					", "+u.getCourseID()+
					", "+u.getSemesterID()+
					", '"+u.getMemberType()+"')";
			pst=con.prepareStatement(q);
			pst.executeUpdate();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return false;
	}

	public static String fetch(String src,int val){		//'0' for name:passwordTicket and '1' for password
		try{
			if(con==null)
				con=Database.getConnection();

			PreparedStatement ps;
			if(val==1){
				ps=con.prepareStatement("SELECT password FROM user WHERE email=? LIMIT 1");
			}else{
				ps=con.prepareStatement("SELECT name FROM user WHERE email=? LIMIT 1");
			}

			ps.setString(1, src);

			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				if(val==1){
					return rs.getString(1);
				}else{
					return rs.getString(1) + ":" + insertTicket(src,null,0); 
				}
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return null;
	}

	public static int insertTicket(String src,String pass,int type){	//'0' for requestReset,'1' for resetSuccess,'2' for mailVerified and '3' for multiSignupStatus
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(4999) + 1;			//number generated in the range 1-4999
		//System.out.println(randomInt);

		try{
			if(con==null)
				con=Database.getConnection();

			PreparedStatement ps;
			String q = null;

			if(type==0){
				q = "UPDATE user SET passwordTicket="+randomInt+" WHERE email='"+src+"'";
				//System.out.println("\nQUERY: "+q);
				ps = con.prepareStatement(q);
			}else if(type==1){
				String passwordHash = Clear.hash(pass.trim());
				q = "UPDATE user SET passwordTicket=NULL, password='"+passwordHash+"' WHERE email='"+src+"'";
				//System.out.println("\nQUERY: "+q);
				ps = con.prepareStatement(q);
			}else if(type==2){
				q = "UPDATE user SET mailTicket=NULL, verified='yes' WHERE email='"+src+"'";
				//System.out.println("\nQUERY: "+q);
				ps = con.prepareStatement(q);
			}else if(type==3){
				q = "UPDATE user SET multiSignupStatus='done' WHERE email='"+src+"'";
				//System.out.println("\nQUERY: "+q);
				ps = con.prepareStatement(q);				
			}else
				return 5050;


			if(ps.executeUpdate()==1){
				//System.out.println("success:"+randomInt);
				return randomInt;
			}else{
				//System.out.println("failure");
				return 5050;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return 5050;
	}

	public static boolean auth(String id, String key){
		try{
			if(con==null)
				con=Database.getConnection();

			PreparedStatement ps = con.prepareStatement("SELECT id FROM user WHERE id="+id+" AND ticket="+key+" LIMIT 1");
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return false;
	}

	public static String getVariables(String src, String[] get) {
		try{
			if(con==null)
				con=Database.getConnection();

			String cols = get[0];
			for(int i=1;i<get.length;i++)
				cols += ","+get[i];

			String q = "SELECT " + cols + " FROM user WHERE email='" + src + "' LIMIT 1";
			//System.out.println("\nQUERY: " + q);
			PreparedStatement ps = con.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				String data = "";
				for(int i=0;i<get.length;i++){						
					if(i!=0)
						data += ":"+rs.getString(i+1);
					else if(get[i].equals("id") || get[i].equals("mailTicket") || get[i].equals("passwordTicket"))			//"id" OR "mailTicket" OR "passwordTicket" must always be at 0th index in get[], if present!
						data += String.valueOf(rs.getInt(i+1));
					else
						data += rs.getString(i+1);
				}

				if(data.isEmpty())
					return null;
				else
					return data;
			}else
				return null;
		}catch(Exception e){
			e.printStackTrace();
			Database.closeConnection(con);
			con = null;
			return null;
		}
	}

	public static String getParentFolderPath(String src){
		try{
			if(con==null)
				con=Database.getConnection();

			String q = "SELECT `userdetails`.`instituteID`, `userdetails`.`courseID`, `userdetails`.`semesterID` FROM userdetails INNER JOIN user ON `userdetails`.`uid`=`user`.`id` WHERE `user`.`email`='"+src+"' LIMIT 1";
			//System.out.println("\nQUERY: " + q);
			PreparedStatement ps = con.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				String data = rs.getInt(1)+"/"+rs.getInt(2)+"/"+rs.getInt(3);

				if(data!=null && !data.isEmpty())
					return data;
			}
		}catch(Exception e){
			e.printStackTrace();
			Database.closeConnection(con);
			con = null;
		}

		return null;
	}

	public static boolean alterCookieData(String src, String encodedData, int type){
		try{
			if(con==null)
				con=Database.getConnection();

			String q = null;
			if(type==0)
				q = "UPDATE user SET cookieData='"+encodedData+"' WHERE email='"+src+"'";
			else
				q = "UPDATE user SET cookieData=NULL WHERE email='"+src+"'";

			//System.out.println("\nQUERY: "+q);

			PreparedStatement ps = con.prepareStatement(q);
			int result = ps.executeUpdate();

			if(result==1)
				return true;
			else
				return false;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}

		return false;
	}

	public static void insertFileDetails(String tableWithCols, String[] values){
		try{
			if(con==null)
				con=Database.getConnection();
			
			String auto="ALTER TABLE "+tableWithCols.split(" ", 2)[0]+" AUTO_INCREMENT = 1";
			PreparedStatement ps = con.prepareStatement(auto);
			ps.executeUpdate();
			
			String valueString = values[0];
			for(int i=1;i<values.length;i++)
				valueString += ", '"+values[i]+"'";

			String q = "INSERT INTO "+tableWithCols+" VALUES ("+valueString+")";
			//System.out.println("\nQUERY: "+q);
			ps = con.prepareStatement(q);
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}
	}

	public static void changeStatusToDeleted(String fileID, String date){
		try{
			if(con==null)
				con=Database.getConnection();

			String q = "UPDATE filesuploaded SET status='deleted', deletionTime='"+date+"' WHERE fileID='"+fileID+"'";
			//System.out.println("\nQUERY: "+q);
			PreparedStatement ps = con.prepareStatement(q);
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			Database.closeConnection(con);
			con = null;
		}
	}
	
	public static String discuss(String email){
		try{
			if(con==null)
				con=Database.getConnection();

			String q = "SELECT `userdetails`.`instituteID`, `userdetails`.`courseID`, `userdetails`.`semesterID` FROM userdetails INNER JOIN user ON `userdetails`.`uid`=`user`.`id` WHERE `user`.`email`='"+email+"' LIMIT 1";
			PreparedStatement ps = con.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				String data = rs.getInt(1)+"/"+rs.getInt(2)+"/"+rs.getInt(3);

				if(data!=null && !data.isEmpty())
					return data;
			}
		}catch(Exception e){
			e.printStackTrace();
			Database.closeConnection(con);
			con = null;
		}

		return null;
	}
}