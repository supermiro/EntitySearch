package DP_Disambiguation_ResultAnalyser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.dmlc.xgboost4j.util.XGBoostError;
import org.xml.sax.SAXException;

import DP_Disambiguation_DumpHandler.Dictionary;
import DP_Disambiguation_DumpHandler.RedirectList;
import DP_Disambiguation_DumpHandler.Tuple;
import DP_Disambiguation_FeatureBuilding.EntityAnalyser;
import DP_Disambiguation_vWHandler.VWWrapper;
import DP_Disambiguation_xgBoost.xgBoostWrapper;
import DP_entity_linking.dataset.Record;

public class ResultAnalyser 
{
//otvorim predict.txt
	
	public static ArrayList<ArrayList<Tuple<Integer, Double>>> interpretePrediction (String predictPathVW,ArrayList <String> xgPredictions ) throws NumberFormatException, IOException
	{
	String predictLine = null;
	FileReader VWPredict = new FileReader(predictPathVW);
	//FileReader XGPredict = new FileReader(predictPathXG);
	BufferedReader predictReaderVW = new BufferedReader(VWPredict);
	//BufferedReader predictReaderXG = new BufferedReader(XGPredict);
	int index = 0; 
	ArrayList <Tuple<Integer,Double>> indexValuePairListVW = new ArrayList <Tuple<Integer,Double>> ();
	ArrayList <Tuple<Integer,Double>> indexValuePairListXG = new ArrayList <Tuple<Integer,Double>> ();
	
	//first we collect results form files and interprete them percentally
	
	
	
	// first vowpal wabbit
	while (predictPathVW != null && (predictLine = predictReaderVW.readLine()) != null) 
    {
    	
    	predictLine = predictLine.replaceAll("\\,", "\\.");
    	
    	//System.out.println(predictLine);
    	Double predictValue = Double.parseDouble(predictLine);
    	
    	Tuple<Integer, Double> indexValuePair = new Tuple<Integer, Double>(index, predictValue);
    	predictValue = 1/(double)(1+Math.exp(-1*predictValue));
    	indexValuePair.setSecond(predictValue);    	
    	indexValuePairListVW.add(indexValuePair);
    	index++;
    }
	
	//second xgBoost
	if(xgPredictions != null){
	index = 0;
	for (String xgPredictLine : xgPredictions) 
    {
    	
    	Double predictValue = Double.parseDouble(xgPredictLine);
    	
    	Tuple<Integer, Double> indexValuePair = new Tuple<Integer, Double>(index, predictValue);
    	indexValuePair.setSecond(predictValue);    	
    	indexValuePairListXG.add(indexValuePair);
    	index++;
    }
	}
	
	// we sort both lists according to values
	
	Tuple <Integer,Double> currentPairVW = null;
	Tuple <Integer,Double> currentPairXG = null;
	
	for (int x = 0; x < (index-1);x++)
	{
		Tuple <Integer,Double> tempPairVW = null;
		Tuple <Integer,Double> tempPairXG = null;
		
		for (int i = 0; i< index-x;i++)
		{
			
			if (predictPathVW != null){
				currentPairVW = indexValuePairListVW.get(i);		
				if (tempPairVW != null && (currentPairVW.getSecond() > tempPairVW.getSecond()))
				{
					Collections.swap(indexValuePairListVW, i-1, i);
				}
				else
				tempPairVW = currentPairVW;
			}
			
			if(xgPredictions != null){
				
				currentPairXG = indexValuePairListXG.get(i);
				if (tempPairXG != null && (currentPairXG.getSecond() > tempPairXG.getSecond()))
				{
					Collections.swap(indexValuePairListXG, i-1, i);
				}
				else
				tempPairXG = currentPairXG;
			}
		}	
	}
	//We print the results
	
	ArrayList <ArrayList <Tuple<Integer,Double>>> results = new ArrayList <ArrayList <Tuple<Integer,Double>>> ();
	
	if(predictPathVW != null){
	
	System.out.println("----Vowpal Wabbit results----");
	int i = 0;
	for (Tuple<Integer,Double> currentPair : indexValuePairListVW)
	{
		i++;
		System.out.println("#" + i + " is on index " + currentPair.getFirst() +  " with value: " + currentPair.getSecond());
	}
		results.add(indexValuePairListVW);
	}
	
	
	if(xgPredictions != null){
	System.out.println("----xgBoost results----");
	int i = 0;
	for (Tuple<Integer,Double> currentPair : indexValuePairListXG)
	{
		i++;
		System.out.println("#" + i + " is on index " + currentPair.getFirst() +  " with value: " + currentPair.getSecond());
	}
	results.add(indexValuePairListXG);
	}
	
	return results;
	
	}
	
	public static void orderCandidates (String canonicID, ArrayList <ArrayList<String>> listOfCandidateLists,Dictionary anchorLink, HashMap <String,String> listOfRedirects, Record record, String method) throws IOException, InterruptedException, XGBoostError, XPathExpressionException, ParserConfigurationException, SAXException, ParseException{
		ArrayList<Integer> endIndexes=null;
		ArrayList<String> vowpalInstances = null;
		ArrayList<String> xgInstances = null;
		EntityAnalyser analyser = new EntityAnalyser (anchorLink, listOfRedirects);
		 
		Tuple <ArrayList <Integer>,Tuple<ArrayList <String>,ArrayList <String>>> outcome = analyser.generateInstaces(canonicID, listOfCandidateLists);
		 
		if (outcome != null)
			endIndexes = outcome.getFirst();
		 
		
		if (outcome == null || endIndexes == null || (endIndexes.size() == 1 && listOfCandidateLists.size() == 1 && listOfCandidateLists.get(0).size() != 1 ))
		{
			if (record != null)
			System.out.print("Unable to procees prediction on question " + record.getQuestion() + " with canonic : " + canonicID + " and list of candidates: ");
			else
				System.out.print("Unable to procees prediction on question with canonic : " + canonicID + " and list of candidates: ");	
			
			for (String l : listOfCandidateLists.get(0))
				System.out.print(l + " ");
			System.out.println();
			return;
		}
		

		 xgInstances = outcome.getSecond().getSecond();
		 vowpalInstances = outcome.getSecond().getFirst();
		
		
		ArrayList<String> xgBoostPrediction = null;
		ArrayList<ArrayList<Tuple<Integer, Double>>> result = null;
		
		if (method.equals("xgboost"))
		{
			xgBoostPrediction = xgBoostWrapper.predict(vowpalInstances, "src\\main\\resources\\data\\dataSets\\xgBoostModel.dat");
			result = ResultAnalyser.interpretePrediction(null, xgBoostPrediction);
			
		}
		
				
		if (method.equals("vowpal"))
		{
			//HardPaths
			VWWrapper.predict("D:\\DP\\vw\\vowpal_wabbit\\vowpalwabbit\\vw","D:\\DP\\EntitySearch\\src\\main\\resources\\data\\dataSets\\candidateListVW.txt", "D:\\DP\\EntitySearch\\src\\main\\resources\\data\\dataSets\\vwModel.dat", "D:\\DP\\EntitySearch\\src\\main\\resources\\data\\predicts\\vwPredictions.txt");
			result = ResultAnalyser.interpretePrediction("D:\\DP\\EntitySearch\\src\\main\\resources\\data\\predicts\\vwPredictions.txt", null);
			
		}	
		
				
		if (method.equals("both"))
		{
			result = ResultAnalyser.interpretePrediction("C:\\Users\\Martin\\git\\EntitySearch\\src\\main\\resources\\data\\predicts\\vwPredictions.txt", xgBoostPrediction);
		}
		
		int index = 0;
		
		if (record != null)
		System.out.print("Most probable candidates with different methods for \"" + canonicID + "\" and query\n" + record.getQuestion() + "\nare :");
		else
		System.out.print("Most probable candidates with different methods for \"" + canonicID + "\"\nare :");	
		
		//2 sets, one for VW prediction and one for XG prediction
		int methodCounter = 0;
		
		if (result != null)
		for ( ArrayList<Tuple<Integer, Double>> predictionMethod:result)
		{
			methodCounter++;
			System.out.print("\nmethod " + methodCounter);
			index = 0;
			int candidateListCounter = 0;
			for (ArrayList<String> specificCandidateList: listOfCandidateLists)
			{
				for (Tuple<Integer,Double> prediction : predictionMethod )
				{
					if (prediction.getFirst() >= endIndexes.get(candidateListCounter) && prediction.getFirst() < endIndexes.get(candidateListCounter+1))
					{
						System.out.print ( "\n\t" + specificCandidateList.get(prediction.getFirst()-endIndexes.get(candidateListCounter)));						
						break;
					}
				}
				candidateListCounter ++;
			}
			System.out.println();
		}
	}
	
	
	
	
	public static void evaluateResults(Double threshold,String testSet, String predict, String MLtool) throws IOException
	{
	String predictLine = null;
	String testSetLine = null;
	int TP = 0;
	int TN = 0;
	int FP = 0;
	int FN = 0;
	int counter = 0;
	
	if (MLtool.equals("vowpal"))
	{
	
	FileReader fRTestSet = new FileReader(testSet);
	FileReader fRPredict = new FileReader(predict);
	BufferedReader predictReader = new BufferedReader(fRPredict);
	BufferedReader testSetReader = new BufferedReader(fRTestSet);
	
	    while ((predictLine = predictReader.readLine()) != null) 
	    {
	    	counter ++;
	    	testSetLine = testSetReader.readLine();
	    	String[] parts = testSetLine.split(" ");
	    	testSetLine = parts[0];
	    	
	    	predictLine = predictLine.replaceAll("\\,", "\\.");
	    	testSetLine = testSetLine.replaceAll("\\,", "\\.");
	    	
	    	
	    	
	    	Double predictValue = Double.parseDouble(predictLine);
	    	Double realValue = Double.parseDouble(testSetLine);
	    	predictValue = 1/(double)(1+Math.exp(-1*predictValue));
	    	
	    	if (realValue == -1)
	    		realValue = 0.0;
	    	
	    	//System.out.println(counter + " " + predictValue + " " + realValue);
	    	
	    	if (realValue == 0)
	    	{
	    		if (predictValue < (1-threshold))
	    		{
	    			TN++;
	    		}
	    		
	    		if ( predictValue >= (1-threshold))
	    		{
	    			FP++;
	    		}
	    			
	    	}
	    	if (realValue == 1)
	    	{
	    		if (predictValue >= (1-threshold))
	    		{
	    			TP++;
	    		}
	    		
	    		if (predictValue < (1-threshold))
	    		{
	    			FN++;
	    		}
	    		
	    	}
	    }
	}
	else
	{
		int index = 0;
		for (String line : predict.split("\n"))
		{
			String vowpalLines [] = testSet.split("\n");
			String[] parts = vowpalLines[index].split(" ");
			
			Double realValue = Double.parseDouble(parts[0]);
			Double predictValue = Double.parseDouble(line);
			
			
			if (realValue == 0)
	    	{
	    		if (predictValue < (1-threshold))
	    		{
	    			TN++;
	    		}
	    		
	    		if ( predictValue >= (1-threshold))
	    		{
	    			FP++;
	    		}
	    			
	    	}
	    	if (realValue == 1)
	    	{
	    		if (predictValue >= (1-threshold))
	    		{
	    			TP++;
	    		}
	    		
	    		if (predictValue < (1-threshold))
	    		{
	    			FN++;
	    		}
	    		
	    	}
	    	
	    	
	    	
		}
	}
	    
	    System.out.println("TP - " + TP + " TN - " + TN + " FP - " + FP + " FN - " + FN);
	    System.out.println("Precission - " + (TP/(double)(TP+FP)) + " Recall - " + (TP/(double)(TP+FN)) + " Accuracy - " + ((TN+TP)/(double)(counter)));
	}
}


