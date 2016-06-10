package DP_Disambiguation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
import DP_Disambiguation_DumpHandler.WikiParser;
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
import DP_Disambiguation_vWHandler.VWWrapper;
import DP_Disambiguation_xgBoost.xgBoostWrapper;
import DP_entity_linking.dataset.DataSet;
import DP_entity_linking.dataset.Record;
import DP_entity_linking.search.Configuration;
import DP_entity_linking.search.DefaultConfiguration;
import DP_entity_linking.search.ResultPreprocessing;
import DP_entity_linking.search.Search;


public class main {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, ParseException, InterruptedException, XGBoostError, org.apache.lucene.queryparser.classic.ParseException 
	{		
		
		File file = new File("disambiguation.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		
		
		 
		String canonicID = "duff_beer";
		
		ArrayList<String> entityCandidates1 = new ArrayList<String>();
		ArrayList<String> entityCandidates2 = new ArrayList<String>();
		
		entityCandidates1.add("simpson_desert");
		entityCandidates1.add("the_simpsons");
		entityCandidates1.add("simpson_and_delilah");
		entityCandidates1.add("the_simpsons_movie");
		entityCandidates1.add("south_park");
		
		
		entityCandidates2.add("kia_rio");
		entityCandidates2.add("seat_toledo");
		entityCandidates2.add("dacia");
		entityCandidates2.add("michael_schumacher");
		
		
		
		ArrayList<ArrayList<String>> entityCandidatesLists = new ArrayList<ArrayList<String>> ();
		entityCandidatesLists.add(entityCandidates1);
		entityCandidatesLists.add(entityCandidates2);
		
		
		
		
		//entityCandidatesLists.add(entityCandidates2);
		
		Dictionary anchorLinkDictionary = new Dictionary();
		HashMap <String,String> redirectList = new HashMap <String,String>  ();
		Categories categories = new Categories();		
		DBpediaTypes dBpediaTypes = new DBpediaTypes();
		DBPediaFetcher dbPediaFetcher = new DBPediaFetcher(dBpediaTypes.getdBpediaTypes());
		EntityAnalyser analyser = new EntityAnalyser (anchorLinkDictionary, redirectList);
		
		WikiParser wikiFetcher = new WikiParser (categories,anchorLinkDictionary,properties.getProperty("knowledgeBasePath"),properties.getProperty("redirectsPath"), properties.getProperty("entityLinksPath"), properties.getProperty("categoriesPath"));
		//wikiFetcher.createListFiles();
		wikiFetcher.loadRedirectsToMemory();
		wikiFetcher.loadAnchorLinksToMemory();
		wikiFetcher.loadCategoriesToMemory();
		
		
		/*
		Dictionary anchorLinkDictionary = new Dictionary();
		RedirectList listOfRedirects = new RedirectList();
		Categories categories = new Categories();
		EntityAnalyser analyser = new EntityAnalyser (anchorLinkDictionary, listOfRedirects);
		
		
		DumpHandler wikiFetcher = new WikipediaHandler ( properties.getProperty("knowledgeBasePath"), anchorLinkDictionary,listOfRedirects,categories);
		
		wikiFetcher.getEntityList() ;
		wikiFetcher.getEntityInformation() ;
		*/
		
		
		PageRankCalculator pageRankCalculator = new PageRankCalculator();
		pageRankCalculator.performCalculation(anchorLinkDictionary.getIDs().values(),60,0.85);
		
		dbPediaFetcher.updateEntities(anchorLinkDictionary, properties.getProperty("dbPediaPath"));		
		
		
		
		
		
				
		analyser.generatePositive(anchorLinkDictionary,properties.getProperty("positiveInstancesPath"));
		analyser.generateNegative(anchorLinkDictionary,properties.getProperty("negativeInstancesPath"));
				
		
		//ResultAnalyser ra = new ResultAnalyser ();
		
		//temporary hard written path
		//ResultAnalyser.evaluateResults(0.5,"C:\\Users\\Martin\\git\\EntitySearch\\testSet.txt", "predict_xgBoost.txt","xgBoost");
		
		//VWWrapper.learnModel(properties.getProperty("vowpalPath"),properties.getProperty("trainSetPathVW"), properties.getProperty("modelPathVW"));
		//VWWrapper.predict(properties.getProperty("vowpalPath"),properties.getProperty("testSetPathVW"), properties.getProperty("modelPathVW"), properties.getProperty("predictionsPathVW"));
		
		//xgBoostWrapper.convertFromVWFormat(properties.getProperty("trainSetPathVW"),properties.getProperty("trainSetPathXG"));		
		//xgBoostWrapper.learnModel(properties.getProperty("trainSetPathXG"), properties.getProperty("modelPathXG"));
		//xgBoostWrapper.predict(xgBoostInput, properties.getProperty("modelPathXG"));
	
				
		//temporary hard written path
		//ResultAnalyser.evaluateResults(0.5,vowpalLines, xgBoostPrediction,"xgBoost");
		//ResultAnalyser.evaluateResults(0.5,properties.getProperty("testSetPathVW"), properties.getProperty("predictionsPathVW"),"vowpal");
		
		
		ResultAnalyser.orderCandidates(canonicID, entityCandidatesLists, anchorLinkDictionary, redirectList, null , "both");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		
		
		// Spracuj dataset
        DataSet dataset = new DataSet();
        List<Record> records = dataset.loadWebquestions();
        records = records.subList(0, 2000);
        Configuration conf;

        // Konfiguracia ziskana cez chromozon
        //Chromozon chromozon = new Chromozon();
        //chromozon.create(randnum);
       // conf = chromozon.get();

        // Defaultna konfiguracia
        conf = new DefaultConfiguration();
        Search search = new Search();
        search.start();
        
        String canonicID = null;
        ArrayList<ArrayList<String>> entityCandidatesLists = new ArrayList<ArrayList<String>> ();
                
        for (Record record : records) {
            List<String> a = search.processRecord(record, null);
             
             ResultPreprocessing result = new ResultPreprocessing();
             List<List<String>> finalResultPreprocessed = result.results(record.getQuestion(), a)
            
            
            //mozny problem s konverziou listu do arraylistu
             * 
             * 
             * 
             * 
             * 
             * 
             * 
             if (a.size() > 0)
            {
		            for (List<String>candidateList : finalResultPreprocessed)
		            {
		            	entityCandidatesLists.add(candidateList);
		            }
            }
         
         
         
         
         
         
         
         
         
         
         
         
            
            if (a.size() > 0)
            {
	            canonicID = a.get(0);
	            
	            for (int x = 1;x < a.size();x++)
	            	entityCandidates.add(a.get(x));
	            
	            ResultAnalyser.orderCandidates(canonicID, entityCandidatesLists, anchorLinkDictionary, listOfRedirects, record, "vowpal");
            }
            
        }

        int fitness = search.getScore();
        //LOGGER.info("------------" + Integer.toString(fitness) + "--------------");
		*/
		
		
		
		
		
		
		
		
		
		
		/*
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
		*/
		
		
		
		//EntityAnalyser entityAnalyser = new EntityAnalyser (anchorLinkDictionary);
		//String entityID = "atlanta";
		//entityAnalyser.analyse(bestPageRankedPage.getName(), "new_york");
		
		
		
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
