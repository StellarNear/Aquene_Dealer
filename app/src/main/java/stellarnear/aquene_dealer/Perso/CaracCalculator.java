package stellarnear.aquene_dealer.Perso;

/**
 * Created by jchatron on 05/01/2018.
 */

public class CaracCalculator {
    private int FOR;
    private int DEX;
    private int CON;
    private int INT;
    private int SAG;
    private int CHA;
    private int CA;
    private int BMO;
    private int DMD;
    private Carac baseCarac;
    private AllStances allStances;
    private AllFeats allFeats;
    public CaracCalculator(Carac baseCarac, AllStances allStances, AllFeats allFeats) {
        this.baseCarac=baseCarac;
        this.allStances=allStances;
        this.allFeats=allFeats;
        setFOR();

    }


    public int getFOR() {
        return FOR;
    }

    public void setFOR() {
        int val=baseCarac.getFOR();
        if (allStances.isActive("bear")) {
            val +=4;
        }
        this.FOR = val;
    }

}
