package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.util.Set;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
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
		User tempUser = null;
		
		//Max perPage set by flickr is 100
		int perPage = 100,page = 0,totalPictureNum,pageNumber;
		
		extras.add("date_upload");
		extras.add("last_update");
		extras.add("tags");
		
		PhotoList list_of_photo = null;
		
		//tempUser holds the current userid's information
		//We use it to get the total number of pictures the 
		//current user has. Then convert it to 
		try {
			tempUser = peopleinterface.getInfo(UserID);
			
			//Get the total number of pictures
			totalPictureNum = tempUser.getPhotosCount();
			pageNumber = (int) Math.ceil(totalPictureNum/perPage);
			
			for (int i=0;i<pageNumber;i++){
				list_of_photo.addAll(peopleinterface.getPublicPhotos(UserID, extras, perPage, i));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list_of_photo;
		

		
	}
	


}
