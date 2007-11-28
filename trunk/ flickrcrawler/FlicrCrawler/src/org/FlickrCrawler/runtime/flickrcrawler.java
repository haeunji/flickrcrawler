package org.FlickrCrawler.runtime;
import java.sql.SQLException;
import java.util.*;
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
    
    public static void main(String[] args) throws InterruptedException, SQLException {
        Authorize authorization=null;
        try {
            authorization = new Authorize();
            f = authorization.getFlickr();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }

        /**
         * The Following two lines are used to generate a list of random user id
         * into a file named userid.txt
         * 
         * This generator needs to be used before adduserlist to database is called.
         * 
         * They are commented out as they are not required on crawling time.
         */
//        RandomUserGenerator gen = new RandomUserGenerator(authorization.getFlickr());
//        validUsers = gen.validateUserId(gen.RandomUserIDGenerator());
        
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
        
        //AddInitialUserListToDatabase s = new AddInitialUserListToDatabase();
        //s.createUserIdList();
        
        Database db = new Database();
        ArrayList<String> UserIdList = null;
        try {
			UserIdList = db.getUserList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * The following part of code crawls the Contact List and updates
		 * corresponding tables in the database
		 */
		/*
        ContactListCrawler contactlistcrawler = new ContactListCrawler(f);
        try {
			contactlistcrawler.Crawl(UserIdList);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		/**
		 * The Following part of code crawls picture ownership and picture details 
		 * And updates corresponding tables in database. 
		 */
		
		/*
		PictureInfoCrawler pictureinfocrawler = new PictureInfoCrawler(f);

        pictureinfocrawler.crawl(UserIdList);

		*/
		
		
		FavouriteCrawler favouritecrawler = new FavouriteCrawler(f);
		favouritecrawler.getListOfFavPictures("90933305@N00");
		
		
		/**
		 * The following part of code crawls picture comments
		 * and tags, using a test picture id
		 */
		
		CommentCrawler commentcrawler = null;
		try {
			commentcrawler = new CommentCrawler(f);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commentcrawler.crawl("417646359");
		
		TagCrawler tagcrawler = new TagCrawler(f);
		tagcrawler.crawl("417646359");


        System.out.println("Finished! ");
        System.exit(0);
    }
    

}
