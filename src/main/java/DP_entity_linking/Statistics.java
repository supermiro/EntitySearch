package DP_entity_linking;

import org.apache.commons.lang.mutable.MutableInt;

import java.util.HashMap;

/**
 * Created by miroslav.kudlac on 10/26/2015.
 */
public class Statistics extends HashMap<Float, MutableInt> {
    public void count(Float input) {
        MutableInt item = this.get(input);
        if (item == null) {
            item = new MutableInt(0);
            this.put(input, item);
        }
        item.increment();
    }


}
