package DP_entity_linking;

import DP_entity_linking.dataset.DataSet;
import DP_entity_linking.dataset.Record;
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
        DataSet dataset = new DataSet();
        List<Record> records = dataset.loadWebquestions();
        records = records.subList(0, 3700);
        Configuration conf;
        ResultPreprocessing result = new ResultPreprocessing();

        // Configuration via chromozom
        //Chromosome chromosome = new Chromosome();
        //chromosome.create(randnum);
       // conf = chromosome.get();

        // Defaultna konfiguracia
        conf = new DefaultConfiguration();
        Search search = new Search();
        search.start();
        FinalSearch finalSearch = new FinalSearch();

        List<String> finalAnswer = finalSearch.processRecord("what language do people from thailand speak?", null);
        List<List<String>> f = result.results("what language do people from thailand speak?", finalAnswer);
        LOGGER.info("+++++++++++++++++" + f + "++++++++++==");
        for (Record record : records) {
            LOGGER.info("------------" + record.getUtterance() + "--------------");
            List<String> a = search.processRecord(record, null);
            if (a.size() > 0) {
                List<List<String>> finalResultPreprocessed = result.results(record.getQuestion(), a);
                LOGGER.info("+++++++++++++++++" + finalResultPreprocessed + "++++++++++==");
            } else {
                LOGGER.info("NOT FOUND!");
            }

        }

        int fitness = search.getScore();
        LOGGER.info("------------" + Integer.toString(fitness) + "--------------");
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
