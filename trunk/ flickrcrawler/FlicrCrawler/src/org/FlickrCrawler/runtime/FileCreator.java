package org.FlickrCrawler.runtime;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.FlickrCrawler.database.Database;

public class FileCreator {

	private static Map getArgValue(String[] args) {
        Map params = new HashMap();
        for (int i = 0; i < args.length; i++) {
			String curr = args[i];
            String argName = curr.toLowerCase();
            String argValue = "";

			int eqIndex = curr.indexOf('=');
			if (eqIndex >= 0) {
				argName = curr.substring(0, eqIndex).trim();
				argValue = curr.substring(eqIndex+1).trim();
            }

            params.put(argName.toLowerCase(), argValue);
        }
		
		return params; 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map params = getArgValue(args);
		String filename = (String) params.get("file");
		String num = (String) params.get("num");
		if (filename == null || "".equals(filename) || num == null || "".equals(num)){
			System.out.println("================================================================");
			System.out.println("Pleasy specify generated file name by -file filename excluding .txt");
			System.out.println("For example, if you want to create files with name \"UserList.txt\"");
			System.out.println("Use the following command : ");
			System.out.println("        java -jar FileCreator.jar file=UserList num=#_of_files");
			System.out.println("This will create a evenly distributed series of files from the database.");
			System.out.println("Database.txt needs to be configured correctly before this program is executed.");
			System.exit(1);
		}
		
		Database db = new Database();
		ArrayList<String> UserIdList = new ArrayList();
		
		UserIdList = db.getUserList();
		
		int NumberOfUserId = UserIdList.size();
		int NumberOfMachine = Integer.parseInt(num);
		int EachPage = NumberOfUserId/NumberOfMachine+1;
		
		System.out.println("Each machine will process "+EachPage+" UserIds.");
		System.out.println("User Ids will be divided into "+NumberOfMachine+" pages.");
		
		for ( int i = 0; i < NumberOfUserId; i++){
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(filename+i+".txt", true));
				System.out.println("Creating "+filename+i+".txt");;
				for ( int j = 0; j < EachPage ; j++){
					if(UserIdList.size()==0){
						out.close();
						System.out.println("Finished!");
						System.exit(0);
					}
					out.write(UserIdList.remove(0));
					out.newLine();

				}
				System.out.println("There are "+UserIdList.size()+" User Ids left");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

}
	


}
