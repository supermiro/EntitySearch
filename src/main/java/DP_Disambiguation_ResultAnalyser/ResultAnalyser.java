package DP_Disambiguation_ResultAnalyser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ResultAnalyser 
{
//otvorim predict.txt
	
	public void evaluateResults(Double threshold,String testSet, String predict) throws IOException
	{
	String predictLine = null;
	String testSetLine = null;
	int TP = 0;
	int TN = 0;
	int FP = 0;
	int FN = 0;
	int counter = 0;
	
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
	    
	    System.out.println("TP - " + TP + " TN - " + TN + " FP - " + FP + " FN - " + FN);
	    System.out.println("Precission - " + (TP/(double)(TP+FP)) + " Recall - " + (TP/(double)(TP+FN)) + " Accuracy - " + ((TN+TP)/(double)(counter)));
	}
}
