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
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.tags.Tag;
import com.aetrion.flickr.tags.TagsInterface;

public class TagCrawler {
	
	TagsInterface tagsinterface;
	Database db;
	Photo photo;
	ArrayList<Tag> tags;
	
	public TagCrawler(Flickr f){
		tagsinterface = f.getTagsInterface();
		db = flickrcrawler.db;
		photo = new Photo();
		tags = new ArrayList<Tag>();
		
	}
	
	public int crawl(String PhotoId){
		try {
			photo = tagsinterface.getListPhoto(PhotoId);
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
		
		tags =  (ArrayList<Tag>) photo.getTags();
		Iterator<Tag> itr=  tags.iterator();
		while(itr.hasNext()){
			Tag tempTag = itr.next();
			String tagid = tempTag.getId();
			String tag = "";
			String tag_author = tempTag.getAuthor();
			db.addTagToPic(PhotoId, tagid, tag, tag_author);
		}
		return tags.size();
	}

}
