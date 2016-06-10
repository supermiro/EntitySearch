package DP_entity_linking;

import DP_entity_linking.dataset.DataSet;
import DP_entity_linking.dataset.Record;
<<<<<<< HEAD
import DP_entity_linking.search.*;
=======
>>>>>>> 0798e8d6419e03b70147c6db3def3393a5b71252
import DP_entity_linking.geneticAlgorithm.GeneticClass;
import DP_entity_linking.search.Configuration;
import DP_entity_linking.search.DefaultConfiguration;
import DP_entity_linking.search.ResultPreprocessing;
import DP_entity_linking.search.Search;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Random;
/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class Main {
    private static Logger LOGGER = Logger.getLogger(Main.class);


    public void normalStart() throws IOException, ParseException {
        DataSet dataset = new DataSet();
        List<Record> records = dataset.loadWebquestions();
/*
<<<<<<< HEAD
        records = records.subList(0, 3700);
        Configuration conf;
        ResultPreprocessing result = new ResultPreprocessing();

        // Configuration via chromozom
        //Chromosome chromosome = new Chromosome();
        //chromosome.create(randnum);
       // conf = chromosome.get();

        // Defaultna konfiguracia
        //records = records.subList(0, 3700);
        records = records.subList(0, 5);
        Configuration conf;
        ResultPreprocessing result = new ResultPreprocessing();
=======
*/
        //records = records.subList(0, 3700);
        records = records.subList(0, 5);
        Configuration conf;
        ResultPreprocessing result = new ResultPreprocessing();
        conf = new DefaultConfiguration();
        Search search = new Search();
        search.start();
        FinalSearch finalSearch = new FinalSearch();

        List<String> finalAnswer = finalSearch.processRecord("what did darry look like", null);
        List<List<String>> f = result.results("what did darry look like", finalAnswer);
        LOGGER.info("+++++++++++++++++" + f + "++++++++++==");
        for (Record record : records) {
            LOGGER.info("------------" + record.getUtterance() + "--------------");
            //List<String> a = search.processRecord(record, null);
            List<Document> a = search.processRecord(record, conf);
            for(int i = 0; i < a.size(); i++) {
                LOGGER.info(a.get(i).get("title"));
            }
            if (a.size() > 0) {
                List<List<String>> finalResultPreprocessed = result.results(record.getQuestion(), a);
                LOGGER.info("+++++++++++++++++" + finalResultPreprocessed + "++++++++++==");
            } else {
                LOGGER.info("NOT FOUND");
            }
        }
        int fitness = search.getScore();
        LOGGER.info(fitness);
    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException {

        PropertyConfigurator.configure("data/log4j.properties");
        Random randnum = new Random();
        randnum.setSeed(123456789);
        GeneticClass geneticSolution = new GeneticClass(randnum);

        /*
         for(int i=0; i < 500; i++) {
             System.out.println(randnum.nextFloat());
         }
        */

        try {
            Main app = new Main();
            //geneticSolution.doJob();
            app.normalStart();

        } catch (IOException ex) {
            LOGGER.error(" error: ", ex);
        }

    }
}
