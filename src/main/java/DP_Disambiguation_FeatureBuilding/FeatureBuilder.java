package DP_Disambiguation_FeatureBuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class FeatureBuilder {
	
	long start;  
	long elapsedTime;
	//-----------------------------------------------
	
	//Connect to KnowledgeBase
	public String getArticle (Entity entity) throws IOException, ParserConfigurationException, SAXException
	{
		//----Wikipedia----
		String fetchURL = "https://en.wikipedia.org/wiki/Special:Export/" + entity.ID;
		//----XXXXXXXXX----
		//....
		//-----------------
		start = System.nanoTime(); 
		System.out.println("Fetching article: " + entity.ID);
		
		InputStream inputStream= new URL(fetchURL).openStream();
        Reader reader = new InputStreamReader(inputStream,"UTF-8");
        InputSource is = new InputSource(reader);
        is.setEncoding("UTF-8");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document wikiPageXML = dBuilder.parse(is);
			
		Element wikiPageRoot = wikiPageXML.getDocumentElement();
		String article = wikiPageRoot.getElementsByTagName("text").item(0).getTextContent();
		
		elapsedTime = System.nanoTime() - start;
		System.out.println("-Fetching done in " + elapsedTime/1000000l + " ms\n");
		
		return article;
				
	}
	
	public void getCategory(Entity entity,String article)
	{
		start = System.nanoTime(); 
		System.out.println("Getting categories for: " + entity.ID);
		
		
		String matchedLink = null;
		ArrayList <String> categories = new ArrayList<String> ();
		Pattern p = Pattern.compile("\\[\\[Category:(.*)\\]\\]");
		Matcher m = p.matcher(article);
				 
		while (m.find()) 
		 {
			matchedLink = m.group(1).replace(" ", "_");
			
			if (matchedLink.contains("|"));
			{
				System.out.println (matchedLink + " " + matchedLink.indexOf("\\|"));
				matchedLink = matchedLink.split("\\|") [0];
			}
			 	
				categories.add(matchedLink.replace(" ", "_"));
			 System.out.println ("Category: " + matchedLink.replace(" ", "_"));
		 }
		
		
	entity.listOfCategories = categories; 
	
	elapsedTime = System.nanoTime() - start;
	System.out.println("-Categories fetched " + elapsedTime/1000000l + " ms\n");
	
	}
	
	public void getOutgoing(Entity entity,String article)
	{
		start = System.nanoTime(); 
		System.out.println("Getting outgoing links for: " + entity.ID);
		
		Pattern p = Pattern.compile("\\[\\[(.*?)\\]\\]");
		Matcher m = p.matcher(article);
		Pattern p2 = null;
		Matcher m2 = null;
		String linkedPage = null;
		
		String linkName = "";
				 
		while (m.find()) 
		 {
			linkName = m.group(1);
			if (! linkName.contains("User:") && ! linkName.contains("Wikipedia:") && ! linkName.contains("File:") && ! linkName.contains("MediWiki:") && ! linkName.contains("Template:") && ! linkName.contains("Help:") && ! linkName.contains("Category:") && ! linkName.contains("Portal:") && ! linkName.contains("Book:")  && ! linkName.contains("Draft:") && ! linkName.contains("Education Program:") && ! linkName.contains("TimedText:") && ! linkName.contains("Module:") && ! linkName.contains("Gadget:") && ! linkName.contains("Gadget definition:") && ! linkName.contains("Topic:")  && ! linkName.contains("Talk:") && ! linkName.contains("User talk:") && ! linkName.contains("Wikipedia talk:") && ! linkName.contains("File talk:") && ! linkName.contains("MediaWiki talk:") && ! linkName.contains("Help talk:")  && ! linkName.contains("Category talk:") && ! linkName.contains("Portal talk:") && ! linkName.contains("Book talk:") && ! linkName.contains("Draft talk:") && ! linkName.contains("Education Program talk:") && ! linkName.contains("TimedText talk:") && ! linkName.contains("Module talk:") && ! linkName.contains("Gadget talk:") && ! linkName.contains("Gadget definition talk:"))
			{
				linkName = Character.toUpperCase(linkName.charAt(0)) + linkName.substring(1);
				
				 p2 = Pattern.compile("(.*)#|(.*)\\||(.*)");
				 m2 = p2.matcher(linkName);
				 
				 if (m2.find())
				 {
					 if (m2.group(1) == null && m2.group(2) == null)
						 linkedPage = m2.group(3);
					 else
						 if (m2.group(2) == null && m2.group(3) == null)
							 linkedPage = m2.group(1);
						 else
							 if (m2.group(3) == null && m2.group(1) == null)
								 linkedPage = m2.group(2);
					 
					 linkedPage = linkedPage.replaceAll(" ", "_");
					 
					 if (entity.listOfLinkedPages == null)
						 entity.listOfLinkedPages = new ArrayList<String>();
					 
					 entity.listOfLinkedPages.add(linkedPage);
					 
				 }
			}
		 }
		
		elapsedTime = System.nanoTime() - start;
		System.out.println("-Outgoing links got in " + elapsedTime/1000000l + " ms\n");
	
	}
		
	public void getIngoing (Entity entity) throws ParserConfigurationException, MalformedURLException, SAXException, IOException, XPathExpressionException
	{
	// InGoing Data - https://en.wikipedia.org/w/index.php?title=Special:WhatLinksHere
		
		start = System.nanoTime(); 
		System.out.println("Getting ingoing links for: " + entity.ID);
		
		int iterationCounter = 0;
		int redirectCounter = 0;
		int userTalkCounter = 0;
		int talkingCounter = 0;
		int userCounter = 0;
		String linkingInstance = "";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document linkingPagesHTML = db.parse(new URL("https://en.wikipedia.org/w/index.php?title=Special:WhatLinksHere/" + entity.ID + "&limit=5000").openStream());
		linkingPagesHTML.getDocumentElement().normalize();
	    XPath xpath = XPathFactory.newInstance().newXPath();
	   
	    String expression = "//div[@id='mw-content-text']//li";		//show all elements that have a bullet, even those, that contain a subset of elements in a list
	    
	    NodeList returnedElements = (NodeList) xpath.evaluate(expression, linkingPagesHTML, XPathConstants.NODESET);
	    LinkedList <Node> linkingPages = new LinkedList <Node> ();	//it is better to handle LinkedList than NodeList
	    	    
	    for (int i=0; i< returnedElements.getLength();i++)
	    	linkingPages.add(returnedElements.item(i));
	    
	    
	    NodeList descendants = null;
	        
		for (iterationCounter = 0; iterationCounter < linkingPages.size() ;iterationCounter++)	//iterating over all retrieved elements
		{
			
			descendants = returnedElements.item(iterationCounter).getChildNodes();
			 
			 for (int i = 0 ; i< descendants.getLength();i++)	//taking care of nested sublists
			 {
				 if (descendants.item(i).getNodeName().equals("ul"))
				 {
					 returnedElements.item(iterationCounter).removeChild(descendants.item(i));
				 }
			 }
			 linkingInstance = linkingPages.get(iterationCounter).getTextContent().replace("\n", "");
			 linkingInstance = linkingInstance.substring(0, linkingInstance.length()-18).replace(" ", "_");
			
		//-----------instance categorisation-----------------
			
			if (linkingInstance.contains("(redirect_page"))
			{
				if (entity.listOfLinkingRedirects == null)
					entity.listOfLinkingRedirects = new ArrayList<String>();
				
				entity.listOfLinkingRedirects.add(linkingInstance);
				entity.incomingRedirectPages ++;
			}
			else
			{
				if (linkingInstance.contains("User talk:"))
				{
					if (entity.listOfLinkingUserTalks == null)
						entity.listOfLinkingUserTalks = new ArrayList<String>();
					
					entity.listOfLinkingUserTalks.add(linkingInstance);
					entity.incomingUserTalkingPages++;
				}
				else
				{
				
					if (linkingInstance.contains("User:"))
					{
						if (entity.listOfLinkingUserProfiles == null)
							entity.listOfLinkingUserProfiles = new ArrayList<String>();
						
						entity.listOfLinkingUserProfiles.add(linkingInstance);
						entity.userProfilesReffering ++;
					}
					else
					{					
						if (linkingInstance.contains("Talk:"))
						{
							if (entity.listOfLinkingTalks == null)
								entity.listOfLinkingTalks = new ArrayList<String>();
							
							entity.listOfLinkingTalks.add(linkingInstance);
							entity.incomingTalkingPages++; 
						}
						else
						{					
							if (linkingInstance.contains("(transclusion"))
							{
								if (entity.listOfLinkingTransclusions == null)
									entity.listOfLinkingTransclusions = new ArrayList<String>();
								
								entity.listOfLinkingTransclusions.add(linkingInstance);
								entity.incomingTransclusions++; 
							}
						}
					}
				}
			}
		}
		entity.otherIncomingPages = (iterationCounter - userCounter - talkingCounter - userTalkCounter - redirectCounter);
		
		elapsedTime = System.nanoTime() - start;
		System.out.println("-Ingoing links got in " + elapsedTime/1000000l + " ms\n");
			
	}
		
	public void getTrafficData (Entity entity) throws MalformedURLException, IOException, ParseException
	 {
		 
		start = System.nanoTime(); 
		System.out.println("Fetching traffic data for entity: " + entity.ID);
		
		 HashMap<Date,Integer> traffic = new HashMap<Date,Integer> ();
		 Date date = null;
		 int trafficRank = 0;
		 
		 BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://stats.grok.se/json/en/latest90/" + entity.ID).openStream()));
		 String trafficTextFromJson = "";
		 String line;
		 while ((line = in.readLine()) != null) 
		 {
			 trafficTextFromJson = trafficTextFromJson.concat(line);
		 }
		 in.close();
		 
		 Pattern p = Pattern.compile("\"([0-9]{4}-[0-1][0-9]-[0-3][0-9])\": ([0-9]{0,})");
		 Matcher m = p.matcher(trafficTextFromJson);
		
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 
		 while (m.find()) 
		 {
			 date = format.parse(m.group(1));
			 traffic.put(date,new Integer(m.group(2)));
		 }
		 
		 //returning traffic info
		 entity.traffic = traffic;
		 
		 
		 p = Pattern.compile("\"rank\": ([0-9]{1,})");
		 m = p.matcher(trafficTextFromJson);
		 
		 if (m.find())
		 {
			 trafficRank = Integer.parseInt(m.group(1));
		 }
		 
		 //returning rank of most viewed
		 entity.trafficRank = trafficRank;
		 
		 elapsedTime = System.nanoTime() - start;
		System.out.println("-Traffic fetching finished in " + elapsedTime/1000000l + " ms\n");
		 
	 }
	
	public void getDisambiguations (Entity entity) throws ParserConfigurationException, MalformedURLException, SAXException, IOException, XPathExpressionException
	{
		start = System.nanoTime(); 
		System.out.println("Fetching disambiguations of an entity: " + entity.ID);
		
		String redirectsURL = ("https://en.wikipedia.org/wiki/" + entity.ID + "_(disambiguation)");
		String disambiguationID;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream wikiDisambiguationPage;
		
		//Return if site has no disambiguation page and stream wont be open
		
		try
			{
				wikiDisambiguationPage = new URL(redirectsURL).openStream();
			}
		catch (IOException e)
		{
			elapsedTime = System.nanoTime() - start;
			System.out.println("-Traffic fetching finished in " + elapsedTime/1000000l + " ms\n");
			return;
		}
		
		Document linkingPagesHTML = db.parse(wikiDisambiguationPage);
		linkingPagesHTML.getDocumentElement().normalize();
		
		if (linkingPagesHTML.getDocumentElement().getTextContent().contains("Wikipedia does not have an article with this exact name"))
			return;
		
	    XPath xpath = XPathFactory.newInstance().newXPath();
	   
	    String expression = "//div[@id='mw-content-text']//ul/li/a [1]/@href [not(ancestor::div [@id='toc'])]";		//show all elements that have a bullet, even those, that contain a subset of elements in a list
	    
	    NodeList returnedElements = (NodeList) xpath.evaluate(expression, linkingPagesHTML, XPathConstants.NODESET);
	    //LinkedList <Node> linkingPages = new LinkedList <Node> ();	//it is better to handle LinkedList than NodeList
	    	    
	    for (int i=0; i< returnedElements.getLength();i++)
	    {
	    	if (!(disambiguationID = returnedElements.item(i).getTextContent()).contains("redlink=1"))
	    	{
	    		disambiguationID = disambiguationID.substring(6); // we assumpt that all links start with /wiki/ which doesnt have to be true
	    		
	    		if (entity.listOfDisambiguations == null)
	    			entity.listOfDisambiguations = new ArrayList<String> ();
	    		
	    		System.out.println(disambiguationID);
	    		entity.listOfDisambiguations.add(disambiguationID);
	    	}
	    }
	    
	    elapsedTime = System.nanoTime() - start;
		System.out.println("-Traffic fetching finished in " + elapsedTime/1000000l + " ms\n");
	}
	
	//Create Entity object and full it with parameters
	public Entity createEntity (String entityID) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException
	{
		Entity entity = new Entity(entityID);
		String articleCanonic = getArticle(entity);
		
		getCategory(entity,articleCanonic);
		getOutgoing(entity,articleCanonic);
		getIngoing(entity);
		getTrafficData(entity);
		getDisambiguations(entity);
		
		return entity;
		
	}
	
}
