package DP_Disambiguation_FeatureBuilding;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ArticleFetcher {

	float elapsedTime,start;
	
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
}
