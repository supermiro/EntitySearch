package DP_Disambiguation_xgBoost;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.Booster;
import org.dmlc.xgboost4j.util.Trainer;
import org.dmlc.xgboost4j.util.XGBoostError;

import biz.k11i.xgboost.Predictor;
import biz.k11i.xgboost.util.FVec;


 public class xgBoostWrapper {

	 
 public static void convertFromVWFormat (String vowpalPath, String outputPath) throws IOException{
		 
		 
		 PrintWriter f = new PrintWriter(outputPath, "UTF-8");
		 String line = null;
		 BufferedReader br = new BufferedReader(new FileReader(vowpalPath));

			while ((line = br.readLine()) != null) {
				
		    	String outline = new String();
		    	String [] parts = line.replaceAll(" *$", "").split("\\|");
		    	int target=1;		
		    	
		    	if (parts[0].split(" ")[0].startsWith("-"))
		    		target=0;
		    	
		    	outline = Integer.toString(target);
		    	
		    	for (int i=1 ; i < parts.length;i++)
		    	{
		    		String ns = parts[i].replaceAll(" *$", "").replaceAll("^ *", "");
		    		String [] tokens = ns.split(" ");
		    		String ns_name = tokens[0].replaceAll(" *$", "").replaceAll("^ *", "");
		    		
		    		
		    		for (int j = 1; j < tokens.length; j++)
		    		{
		    			String token = tokens[j];
    	                String key = token;
    	                String val = null;
    	                
    	                if (token.contains(":"))
    	                {
    	                	key = token.split(":")[0];
    	                	val = token.split(":")[1];
    	                }
    	                if (val == null)
    	                	outline += (" " + (Integer.toString(Math.abs(key.hashCode())%10000)) + ":" + "1");
    	                else
    	                	outline += (" " + (Integer.toString(Math.abs(key.hashCode())%10000)) + ":" + val);
		    		}
		    	outline.replaceAll("\n", "");
		    	}
		    	f.write(outline + "\n");
		    }
			f.close();
	 }
	 
	 
	 public static ArrayList <String> convertFromVWFormat (ArrayList <String> vowpalLines){
		 
		 ArrayList <String> result = new ArrayList <String>();
		 
		    for (String line : vowpalLines)
		    {
		    	String outline = new String();
		    	String [] parts = line.replaceAll(" *$", "").split("|");
		    	int target=1;		
		    	
		    	if (parts[0].split(" ")[0].startsWith("-"))
		    		target=0;
		    	
		    	outline = Integer.toString(target);
		    	
		    	for (int i=1 ; i < parts.length;i++)
		    	{
		    		String ns = parts[i].replaceAll(" *$", "").replaceAll("^ *", "");
		    		String [] tokens = ns.split(" ");
		    		String ns_name = tokens[0].replaceAll(" *$", "").replaceAll("^ *", "");
		    		
		    		
		    		for (int j = 1; j < tokens.length; j++)
		    		{
		    			String token = tokens[j];
    	                String key = token;
    	                String val = null;
    	                
    	                if (token.contains(":"))
    	                {
    	                	key = token.split(":")[0];
    	                	val = token.split(":")[1];
    	                }
    	                if (val == null)
    	                	outline += (" " + (Integer.toString(Math.abs(key.hashCode())%10000)) + ":" + "1");
    	                else
    	                	outline += (" " + (Integer.toString(Math.abs(key.hashCode())%10000)) + ":" + val);
		    		}
		    	outline.replaceAll("\n", "");
		    	result.add (outline);
		    	}
		    	/*
		    	                        
		    	                        outline += (' '+str(get_hash(key))+':') + (val if val else '1')
		    	            f.write(outline.replace('\n','')+'\n')
		    	    f.close()
		    	    */
		    }
		 return result;
	 }
	
	 public static void learnModel (String trainFilePath, String modelPath) throws XGBoostError, FileNotFoundException, UnsupportedEncodingException, IOException
		{
		 BasicConfigurator.configure();
			//-----setting up train and test data-----
			
			DMatrix trainMat = new DMatrix(trainFilePath);
			
			//-----setting up model parameters------
			
			Map<String, Object> paramMap = new HashMap<String, Object>() {
				  {
					put("booster", "gbtree");
					put("objective", "binary:logistic");
					put("eval_metric", "auc");  
					put("alpha", 4); 
					put("early.stoping.rounds", 1 );  
					put("seed", 12); 
					  
				    put("gamma", 1.0);
				    put("min_child_weight", 4);
				    put("max_depth", 20);
				    
				    put("silent", 1);
				    put("objective", "binary:logistic");
				    put("eval_metric", "logloss");
				    
				    put("save_period", 50);
				  }
				};
				Iterable<Entry<String, Object>> params = paramMap.entrySet();
			
				
			//specifiy a watchList to see the performance
			//any Iterable<Entry<String, DMatrix>> object could be used as watchList
			List<Entry<String, DMatrix>> watchs =  new ArrayList<>();
			
			watchs.add(new AbstractMap.SimpleEntry<>("train", trainMat));
			int round = 200;
			
			
			//-------------training---------
			Booster booster = Trainer.train(params, trainMat, round, watchs, null, null);
			//saving model
			booster.saveModel(modelPath);
		}
	 
	public static ArrayList<String> predict (ArrayList<String> input, String modelPath) throws XGBoostError, FileNotFoundException, UnsupportedEncodingException, IOException
	{	
		 double sum = 0;
		 Predictor predictor = new Predictor(xgBoostWrapper.class.getResourceAsStream(modelPath));
		 ArrayList <String> output = new ArrayList <String>();
		
		  List<SimpleEntry<Integer, FVec>> data = new ArrayList<>();

	        for (String line : input) 
		        {
		            String[] values = line.split(" ");
	
		            Map<Integer, Float> map = new HashMap<>();
	
		            for (int i = 1; i < values.length; i++) {
		                String[] pair = values[i].split(":");
		                map.put(Integer.parseInt(pair[0]), Float.parseFloat(pair[1]));
		            }
	
		            data.add(new SimpleEntry<>(Integer.parseInt(values[0]), FVec.Transformer.fromMap(map)));
		        }

	        
	        

	        for (SimpleEntry<Integer, FVec> pair : data) {

	            double[] predicted = predictor.predict(pair.getValue());

	            System.out.println(predicted[0]);
	            output.add(Double.toString(predicted[0]));
	            
	            
	            double predValue = Math.min(Math.max(predicted[0], 1e-15), 1 - 1e-15);
	            System.out.println("alternative\t" + predicted[0]);
	            //int actual = pair.getKey();
	            //sum = actual * Math.log(predValue) + (1 - actual) * Math.log(1 - predValue);
	        }

	       // double logLoss = -sum / data.size();

	       // System.out.println("Logloss: " + logLoss);
	        
	        
			
			return output;	
		
		
		
		/*
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		BasicConfigurator.configure();
		//-----setting up train and test data-----
		
		
		
		//need to put converted VW input into DMatrix !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		for! (String xgLine : input)
			
		{
			
		}
		
		
		
		DMatrix testMat = new DMatrix(testFilePath);
		
		//-----setting up model parameters------
		
		Map<String, Object> paramMap = new HashMap<String, Object>() {
			  {
				put("booster", "gbtree");
				put("objective", "binary:logistic");
				put("eval_metric", "auc");  
				put("alpha", 4); 
				put("early.stoping.rounds", 1 );  
				put("seed", 12); 
				  
			    put("gamma", 1.0);
			    put("min_child_weight", 4);
			    put("max_depth", 20);
			    
			    put("silent", 1);
			    put("objective", "binary:logistic");
			    put("eval_metric", "logloss");
			    
			    put("save_period", 50);
			  }
			};
			Iterable<Entry<String, Object>> params = paramMap.entrySet();
		
			
		//specifiy a watchList to see the performance
		//any Iterable<Entry<String, DMatrix>> object could be used as watchList
		List<Entry<String, DMatrix>> watchs =  new ArrayList<>();
		
		watchs.add(new AbstractMap.SimpleEntry<>("test", testMat));
		
		//loading model	
		Booster booster = new Booster(params, modelPath);
		 
		//predict
		float[][] predicts = booster.predict(testMat);
		
		
		//PrintWriter writer = new PrintWriter(predictPath, "UTF-8");
		ArrayList <String> output = new ArrayList <String>();
		
		for(int i = 0; i < predicts.length; i++)
		{
		    for(int j = 0; j < predicts[i].length; j++)
		    {
		    	//writer.println(predicts[i][j]);
		    	output.add(Float.toString(predicts[i][j]));
		    }
		}
		//writer.close();
		
		return output;
		
		//predict leaf
		
	*/
	}
	
}
