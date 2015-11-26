package relation_linking;

import java.util.Collection;
import java.io.*;
import word2vec.*;
import util.*;

/**
 * Created by agibsonccc on 10/9/14.
 */
public class Word2VecEngine {

	public GloVeSpace model;
	private PrintWriter output;

	public Word2VecEngine(boolean glove) {

		if (glove) {
			model = GloVeSpace.load("/Users/fjuras/Downloads/glove.6B/glove.6B.50d.txt", false, false);
			System.out.println(model.distanceSimilarity(model.sentenceVector("married to"), model.vector("spouse")));
		}
	}
	
	public double getSimilarity(String sentence, String word){
		double similarity = model.distanceSimilarity(model.sentenceVector(sentence), model.vector(word));
		System.out.println(similarity);
		return similarity;
	}
	
	public double getWordsSimilarity(String word1, String word2){
		double similarity = model.distanceSimilarity(word1, word2);
		System.out.println(similarity);
		return similarity;
	}
	
	public boolean isWordInModel(String word){
		return model.contains(word);
	}
}