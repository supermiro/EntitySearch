package DP_entity_linking.geneticAlgorithm.similarity;

import DP_entity_linking.geneticAlgorithm.GenSequence;
import DP_entity_linking.geneticAlgorithm.Interval;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class LmJelink implements GenSequence<Similarity> {
    private Interval value;

    public LmJelink() {
        this.value = new Interval(0.000001f, 0.0001f, 100);
        this.value.setUsek(100);
    }

    @Override
    public void mutate(Random rnd) {
        value.mutate(rnd);
    }

    @Override
    public void create(Random rnd) {
        this.value.create(rnd);
    }

    @Override
    public Similarity get() {
        return new LMJelinekMercerSimilarity( value.getCurrentPosition() );
    }
}