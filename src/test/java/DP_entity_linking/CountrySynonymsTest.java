package DP_entity_linking;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.junit.Test;

/**
 * Created by miroslav.kudlac on 10/4/2015.
 */
public class CountrySynonymsTest {

    @Test
    public void testBuildMap() throws Exception {
        SynonymMap map = CountrySynonyms.buildMap();
    }

    @Test
    public void testFillMap() throws Exception {
        CountrySynonyms.fillMap();
    }
}