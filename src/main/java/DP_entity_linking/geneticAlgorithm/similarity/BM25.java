package DP_entity_linking.geneticAlgorithm.similarity;

import DP_entity_linking.geneticAlgorithm.GenSequence;
import DP_entity_linking.geneticAlgorithm.Interval;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;

import java.util.Random;

/**
 * Created by miroslav.kudlac on 4/4/2016.
 */
public class BM25 implements GenSequence {
    private Interval value1;
    private Interval value2;

    public BM25() {
        this.value1 = new Interval(1.1f, 1.4f, 100);
        this.value1.setUsek(10);
        this.value2 = new Interval(0.65f, 0.8f, 100);
        this.value2.setUsek(10);
    }

    @Override
    public void mutate(Random rnd) {
        value1.mutate(rnd);
        value2.mutate(rnd);
    }

    @Override
    public void create(Random rnd) {
        this.value1.create(rnd);
        this.value2.create(rnd);
    }

    @Override
    public Similarity get() {
        return new BM25Similarity( value1.getCurrentPosition(), value2.getCurrentPosition());
    }
}
