package DP_entity_linking.search;

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
import java.util.*;

/**
 * Created by miroslav.kudlac on 3/5/2016.
 */
public class FinalSearch {




        private static Logger LOGGER = Logger.getLogger(Search.class);

        private Analyzer analyzer;
        private IEvaluation iEvaluation;
        private IndexSearcher indexSearcher;
        private BackMapping backMapping;

        public FinalSearch() throws IOException {
            Directory directory = new MMapDirectory(new File("/workspace/erd/index_wikipedia"));
            backMapping = new BackMapping();
            analyzer = new MyAnalyzer();
            IndexReader indexreader = IndexReader.open(directory);
            indexSearcher = new IndexSearcher(indexreader);
            iEvaluation = new SimpleEvaluation(indexSearcher);
        }

        public List<String> processRecord(String r, Configuration conf) throws IOException, ParseException {
            if (conf == null){
                conf = new DefaultConfiguration();
            }
            TokenStream stream = analyzer.tokenStream(null, new StringReader(r));
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
            return this.retrieve(r, newQuery, conf);
        }


        public  BackMapping backMapped(String query, String form) {
            boolean isMapped = false;
            boolean contains;
            int string_count = 0;
            String result = "";
            form = form.replaceAll("[^a-zA-Z0-9]+", " ").trim();
            String [] split = form.split(" ");
            split = new HashSet<String>(Arrays.asList(split)).toArray(new String[0]);
            if (split.length <= 2) {
                if (split.length > 1 && form.matches(".*\\bdisambiguation\\b.*")) {
                    form = form.replace("disambiguation", "");
                }
                isMapped = query.toLowerCase().contains(form.toLowerCase().trim());
                backMapping.setIsMapped(isMapped);
                backMapping.setWords(form.trim());
            } else {
                for (String s : split){
                    if (s.length() < 3  && !Character.isUpperCase(s.charAt(0))){
                        continue;
                    }
                    s = s.replaceAll("[^a-zA-Z0-9]+"," ").trim();
                    contains = query.toLowerCase().matches(".*\\b" + s.toLowerCase() + "\\b.*");
                    if (contains) {
                        string_count++;
                        result = result + " " + s;
                    }
                }
                if (string_count >= 2){
                    isMapped = true;
                    backMapping.setIsMapped(isMapped);
                    backMapping.setWords(result.trim());
                }
            }
            return backMapping;
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
            return queryL;
        }

        private List<String> retrieve(String record, String query, Configuration conf) throws IOException, ParseException {
            boolean backMapped_name = false;
            boolean backMapped = false;
            boolean backMapped_alias = false;
            List<String> finalId = new ArrayList<>();
            BackMapping backMappingResult_alias = null;
            BackMapping backMappingResult_name = null;
            BackMapping backMappingResult = null;

            ArrayList<String> toBackMapping = new ArrayList<String>();
            indexSearcher.setSimilarity(new MultiSimilarity(conf.getSims()));
            Query queryL = this.buildLuceneQuery(query, conf);
            TopDocs results = indexSearcher.search(queryL, conf.getNumSearchRes());
            ScoreDoc[] hits = results.scoreDocs;
            List<ScoreDoc> backMappedResults = new ArrayList<ScoreDoc>();

            // USE FOR BACKMAPPING
            for (int i = 0; i < hits.length; i++) {
                Document doc = indexSearcher.doc(hits[i].doc);
                IndexableField[] altNames = doc.getFields("alt");
                String fb_name = doc.get("fb_name");
                String fb_alias = doc.get("fb_alias");
                backMapped_name = false;
                backMapped_alias = false;
                if (fb_name != null) {
                    backMappingResult_name = this.backMapped(record, fb_name);
                    backMapped_name =  backMappingResult_name.isMapped();
                }
                if (fb_alias != null) {
                    backMappingResult_alias = this.backMapped(record, fb_name);
                    backMapped_name =  backMappingResult_alias.isMapped();
                }
                if (backMapped_name) {
                    toBackMapping.add(backMappingResult_name.getWords().toLowerCase());
                    backMappedResults.add(hits[i]);
                } else if (backMapped_alias) {
                    toBackMapping.add(backMappingResult_alias.getWords().toLowerCase());
                    backMappedResults.add(hits[i]);
                } else {
                    for (IndexableField altName : altNames) {
                        backMappingResult = this.backMapped(record, altName.stringValue());
                        backMapped = backMappingResult.isMapped();
                        if (backMapped) {
                            String haystack = backMappingResult.getWords().toLowerCase();
                            toBackMapping.add(haystack);
                            backMappedResults.add(hits[i]);
                            break;
                        }
                    }
                }
                LOGGER.info("title: " + doc.get("title") + " fb_name: " + fb_name + " fb_alias: " + fb_alias);
            }
            if ( backMappedResults.size() > 0) {
                ScoreDoc hit = backMappedResults.get(0);
                Document finalDoc = indexSearcher.doc(hit.doc);
                String resultId = finalDoc.get("title").replace(" ", "_");

                finalId.add(resultId);
                LOGGER.info("FINAL RESULT IS: " + finalId);

            } else {
                LOGGER.info("CANNOT Be BACKMAPPED");
            }
            //USE BACKMAPPING TO MAPPED VALUES
            if (!toBackMapping.isEmpty()) {
                String question = record;
                Collections.sort(toBackMapping, new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        return t1.length() - s.length();
                    }
                });
                question = question.toLowerCase();
                for (String backMap : toBackMapping) {
                    String[] backArray = backMap.split(" ");
                    for (String b : backArray) {

                        question = question.replaceAll(b, "").replace("?", "").trim();
                    }
                }
                LOGGER.info("NEW QUESTION: " + question);
                if (question.length() > 0) {
                    Query queryBackMapped = this.buildLuceneQuery(question, conf);
                    TopDocs resultsBackMapped = indexSearcher.search(queryBackMapped, 5);
                    ScoreDoc[] hitsBackMapped = resultsBackMapped.scoreDocs;

                    for (int j = 0; j < hitsBackMapped.length; j++) {
                        Document docBackMapped = indexSearcher.doc(hitsBackMapped[j].doc);
                        if (j == 0) {
                            String resultBMId = docBackMapped.get("title").replace(" ", "_");
                            finalId.add(resultBMId);
                        }
                    }
                }
            }
            return finalId;
        }


    }

