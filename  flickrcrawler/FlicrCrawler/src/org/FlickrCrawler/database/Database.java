package org.FlickrCrawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.ResultSet;


public class Database {
	//static String databaseUrl = "jdbc:mysql://rogeryin.no-ip.org:3307/flickrcrawler";
	static String databaseUrl = "jdbc:mysql://localhost/flickrcrawler";
	static String user = "flickr";
	static String passwd = "1234";
	
	
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
		Connection con = getConnection();
		Statement s;
		try {
			s = con.createStatement();
			String query = "INSERT INTO fav_picture ( id,userid,pictureid,pictureownerid,timestamp ) VALUES " +
			"(NULL,'"+userid+"','"+pictureid+"','"+pictureownerid+"',NOW())";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void addContact(
			String userid, 
			String contactid){
		//PRE: All inputs are valid
		//POST: one of the contact of userid is added to the table, a timestamp of insertion time will
		//be added by database upon data insertion. 
		Connection con = getConnection();
		try
		{
			Statement s = con.createStatement();
			String query = "INSERT INTO contact_list (id ,userid ,contactid,timestamp ) VALUES " +
					"(NULL,'"+userid+"','"+contactid+"',NOW())";
			s.executeUpdate(query); // Executing Query
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void addPicture(
			String userid,
			String pictureid) throws SQLException{
		//PRE:
		//POST: database table user_picture is updated with latest picture 
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO user_picture (id,userid,pictureid,timestamp ) VALUES " +
				" (NULL, '"+userid+"','"+pictureid+"',NOW())";
		s.executeUpdate(query);
	}
	
	public void addPictureDetails(
			String pictureid,
			long l,
			long m){
		//Add a picture detail to Picture Detail table
		Connection con = getConnection();
		Statement s;
		try {
			s = con.createStatement();
			String query = "INSERT INTO picture_details (`pictureid`,`date_posted`,`last_update` ) VALUES " +
				" ( '"+pictureid+"',FROM_UNIXTIME('"+l+"'),FROM_UNIXTIME('"+m+"'))";
			s.executeUpdate(query);
		} catch (SQLException e) {
			// If SQLException happens, means Picture already in database, ignore
			//e.printStackTrace();
		}

	}
	
	public void addTagToPic(
			String pictureid,
			String tagid,
			String tag,
			String tag_author) throws SQLException{
		//PRE:
		//POST: a tag is added to a corresponding picture, including author info
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO tags_of_a_pic (id,pictureid,tagid,tag,tag_author,timestamp ) VALUES " +
				" (NULL, '"+pictureid+"','"+tagid+"','"+tag+"','"+tag_author+"',NOW())";
		s.executeUpdate(query);
	}
	
	public void addCommentToPic(
			String authorid,
			String commentid,
			String pictureid,
			long datecreated) throws SQLException{
		//PRE:
		//POST: a comment id is added to a corresponding picture, including author info
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO comments_of_a_pic (id,authorid,commentid,pictureid,datecreate,timestamp ) VALUES " +
				" (NULL, '"+authorid+"','"+commentid+"','"+pictureid+"',FROM_UNIXTIME('"+datecreated+"'),NOW())";
		s.executeUpdate(query);
	}
	
	public void addTagToUser(
			String userid,
			String tagid,
			String tag) throws SQLException{
		//PRE:
		//POST: a Tag is added under the corresponding userid 
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO user_tag (id,userid,tagid,tag,timestamp ) VALUES " +
				" (NULL, '"+userid+"','"+tagid+"','"+tag+"',NOW())";
		s.executeUpdate(query);
	}
	
	public static void addUser(
			String userid) throws SQLException{
		//PRE:
		//POST: a user is added to the list of user table
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO list_of_user (userid ) VALUES " +
				" ( '"+userid+"')";
		s.executeUpdate(query);
	}
	
	public ArrayList getUserList() throws SQLException{
		//
		//Post: returns an ArrayList of Usernames
		ArrayList<String> userList = new ArrayList<String>();
		Connection con = getConnection();
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
		return userList;
	}
	
	/**
	 * To use for testing purpose only, loads first 30 users.
	 * @return
	 * @throws SQLException
	 */
	public ArrayList getUserListTest() throws SQLException{
		//
		//Post: returns an ArrayList of Usernames
		ArrayList<String> userList = new ArrayList<String>();
		Connection con = getConnection();
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
	public boolean CheckUser(String UserId) throws SQLException{
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "SELECT userid FROM list_of_user where userid='"+UserId+"'";
		ResultSet temp = (ResultSet) s.executeQuery(query);
		if (!temp.next()){return false;}
		else { return true;}
		
	}
}
