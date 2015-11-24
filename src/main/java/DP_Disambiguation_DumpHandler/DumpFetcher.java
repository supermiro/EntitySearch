package DP_Disambiguation_DumpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class DumpFetcher extends DefaultHandler{
	
	private String wikipediaPath = "";
	private int counter;
	private Categories categories = null;
	private Dictionary anchorLinks;
	private ID currentlyProcessedEntity = null;
	private String pageTitle = "";
	private String redirectName = null;
	private RedirectList listOfRedirects = null;
	private boolean readingTexts = false;
	
	public DumpFetcher(String dumpPath,Dictionary dict, RedirectList providedListOfRedirects,Categories providedCategories) {
		this.wikipediaPath = dumpPath;
		this.anchorLinks = dict;
		this.listOfRedirects = providedListOfRedirects;
		this.categories = providedCategories;
		this.counter = 0;
	}
	public class MySAXTerminatorException extends SAXException {

	}
	
	public void getEntityList () throws ParserConfigurationException, SAXException, IOException
	{
		//------------Primare definitions for SAX parsing module----------------------
		// Set parser only to read titles	   
				readingTexts = false;
				counter = 0;
		// File to write into
				PrintWriter writer = new PrintWriter("IDs.txt", "UTF-8");  
		// Obtain and configure a SAX based parser
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		// Obtain object for SAX parser
				SAXParser saxParser = saxParserFactory.newSAXParser();
		//-------------------Operations--------------------------------
			   
				File wikipedia = new File(this.wikipediaPath);
				InputStream inputStream= new FileInputStream(wikipedia);
				Reader reader = new InputStreamReader(inputStream,"UTF-8");  
				InputSource is = new InputSource(reader);
				is.setEncoding("UTF-8");
				
				try
				{
					saxParser.parse(is, defaultHandler);
				}
				catch(MySAXTerminatorException e)
				{
					
				}
	}
	
	public void getEntityInformation () throws ParserConfigurationException, SAXException, IOException
	{
		readingTexts = true;
		counter = 0;
		// Obtain and configure a SAX based parser
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		// Obtain object for SAX parser
				SAXParser saxParser = saxParserFactory.newSAXParser();
		//-------------------Operations--------------------------------
	   
		File wikipedia = new File(this.wikipediaPath);
		InputStream inputStream= new FileInputStream(wikipedia);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");  
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		
		try
		{
			saxParser.parse(is, defaultHandler);
		}
		catch(MySAXTerminatorException e)
		{
			
		}
	}
	
	
	DefaultHandler defaultHandler = new DefaultHandler()
		   {
			         
		    String title="close";
		    String text="close";
		    String redirect="close";
		    
	//----------------------- Handling open tag of element------------------------------		    
		    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
			    {
		    	   		    	
			   
			     if (qName.equalsIgnoreCase("title")) 
				     {
				      title = "open";
				     }
			     
			     
			     
			     
			     
			     
			     
			     if (qName.equalsIgnoreCase("text")) 
				     {
			    	 //I have to change  redirect name here, because of the redirects and redirect listing
			    	 	pageTitle = pageTitle.toLowerCase();
					    if (pageTitle.startsWith(" "))
					    	pageTitle = pageTitle.substring(1);
					    pageTitle = pageTitle.replace(' ', '_');
					    
					    
			    	 
			    	HashMap <String,ID> iDList = anchorLinks.getIDs();
				      
				      //I only fill the lsit once, and ever since, all the Elements should be accounted
				      
				      if (readingTexts == false &&!iDList.containsKey(pageTitle) )
		    	 		{
			    	 		if(!pageTitle.matches("") && !pageTitle.matches("^[Mm]ediazilla::.*") &&  !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ranslate[Ww]iki:.*") && !pageTitle.matches("^[Bb]e-[Xx]-[Oo]ld:.*") && !pageTitle.matches("^[Tt]emplate_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikisource:.*") &&  !pageTitle.matches("^[Ss]imple:.*") && !pageTitle.matches("^[Mm]edia[Ww]iki:.*") && !pageTitle.matches("[Mm]eta:.*") && !pageTitle.matches("[Ww]iki[Bb]ooks:.*") && !pageTitle.matches("[Mm]ail[Aa]rchive:.*") && !pageTitle.matches("^.{0,3}:.*") && !pageTitle.matches("[Mm]ail:.*") && !pageTitle.matches("[Ss]pecial:.*") && !pageTitle.matches("[Ww]iktionary:.*") && !pageTitle.matches("[Cc]ommons:.*") && !pageTitle.matches("^[Ww]ikt:.*") &&!pageTitle.matches("^[Ii]mage:.*") &&  !pageTitle.matches("^[Uu]ser:.*") &&  !pageTitle.matches("^[Ww]ikipedia:.*") &&  !pageTitle.matches("^[Ff]ile:.*") &&  !pageTitle.matches("^[Mm]ediWiki:.*") &&  !pageTitle.matches("^[Tt]emplate:.*") &&  !pageTitle.matches("^[Hh]elp:.*") &&  !pageTitle.matches("^[Cc]ategory:.*") &&  !pageTitle.matches("^[Pp]ortal:.*") &&  !pageTitle.matches("^[Bb]ook:.*")  &&  !pageTitle.matches("^[Dd]raft:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext:.*") &&  !pageTitle.matches("^[Mm]odule:.*") &&  !pageTitle.matches("^[Gg]adget:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition:.*") &&  !pageTitle.matches("^[Tt]opic:.*")  &&  !pageTitle.matches("^[Tt]alk:.*") &&  !pageTitle.matches("^[Uu]ser_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ff]ile_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]edia[Ww]iki_[Tt]alk:.*") &&  !pageTitle.matches("^[Hh]elp_[Tt]alk:.*")  &&  !pageTitle.matches("^[Cc]ategory_[Tt]alk:.*") &&  !pageTitle.matches("^[Pp]ortal_[Tt]alk:.*") &&  !pageTitle.matches("^[Bb]ook_[Tt]alk:.*") &&  !pageTitle.matches("^[Dd]raft_[Tt]alk:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram_[Tt]alk:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]odule_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition_[Tt]alk:.*"))
			    	 			{
			    	 				  currentlyProcessedEntity = anchorLinks.addLink(pageTitle);
							    	  counter++;
							    	  if (counter % 1000 == 0)
							    		  Printer.print("--Fetching entitylist--",String.valueOf(counter));
			    	 			}	
			    	 	}
		    		 else
			    		 {
		    			 if(!pageTitle.matches("") && !pageTitle.matches("^[Mm]ediazilla::.*") &&  !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ranslate[Ww]iki:.*") && !pageTitle.matches("^[Bb]e-[Xx]-[Oo]ld:.*") && !pageTitle.matches("^[Tt]emplate_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikisource:.*") &&  !pageTitle.matches("^[Ss]imple:.*") && !pageTitle.matches("^[Mm]edia[Ww]iki:.*") && !pageTitle.matches("[Mm]eta:.*") && !pageTitle.matches("[Ww]iki[Bb]ooks:.*") && !pageTitle.matches("[Mm]ail[Aa]rchive:.*") && !pageTitle.matches("^.{0,3}:.*") && !pageTitle.matches("[Mm]ail:.*") && !pageTitle.matches("[Ss]pecial:.*") && !pageTitle.matches("[Ww]iktionary:.*") && !pageTitle.matches("[Cc]ommons:.*") && !pageTitle.matches("^[Ww]ikt:.*") &&!pageTitle.matches("^[Ii]mage:.*") &&  !pageTitle.matches("^[Uu]ser:.*") &&  !pageTitle.matches("^[Ww]ikipedia:.*") &&  !pageTitle.matches("^[Ff]ile:.*") &&  !pageTitle.matches("^[Mm]ediWiki:.*") &&  !pageTitle.matches("^[Tt]emplate:.*") &&  !pageTitle.matches("^[Hh]elp:.*") &&  !pageTitle.matches("^[Cc]ategory:.*") &&  !pageTitle.matches("^[Pp]ortal:.*") &&  !pageTitle.matches("^[Bb]ook:.*")  &&  !pageTitle.matches("^[Dd]raft:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext:.*") &&  !pageTitle.matches("^[Mm]odule:.*") &&  !pageTitle.matches("^[Gg]adget:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition:.*") &&  !pageTitle.matches("^[Tt]opic:.*")  &&  !pageTitle.matches("^[Tt]alk:.*") &&  !pageTitle.matches("^[Uu]ser_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ff]ile_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]edia[Ww]iki_[Tt]alk:.*") &&  !pageTitle.matches("^[Hh]elp_[Tt]alk:.*")  &&  !pageTitle.matches("^[Cc]ategory_[Tt]alk:.*") &&  !pageTitle.matches("^[Pp]ortal_[Tt]alk:.*") &&  !pageTitle.matches("^[Bb]ook_[Tt]alk:.*") &&  !pageTitle.matches("^[Dd]raft_[Tt]alk:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram_[Tt]alk:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]odule_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition_[Tt]alk:.*"))
		    	 			{
			    			 currentlyProcessedEntity = iDList.get(pageTitle);
						      if (readingTexts == true && redirectName == null)
							      {
							    	  counter++;
							    	  if (counter % 1000 == 0)
							    	  Printer.print("--Reading entity information--","Overal progress - " + String.format("%.2f", ((((float)counter/ (float)anchorLinks.getIDs().size()) * 100))) + " % " + "\tCategories fetched - " + categories.getCategoryList().size() + " \tNumber of Anchors - " + anchorLinks.getAnchors().size());
							      }
		    	 			}
		    			 else currentlyProcessedEntity = null;
			    		 }
				      
				      if (readingTexts == true && currentlyProcessedEntity != null)
				    	 {
				    		 text = "open";
				    	 }  
				      
				     }
			   
			     
			     
			     
			     
			     if (qName.equalsIgnoreCase("redirect")) 
				     {
			    	  redirect = "open";
			    	  redirectName = attributes.getValue(0);
			    				    	  
			    	  String temp = pageTitle;
		    	 	  pageTitle = redirectName;
		    	 	  redirectName = temp;
			    	  
			    	  
			    	  if (redirect.startsWith(" "))
			    		  redirect = redirect.substring(1);
			    	  redirectName = redirectName.replace(' ', '_');
			    	  
			    	  if (pageTitle.startsWith(" "))
			    		  pageTitle = pageTitle.substring(1);
			    	  pageTitle = pageTitle.replace(' ', '_');
			    	  pageTitle = pageTitle.toLowerCase();
			    	  //if i read a redirect page and the page it is redirecting to has not been accounted yet, i create her
			    	  if (readingTexts == false &&!anchorLinks.getIDs().containsKey(pageTitle) )
		    	 		{
			    	 		if(!pageTitle.matches("") && !pageTitle.matches("^[Mm]ediazilla::.*") &&  !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ools:.*") && !pageTitle.matches("^[Tt]ranslate[Ww]iki:.*") && !pageTitle.matches("^[Bb]e-[Xx]-[Oo]ld:.*") && !pageTitle.matches("^[Tt]emplate_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikisource:.*") &&  !pageTitle.matches("^[Ss]imple:.*") && !pageTitle.matches("^[Mm]edia[Ww]iki:.*") && !pageTitle.matches("[Mm]eta:.*") && !pageTitle.matches("[Ww]iki[Bb]ooks:.*") && !pageTitle.matches("[Mm]ail[Aa]rchive:.*") && !pageTitle.matches("^.{0,3}:.*") && !pageTitle.matches("[Mm]ail:.*") && !pageTitle.matches("[Ss]pecial:.*") && !pageTitle.matches("[Ww]iktionary:.*") && !pageTitle.matches("[Cc]ommons:.*") && !pageTitle.matches("^[Ww]ikt:.*") &&!pageTitle.matches("^[Ii]mage:.*") &&  !pageTitle.matches("^[Uu]ser:.*") &&  !pageTitle.matches("^[Ww]ikipedia:.*") &&  !pageTitle.matches("^[Ff]ile:.*") &&  !pageTitle.matches("^[Mm]ediWiki:.*") &&  !pageTitle.matches("^[Tt]emplate:.*") &&  !pageTitle.matches("^[Hh]elp:.*") &&  !pageTitle.matches("^[Cc]ategory:.*") &&  !pageTitle.matches("^[Pp]ortal:.*") &&  !pageTitle.matches("^[Bb]ook:.*")  &&  !pageTitle.matches("^[Dd]raft:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext:.*") &&  !pageTitle.matches("^[Mm]odule:.*") &&  !pageTitle.matches("^[Gg]adget:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition:.*") &&  !pageTitle.matches("^[Tt]opic:.*")  &&  !pageTitle.matches("^[Tt]alk:.*") &&  !pageTitle.matches("^[Uu]ser_[Tt]alk:.*") &&  !pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !pageTitle.matches("^[Ff]ile_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]edia[Ww]iki_[Tt]alk:.*") &&  !pageTitle.matches("^[Hh]elp_[Tt]alk:.*")  &&  !pageTitle.matches("^[Cc]ategory_[Tt]alk:.*") &&  !pageTitle.matches("^[Pp]ortal_[Tt]alk:.*") &&  !pageTitle.matches("^[Bb]ook_[Tt]alk:.*") &&  !pageTitle.matches("^[Dd]raft_[Tt]alk:.*") &&  !pageTitle.matches("^[Ee]ducation_[Pp]rogram_[Tt]alk:.*") &&  !pageTitle.matches("^[Tt]imed[Tt]ext_[Tt]alk:.*") &&  !pageTitle.matches("^[Mm]odule_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Tt]alk:.*") &&  !pageTitle.matches("^[Gg]adget_[Dd]efinition_[Tt]alk:.*"))
			    	 			{
			    	 				  currentlyProcessedEntity = anchorLinks.addLink(pageTitle);
			    	 			}
			    	 		
		    	 		}
			    	  else
			    	  {
			    		  HashMap <String,ID> iDList = anchorLinks.getIDs();
			    		  currentlyProcessedEntity = iDList.get(pageTitle);
			    	  }
			    	  
			    	  
			    	  if(readingTexts == false && !redirectName.matches("") && !redirectName.matches("^[Mm]ediazilla::.*") &&  !redirectName.matches("^[Tt]ools:.*") && !redirectName.matches("^[Tt]ools:.*") && !redirectName.matches("^[Tt]ranslate[Ww]iki:.*") && !redirectName.matches("^[Bb]e-[Xx]-[Oo]ld:.*") && !redirectName.matches("^[Tt]emplate_[Tt]alk:.*") &&  !redirectName.matches("^[Ww]ikipedia_[Tt]alk:.*") &&  !redirectName.matches("^[Ww]ikisource:.*") &&  !redirectName.matches("^[Ss]imple:.*") && !redirectName.matches("^[Mm]edia[Ww]iki:.*") && !redirectName.matches("[Mm]eta:.*") && !redirectName.matches("[Ww]iki[Bb]ooks:.*") && !redirectName.matches("[Mm]ail[Aa]rchive:.*") && !redirectName.matches("^.{0,3}:.*") && !redirectName.matches("[Mm]ail:.*") && !redirectName.matches("[Ss]pecial:.*") && !redirectName.matches("[Ww]iktionary:.*") && !redirectName.matches("[Cc]ommons:.*") && !redirectName.matches("..^:.*") && !redirectName.matches("^:.*") && !redirectName.matches("^[Ww]ikt:.*") &&!redirectName.matches("^[Ii]mage:.*") &&  !redirectName.matches("^[Uu]ser:.*") &&  !redirectName.matches("^[Ww]ikipedia:.*") &&  !redirectName.matches("^[Ff]ile:.*") &&  !redirectName.matches("^[Mm]ediWiki:.*") &&  !redirectName.matches("^[Tt]emplate:.*") &&  !redirectName.matches("^[Hh]elp:.*") &&  !redirectName.matches("^[Cc]ategory:.*") &&  !redirectName.matches("^[Pp]ortal:.*") &&  !redirectName.matches("^[Bb]ook:.*")  &&  !redirectName.matches("^[Dd]raft:.*") &&  !redirectName.matches("^[Ee]ducation [Pp]rogram:.*") &&  !redirectName.matches("^[Tt]imed[Tt]ext:.*") &&  !redirectName.matches("^[Mm]odule:.*") &&  !redirectName.matches("^[Gg]adget:.*") &&  !redirectName.matches("^[Gg]adget [Dd]efinition:.*") &&  !redirectName.matches("^[Tt]opic:.*")  &&  !redirectName.matches("^[Tt]alk:.*") &&  !redirectName.matches("^[Uu]ser [Tt]alk:.*") &&  !redirectName.matches("^[Ww]ikipedia [Tt]alk:.*") &&  !redirectName.matches("^[Ff]ile [Tt]alk:.*") &&  !redirectName.matches("^[Mm]edia[Ww]iki [Tt]alk:.*") &&  !redirectName.matches("^[Hh]elp [Tt]alk:.*")  &&  !redirectName.matches("^[Cc]ategory [Tt]alk:.*") &&  !redirectName.matches("^[Pp]ortal [Tt]alk:.*") &&  !redirectName.matches("^[Bb]ook [Tt]alk:.*") &&  !redirectName.matches("^[Dd]raft [Tt]alk:.*") &&  !redirectName.matches("^[Ee]ducation [Pp]rogram talk:.*") &&  !redirectName.matches("^[Tt]imed[Tt]ext [Tt]alk:.*") &&  !redirectName.matches("^[Mm]odule [Tt]alk:.*") &&  !redirectName.matches("^[Gg]adget [Tt]alk:.*") &&  !redirectName.matches("^[Gg]adget [Dd]efinition talk:.*"))
		    	 		{
			    	 		listOfRedirects.addRedirect(redirectName,currentlyProcessedEntity);
		    	 		}			    	  
			    	      	 	  
			    	  	 		//we have done all with redirects, we continue reding other pages
			    	 	
			    	  
				     }
			    }

	//-----------------prints data stored in between '<' and '>' tags------------------
		    public void characters(char ch[], int start, int length)
		      throws SAXException 
		    	{
		    	int x;	
		    		    	
			     if (title.equals("open")) 
			     	{
					    pageTitle = new String(ch, start, length);
					   			    
			     	}
			     
			     if (text.equals("open")) 	//sekvencne spracovavanie textu
			     	{  				    	 
				    	 x = start;
				    	 boolean isReading = false;
				    	 String link = "";
				    	 String anchor = "";
				    	 String anchorLinkText = "";
				    	 String categoryName = "";
				    	 Tuple <Anchor,ID> context = null;
				    	 	
				    	 //------------First we create an unique Entity if it was not created (ignoring redirects)---------				    	 	   	 	
				    	 	
				    	 	while (x < (length - 1)) 
				    	 	{
				    			    	 		
				    	 		
					    	 	if (ch[x] == '[' && ch[x+1] == '[')
					    	 	{
					    	 		x = x + 2;
					    	 		isReading = true; //I found a beginnig of a link i am starting to read through it
					    	 		anchorLinkText = "";
					    	 	}
					    	 		
					    	 	if (isReading == true && ch[x] == ']' && ch[x+1] == ']')
						    	{
					    	 		
					    	 		if ((anchorLinkText.length()>0) && !anchorLinkText.matches("") && (anchorLinkText.charAt(0) != ('#')) && ( ! anchorLinkText.contains("{")) && ( ! anchorLinkText.contains("}"))  && ( ! anchorLinkText.contains("$")))	//osetrenie, ak clanok staticky ukazuje sam na seba, alebo do seba
						    	 		{
							    	 		if (anchorLinkText.contains("#"))//ak ukazuje do vnutrajsku nejakeho clanku, tak mazem zo stringu vsetko od # az po | resp koniec Stringu
							    	 			{
							    	 				anchorLinkText = anchorLinkText.replaceAll("#.*\\|", "|");
							    	 				anchorLinkText = anchorLinkText.replaceAll("#.*\\\0", "");
							    	 			}
								    	 		
							    	 								    	 		
							    	 		if ( anchorLinkText.startsWith("Category:"))
								    	 		{
							    	 				anchorLinkText = anchorLinkText.replaceFirst("Category:", "");
							    	 				
								    	 			if (anchorLinkText.contains("|"))
								    	 				categoryName = anchorLinkText.substring(0,anchorLinkText.indexOf('|'));
								    	 			else
								    	 				categoryName = anchorLinkText;
								    	 			
								    	 			categoryName = categoryName.replaceAll("^Category:", "");
								    	 			categoryName = categoryName.toLowerCase();
								    	 			
								    	 			if (categoryName.startsWith(" "))
								    	 				categoryName = categoryName.substring(1);
								    	 			categoryName = categoryName.replace(' ', '_');
								    	 			categoryName = categoryName.replace(':', '_');
								    	 			String category = new String (categoryName);
								    	 			categories.addCategory(category);
								    	 			currentlyProcessedEntity.addCategory(category);
								    	 		}
								    	 	else
								    	 	{
									    	 	if (anchorLinkText.contains("|") && (!anchorLinkText.endsWith("| ")))	//zachovavame tvar vypisu aj pre pre rovnomenne linky anchor-text
										    	 	{
									    	 		
									    	 			link = anchorLinkText.substring(0, anchorLinkText.indexOf('|'));	
									    	 												    	 												    	 			
									    	 			 if (link.startsWith(" "))
									    	 				link = link.substring(1);
									    	 			 
									    	 			link = link.replace(' ', '_');
									    	 			
									    	 			
									    	 			 
									    	 			 //check whether link is not in a redirect list...if so, exchange for target entity
									    	 			 String temp;
									    	 			 if ( (temp = listOfRedirects.getRedirectName(link)) != null)
									    	 				link = temp;
									    	 			 
									    	 												    	 			
									    	 			anchor = anchorLinkText.substring(anchorLinkText.indexOf('|')+1,anchorLinkText.length());									    	 			
									    	 			
									    	 			anchor = anchor.toLowerCase();
									    	 			link = link.toLowerCase();
									    	 			
									    	 			if (anchor.startsWith(" "))
									    	 				anchor = anchor.substring(1);
									    	 			
									    	 			context = anchorLinks.addAnchorLink(anchor, link);
									    	 			if (context != null)
									    	 			{
									    	 				currentlyProcessedEntity.addOutgoing(context); //before was as Ingoing ?!
									    	 				context.getSecond().addIngoing(new Tuple<Anchor, ID> (context.getFirst(),currentlyProcessedEntity));
										    	 		}
									    	 			
									    	 			
										    	 	}
									    	 	else
											     	{
									    	 			anchor = anchorLinkText;
									    	 			link = anchorLinkText;
									    	 			
									    	 			if (link.startsWith(" "))
									    	 				link = link.substring(1);
									    	 			
									    	 			link = link.replace(' ', '_');
									    	 			
									    	 											    	 			
									    	 			 //check whether link is not in a redirect list...if so, exchange for target entity
									    	 			 String temp;
									    	 			 if ( (temp = listOfRedirects.getRedirectName(link)) != null)
									    	 				link = temp;
									    	 			
									    	 			if (anchor.startsWith(" "))
									    	 				anchor = anchor.substring(1);
									    	 			
									    	 			link = link.toLowerCase();
									    	 			anchor = anchor.toLowerCase();
									    	 			
									    	 			context = anchorLinks.addAnchorLink(anchor, link);
									    	 			
									    	 			if (context != null)
									    	 			{
									    	 				currentlyProcessedEntity.addOutgoing(context);
									    	 				context.getSecond().addIngoing(new Tuple<Anchor, ID> (context.getFirst(),currentlyProcessedEntity));
									    	 			}
											     	}
								    	 	}
							    	 	}
					    	 		isReading = false;
					    	 		x++;
						    	}
					    	 						    	 	
					    	 	if (isReading == true)
					    	 	{
					    	 		anchorLinkText = anchorLinkText + ch[x];
					    	 	}
					    	 	x++;
				    	 	} 
				    	 
			    	 }
			    }

	//----------------------- Handling end tag of element------------------------------
		    public void endElement(String uri, String localName, String qName)
		      throws SAXException 
		    	{
		     
			     if (qName.equalsIgnoreCase("title")) 
				     {
				      title = "close";
				     }
			     
			     if (qName.equalsIgnoreCase("text")) 
					 {
					  text = "close";
					  redirectName = null;
					 }
			     if (qName.equalsIgnoreCase("redirect")) 
					 {
					  redirect = "close";
					 }
			     
			    }
		   		
		   };		
		   
		   
	//-------------------	   
	
		
	

}
