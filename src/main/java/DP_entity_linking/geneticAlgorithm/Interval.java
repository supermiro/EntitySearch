package DP_entity_linking.geneticAlgorithm;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by miroslav.kudlac on 11/22/2015.
 */
public class Interval implements SekvenciaGenov<Float>, Serializable {
    float min; // 0.0001f;
    float max; // 0.000001f;
    int pocetUsekov; // na kolko usekov to rozdelime?
    int aktualnyUsek;

    public Interval(float min, float max, int pocetUsekov) {
        this.min = min;
        this.max = max;
        this.pocetUsekov = pocetUsekov;
    }

    private float interpolacia(float t) {
        return (1-t)*min + t*max;
    }

    public void setUsek(int x) {
        this.aktualnyUsek = x % (pocetUsekov + 1);
    }

    public int getUsek() {
        return aktualnyUsek;
    }

    public float getCurrentPosition() {
        float delta = 1.0f / ((float) this.pocetUsekov);
        float pozicia = delta * ((float) this.aktualnyUsek);
        if(pozicia > 1.0f) {
            pozicia = 1.0f;
        }
        return interpolacia(pozicia);
    }

    public void mutate(Random rnd) {
        setUsek(rnd.nextInt(this.pocetUsekov));
    }

    public void create(Random rnd) {
        setUsek(rnd.nextInt(this.pocetUsekov));
    }

    public Float get() {
        return getCurrentPosition();
    }
}