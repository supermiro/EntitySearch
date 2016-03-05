package DP_entity_linking.search;

import junit.framework.TestCase;

/**
 * Created by miroslav.kudlac on 3/1/2016.
 */
public class SearchTest extends TestCase {
    public void testBackMapping() throws Exception{
        String query = "what are the landlocked countries in latin america?";

        BackMapping a = new Search().backMapped(query, "Landlocked");
        System.out.println("is mapped: " + a.isMapped());
    }
}