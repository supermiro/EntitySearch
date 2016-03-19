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
	private static File JSON_FILE;

	public DataSet(String path) {
		JSON_FILE = new File(path);
	}

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
		return (List<Record>) mapper.readValue(JSON_FILE, Records.class);
	}
}
