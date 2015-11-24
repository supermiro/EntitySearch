package DP_Disambiguation_FeatureBuilding;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IngoingLinkFetcher {

	float elapsedTime,start;
	
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
	
}
