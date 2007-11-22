package org.FlickrCrawler.Crawlers;

import java.util.Set;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;

public class PictureInfoCrawler {
	
	PhotosInterface photosinterface;
	PeopleInterface peopleinterface;
	public PictureInfoCrawler(Flickr f){
		photosinterface = f.getPhotosInterface();
		peopleinterface = f.getPeopleInterface();
		
	}
	
	public PhotoList getListOfPhotos(String UserID){
		//extras corresponding to extra fields to get when crawling
		//In this crawler we get date_upload,last_update, and tags
		Set<String> extras = null;
		User tempUser;
		int perPage,page,totalPictureNum;
		
		extras.add("date_upload");
		extras.add("last_update");
		extras.add("tags");
		
		PhotoList list_of_photo = null;
		
		//tempUser holds the current userid's information
		//We use it to get the total number of pictures the 
		//current user has. Then convert it to 
		tempUser = peopleinterface.getInfo(UserID);
		
		//Get the total number of pictures
		totalPictureNum = tempUser.getPhotosCount();
		list_of_photo=peopleinterface.getPublicPhotos(UserID, extras, perPage, page);
	}

}
