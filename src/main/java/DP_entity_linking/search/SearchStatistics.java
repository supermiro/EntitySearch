package DP_entity_linking.search;

import DP_entity_linking.evalution.Statistics;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import java.util.Map;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class SearchStatistics {
    private static Logger LOGGER = Logger.getLogger(SearchStatistics.class);

    public Statistics<Float> statisticScore = new Statistics();
    public Statistics<String> statisticDbCategory = new Statistics();
    public Statistics<String> statisticFbCategory = new Statistics();
    public int countBackMapped = 0;

        public int getCountBackMapped() {
            return countBackMapped;
        }
    public void print() {
        for (Map.Entry entry : statisticScore.entrySet()) {
            LOGGER.info(entry.getKey() + "," + entry.getValue());
        }
       /* for (Map.Entry entry : statisticDbCategory.entrySet()) {
            LOGGER.info(entry.getKey() + "," + entry.getValue());
        }
        for (Map.Entry entry : statisticFbCategory.entrySet()) {
            LOGGER.info("fb_" + entry.getKey() + "," + entry.getValue());
        }*/
        LOGGER.info("backmapped: " + countBackMapped);
    }

    /**
     * For statistics of freebase categories
     * @param doc
     */
    public void fbStats(Document doc) {
        IndexableField[] fbCats = doc.getFields("db_category");
        for(IndexableField fbCat : fbCats) {
            statisticFbCategory.count(fbCat.stringValue());
        }
    }

    /**
     * For statistics of dbpedia categories
     * @param doc
     */
    public void dbStats(Document doc) {
        IndexableField[] dbCats = doc.getFields("db_category");
        for(IndexableField dbCat : dbCats) {
            statisticDbCategory.count(dbCat.stringValue());
        }
    }

    public int getScore() {
        int pocetNajdenych = 0;
        for (Map.Entry<Float, MutableInt> entry : statisticScore.entrySet()) {
            if(entry.getKey().floatValue() > 0.0f) {
                pocetNajdenych += entry.getValue().intValue();

            }
        }
        return pocetNajdenych;
    }

}
