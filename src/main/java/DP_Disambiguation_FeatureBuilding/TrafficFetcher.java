package DP_Disambiguation_FeatureBuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrafficFetcher implements Runnable {
	
	
	private Thread t;
	Entity entity;

	long start, elapsedTime;
	
	public void getTrafficData (String entityID, HashMap<String,Entity> entities) throws MalformedURLException, IOException, ParseException
	 {
		
		if (entities.containsKey(entityID))
		{
			this.entity = entities.get(entityID);
		}
		else
		{
			this.entity = new Entity(entityID);
			entities.put(entityID,this.entity);
			
		}
		
		System.out.println("Starting fetching thread" );
		
		this.entity = entity;
	      if (t == null)
	      {
	         t = new Thread (this, "fetching thread");
	         t.start ();
	         
	      }
	 }
	

	public void run () {
		
		start = System.nanoTime(); 
		System.out.println("Fetching traffic data for entity: " + this.entity.ID);
		
		 HashMap<Date,Integer> traffic = new HashMap<Date,Integer> ();
		 Date date = null;
		 int trafficRank = 0;
		 
		 BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new URL("http://stats.grok.se/json/en/latest90/" + this.entity.ID).openStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 String trafficTextFromJson = "";
		 String line;
		 try {
			while ((line = in.readLine()) != null) 
			 {
				 trafficTextFromJson = trafficTextFromJson.concat(line);
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 Pattern p = Pattern.compile("\"([0-9]{4}-[0-1][0-9]-[0-3][0-9])\": ([0-9]{0,})");
		 Matcher m = p.matcher(trafficTextFromJson);
		
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 
		 while (m.find()) 
		 {
			 try {
				date = format.parse(m.group(1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 traffic.put(date,new Integer(m.group(2)));
		 }
		 
		 //returning traffic info
		 this.entity.traffic = traffic;
		 
		 
		 p = Pattern.compile("\"rank\": ([0-9]{1,})");
		 m = p.matcher(trafficTextFromJson);
		 
		 if (m.find())
		 {
			 trafficRank = Integer.parseInt(m.group(1));
		 }
		 
		 //returning rank of most viewed
		 this.entity.trafficRank = trafficRank;
		 
		 elapsedTime = System.nanoTime() - start;
		System.out.println("-Traffic fetching finished in " + elapsedTime/1000000l + " ms\n");
		
	}

}
