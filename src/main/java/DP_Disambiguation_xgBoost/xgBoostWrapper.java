package DP_Disambiguation_xgBoost;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.Booster;
import org.dmlc.xgboost4j.util.Trainer;
import org.dmlc.xgboost4j.util.XGBoostError;

 public class xgBoostWrapper {

	
	public static void predict (String trainFile, String testFile) throws XGBoostError, FileNotFoundException, UnsupportedEncodingException, IOException
	{	
		
		BasicConfigurator.configure();
		//-----setting up train and test data-----
		
		DMatrix trainMat = new DMatrix(trainFile);
		DMatrix testMat = new DMatrix(testFile);
		
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
		watchs.add(new AbstractMap.SimpleEntry<>("test", testMat));
		int round = 200;
		
		
		//-------------training---------
		Booster booster = Trainer.train(params, trainMat, round, watchs, null, null);
		//saving model
		booster.saveModel(".\\src\\main\\resources\\data\\model.bin");

		
		//loading model	
		//Booster booster = new Booster(params, ".\\src\\main\\resources\\data\\model.bin");
		 
		//predict
		float[][] predicts = booster.predict(testMat);
		
		PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\data\\predict_xgBoost.txt", "UTF-8");
		
		for(int i = 0; i < predicts.length; i++)
		{
		    for(int j = 0; j < predicts[i].length; j++)
		    {
		    	writer.println(predicts[i][j]);
		    }
		}
		writer.close();
		
		//predict leaf
		
	
	}
	
}
