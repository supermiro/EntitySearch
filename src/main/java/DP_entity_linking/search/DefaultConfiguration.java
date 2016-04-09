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
                new BM25Similarity((float) 1.1600001, (float) 0.69949996),
                //new LMJelinekMercerSimilarity((float) 0.0003),
                new LMJelinekMercerSimilarity((float) 0.000085),
                new LMDirichletSimilarity(),
                new DefaultSimilarity(),

        };
        Map<String, Float> boost = new HashMap<>();
        boost.put("title", 0.14f);
        boost.put("anchor",  0.39999998f);
        boost.put("alt",  0.48f);
        boost.put("text", 0.97999996f);
        boost.put("abstract", 0.099999994f);
        boost.put("fb_name", 0.32f);
        boost.put("fb_alias", 0.53999996f);
        boost.put("section", 0.68f);
        boost.put("sentence",  0.64f);
        boost.put("abs",  0.58f);

        setSims(sims);
        setBoost(boost);
    }
}
