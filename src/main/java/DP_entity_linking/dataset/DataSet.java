package DP_entity_linking.dataset;

import DP_entity_linking.search.Search;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class DataSet {
    private static Logger LOGGER = Logger.getLogger(Search.class);
//    private static final File JSON_FILE = new File("C:\\workspace\\webquestions.train");
    //private static final File JSON_FILE = new File("data/webquestions.train");
    private static final File JSON_FILE = new File("data/data.json");

    public static class Records extends ArrayList<Record> {
        public Records() {
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public List<Record> loadWebquestions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (List<Record>)mapper.readValue(JSON_FILE, Records.class);
    }
}
