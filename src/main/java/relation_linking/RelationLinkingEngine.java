package relation_linking;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DP_entity_linking.*;
import DP_entity_linking.TrainJsonRead.Records;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class RelationLinkingEngine {

	private static PrintWriter output;
	private static DBPediaOntologyExtractor doe;
	private static FBCategoriesExtractor fce;
	private static int numberOfMatches;

	public static void main(String[] args) throws Exception {

		TrainJsonRead tjr = new TrainJsonRead();
		doe = new DBPediaOntologyExtractor("/Users/fjuras/OneDriveBusiness/DPResources/dbpedia_2015-04.nt");
		Records records = tjr.loadWebquestions();
		fce = new FBCategoriesExtractor(tjr);
		// Word2VecEngine wtv = new Word2VecEngine();
		LexicalParsingEngine lp = new LexicalParsingEngine();

		output = new PrintWriter("/Users/fjuras/OneDriveBusiness/DPResources/Relations.txt", "UTF-8");
		numberOfMatches = 0;
		int numberOfRecords = 0;

		for (Record record : records) {
			// wtv.vectorizeSentence(record.getUtterance());
			numberOfRecords++;
			getRelations(record.getUtterance());
			// lp.parseSentence(record.getUtterance());
		}

		output.println(numberOfMatches + " of " + numberOfRecords);
		output.close();

		// lp.endOfParsing();
	}

	private static boolean isDBPediaRelation(String word) {
		return doe.getLowerDBPediaRelations().contains(word.toLowerCase());
	}

	private static boolean isFBCategory(String word) {
		return fce.getCategories().contains(word);
	}

	private static boolean isInComposedDBPediaRelations(HasWord word, List<HasWord> sentence) {
		boolean matched = false;
		String key = new String();

		for (Map.Entry<String, String> entry : doe.getCleanDBPediaTypes().entrySet()) {
			if (entry.getValue().toLowerCase().equals(word.toString().toLowerCase())) {
				int wordIndex = sentence.indexOf(word);
				key = entry.getKey();
				key = key.substring(0, key.length() - 1);
				String[] r = doe.splitKey(key);
				if (word.toString().toLowerCase().equals(r[0].toLowerCase())) {
					matched = true;
					for (int i = 1; i < r.length; i++) {
						if (sentence.size() < r.length + wordIndex) {
							matched = false;
							break;
						} else if (!r[i].toLowerCase().equals(sentence.get(wordIndex + i).toString().toLowerCase())) {
							matched = false;
							break;
						}
					}
				}
				if (matched) {
					output.println("ComposedDBPedia: " + key);
					return matched;
				}
			}
		}
		return matched;
	}

	private static boolean isInComposedFBRelations(HasWord word, List<HasWord> sentence) {
		boolean matched = false;
		String key = new String();

		for (Map.Entry<String, String> entry : fce.getCleanFBCategories().entrySet()) {
			if (entry.getValue().equals(word.toString().toLowerCase())) {
				int wordIndex = sentence.indexOf(word);
				key = entry.getKey();
				key = key.substring(0, key.length() - 1);
				String[] r = fce.splitKey(key);
				if (word.toString().toLowerCase().equals(r[0])) {
					matched = true;
					for (int i = 1; i < r.length; i++) {
						if (sentence.size() < r.length + wordIndex) {
							matched = false;
							break;
						} else if (!r[i].equals(sentence.get(wordIndex + i).toString().toLowerCase())) {
							matched = false;
							break;
						}
					}
				}
				if (matched) {
					output.println("ComposedFreebase: " + key);
					return matched;
				}
			}
		}
		return matched;
	}

	private static boolean getRelations(String sentence) throws FileNotFoundException, UnsupportedEncodingException {

		Reader reader = new StringReader(sentence);
		boolean matched = false;

		for (Iterator<List<HasWord>> iterator = new DocumentPreprocessor(reader).iterator(); iterator.hasNext();) {
			List<HasWord> word = iterator.next();
			matched = false;
			int matchedInSentence = 0;
			for (int i = 0; i < word.size(); i++) {
				if (isDBPediaRelation(word.get(i).toString())) {
					output.println("DBPedia: " + word.get(i));
					output.println(sentence);
					matched = true;
					matchedInSentence++;
				} else if (isFBCategory(word.get(i).toString())) {
					output.println("Freebase: " + word.get(i));
					output.println(sentence);
					matched = true;
					matchedInSentence++;
				} else if (isInComposedDBPediaRelations(word.get(i), word)) {
					output.println(sentence);
					matched = true;
					matchedInSentence++;
				} else if (isInComposedFBRelations(word.get(i), word)) {
					output.println(sentence);
					matched = true;
					matchedInSentence++;
				}
			}

			if (!matched) {
				output.println();
				output.println(sentence);
				output.println();
			} else {
				output.println(matchedInSentence);
				numberOfMatches++;
			}
		}
		return matched;
	}
}
