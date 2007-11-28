package org.FlickrCrawler.Crawlers;

import org.FlickrCrawler.database.Database;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.favorites.FavoritesInterface;

public class FavouriteCrawler {
	
	FavoritesInterface favoritesinterface;
	Database db;
	
	public FavouriteCrawler(Flickr f){
		
		favoritesinterface = f.getFavoritesInterface();
		db = new Database();
		
	}
	
	/**
	 * Takes the UserId and get a list of fav pics by this userid then store the in
	 * the database
	 * 
	 * 
	 * @param UserId
	 */
	public void Crawl(String UserId){
		favoritesinterface.getPublicList(UserId, arg1, arg2, arg3)
	}

}
