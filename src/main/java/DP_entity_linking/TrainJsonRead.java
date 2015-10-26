package DP_entity_linking;

/**
 * Created by miroslav.kudlac on 10/3/2015.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrainJsonRead {
    private static Logger LOGGER = Logger.getLogger(TrainJsonRead.class);
    private static final File JSON_FILE = new File("C:\\workspace\\webquestions.json");

    private ObjectMapper mapper;
    private Directory directory = new MMapDirectory(new File("/workspace/erd/index_wikipedia"));
    private Analyzer analyzer;
    private int numSearchRes = 20;
    private ArrayList<IndexSearcher> searchers = new ArrayList<IndexSearcher>();
    private IEvaluation iEvaluation;
    private IndexSearcher indexSearcher;

    public TrainJsonRead() throws IOException {
        analyzer = new CountryAnalyzer();
        mapper = new ObjectMapper();
        indexSearcher = new IndexSearcher(IndexReader.open(directory));
        iEvaluation = new SimpleEvaluation(indexSearcher);
    }

    public static class Records extends ArrayList<Record> {
        public Records() {
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public Records loadWebquestions() throws IOException {
        return mapper.readValue(JSON_FILE, Records.class);
    }

    /**
     * @param r
     * @throws IOException
     */
    public void retrieveRecords(Record r) throws IOException, ParseException {
        TokenStream stream = analyzer.tokenStream(null, new StringReader(r.getUtterance()));
        CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);

        stream.reset();
        String[] newList = new String[30];
        StringBuilder strBuilder = new StringBuilder();
        while (stream.incrementToken()) {
            strBuilder.append(cattr.toString() + " ");
        }
        stream.end();
        stream.close();
        String newQuery = strBuilder.toString().trim();
        this.retrieve(r, newQuery, numSearchRes);
    }

    public boolean backMapped(String query, String form) {

        boolean isMapped;
        isMapped = query.toLowerCase().contains(form.toLowerCase());
        return isMapped;
    }

    private final Query buildLuceneQuery(final String queryIn, final Analyzer analyzerIn) throws ParseException {
        Query queryL = null;
        String query = queryIn;
        String[] fields = null;
        Map<String, Float> boosts = new HashMap<String, Float>();
        fields = new String[]{"title", "fb_alias", "alt", "fb_name"};
        //fields = new String[] { "title", "anchor", "alt", "fb_name", "abs" };
        //boosts.put("title", (float) 0.4);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzerIn, boosts);

        queryL = parser.parse(query);
        return queryL;
    }

    private Statistika statistika = new Statistika();

    private void retrieve(Record record, String query, int numSearchRes) throws IOException, ParseException {
        boolean backMapped;
        Similarity[] sims = {
                new BM25Similarity((float) 1.75, (float) 0.4),
                new LMJelinekMercerSimilarity((float) 0.0003),
                new LMDirichletSimilarity(),
                // new DefaultSimilarity()
        };
        indexSearcher.setSimilarity(new MultiSimilarity(sims));
        Query queryL = this.buildLuceneQuery(query, analyzer);
        TopDocs results = indexSearcher.search(queryL, numSearchRes);
        ScoreDoc[] hits = results.scoreDocs;

        String answer = record.getAnswer();
        float score = iEvaluation.getScore(hits, answer);
        statistika.count(score);
        LOGGER.info(record.getQuestion() + ", " + answer + " => " + score);
        /*
        for (int i = 0; i < hits.length; i++) {
            Document doc = indexSearcher.doc(hits[i].doc);
            backMapped = this.backMapped(record.getQuestion(), doc.get("title"));
            if (backMapped) {
                LOGGER.info(doc.get("title") + "--- back mapped: " + backMapped + "----");
            } else {
                LOGGER.info(doc.get("title"));
            }
        }*/
    }

    /**
     * @throws IOException
     */
    public void doJob() throws IOException, ParseException {
        Records records = this.loadWebquestions();

        for (Record record : records) {
            LOGGER.info("------------" + record.getUtterance() + "--------------");
            this.retrieveRecords(record);

        }

        for (Map.Entry entry : statistika.entrySet()) {
            LOGGER.info(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException {

        PropertyConfigurator.configure("log4j.properties");
        // Spellcheck spell = new Spellcheck(new WordFrequenciesT("EntityStore"), "C:\\workspace\\erd\\EntityStore\\spellindex\\1\\spellindex");

        try {
            TrainJsonRead app = new TrainJsonRead();
            app.doJob();

        } catch (IOException ex) {
            LOGGER.error(" error: ", ex);
        }

    }

}

