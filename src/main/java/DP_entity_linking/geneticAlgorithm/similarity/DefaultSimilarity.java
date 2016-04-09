package DP_entity_linking.geneticAlgorithm.similarity;

import DP_entity_linking.geneticAlgorithm.GenSequence;
import DP_entity_linking.geneticAlgorithm.Interval;
import org.apache.lucene.search.similarities.Similarity;

import java.util.Random;

/**
 * Created by miroslav.kudlac on 4/4/2016.
 */
public class DefaultSimilarity implements GenSequence {
private Interval value;

public DefaultSimilarity() {

        }

@Override
public void mutate(Random rnd) {
        }

@Override
public void create(Random rnd) {
        }

@Override
public Similarity get() {
        return   new org.apache.lucene.search.similarities.DefaultSimilarity();
        }
        }