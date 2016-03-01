package DP_entity_linking;

import DP_entity_linking.dataset.DataSet;
import DP_entity_linking.dataset.Record;
import DP_entity_linking.search.Configuration;
import DP_entity_linking.search.DefaultConfiguration;
import DP_entity_linking.search.Search;
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
        records = records.subList(0, 200);
        Configuration conf;

        // Konfiguracia ziskana cez chromozon
        //Chromozon chromozon = new Chromozon();
        //chromozon.create(randnum);
       // conf = chromozon.get();

        // Defaultna konfiguracia
        conf = new DefaultConfiguration();
        Search search = new Search();
        search.start();

        for (Record record : records) {
            LOGGER.info("------------" + record.getUtterance() + "--------------");
            List<String> a = search.processRecord(record, null);
            LOGGER.info("+++++++++++++++++" + a + "++++++++++==");
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
        // Spellcheck spell = new Spellcheck(new WordFrequenciesT("EntityStore"), "C:\\workspace\\erd\\EntityStore\\spellindex\\1\\spellindex");

        try {
            Main app = new Main();
            app.doJob(randnum);

        } catch (IOException ex) {
            LOGGER.error(" error: ", ex);
        }

    }
}
