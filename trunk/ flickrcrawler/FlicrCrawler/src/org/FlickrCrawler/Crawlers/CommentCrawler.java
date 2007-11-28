package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.comments.Comment;
import com.aetrion.flickr.photos.comments.CommentsInterface;

public class CommentCrawler {
	CommentsInterface commentsinterface;
	Database db;
	ArrayList<Comment> CommentsList;
	String apiKey;
	
	public CommentCrawler(Flickr f){

		apiKey = f.getApiKey();

		
	}
	
	public void crawl(String PictureId){

		try {
			CommentsList = new ArrayList<Comment>();
			commentsinterface = new CommentsInterface(apiKey, null);
			ArrayList<Comment> list = (ArrayList<Comment>) commentsinterface.getList(PictureId);
			CommentsList = list;
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<Comment> itr = CommentsList.iterator();
		while(itr.hasNext()){
			String author = itr.next().getAuthor();
			String commentid = itr.next().getId();
			Date datecreate = itr.next().getDateCreate();
			
			try {
				db.addCommentToPic(author, commentid, PictureId, datecreate.getTime()/1000);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
