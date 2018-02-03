package stellarnear.aquene_dealer.Perso;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String,Integer> mapAttackidRemaning=new HashMap<>();
    public Ressources(Context mC)
    {
        this.mC = mC;
        mapAttackidRemaning.put("attack_stun",2);
        mapAttackidRemaning.put("attack_palm",1);
    }

    public void sleepReset(){

    }

    public void spendKi(int cost) {
        this.kiPool-=cost;
    }

    public void spendAttack(String atkId){
        try {
            mapAttackidRemaning.put(atkId,mapAttackidRemaning.get(atkId)-1);
        } catch (Exception e) { }
    }

    public int getRemaningAttack(String atkId) {
        int remain;
        try {
            remain=mapAttackidRemaning.get(atkId);
        } catch (Exception e) { remain=0; }
        return remain;
    }
}
