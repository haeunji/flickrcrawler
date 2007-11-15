package org.FlickrCrawler.PreCrawlTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;
import org.FlickrCrawler.database.*;

public class AddInitialUserListToDatabase {
	
	String UserIDListFileName = "Userid.txt";
	
	public ArrayList<String> createUserIdList(){
		ArrayList<String> UserID= null;
		Database db = new Database();
		try{
			BufferedReader in = new BufferedReader(new FileReader(this.UserIDListFileName));
			String str;
			Connection con = db.getConnection();
			Statement s = null;
			try {
				s = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while ((str = in.readLine()) != null){
				//do something to this UserID
				//UserID.add(str);
				String query = "INSERT INTO list_of_user (userid ) VALUES " +
				" ('"+str+"')";
				try {
					s.executeUpdate(query);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		return UserID;
	}
}
