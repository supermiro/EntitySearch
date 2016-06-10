package DP_Disambiguation_DumpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public abstract class DumpHandler extends DefaultHandler{
	
	//----global variable declaration----
	protected String dumpPath = "";
	protected int counter;
	protected Categories categories = null;
	protected Dictionary anchorLinks;
	protected ID currentlyProcessedEntity = null;
	protected String pageTitle = "";
	protected String redirectName = null;
	protected RedirectList listOfRedirects = null;
	protected boolean readingTexts = false;
	protected DefaultHandler currentHandler = null;
	protected PrintWriter out = null;
	
	//----constructor---
	public DumpHandler(String dumpFilePath,Dictionary dict, RedirectList providedListOfRedirects,Categories providedCategories) {
		this.dumpPath = dumpFilePath;
		this.anchorLinks = dict;
		this.listOfRedirects = providedListOfRedirects;
		this.categories = providedCategories;
		this.counter = 0;		
	}
	
	//----functions
	public void getEntityList () throws ParserConfigurationException, SAXException, IOException
	{
		//------------Definitions for SAX parsing module----------------------
			// Set parser only to read titles in the first iteration	   
			this.readingTexts = false;
			this.counter = 0;
			// Obtain and configure a SAX based parser
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			// Obtain object for SAX parser
			SAXParser saxParser = saxParserFactory.newSAXParser();
				
		//-------------------Operations--------------------------------	
			// Defining an input stream from dumpPath
			InputSource is = new InputSource(new InputStreamReader(new FileInputStream(new File(dumpPath)),"UTF-8"));
			// Parsig according to the setted Handler
			saxParser.parse(is, this.currentHandler);
	}
	
	public void getEntityInformation () throws ParserConfigurationException, SAXException, IOException
	{
		//------------Definitions for SAX parsing module----------------------
			// Set parser only to read titles in the first iteration	   
			this.readingTexts = true;
			this.counter = 0;
			// Obtain and configure a SAX based parser
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			// Obtain object for SAX parser
			SAXParser saxParser = saxParserFactory.newSAXParser();
			
		//-------------------Operations--------------------------------
			// Defining an input streem from dumpPath
		    InputSource is = new InputSource(new InputStreamReader(new FileInputStream(new File(dumpPath)),"UTF-8"));
		    // Parsig according to the setted Handler
			saxParser.parse(is, this.currentHandler);
	}			   

	public static Boolean isSpecialNameSpace (String pageTitle)
	{
		if (	pageTitle.matches("")
				|| pageTitle.matches("^[Mm]ediazilla::.*")
				|| pageTitle.matches("^[Tt]ools:.*")
				|| pageTitle.matches("^[Tt]ools:.*")
				|| pageTitle.matches("^[Tt]ranslate[Ww]iki:.*")
				|| pageTitle.matches("^[Bb]e-[Xx]-[Oo]ld:.*")
				|| pageTitle.matches("^[Tt]emplate_[Tt]alk:.*")
				|| pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*")
				|| pageTitle.matches("^[Ww]ikisource:.*")
				|| pageTitle.matches("^[Ss]imple:.*")
				|| pageTitle.matches("^[Mm]edia[Ww]iki:.*")
				|| pageTitle.matches("[Mm]eta:.*")
				|| pageTitle.matches("[Ww]iki[Bb]ooks:.*")
				|| pageTitle.matches("[Mm]ail[Aa]rchive:.*")
				|| pageTitle.matches("^.{0,3}:.*")
				|| pageTitle.matches("[Mm]ail:.*")
				|| pageTitle.matches("[Ss]pecial:.*")
				|| pageTitle.matches("[Ww]iktionary:.*")
				|| pageTitle.matches("[Cc]ommons:.*")
				|| pageTitle.matches("^[Ww]ikt:.*")
				|| pageTitle.matches("^[Ii]mage:.*")
				|| pageTitle.matches("^[Uu]ser:.*")
				|| pageTitle.matches("^[Uu]ser_[Tt]alk:.*")
				|| pageTitle.matches("^[Ww]ikipedia:.*")
				|| pageTitle.matches("^[Ff]ile:.*")
				|| pageTitle.matches("^[Mm]ediWiki:.*")
				|| pageTitle.matches("^[Tt]emplate:.*")
				|| pageTitle.matches("^[Hh]elp:.*")
				|| pageTitle.matches("^[Cc]ategory:.*")
				|| pageTitle.matches("^[Pp]ortal:.*")
				|| pageTitle.matches("^[Bb]ook:.*")
				|| pageTitle.matches("^[Dd]raft:.*")
				|| pageTitle.matches("^[Ee]ducation_[Pp]rogram:.*")
				|| pageTitle.matches("^[Tt]imed[Tt]ext:.*")
				|| pageTitle.matches("^[Mm]odule:.*")
				|| pageTitle.matches("^[Gg]adget:.*")
				|| pageTitle.matches("^[Gg]adget_[Dd]efinition:.*")
				|| pageTitle.matches("^[Tt]opic:.*")
				|| pageTitle.matches("^[Tt]alk:.*")
				|| pageTitle.matches("^[Uu]ser_[Tt]alk:.*")
				|| pageTitle.matches("^[Ww]ikipedia_[Tt]alk:.*")
				|| pageTitle.matches("^[Ff]ile_[Tt]alk:.*")
				|| pageTitle.matches("^[Mm]edia[Ww]iki_[Tt]alk:.*")
				|| pageTitle.matches("^[Hh]elp_[Tt]alk:.*")
				|| pageTitle.matches("^[Cc]ategory_[Tt]alk:.*")
				|| pageTitle.matches("^[Pp]ortal_[Tt]alk:.*")
				|| pageTitle.matches("^[Bb]ook_[Tt]alk:.*")
				|| pageTitle.matches("^[Dd]raft_[Tt]alk:.*")
				|| pageTitle.matches("^[Ee]ducation_[Pp]rogram_[Tt]alk:.*")
				|| pageTitle.matches("^[Tt]imed[Tt]ext_[Tt]alk:.*")
				|| pageTitle.matches("^[Mm]odule_[Tt]alk:.*")
				|| pageTitle.matches("^[Gg]adget_[Tt]alk:.*")
				|| pageTitle.matches("^[Gg]adget_[Dd]efinition_[Tt]alk:.*")
				){
			return true ;	
		}
		else
			return false;
	}
	
	public static String stringNormalisation(String text)
	{
		text = text.toLowerCase();
		if (text.startsWith(" "))
			text = text.substring(1);
		text = text.replace(' ', '_');
		
		return text;
	}
	
}
