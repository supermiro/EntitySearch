package DP_entity_linking.search;

import org.apache.lucene.search.similarities.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class DefaultConfiguration extends Configuration {
    public DefaultConfiguration() {
        Similarity[] sims = new Similarity[]{
                new BM25Similarity((float) 1.79, (float) 0.35),
                //new LMJelinekMercerSimilarity((float) 0.0003),
                new LMJelinekMercerSimilarity((float) 0.00001),
                new LMDirichletSimilarity(),
                new DefaultSimilarity(),

        };
        Map<String, Float> boost = new HashMap<>();
        boost.put("title", 0.4f);
        boost.put("anchor", 0.0f);
        boost.put("alt", 0.0f);
        boost.put("fb_name", 0.0f);
        boost.put("abs", 0.0f);

        setSims(sims);
        setBoost(boost);
    }
}
