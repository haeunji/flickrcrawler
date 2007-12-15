package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.FlickrCrawler.database.Database;
import org.FlickrCrawler.runtime.flickrcrawler;
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
		Database db = flickrcrawler.db;
		//Database db = new Database();
		int counter=0;
		//db.getConnection();
		while(itr.hasNext()){
			String userid = (String)itr.next();
			try {
				ContactsCollection=contactsinterface.getPublicList(userid);
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
			int number_contacts = ContactsCollection.size();
			int latest_contact_num = db.GetLatestContactNum(userid);
			System.out.println("Contact List Crawling : "+counter+"/"+UserIDList.size());			
			if ( number_contacts == latest_contact_num ){
				System.out.println("	No change since last crawl, skip to next userid.");
			}else{

				System.out.println("	Adding " + number_contacts
						+ " contacts, Please wait.");
				while (itr2.hasNext()) {
					// Get the UserID of the Contact
					String contactid = ((Contact) itr2.next()).getId();
					// If the contact is not already in the List_of_user table,
					// add it

					if (!db.CheckUser(contactid)) {
						db.addUser(contactid);
					}
					db.addContact(userid, contactid);
				}
			}
			db.addUserContactNumbers(userid, number_contacts);
		}
	}
}
