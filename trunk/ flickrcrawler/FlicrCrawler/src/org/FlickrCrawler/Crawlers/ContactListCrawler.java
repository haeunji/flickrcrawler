package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.contacts.*;

public class ContactListCrawler {
	ContactsInterface contactsinterface;

	public ContactListCrawler(Flickr f){
		contactsinterface = f.getContactsInterface();
	}

	/**
	 * Crawl() gets all the contact list for all the UserID listed in UserIDList
	 * If a contactid is not already in the list_of_user table, it will be added
	 * to the table. This addition should not be included in the current iteration
	 * as All ids are loaded into memory at the start of the program.
	 * 
	 * @param UserIDList
	 * @throws IOException
	 * @throws SAXException
	 * @throws FlickrException
	 * @throws SQLException
	 * @throws InterruptedException 
	 */
	public void Crawl(ArrayList<String> UserIDList) throws SAXException, SQLException, InterruptedException{
		Collection<Contact> ContactsCollection = null;
		Iterator itr = UserIDList.iterator();
		Database db = new Database();
		int counter=0;
		//db.getConnection();
		while(itr.hasNext()){
			try {
				ContactsCollection=contactsinterface.getPublicList((String) itr.next());
				counter++;
			} catch (IOException e1) {
				//IOException happens when the connection is timedout, need to sleep for longer
				//This number is still experimental, trying to figure out the best sleep time for flickr
				Thread.sleep(500);
			} catch (FlickrException e1) {
				//Sometimes FlickrException happens when the crawler is 
				//crawling too fast, sleep for a bit to get things going
				Thread.sleep(200);
			}
			Iterator itr2 = ContactsCollection.iterator();
			while(itr2.hasNext()){
				String userid = (String) itr.next();
				// Get the UserID of the Contact 
				String contactid = ((Contact) itr2.next()).getId();
				// If the contact is not already in the List_of_user table, add it
				if (!db.CheckUser(contactid)){db.addUser(contactid);}
				db.addContact(userid, contactid);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Contact List Crawling : "+counter+"/"+UserIDList.size());
		}
	}
}
