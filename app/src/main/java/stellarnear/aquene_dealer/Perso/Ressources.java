package stellarnear.aquene_dealer.Perso;

import android.content.Context;

/**
 * Created by jchatron on 02/02/2018.
 */

public class Ressources {
    private Context mC;
    private int kiPool;
    private int maxHp;
    private int currentHp;
    private int stunFist;
    private int palm;
    public Ressources(Context mC)
    {
        this.mC = mC;
    }

    public void sleepReset(){

    }

    public void spendKi(int cost) {
        this.kiPool-=cost;
    }
}
