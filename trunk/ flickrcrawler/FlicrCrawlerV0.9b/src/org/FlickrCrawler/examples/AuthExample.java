package org.FlickrCrawler.examples;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.util.IOUtilities;


/**
 * 
 * If you registered API keys, you find them with the shared secret at your
 * <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API keys</a>
 * 
 * @author mago
 * @version $Id: AuthExample.java,v 1.3 2005/12/19 23:03:04 x-mago Exp $
 */
public class AuthExample {
	static String restHost = "www.flickr.com";
        Auth auth;
	Flickr f;
	REST rest;
	RequestContext requestContext;
        String apiKey = "";
        String secret = "";
	String frob = "";
	String token = "";
	Properties properties = null;

	public AuthExample(String api,String sec) throws ParserConfigurationException, IOException, SAXException {
//		InputStream in = null;
//		try {
//			in = getClass().getResourceAsStream("/setup.properties");
//			properties = new Properties();
//			properties.load(in);
//		} finally {
//			IOUtilities.close(in);
//		}
                apiKey = api;
                secret = sec;
		rest = new REST();
		rest.setHost(restHost);
		f = new Flickr(apiKey,rest);
		Flickr.debugStream = false;
		// Set the shared secret which is used for any calls which require signing.
		requestContext = RequestContext.getRequestContext();
		requestContext.setSharedSecret(secret);
		AuthInterface authInterface = f.getAuthInterface();
		try {
			frob = authInterface.getFrob();
		} catch(FlickrException e) {
			e.printStackTrace();
		}
		System.out.println("frob: "+frob);
		URL url = authInterface.buildAuthenticationUrl(Permission.WRITE, frob);
		System.out.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());
		BufferedReader infile =
		new BufferedReader ( new InputStreamReader (System.in) );
		String line = infile.readLine();
		try {
			auth = authInterface.getToken(frob);
			System.out.println("Authentication success");
			System.out.println("Token: "+auth.getToken());
			System.out.println("nsid: "+auth.getUser().getId());
			System.out.println("Realname: "+auth.getUser().getRealName());
			System.out.println("Username: "+auth.getUser().getUsername());
			System.out.println("Permission: "+auth.getPermission().getType());
		} catch(FlickrException e) {
			System.out.println("Authentication failed");
			e.printStackTrace();
		}
                auth.setToken(auth.getToken());
	}
        
        /**
         *Returns the authenticated Flickr instance
         **/
        public Flickr getFlickr(){
            return f;
        }
        
        public String getFrob(){
            return frob;
        }
        
        public Auth getAuth(){
            return auth;
        }
        
        public String getToken(){
            return auth.getToken();
            
        }
}
