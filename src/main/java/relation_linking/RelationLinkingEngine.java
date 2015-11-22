package relation_linking;

import java.io.*;
import java.util.Iterator;
import java.util.List;

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

		output.println(numberOfMatches +" of "+numberOfRecords);
		output.close();

		// lp.endOfParsing();
	}

	private static void getRelations(String sentence) throws FileNotFoundException, UnsupportedEncodingException {

		Reader reader = new StringReader(sentence);

		for (Iterator<List<HasWord>> iterator = new DocumentPreprocessor(reader).iterator(); iterator.hasNext();) {
			List<HasWord> word = iterator.next();
			boolean matched = false;
			int matchedInSentence = 0;
			for (int i = 0; i < word.size(); i++) {
				if (doe.getDBPediaRelations().contains(word.get(i).toString())
						|| fce.getCategories().contains(word.get(i).toString())) {
					output.println(sentence);
					output.println(word.get(i));
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
	}
}
