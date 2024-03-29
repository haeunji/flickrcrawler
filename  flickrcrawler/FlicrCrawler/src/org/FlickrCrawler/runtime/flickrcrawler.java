package org.FlickrCrawler.runtime;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.contacts.ContactsInterface;
import com.aetrion.flickr.util.IOUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.FlickrCrawler.PreCrawlTools.*;
import org.FlickrCrawler.database.Database;

import org.FlickrCrawler.Authorization.Authorize;
import org.FlickrCrawler.Crawlers.CommentCrawler;
import org.FlickrCrawler.Crawlers.ContactListCrawler;
import org.FlickrCrawler.Crawlers.FavouriteCrawler;
import org.FlickrCrawler.Crawlers.PictureInfoCrawler;
import org.FlickrCrawler.Crawlers.TagCrawler;
import org.xml.sax.SAXException;

import snaq.db.ConnectionPool;

/*
 * flickrcrawler.java
 *
 * Created on November 1, 2007, 12:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Roger
 */
public class flickrcrawler {
    static String restHost = "www.flickr.com";
    String apiKey = "778b0fbe7fff7b24c9d7e97a11add8f5";
    String frob;
    String token;
    REST rest;
    static Flickr f;
    static String secret = "ba20e9e81829dde5";
    public static Database db = new Database();
    
    public static void main(String[] args) throws InterruptedException, SQLException {
        Authorize authorization=null;
        try {
            authorization = new Authorize();
            f = authorization.getFlickr();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        
        Map params = getArgValue(args);
        
        String filename = (String) params.get("file");
        if (filename == null || "".equals(filename)){
			System.out.println("Pleasy specify generated file name by file=filename.txt");
			System.out.println("Use the following command : ");
			System.out.println("        java -jar flickrcrawler.jar file=UserList.txt ");
			System.out.println("Database.txt needs to be configured correctly before this program is executed.");
			System.exit(1);
		}
        
        /**
         * Testing database connection pool
         */


        /**
         * Grabs a list of userIds from the UserListFile.txt.
         */
        ArrayList<String> UserIdList = new ArrayList();
        try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
	        while ((str = in.readLine()) != null) {
	        	UserIdList.add(str.trim());
	        }
	        System.out.println("Successfully loaded "+UserIdList.size()+" userids.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("UserId list file cannot be found. FlickrCrawler will now terminate.");
		} catch (IOException e){
			e.printStackTrace();
		}
        
        /*try {
			//UserIdList = db.getUserListTest();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		crawl(UserIdList);


        System.out.println("Finished! ");
        System.exit(0);
    }
    
    /**'
     * THis is the central function, crawls everything.
     * It reads from the UserId list,
     * Then goes thru each user id,
     * 		Grabs all the contact list of each of the user
     * 		Then Grabs All the Pictures of each of the User
     * 				Out of each pictures, grab all the comments.
     * 							  grab all the tags
     * 		Then Grabs all the favorites of the user
     * Then jumps to next user and start the whole process again
     * @param UserIdList
     * @throws InterruptedException 
     * @throws SQLException 
     */
    public static void crawl(ArrayList UserIdList) throws SQLException, InterruptedException{
    	
		/**
		 * The following part of code crawls the Contact List and updates
		 * corresponding tables in the database
		 */
        ContactListCrawler contactlistcrawler = new ContactListCrawler(f);
        try {
			contactlistcrawler.Crawl(UserIdList);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * The Following part of code crawls picture ownership and picture details 
		 * And updates corresponding tables in database
		 * CommentCrawler and TagCrawler part is integrated into this as well. 
		 */
		PictureInfoCrawler pictureinfocrawler = new PictureInfoCrawler(f);
        pictureinfocrawler.crawl(UserIdList);

        
        
        /**
         * FavouriteCrawler 
         */
		FavouriteCrawler favouritecrawler = new FavouriteCrawler(f);
		favouritecrawler.Crawl(UserIdList);
		
    }
    
    public void generateRandom(Flickr f) throws InterruptedException{
        /**
         * The Following two lines are used to generate a list of random user id
         * into a file named userid.txt
         * 
         * This generator needs to be used before adduserlist to database is called.
         * 
         * They are commented out as they are not required on crawling time.
         */
        RandomUserGenerator gen = new RandomUserGenerator(f);
        gen.validateUserId(gen.RandomUserIDGenerator());
    	
    }
    
    public void addUser2db(){
        /**
         * The following two lines are used to initialize user-list in database, Initial 
         * userids are read from prepared userid.txt file and inserted into the database 
         * with the table named list_of_user. 
         * The timestamp will be updated upon re-insertion.
         * Duplicated records are not allowed in database.
         * If reinitializing is required, the database table needs to be emptied first
         * 
         * They are commented out as they are not required on crawling time.
         */
        
        AddInitialUserListToDatabase s = new AddInitialUserListToDatabase();
        s.createUserIdList();
    }
    
	private static Map getArgValue(String[] args) {
        Map params = new HashMap();
        for (int i = 0; i < args.length; i++) {
			String curr = args[i];
            String argName = curr.toLowerCase();
            String argValue = "";

			int eqIndex = curr.indexOf('=');
			if (eqIndex >= 0) {
				argName = curr.substring(0, eqIndex).trim();
				argValue = curr.substring(eqIndex+1).trim();
            }

            params.put(argName.toLowerCase(), argValue);
        }
		
		return params; 
	}

}
