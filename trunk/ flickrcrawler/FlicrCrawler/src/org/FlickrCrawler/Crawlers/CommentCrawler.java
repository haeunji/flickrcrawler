package org.FlickrCrawler.Crawlers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.FlickrCrawler.database.Database;
import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.comments.Comment;
import com.aetrion.flickr.photos.comments.CommentsInterface;

public class CommentCrawler {
	CommentsInterface commentsinterface;
	Database db;
	ArrayList<Comment> CommentsList;
	String apiKey;
	REST rest;
	static String restHost = "www.flickr.com";
	
	public CommentCrawler(Flickr f) throws ParserConfigurationException{

		apiKey = f.getApiKey();
		rest = new REST();
		rest.setHost(restHost);
		db = new Database();
		
	}
	
	public int crawl(String PictureId){

		try {
			CommentsList = new ArrayList<Comment>();
			commentsinterface = new CommentsInterface(apiKey, rest);
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
		long date_create=0;
		while(itr.hasNext()){
			Comment tempComment = itr.next();
			String author = tempComment.getAuthor();
			String commentid = tempComment.getId();
			
			if (null == tempComment.getAuthor()){}else{date_create = tempComment.getDateCreate().getTime()/1000;}
			
			try {
				db.addCommentToPic(author, commentid, PictureId, date_create);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return CommentsList.size();
		
	}

}
