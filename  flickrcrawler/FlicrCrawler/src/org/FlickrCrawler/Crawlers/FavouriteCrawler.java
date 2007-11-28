package org.FlickrCrawler.Crawlers;

import java.io.IOException;

import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.favorites.FavoritesInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.PhotoList;

public class FavouriteCrawler {
	
	FavoritesInterface favoritesinterface;
	Database db;
	
	public FavouriteCrawler(Flickr f){
		
		favoritesinterface = f.getFavoritesInterface();
		db = new Database();
		
	}
	
	public PhotoList getListOfFavPictures(String UserId){
		User tempUser = new User();
		PhotoList tempPhotoList = new PhotoList();
		PhotoList FavePhotoList = new PhotoList();
		int perPage = 100; // Flickr max perpage is 100
		int Page=1; //Initial to be first page.
		
		String[] Extras=null;
		try {
			tempPhotoList = favoritesinterface.getPublicList(UserId, perPage, Page, Extras);
			int total = tempPhotoList.getTotal();
			
			/**
			 * If size of tempPhotoList <perPage, no more pages, jumps out of loop
			 */
			while(tempPhotoList.size()==perPage){
				FavePhotoList.addAll(tempPhotoList);
				Page++;
				tempPhotoList = favoritesinterface.getPublicList(UserId, perPage, Page, Extras);
				
			}
			FavePhotoList.addAll(tempPhotoList);

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
		return tempPhotoList;
	}
	
	/**
	 * Takes the UserId and get a list of fav pics by this userid then store the in
	 * the database
	 * 
	 * 
	 * @param UserId
	 */
	public void Crawl(String UserId){
		//favoritesinterface.getPublicList(UserId, arg1, arg2, arg3)
	}

}
