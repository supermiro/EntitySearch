package DP_entity_linking.geneticAlgorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class BoostParameters implements GenSequence<Map<String, Float>>, Serializable {
    Map<String, Interval> konfiguracia;

    public BoostParameters() {
        create(null);
    }

    public void create(Random rnd) {
        konfiguracia = new HashMap();
        konfiguracia.put("title", new Interval(0.0f, 1.0f, 100, 1.0f)); // názov ?lánku vo Wikipédii,
        konfiguracia.put("alt", new Interval(0.0f, 1.0f, 100, 1.0f));  //  alternatívne mená ?lánku,
        konfiguracia.put("text", new Interval(0.0f, 1.0f, 100, 0.3f)); //  text ?lánku,
        konfiguracia.put("sentence", new Interval(0.0f, 1.0f, 100, 0.4f)); //  prvá veta ?lánku,
        konfiguracia.put("abstract", new Interval(0.0f, 1.0f, 100, 0.3f)); //  abstrakt textu ?lánku,
        konfiguracia.put("section", new Interval(0.0f, 1.0f, 100, 0.3f)); //  sekcia hlavi?ky ?lánku,
        konfiguracia.put("fb_name", new Interval(0.0f, 1.0f, 100, 0.9f)); //  Freebase názov ?lánku,
        konfiguracia.put("fb_alias", new Interval(0.0f, 1.0f, 100, 1.0f)); //  alternatívne názvy ?lánku vo Freebase,
        konfiguracia.put("anchor", new Interval(0.0f, 1.0f, 100, 0.5f));
        konfiguracia.put("abs", new Interval(0.0f, 1.0f, 100, 0.3f));


        for (Map.Entry<String, Interval> entry : konfiguracia.entrySet()) {
            if(rnd == null) {
                entry.getValue().setUsek(100); // set max value
            } else {
                entry.getValue().create(rnd); // set random value
            }
        }
    }

    public void mutate(Random rnd) {
        for (Map.Entry<String, Interval> entry : konfiguracia.entrySet()) {
            entry.getValue().mutate(rnd);
        }

    }

    public Map<String, Float> get() {
        Map<String, Float> mapa = new HashMap();
        for (Map.Entry<String, Interval> entry : konfiguracia.entrySet()) {
            float x = entry.getValue().get();
            // If value is bigger than 0 add to boost
            if(x > 0.0f) {
                mapa.put(entry.getKey(), x);
            }
        }
        return mapa;
    }
}