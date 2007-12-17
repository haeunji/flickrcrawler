package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.FlickrCrawler.runtime.flickrcrawler;
import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.favorites.FavoritesInterface;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

public class FavouriteCrawler {
	
	FavoritesInterface favoritesinterface;
	Database db;
	
	public FavouriteCrawler(Flickr f){
		
		favoritesinterface = f.getFavoritesInterface();
		db = flickrcrawler.db;
		
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
			FavePhotoList.addAll(tempPhotoList);
			int totalPages = tempPhotoList.getPages();
			int total = tempPhotoList.getTotal();
			
			for (int j=2;j<=totalPages;j++){
				tempPhotoList = favoritesinterface.getPublicList(UserId, perPage, j, Extras);
				FavePhotoList.addAll(tempPhotoList);
			
			}
			
			System.out.println("total fav = "+total);
			System.out.println("total FavePhotoList = "+FavePhotoList.size());
			


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
		return FavePhotoList;
	}
	
	/**
	 * Takes the UserId and get a list of fav pics by this userid then store the in
	 * the database
	 * 
	 * 
	 * @param UserId
	 */
	public void Crawl(ArrayList<String> UserIdList){
		
		java.util.Iterator<String> itr = UserIdList.iterator();
		int counter = 1;
		while (itr.hasNext()){
			String UserId = itr.next();
			System.out.println("Favourite List Crawling : "+counter+" / "+UserIdList.size());
			PhotoList favPhotoList = this.getListOfFavPictures(UserId);
			int num_of_favs = favPhotoList.size();
			java.util.Iterator<Photo> PhotoListItr = favPhotoList.iterator();
			System.out.println("	Adding "+num_of_favs+" favourites, please wait...");
			db.addUserFavNumbers(UserId, num_of_favs);
			while( PhotoListItr.hasNext()){
				Photo tempPhoto = PhotoListItr.next();

				db.addFav(UserId, tempPhoto.getId(), tempPhoto.getOwner().getId());

			}
			counter++;
			
		}
		
	}

}
