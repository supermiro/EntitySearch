package DP_entity_linking.search;

/**
 * Created by miroslav.kudlac on 10/3/2015.
 */

import DP_entity_linking.dataset.Record;
import DP_entity_linking.evalution.IEvaluation;
import DP_entity_linking.evalution.SimpleEvaluation;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

public class Search {
    private static Logger LOGGER = Logger.getLogger(Search.class);

    private Analyzer analyzer;
    private IEvaluation iEvaluation;
    private IndexSearcher indexSearcher;
    private SearchStatistics statistics;

    public Search() throws IOException {
        Directory directory = new MMapDirectory(new File("/workspace/erd/index_wikipedia"));

        analyzer = new MyAnalyzer();
        IndexReader  indexreader = IndexReader.open(directory);
        indexSearcher = new IndexSearcher(indexreader);
        iEvaluation = new SimpleEvaluation(indexSearcher);
    }

    public void start() {
        statistics = new SearchStatistics();
    }

    public void processRecord(Record r, Configuration conf) throws IOException, ParseException {
        TokenStream stream = analyzer.tokenStream(null, new StringReader(r.getUtterance()));
        //TokenStream stream = analyzer.tokenStream(null, new StringReader(r.getAnswer()));
        CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);

        stream.reset();
        StringBuilder strBuilder = new StringBuilder();
        while (stream.incrementToken()) {
            strBuilder.append(cattr.toString() + " ");
        }
        stream.end();
        stream.close();
        String newQuery = strBuilder.toString().trim();
        this.retrieve(r, newQuery, conf);
    }

    public int getScore() {
        statistics.print();
        return statistics.getScore();
    }

    private boolean backMapped(String query, String form) {
        boolean isMapped;
        isMapped = query.toLowerCase().contains(form.toLowerCase());
        return isMapped;
    }

    private String[] prepareFields( Map<String, Float> map) {
        int size = map.size();
        String[] fields = new String[size];
        int i = 0;
        for( Map.Entry<String, Float> entry : map.entrySet()) {
            fields[i] = entry.getKey();
            i++;
        }
        return fields;
    }

    private final Query buildLuceneQuery(final String queryIn, Configuration conf) throws ParseException {
        Query queryL = null;
        String query = queryIn;

        Map<String, Float> boost = conf.getBoost();
        String[] fields = prepareFields(boost);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer, boost);

        queryL = parser.parse(query);
        //BooleanQuery categoryQuery = new BooleanQuery();
        //BooleanQuery mainQuery = new BooleanQuery();
        /*TermQuery catQuery1 = new TermQuery(new Term("db_category", "person"));
        TermQuery catQuery2 = new TermQuery(new Term("db_category", "place"));
        TermQuery catQuery3 = new TermQuery(new Term("db_category", "country"));
        TermQuery catQuery4 = new TermQuery(new Term("db_category", "movie"));
        TermQuery catQuery5 = new TermQuery(new Term("db_category", "language"));
        categoryQuery.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
        categoryQuery.add(new BooleanClause(catQuery2, BooleanClause.Occur.SHOULD));
        categoryQuery.add(new BooleanClause(catQuery3, BooleanClause.Occur.SHOULD));
        categoryQuery.add(new BooleanClause(catQuery4, BooleanClause.Occur.SHOULD));
        categoryQuery.add(new BooleanClause(catQuery5, BooleanClause.Occur.SHOULD));
        mainQuery.add(new BooleanClause(queryL, BooleanClause.Occur.SHOULD));
        mainQuery.add(new BooleanClause(categoryQuery, BooleanClause.Occur.MUST));*/
        return queryL;
    }

    private void retrieve(Record record, String query, Configuration conf) throws IOException, ParseException {
        boolean backMapped_name = false;
        boolean backMapped = false;
        boolean backMapped_alias = false;
        ArrayList<String> toBackMapping = new ArrayList<String>();
        indexSearcher.setSimilarity(new MultiSimilarity(conf.getSims()));
        Query queryL = this.buildLuceneQuery(query, conf);
        TopDocs results = indexSearcher.search(queryL, conf.getNumSearchRes());
        ScoreDoc[] hits = results.scoreDocs;

        // return hits;
        // TUTO to ODREZ

        String answer = record.getAnswer();
        float score = iEvaluation.getScore(hits, answer);
        statistics.statisticScore.count(score);

       // LOGGER.info(record.getQuestion() + ", " + answer + " => " + score);
        for (int i = 0; i < hits.length; i++) {
            Document doc = indexSearcher.doc(hits[i].doc);
            statistics.fbStats(doc);
            statistics.dbStats(doc);

        }
    // USE FOR BACKMAPPING
        for (int i = 0; i < hits.length; i++) {
            Document doc = indexSearcher.doc(hits[i].doc);
            IndexableField[] altNames = doc.getFields("alt");
            String fb_name = doc.get("fb_name");
            String fb_alias = doc.get("fb_alias");
            backMapped_name = false;
            backMapped_alias = false;
            if (fb_name != null) {
                backMapped_name = this.backMapped(record.getQuestion(), fb_name);
            }
            if (fb_alias != null) {
                backMapped_alias = this.backMapped(record.getQuestion(), fb_alias);
            }
            if (backMapped_name) {
                toBackMapping.add(fb_name.toLowerCase());
            } else if (backMapped_alias) {
                toBackMapping.add(fb_alias.toLowerCase());
            } else {
                for (IndexableField altName : altNames) {
                    backMapped = this.backMapped(record.getQuestion(), altName.stringValue());
                    if (backMapped) {
                        String haystack = altName.stringValue().toLowerCase();
                        toBackMapping.add(haystack);
                        break;
                    }
                }
            }
            LOGGER.info(doc.get("title"));
        }
        //USE BACKMAPPING TO MAPPED VALUES
        if (!toBackMapping.isEmpty()) {
            statistics.countBackMapped++;
            LOGGER.info("QUESTION CAN BE BACKMAPPED: " + record.getQuestion());
            String question = record.getUtterance();
            for (String backMap : toBackMapping) {
                question = question.replaceAll(backMap, "").replace("?", "").trim();
            }
            LOGGER.info("BACKMAPPED QUERY: " + question);
            Query queryBackMapped = this.buildLuceneQuery(question, conf);
            TopDocs resultsBackMapped = indexSearcher.search(queryBackMapped, 5);
            ScoreDoc[] hitsBackMapped = resultsBackMapped.scoreDocs;

            answer = record.getAnswer();
            for (int j = 0; j < hitsBackMapped.length; j++) {
                Document docBackMapped = indexSearcher.doc(hitsBackMapped[j].doc);
                LOGGER.info("ANSWERS FOR BACKMAPPED QUESTIONS: " + docBackMapped.get("title") + " --- back mapped---");
            }
            LOGGER.info(question + ", " + answer);

        }
    }


}

