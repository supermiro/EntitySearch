package DP_entity_linking;

import DP_entity_linking.dataset.DataSet;
import DP_entity_linking.dataset.Record;
import DP_entity_linking.geneticAlgorithm.Chromosome;
import DP_entity_linking.search.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class);
    /**
     * Call retrieveRecords and write to log
     * @throws IOException
     */
    public void doJob(Random randnum) throws IOException, ParseException {
        // Spracuj dataset
        DataSet dataset = new DataSet("C:\\workspace\\webquestions.json");
        List<Record> records = dataset.loadWebquestions();
        records = records.subList(0, 3700);
        Configuration conf;
        Configuration bestConf = null;
        ResultPreprocessing result = new ResultPreprocessing();

        // Configuration via chromozom
        Chromosome chromosome = new Chromosome();

        // Defaultna konfiguracia
        conf = new DefaultConfiguration();

        int bestFitnes = 0;
        int countBM = 0;
        //for (int i = 0; i < 100; i++) {
            boolean confFound = false;
            //chromosome.create(randnum);
            //conf = chromosome.get();
            Search search = new Search();
            search.start();
            //FinalSearch finalSearch = new FinalSearch();

            //List<String> finalAnswer = finalSearch.processRecord("what did darry look like", null);
            //List<List<String>> f = result.results("what did darry look like", finalAnswer);
            //LOGGER.info("+++++++++++++++++" + f + "++++++++++==");

            //LOGGER.info("CONF: " + conf);

            for (Record record : records) {
                LOGGER.info("------------" + record.getUtterance() + "--------------");
                List<String> a = search.processRecord(record, conf);
                if (a.size() > 0) {
                    List<List<String>> finalResultPreprocessed = result.results(record.getQuestion(), a);
                    LOGGER.info("+++++++++++++++++" + finalResultPreprocessed + "++++++++++==");
                } else {
                   LOGGER.info("NOT FOUND!");
                }

            }

            int fitness = search.getScore();
            LOGGER.warn("NEW BESTFITNESS: " + Integer.toString(fitness));
        /*if (bestFitnes <= fitness) {
                bestFitnes = fitness;
                bestConf = conf;
                countBM = search.getCountBackMapped();
                if (search.getCountBackMapped() > countBM) {
                    confFound = true;
                }
                LOGGER.warn(i + " NEW CONF: " + bestConf);
                LOGGER.warn("NEW BESTFITNESS: " + Integer.toString(bestFitnes));
            }
            if (confFound) {
                LOGGER.warn(i + " NEW BESTCONF WITH BEST POSSIBLE BACKMAPPING: " + bestConf);
            } else {

            }
        }*/

        //int fitness = search.getScore();
        //LOGGER.info("------------" + Integer.toString(fitness) + "--------------");
    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException {

        PropertyConfigurator.configure("log4j.properties");
        Random randnum = new Random();
        randnum.setSeed(123456789);

        try {
            Main app = new Main();
            app.doJob(randnum);

        } catch (IOException ex) {
            LOGGER.error(" error: ", ex);
        }

    }
}
