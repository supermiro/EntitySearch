package DP_entity_linking.geneticAlgorithm;

import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public interface GenSequence<T> {
    void create(Random rnd);
    void mutate(Random rnd);
    T get();
    String toString();
}