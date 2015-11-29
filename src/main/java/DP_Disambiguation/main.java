package DP_Disambiguation;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.dmlc.xgboost4j.util.XGBoostError;
import org.xml.sax.SAXException;

import DP_Disambiguation_DumpHandler.Anchor;
import DP_Disambiguation_DumpHandler.Categories;
import DP_Disambiguation_DumpHandler.DBpediaTypes;
import DP_Disambiguation_DumpHandler.Dictionary;
import DP_Disambiguation_DumpHandler.DumpHandler;
import DP_Disambiguation_DumpHandler.ID;
import DP_Disambiguation_DumpHandler.RedirectList;
import DP_Disambiguation_DumpHandler.Tuple;
import DP_Disambiguation_DumpHandler.WikipediaHandler;
import DP_Disambiguation_FeatureBuilding.DBPediaFetcher;
import DP_Disambiguation_FeatureBuilding.Entity;
import DP_Disambiguation_FeatureBuilding.EntityAnalyser;
import DP_Disambiguation_FeatureBuilding.FeatureBuilder;
import DP_Disambiguation_FeatureBuilding.PageRankCalculator;
import DP_Disambiguation_InputProcessing.*;
import DP_Disambiguation_ResultAnalyser.ResultAnalyser;
import DP_Disambiguation_trainigSetCreation.TestSetCreator;
import DP_Disambiguation_vWHandler.VWHandler;
import DP_Disambiguation_xgBoost.xgBoostWrapper;


public class main {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException 
	{
		/*
		ArrayList<String> canonicIDs = new ArrayList<String>();
		canonicIDs.add("Springfield_(The_Simpsons)");
		canonicIDs.add("Tom_Springfield");
		canonicIDs.add("Springfield_rifle");
		canonicIDs.add("Springfield,_Belize");
		canonicIDs.add("Springfield,_New_South_Wales");
		canonicIDs.add("Springfield_(horse)");
		
		
		ArrayList<String> candidateIDs = new ArrayList<String>();
		candidateIDs.add("Duff_(surname)");
		candidateIDs.add("Duff_Goldman");
		candidateIDs.add("Duff_Beer");
		candidateIDs.add("Duff_(food)");
		candidateIDs.add("HMS_Duff");
		candidateIDs.add("Duff_House");
		*/
		
		//PrintWriter writer = new PrintWriter("IDs.txt", "UTF-8");
		
		//String wikiPath = "simplewiki-latest-pages-articles.xml";
		//String wikiPath = "enwiki-20150304-pages-articles.xml";
		
		Dictionary anchorLinkDictionary = new Dictionary();
		RedirectList listOfRedirects = new RedirectList();
		Categories categories = new Categories();
		DBpediaTypes dBpediaTypes = new DBpediaTypes();
		EntityAnalyser analyser = new EntityAnalyser (anchorLinkDictionary);
		DBPediaFetcher dbPediaFetcher = new DBPediaFetcher(dBpediaTypes.getdBpediaTypes());
		
		
		DumpHandler wikiFetcher = new WikipediaHandler (args[0], anchorLinkDictionary,listOfRedirects,categories);
		
		wikiFetcher.getEntityList() ;
		wikiFetcher.getEntityInformation() ;
		
		PageRankCalculator pageRankCalculator = new PageRankCalculator();
		pageRankCalculator.performCalculation(anchorLinkDictionary.getIDs().values(),60,0.85);
		
		//dbPediaFetcher.updateEntities(anchorLinkDictionary, args[1]);
		
		//analyser.generatePositive(anchorLinkDictionary,"positiveExamples");
		//analyser.generateNegative(anchorLinkDictionary,"negativeExamples");
		
		//ResultAnalyser ra = new ResultAnalyser ();
		
		//temporary hard written path
		//ra.evaluateResults(0.1,"C:\\Users\\User\\git\\EntitySearch\\src\\main\\resources\\data\\testSet.txt", "C:\\Users\\User\\git\\EntitySearch\\src\\main\\resources\\data\\predict.txt");
		/*
		try {
			xgBoostWrapper.predict("C:\\Users\\User\\git\\EntitySearch\\src\\main\\resources\\data\\trainSet.svm.txt", "C:\\Users\\User\\git\\EntitySearch\\src\\main\\resources\\data\\testSet.svm.txt");
		} catch (XGBoostError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//temporary hard written path
		//ResultAnalyser.evaluateResults(0.5,".\\src\\main\\resources\\data\\testSet.txt", ".\\src\\main\\resources\\data\\predict_xgBoost.txt","xgBoost");
				
		
		
		double max = 0;
		ID bestPageRankedPage = null;
		for (Object o :  anchorLinkDictionary.getIDs().values())
		{
			if (((ID)o).getPageRank()>=max)
			{
				max = ((ID)o).getPageRank();
				bestPageRankedPage = (ID)o;
			}
				
		}
		
		
		
		
		EntityAnalyser entityAnalyser = new EntityAnalyser (anchorLinkDictionary);
		String entityID = "atlanta";
		entityAnalyser.analyse(bestPageRankedPage.getName(), "new_york");
		
		
		
		/*
		Anchor america = (Anchor) anchorLinkDictionary.getAnchors().get("europe");				
		for (ID o : america.getAllReferencedIDs())
		{
			System.out.println(o.getName());
		}
		
		System.out.println("------------------------");
		
		for (String o : listOfRedirects.getRedirectList().keySet())
		{
			System.out.println(o);
		}
		*/
	}
}
