import java.util.*;
import java.lang.*;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.util.IOUtilities;
import javax.xml.parsers.ParserConfigurationException;

/*
 * flickrcrawler.java
 *
 * Created on November 1, 2007, 12:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Roger
 */
public class flickrcrawler {
    static String restHost = "www.flickr.com";
    String apiKey = "778b0fbe7fff7b24c9d7e97a11add8f5";
    String frob;
    String token;
    REST rest;
    Flickr f;
    static String secret = "ba20e9e81829dde5";
    
    public static void main(String[] args) throws InterruptedException {
        Authorize authorization=null;
        try {
            authorization = new Authorize();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        ArrayList validUsers = new ArrayList();

//        try {
//            authorization = new AuthExample(apiKey,secret);
//            flickr = authorization.getFlickr();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
        RandomUserGenerator gen = new RandomUserGenerator(authorization.getFlickr());
        validUsers = gen.validateUserId(gen.RandomUserIDGenerator());
        System.out.println("Finished! Following a list of valid user ids");
        System.exit(0);
    }
    

}
