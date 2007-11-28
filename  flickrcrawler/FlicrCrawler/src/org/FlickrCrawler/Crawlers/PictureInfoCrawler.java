package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.people.PeopleInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.comments.CommentsInterface;

public class PictureInfoCrawler {
	
	PhotosInterface photosinterface;
	PeopleInterface peopleinterface;
	Database db;

	
	public PictureInfoCrawler(Flickr f){
		photosinterface = f.getPhotosInterface();
		peopleinterface = f.getPeopleInterface();
		db = new Database();
		
	}
	
	public PhotoList getListOfPhotos(String UserID){
		//extras corresponding to extra fields to get when crawling
		//In this crawler we get date_upload,last_update, and tags
		Set<String> extras = (Set) new TreeSet<String>();
		User tempUser = null;
		
		//Max perPage set by flickr is 100
		int perPage = 100,page = 0,totalPictureNum,pageNumber;
		
		extras.add("date_upload");
		extras.add("last_update");
		extras.add("tags");
		
		PhotoList list_of_photo = new PhotoList();
		
		
		//tempUser holds the current userid's information
		//We use it to get the total number of pictures the 
		//current user has. Then convert it to 
		try {
			tempUser = peopleinterface.getInfo(UserID);
			
			//Get the total number of pictures
			totalPictureNum = tempUser.getPhotosCount();
			
			//if no pic, return null
			if (totalPictureNum == 0){return null;}
			
			pageNumber = (int)(totalPictureNum/perPage+1);
			
			for (int i=0;i<pageNumber;i++){
				PhotoList tempPhotoList=peopleinterface.getPublicPhotos(UserID, extras, perPage, i+1);
				Iterator itr=tempPhotoList.iterator();
				while(itr.hasNext()){
					list_of_photo.add(itr.next());
				}
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
	
	/**
	 * This function gets the date_posted, and last_update from a specific picture
	 * and store them into the database. And update user_picture table with userid,pictureid
	 * @param PictureID
	 */
	public void getPictureDetails(String PictureID){
		Photo tempPhoto = new Photo();
		String photoId = PictureID;
		String userid;
		long date_posted = 0;
		long last_update = 0;
		//optional secret, not used
		String secret = null;
		try {
			tempPhoto = photosinterface.getInfo(photoId, secret);
			
			//Sometimes the date_posted or last_update is null, so need this to bypass nullPointerException
			if (null == tempPhoto.getDatePosted()){}else{date_posted = tempPhoto.getDatePosted().getTime()/1000;}
			if (null == tempPhoto.getLastUpdate()){}else{last_update = tempPhoto.getLastUpdate().getTime()/1000;}
			User owner = tempPhoto.getOwner();
			
			db.addPictureDetails(photoId, date_posted, last_update);
			db.addPicture(owner.getId(), photoId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		
	}
	
	public void crawl(ArrayList<String> UserIdList){
		Iterator itr = UserIdList.iterator();
		PhotoList tempPhotoList = new PhotoList();
		int counter = 0;
		//For each User, get a list of photos
		while(itr.hasNext()){
			tempPhotoList = this.getListOfPhotos((String) itr.next());
			//If there the user has no pictures
			if (null != tempPhotoList){
				Iterator<Photo> itrPhoto = tempPhotoList.iterator();
				
				//For each photos, get the details
				while(itrPhoto.hasNext()){
					this.getPictureDetails(itrPhoto.next().getId());
				}
			}
			
			counter++;
			System.out.println("PictureInfoCrawler Progress : "+counter+" / "+UserIdList.size());
		}
	}


}
