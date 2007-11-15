package org.FlickrCrawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSet;


public class Database {
	static String databaseUrl = "jdbc:mysql://rogeryin.no-ip.org:3307/flickrcrawler";
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
	public static void addContact(
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
	
	public static void addPicture(
			String userid,
			String pictureid,
			String date_upload,
			String last_update) throws SQLException{
		//PRE:
		//POST: database table user_picture is updated with latest picture 
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO user_picture (id,userid,pictureid,date_upload,last_update,timestamp ) VALUES " +
				" (NULL, '"+userid+"','"+pictureid+"','"+date_upload+"','"+last_update+"',NOW())";
		s.executeUpdate(query);
	}
	
	public static void addTagToPic(
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
	
	public static void addCommentToPic(
			String authorid,
			String commentid,
			String pictureid,
			String datecreate) throws SQLException{
		//PRE:
		//POST: a comment id is added to a corresponding picture, including author info
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "INSERT INTO comments_of_a_pic (id,authorid,commentid,pictureid,datecreate,timestamp ) VALUES " +
				" (NULL, '"+authorid+"','"+commentid+"','"+pictureid+"','"+datecreate+"',NOW())";
		s.executeUpdate(query);
	}
	
	public static void addTagToUser(
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
		ArrayList<String> userList = null;
		Connection con = getConnection();
		Statement s = con.createStatement();
		String query = "SELECT userid FROM list_of_user";
		ResultSet temp = (ResultSet) s.executeQuery(query);
		int i=0;
		while(temp.next()) //Cycling through rows in ResultSet
		{
			userList.add(temp.getString("userid"));
			i++;
		}
		System.out.println("Total of "+i+" userids loaded.");
		return userList;
	}
}