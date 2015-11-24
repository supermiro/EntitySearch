package DP_entity_linking.evalution;

import org.apache.commons.lang.mutable.MutableInt;

import java.util.HashMap;

/**
 * Created by miroslav.kudlac on 10/26/2015.
 */
public class Statistics<T> extends HashMap<T, MutableInt> {
    public void count(T input) {
        MutableInt item = this.get(input);
        if (item == null) {
            item = new MutableInt(0);
            this.put(input, item);
        }
        item.increment();
    }


}
