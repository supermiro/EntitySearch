package DP_entity_linking;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
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
import org.junit.Test;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miroslav.kudlac on 10/4/2015.
 */
public class CountryAnalyzerTest {
    private final Query buildLuceneQuery(final String queryIn, final Analyzer analyzerIn) throws ParseException {
        Query queryL = null;
        String query = queryIn;
        String[] fields = null;
        Map<String, Float> boosts = new HashMap<String, Float>();
        fields = new String[] { "title", "anchor", "alt", "fb_name", "abs" };
        boosts.put("title", (float) 0.4);
        //boosts.put("abs", (float) 0.2);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzerIn, boosts);
        queryL = parser.parse(query);
        return queryL;
    }
    public boolean backMapped(String query, String form) {

        boolean isMapped;
        isMapped = query.toLowerCase().contains(form.toLowerCase());
        return isMapped;
    }
    @Test
    public void testCreateComponents() throws Exception {
        Analyzer analyzer = new CountryAnalyzer();
        Directory directory =  new MMapDirectory(new File("/workspace/erd/index_wikipedia"));
        IndexSearcher indexSearcher = new IndexSearcher(IndexReader.open(directory));
        //indexSearcher.setSimilarity(new LMJelinekMercerSimilarity((float) 0.1));
        //indexSearcher.setSimilarity(new BM25Similarity());
        //indexSearcher.setSimilarity(new DefaultSimilarity());
        Similarity[] sims = {
                new BM25Similarity((float) 1.75, (float) 0.4),
                new LMJelinekMercerSimilarity((float) 0.0003),
                new LMDirichletSimilarity(),
               // new DefaultSimilarity()
        };
        indexSearcher.setSimilarity(new MultiSimilarity(sims));
        String question = " Religion in france blabblalblabla";
        TokenStream stream = analyzer.tokenStream(null, new StringReader(question));
        CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        String[] newList = new String[30];
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        while (stream.incrementToken()) {
           strBuilder.append(cattr.toString() + " ");
            i++;
        }
        stream.end();
        stream.close();
        String newQuery = strBuilder.toString().trim();
        Query queryL = this.buildLuceneQuery(newQuery, analyzer );
        TopDocs results = indexSearcher.search(queryL, 5);
        ScoreDoc[] hits = results.scoreDocs;
        for (int j = 0; j < hits.length; j++) {
            Document doc = indexSearcher.doc(hits[j].doc);
            boolean backMapped = this.backMapped(question, doc.get("title"));
            float score = hits[j].score/300;
            System.out.println(doc.get("title") + " ALTER " +  doc.get("alt")  + " fb_name " +  doc.get("fb_name") + " SCORE " + score);
        }
        System.out.println(newQuery);

    }
}