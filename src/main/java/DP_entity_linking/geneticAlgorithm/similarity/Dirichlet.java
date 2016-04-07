package DP_entity_linking.geneticAlgorithm.similarity;

import DP_entity_linking.geneticAlgorithm.GenSequence;
import DP_entity_linking.geneticAlgorithm.Interval;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import java.util.Random;

/**
 * Created by miroslav.kudlac on 4/5/2016.
 */
public class Dirichlet implements GenSequence {
    private Interval value;

    public Dirichlet() {

    }

    @Override
    public void mutate(Random rnd) {
    }

    @Override
    public void create(Random rnd) {
    }

    @Override
    public Similarity get() {
        return new LMDirichletSimilarity();
    }
}
