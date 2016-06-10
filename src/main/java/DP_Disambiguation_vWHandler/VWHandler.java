package DP_Disambiguation_vWHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import DP_Disambiguation_FeatureBuilding.Entity;

public class VWHandler 
{
	
	public void interpretPrediction(ArrayList<String> canonicIDs,ArrayList<String> candidateIDs)
	{
		Float vowpalValue = null;
		Float predictionValue = null;
		int iterator = 0;
		
		ArrayList<String> combinationString = new ArrayList<String>();
		ArrayList<Float> combinationFloat = new ArrayList<Float>();
		ArrayList<String> sortedCombination = new ArrayList<String>();
		
		for (String canonicEntity : canonicIDs) 
		{
		
					for (String candidateEntity : candidateIDs) 
					{
						combinationString.add(canonicEntity + " - " + candidateEntity + ": ");
					}
		}
		
		
		{
		try(BufferedReader br = new BufferedReader(new FileReader("0001A.predict.tmp"))) 
		{
		    for(String line; (line = br.readLine()) != null; ) 
			    {
		    		vowpalValue = Float.parseFloat(line);
			        predictionValue = (float) (1/(1+Math.pow(2.718281828459045, (-1*vowpalValue))));
			        
			        combinationFloat.add(predictionValue);
			        
			        System.out.println(combinationString.get(iterator) + predictionValue);
			        iterator++;
			    }
		    		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		}
	}
}
