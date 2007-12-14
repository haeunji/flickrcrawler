package org.FlickrCrawler.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import snaq.db.ConnectionPool;

import com.mysql.jdbc.ResultSet;


public class Database {
	//static String databaseUrl = "jdbc:mysql://rogeryin.no-ip.org:3307/flickrcrawler";
	static String databaseUrl = "jdbc:mysql://localhost/flickrcrawler";
	static String user = "flickr";
	static String passwd = "1234";
	
	public Database(){
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("database.txt"));
			String str;
	        while ((str = in.readLine()) != null) {
	            if ( str.substring(0, 3).equals("url")){
	            	databaseUrl = "jdbc:mysql://"+str.substring(4);
	            }
	            if ( str.substring(0, 4).equals("user")){
	            	user = str.substring(5);
	            }
	            if ( str.substring(0, 8).equals("password")){
	            	passwd = str.substring(9);
	            }
	        }
	        in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot find database.txt file! Program will now terminate.");
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	static public Connection getConnection()
	{
		Connection con = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(databaseUrl, user, passwd);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();

		}
		return con;
	}
	
	
	/**
	 * This function adds one entry in the database table fav_pic
	 * 
	 * @param userid
	 * @param pictureid
	 * @param pictureownerid
	 */
	public void addFav(
			String userid,
			String pictureid,
			String pictureownerid
			){
		
		Statement s;
		Connection con = null;

		try {
			con = getConnection();
			s = con.createStatement();
			String query = "INSERT INTO fav_picture ( id,userid,pictureid,pictureownerid,timestamp ) VALUES " +
			"(NULL,'"+userid+"','"+pictureid+"','"+pictureownerid+"',NOW())";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try { con.close(); }
			catch (SQLException e) { }
		}
		
		
	}
	
	
	public void addContact(
			String userid, 
			String contactid){
		//PRE: All inputs are valid
		//POST: one of the contact of userid is added to the table, a timestamp of insertion time will
		//be added by database upon data insertion. 
		Connection con = null;
		try
		{
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO contact_list (id ,userid ,contactid,timestamp ) VALUES " +
					"(NULL,'"+userid+"','"+contactid+"',NOW())";
			s.executeUpdate(query); // Executing Query
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally {
			try { con.close(); }
			catch (SQLException e) { }
		}
	}
	
	public void addUserNumbers(
			String userid,
			int num_of_pics,
			int num_of_contacts,
			int num_of_favs){
		//PRE: All inputs are valid
		//POST: The number of a user at a time is updated
		Connection con = null;
		try
		{
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO pic_contact_fav_numbers (id ,userid ,num_of_pics,num_of_contacts,num_of_favs,timestamp ) VALUES " +
					"(NULL,'"+userid+"','"+num_of_pics+"','"+num_of_contacts+"','"+num_of_favs+"',NOW())";
			s.executeUpdate(query); // Executing Query
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}
	}
	
	
	public void addPicNumbers(
			int PictureId,
			int num_of_tags,
			int num_of_comments){
		//PRE: All inputs are valid
		//POST: The number of a pic at a time is updated
		Connection con = null;
		try
		{
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO tags_comments_numbers (id ,pictureid ,num_of_tags,num_of_comments,timestamp ) VALUES " +
					"(NULL,'"+PictureId+"','"+num_of_tags+"','"+num_of_comments+"',NOW())";
			s.executeUpdate(query); // Executing Query
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}
	}
	
	public void addPicture(
			String userid,
			String pictureid){
		//PRE:
		//POST: database table user_picture is updated with latest picture 
		
		Connection con = null;
		
		try{
		con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO user_picture (id,userid,pictureid,timestamp ) VALUES " +
				" (NULL, '"+userid+"','"+pictureid+"',NOW())";
		s.executeUpdate(query);
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try { con.close(); }
			catch (SQLException e) { }
		}
	}
	
	public void addPictureDetails(
			String pictureid,
			long l,
			long m){
		//Add a picture detail to Picture Detail table
		Connection con = null;
		Statement s;
		try {
			con = getConnection();
			s = con.createStatement();
			String query = "INSERT INTO picture_details (`pictureid`,`date_posted`,`last_update` ) VALUES " +
				" ( '"+pictureid+"',FROM_UNIXTIME('"+l+"'),FROM_UNIXTIME('"+m+"'))";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// If SQLException happens, means Picture already in database, ignore
			//e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

	}
	
	public void addTagToPic(
			String pictureid,
			String tagid,
			String tag,
			String tag_author){
		//PRE:
		//POST: a tag is added to a corresponding picture, including author info
		
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO tags_of_a_pic (id,pictureid,tagid,tag,tag_author,timestamp ) VALUES " +
					" (NULL, '"+pictureid+"','"+tagid+"','"+tag+"','"+tag_author+"',NOW())";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

	}
	
	public void addCommentToPic(
			String authorid,
			String commentid,
			String pictureid,
			long datecreated){
		//PRE:
		//POST: a comment id is added to a corresponding picture, including author info
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO comments_of_a_pic (id,authorid,commentid,pictureid,datecreate,timestamp ) VALUES " +
					" (NULL, '"+authorid+"','"+commentid+"','"+pictureid+"',FROM_UNIXTIME('"+datecreated+"'),NOW())";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

	}
	
	public void addTagToUser(
			String userid,
			String tagid,
			String tag){
		//PRE:
		//POST: a Tag is added under the corresponding userid 
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO user_tag (id,userid,tagid,tag,timestamp ) VALUES " +
					" (NULL, '"+userid+"','"+tagid+"','"+tag+"',NOW())";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

	}
	
	public void addUser(
			String userid){
		//PRE:
		//POST: a user is added to the list of user table
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "INSERT INTO list_of_user (userid ) VALUES " +
					" ( '"+userid+"')";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

	}
	
	public ArrayList getUserList() {
		//
		//Post: returns an ArrayList of Usernames
		ArrayList<String> userList = new ArrayList<String>();
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "SELECT userid FROM list_of_user";
			ResultSet temp = (ResultSet) s.executeQuery(query);
			int i=0;
			while(temp.next()) //Cycling through rows in ResultSet
			{
				String j = temp.getString("userid");
				userList.add(j);
				i++;
			}
			System.out.println("Total of "+i+" userids loaded.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

		return userList;
	}
	
	/**
	 * To use for testing purpose only, loads first 30 users.
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUserListTest(){
		//
		//Post: returns an ArrayList of Usernames
		ArrayList<String> userList = new ArrayList<String>();
		Connection con = null;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "SELECT userid FROM list_of_user";
			ResultSet temp = (ResultSet) s.executeQuery(query);
			int i=0;
			while(temp.next()) //Cycling through rows in ResultSet
			{
				String j = temp.getString("userid");
				userList.add(j);
				i++;
				if (i == 30){break;}
			}
			System.out.println("Total of "+i+" userids loaded.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { con.close(); }
			catch (SQLException e) { }
		}

		return userList;
	}
	
	
	/**
	 * Check If User ID exists in the List_Of_User Table
	 * @param UserId
	 * @return
	 *  True if Exist
	 *  False if not exist
	 * @throws SQLException 
	 */
	public boolean CheckUser(String UserId){
		Connection con = null;
		ResultSet temp = null;
		boolean exist = false;
		try {
			con = getConnection();
			Statement s = con.createStatement();
			String query = "SELECT userid FROM list_of_user where userid='"+UserId+"'";
			temp = (ResultSet) s.executeQuery(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try { 
				if (!temp.next()){exist= false;}
				else { exist= true;}
				con.close(); 
			}
			catch (SQLException e) { }
		}
		return exist;

	}
}
