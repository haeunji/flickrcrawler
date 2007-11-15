package org.FlickrCrawler.PreCrawlTools;

import java.io.*;
import java.util.*;
import com.aetrion.flickr.*;
import com.aetrion.flickr.people.*;
import org.xml.sax.SAXException;

/*
 * RandomUserGenerator.java
 *
 * Created on November 1, 2007, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class RandomUserGenerator {
    static int MAX_RANDOM_USER =100000;
    static int MAX_SERVER_FARM_ID = 4;
    Flickr f;
    
    //this is a mapping for generating random ids
  
    
    /** Creates a new instance of RandomUserGenerator */
    public RandomUserGenerator(Flickr flickr) {
        
        f = flickr;
    }
    
    public ArrayList<String> RandomUserIDGenerator(){
        Random generator = new Random();
        
        //Create a String array list to contain Generated User Ids
        ArrayList<String> list = new ArrayList<String>();
        
        for (int i = 0; i < MAX_RANDOM_USER; i++)
        {
            /**
             * Generate a random number between K1(1000000) and K2(99999999),
             * in order to use nextInt(n) n will be K2-K1+1
             */ 
            int randomIndex = generator.nextInt(99000000)+1000000;
            
            
            for (int j=0; j< MAX_SERVER_FARM_ID;j++){
                String randomUserID = Integer.toString(randomIndex) + "@N0" + Integer.toString(j);
                list.add(randomUserID);        
            }
            
            
           
        }
        
        return list;
    }
    
    ArrayList validateUserId(ArrayList<String> userIDlist) throws InterruptedException {
        
        ArrayList<String> list = userIDlist;
        ArrayList ValidUserList = new ArrayList<User>();
        
        PeopleInterface people = f.getPeopleInterface();
        User validUser = new User();
        Iterator<String> itr = list.iterator();
        int number=0;
        int prog=1;
        while( itr.hasNext() ){
            boolean Valid = true;
            String userid = itr.next();
            try {
                validUser = people.getInfo(userid);
            } catch (FlickrException ex) {
                //System.out.println("FlickrException caught! ErrorCode is "+ ex.getErrorCode());
                if ( ex.getErrorCode().equals("1")){
                    System.out.println("Bad userid!");
                    Thread.sleep(1000);
                    
                }
                Valid = false;
            } catch (java.io.IOException ex){
                System.out.println("IOException caught!");
                Valid = false;
                Thread.sleep(30000);
                //ex.printStackTrace();
            } catch (org.xml.sax.SAXException ex){
                System.out.println("SAXException caught");
                Valid = false;
                //ex.printStackTrace();
                
            }
            System.out.println("Current progress " + prog + " out of " + list.size() +" Current Userid :"+userid);
            
            prog++;
            if ( Valid ){
                
                number = number+1;
                //ValidUserList.add(validUser);
                System.out.println("Congrates! Valid User found! Total Found : "+ number);
                System.out.println("The valid userid is "+ userid);
                this.write2File(userid);
                
            }
        }
        
        System.out.println("Total number of valid users found : "+ValidUserList.size());
        
        return ValidUserList;
    }
    
    public void write2File(String UserId){
        String validUserId = UserId;
        try {
            FileWriter fstream = new FileWriter("UserId.txt", true);
            BufferedWriter out = new BufferedWriter (fstream);
            out.write(validUserId);
            out.newLine();
            out.close();
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        
    }
}
