package DP_entity_linking.search;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by miroslav.kudlac on 3/23/2016.
 */
public class BackMapping2 implements BackMappingInterface {
    @Override
    public BackMapping backMapped(String query, String form) {
        boolean isMapped = false;
        boolean contains;
        int string_count = 0;
        String result = "";
        form = form.replaceAll("[^a-zA-Z0-9]+", " ").trim();
        String [] split = form.split(" ");
        split = new HashSet<String>(Arrays.asList(split)).toArray(new String[0]);
        return null;
    }
}
